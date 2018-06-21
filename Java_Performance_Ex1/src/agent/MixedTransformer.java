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
	
	protected byte[] getCodeBefore(CtBehavior callSite, CtBehavior calledMethod) {
		CtClass calledClass = calledMethod.getDeclaringClass();
		boolean calledClassWasFrozen = calledClass.isFrozen();
		if (calledClassWasFrozen) calledClass.defrost();
		
		MethodInfo callerInfo = callSite.getMethodInfo();
		MethodInfo calleeInfo = calledMethod.getMethodInfo();
		
		List<String> argTypes = getArgumentTypes(calleeInfo.getDescriptor());
		Bytecode code = new Bytecode(callerInfo.getConstPool(), 0, argTypes.size());
		
		// [
		pushArgumentsArray(code, argTypes);
		// [ args
		code.addLdc(callSite.getDeclaringClass().getName() + "." + callSite.getName());
		// [ args, callSite
		code.addLdc(calledMethod.getName());
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
		
		// [ 
		duplicateReturnValue(code, returnType);
		// [ return-value
		code.addLdc(callSite.getDeclaringClass().getName() + "." + callSite.getName());
		// [ return-value, callSite
		code.addLdc(calledMethod.getName());
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
				type += descriptor.substring(i + 1, semicolon);
				i = semicolon;
			} else {			//primitive type
				type += c;
			}
			result.add(type);
		}
		return result;
	}
	
	/**
	 * Pushes an Object-array on the stack with the argument values from the current method
	 */
	private void pushArgumentsArray(Bytecode code, List<String> argTypes) {
		// store individual arguments into local variables and then back to stack
		for (int i = 0; i < argTypes.size(); i++) {
			code.addStore(i, getCtClass(argTypes.get(i)));
		}
		for (int i = argTypes.size() - 1; i >= 0; i--) {
			code.addLoad(i, getCtClass(argTypes.get(i)));
		}
		
		// [
		code.addIconst(argTypes.size());
		// [ argument count
		code.addAnewarray("java.lang.Object");
		// [ array
		int displacement = 0;
		for(int i = 0; i < argTypes.size(); i++) {
			// [ array
			code.add(Opcode.DUP);
			// [ array, array
			code.addIconst(i);
			// [ array, array, i
			displacement += loadLocal(code, argTypes, i, displacement);
			// [ array, array, i, i-th argument
			code.add(Opcode.AASTORE);
			// [ array
		}
	}
	
	/**
	 * Loads the local parameter value of parameter i into the stack.<br>
	 * Returns a displacement of 1 for types Long("J") and Double("D").
	 */
	private int loadLocal(Bytecode code, List<String> arguments, int i, int displacement) {
		switch(arguments.get(i)) {
			case "B":
				code.addIload(i + displacement);
				code.addInvokestatic("java.lang.Byte", "valueOf", "(B)Ljava/lang/Byte;");
				return 0;
			case "S":
				code.addIload(i + displacement);
				code.addInvokestatic("java.lang.Short", "valueOf", "(S)Ljava/lang/Short;");
				return 0;
			case "I":
				code.addIload(i + displacement);
				code.addInvokestatic("java.lang.Integer", "valueOf", "(I)Ljava/lang/Integer;");
				return 0;
			case "J":
				code.addLload(i + displacement);
				code.addInvokestatic("java.lang.Long", "valueOf", "(J)Ljava/lang/Long;");
				return 1;
			case "F":
				code.addFload(i + displacement);
				code.addInvokestatic("java.lang.Float", "valueOf", "(F)Ljava/lang/Float;");
				return 0;
			case "D":
				code.addDload(i + displacement);
				code.addInvokestatic("java.lang.Double", "valueOf", "(D)Ljava/lang/Double;");
				return 1;
			case "Z":
				code.addIload(i + displacement);
				code.addInvokestatic("java.lang.Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
				return 0;
			case "C":
				code.addIload(i + displacement);
				code.addInvokestatic("java.lang.Character", "valueOf", "(C)Ljava/lang/Character;");
				return 0;
			default: // L, [
				code.addAload(i + displacement);
				return 0;
		}
	}

	/**
	 * Gets a CtClass from a type-descriptor
	 */
	private CtClass getCtClass(String type) {
		switch(type) {
			case "B":
				return CtClass.byteType;
			case "S":
				return CtClass.shortType;
			case "I":
				return CtClass.intType;
			case "J":
				return CtClass.longType;
			case "F":
				return CtClass.floatType;
			case "D":
				return CtClass.doubleType;
			case "Z":
				return CtClass.booleanType;
			case "C":
				return CtClass.charType;
			default: // L, [
			try {
				return ClassPool.getDefault().get(type);
			} catch (NotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * Duplicates the return value currently on top of the Stack.
	 */
	private void duplicateReturnValue(Bytecode code, String returnType) {
		// [
		if (returnType.equals("V")) {
			code.add(Opcode.ACONST_NULL);
		}
		else if (returnType.equals("J") || returnType.equals("D")) {
			code.add(Opcode.DUP2);
		}
		else {
			code.add(Opcode.DUP);
		}
		// [ return-value (as primitive)
		wrapPrimitiveWithReference(code, returnType);
		// [ return-value (as Object)
	}
	
	/**
	 * Converts a primitive value on the stack to a Reference value.
	 */
	private void wrapPrimitiveWithReference(Bytecode code, String type) {
		switch(type) {
			case "B":
				code.addInvokestatic("java.lang.Byte", "valueOf", "(B)Ljava/lang/Byte;");
				break;
			case "S":
				code.addInvokestatic("java.lang.Short", "valueOf", "(S)Ljava/lang/Short;");
				break;
			case "I":
				code.addInvokestatic("java.lang.Integer", "valueOf", "(I)Ljava/lang/Integer;");
				break;
			case "J":
				code.addInvokestatic("java.lang.Long", "valueOf", "(J)Ljava/lang/Long;");
				break;
			case "F":
				code.addInvokestatic("java.lang.Float", "valueOf", "(F)Ljava/lang/Float;");
				break;
			case "D":
				code.addInvokestatic("java.lang.Double", "valueOf", "(D)Ljava/lang/Double;");
				break;
			case "Z":
				code.addInvokestatic("java.lang.Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
				break;
			case "C":
				code.addInvokestatic("java.lang.Character", "valueOf", "(C)Ljava/lang/Character;");
				break;
			default: // L, [ --> don't wrap since already reference
				break;	
		}
	}
	
}
