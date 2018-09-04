package agent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

public class MixedTransformer implements ClassFileTransformer {
	
	protected static final String loggerClassName = HotMethodLogger.class.getName();
	protected static final List<Integer> methodCallOpcodes = Arrays.asList(new Integer[] {
			Opcode.INVOKEDYNAMIC, Opcode.INVOKEINTERFACE, Opcode.INVOKESPECIAL, Opcode.INVOKESTATIC, Opcode.INVOKEVIRTUAL
	});
	
	protected final List<String> includedClasses;
	
	public MixedTransformer(List<String> includedPackages) {
		final ArrayList<String> includedClasses = new ArrayList<>();
		for (String clazz : includedPackages) {
			includedClasses.add(clazz.replace('.', '/'));
		}
		this.includedClasses = Collections.unmodifiableList(includedClasses);
	}
	
	@Override
	public byte[] transform(ClassLoader loader, String name, Class<?> ___, ProtectionDomain __, byte[] classfile)
			throws IllegalClassFormatException {
		try {
			if (name == null || !includedClasses.contains(name)) {
				return classfile;	//not included -> do not transform
			}
			
			ClassPool cp = ClassPool.getDefault();
			CtClass clazz = cp.makeClass(new ByteArrayInputStream(classfile));
			
			if (HotMethodAgent.DEBUG) System.out.printf("----------------------------- START TRANSFORMING %s -----------------------------%n", clazz.getName());
			
			for (CtBehavior method : clazz.getDeclaredBehaviors()) {
				transform(method); // transform behaviors (=methods and constructors)
			}
			
			if (HotMethodAgent.DEBUG) System.out.printf("----------------------------- STOP TRANSFORMING %s -----------------------------%n", clazz.getName());
			return clazz.toBytecode(); // return transformed class
		} catch (IOException | RuntimeException | CannotCompileException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void transform(CtBehavior method) {
		if (isNative(method) || isAbstract(method))
			return;
		if (HotMethodAgent.DEBUG) System.out.printf("----- TRANSFORMING METHOD %s%s -----%n", method.getName(), method.getSignature());
		MethodInfo methodInfo = method.getMethodInfo();
		CodeIterator codeIterator = methodInfo.getCodeAttribute().iterator();
		
		try {
			final QueueMap<Integer, CtBehavior> lineToCalledMethods = new QueueMap<>();
			
			method.instrument(new ExprEditor() {
				@Override
				public void edit(NewExpr expr) throws CannotCompileException {
					try {
						if (HotMethodAgent.DEBUG) {
							System.out.printf("INFO: Constructor \"%s\" is being called by \"%s\" from line number %d. [bytecode index=%d]%n", 
									expr.getConstructor().getName(), method.getName(), expr.getLineNumber(), expr.indexOfBytecode());
						}
						lineToCalledMethods.add(expr.getLineNumber(), expr.getConstructor());
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void edit(MethodCall mc) throws CannotCompileException {
					try {
						if (HotMethodAgent.DEBUG) {
							System.out.printf("INFO: Method \"%s\" is being called by \"%s\" from line number %d. [bytecode index=%d]%n", 
									mc.getMethodName(), method.getName(), mc.getLineNumber(), mc.indexOfBytecode());
						}
						lineToCalledMethods.add(mc.getLineNumber(), mc.getMethod());
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
				}
			});
			
			codeIterator.begin();
			while (codeIterator.hasNext()) {
				int nextInstrIndex = codeIterator.lookAhead();
				int nextInstrOpcode = codeIterator.byteAt(nextInstrIndex);
				int lineNumber = methodInfo.getLineNumber(nextInstrIndex);
				
				if (methodCallOpcodes.contains(nextInstrOpcode)) {
					CtBehavior calledMethod = lineToCalledMethods.remove(lineNumber);
					
					if (calledMethod != null) {		//only include methods that were detected by the ExprEditor
						if (HotMethodAgent.DEBUG) {
							System.out.printf("Next call from bytecode: [bytecode=%d, linenum = %d] %s%n", 
									nextInstrIndex, lineNumber, calledMethod.getName());
						}
						
						//produce Bytecode before method and insert (before method call)
						codeIterator.insert(getCodeBefore(method, calledMethod));
						//go to actual method call bytecode, next() is now the next bytecode after method call
						codeIterator.next();
						//produce Bytecode after method and insert (before next bytecode)
						codeIterator.insert(getCodeAfter(method, calledMethod));
						continue;
					}
				}
				codeIterator.next();
			}
			
			//finalize
			methodInfo.getCodeAttribute().computeMaxStack();
			methodInfo.rebuildStackMap(ClassPool.getDefault());
		} catch (CannotCompileException | BadBytecode e) {
			e.printStackTrace();
		}
	}

	private boolean isNative(CtBehavior method) {
		return Modifier.isNative(method.getModifiers());
	}
	
	private boolean isAbstract(CtBehavior method) {
		return Modifier.isAbstract(method.getModifiers());
	}
	
	private byte[] getCodeBefore(CtBehavior callSite, CtBehavior calledMethod) {
		CtClass calledClass = calledMethod.getDeclaringClass();
		boolean calledClassWasFrozen = calledClass.isFrozen();
		if (calledClassWasFrozen) calledClass.defrost();
		
		MethodInfo callerInfo = callSite.getMethodInfo();
		MethodInfo calleeInfo = calledMethod.getMethodInfo();
		
		List<String> argTypes = getArgumentTypes(calleeInfo.getDescriptor());
		Bytecode code = new Bytecode(callerInfo.getConstPool());
		BytecodeHelper helper = new BytecodeHelper(code);
		
		// [
		helper.pushArgumentsArray(argTypes);
		// [ args
		code.addLdc(callSite.getDeclaringClass().getName() + "." + callSite.getName());
		// [ args, callSite
		code.addLdc(calledMethod.getDeclaringClass().getName() + "." + calledMethod.getName());
		// [ args, callSite, calledMethodName
		code.addInvokestatic(loggerClassName, "startTrackingMethod", "([Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V");
		// [
		
		if (calledClassWasFrozen) calledClass.freeze();
		return code.get();
	}
	
	private byte[] getCodeAfter(CtBehavior callSite, CtBehavior calledMethod) {
		CtClass calledClass = calledMethod.getDeclaringClass();
		boolean calledClassWasFrozen = calledClass.isFrozen();
		if (calledClassWasFrozen) calledClass.defrost();
		
		MethodInfo callerInfo = callSite.getMethodInfo();
		MethodInfo calleeInfo = calledMethod.getMethodInfo();
		
		String returnType = calleeInfo.getDescriptor().substring(calleeInfo.getDescriptor().indexOf(')') + 1);
		Bytecode code = new Bytecode(callerInfo.getConstPool());
		BytecodeHelper helper = new BytecodeHelper(code);
		
		// [ 
		helper.duplicateReturnValue(returnType);
		// [ return-value
		code.addLdc(callSite.getDeclaringClass().getName() + "." + callSite.getName());
		// [ return-value, callSite
		code.addLdc(calledMethod.getDeclaringClass().getName() + "." + calledMethod.getName());
		// [ return-value, callSite, calledMethodName
		code.addInvokestatic(loggerClassName, "stopTrackingMethod", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V");
		// [
		
		if (calledClassWasFrozen) calledClass.freeze();
		return code.get();
	}
	
	// --------- Helper Methods for generating Bytecode ---------
	
	/**
	 * Gets a List of Strings from a descriptor, one element being one argument type of the method described.
	 * @param descriptor - example: ([Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;IJ)V
	 * @return
	 */
	private List<String> getArgumentTypes(String descriptor) {
		List<String> result = new ArrayList<>();
		for(int i = 1; descriptor.charAt(i) != ')'; i++) {
			char c = descriptor.charAt(i);
			String type = "";
			while(c == '[') {	//array "depth"
				type += "[";
				i++;
				c = descriptor.charAt(i);
			}
			if (c == 'L') {		//object type
				int semicolon = descriptor.indexOf(";", i + 1);
				type += descriptor.substring(i, semicolon + 1);
				i = semicolon;
			} else {			//primitive type
				type += c;
			}
			result.add(type);
		}
		return result;
	}
	
}
