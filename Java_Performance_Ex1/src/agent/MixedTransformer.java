package agent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class MixedTransformer implements ClassFileTransformer {

	private static final String thisClassName;
	private static CtClass thisClass;

	static {
		thisClassName = MixedTransformer.class.getName();
		try {
			thisClass = ClassPool.getDefault().get(thisClassName);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	protected final List<String> includedPackages;

	public MixedTransformer(String[] includedPackages) {
		this.includedPackages = Arrays.asList(includedPackages);
	}

	@Override
	public byte[] transform(ClassLoader loader, String name, Class<?> ___, ProtectionDomain __, byte[] classfile)
			throws IllegalClassFormatException {
		try {
			for (int i = 0; i < includedPackages.size(); i++) {
				if (name.startsWith(includedPackages.get(i)))
					break;	//class included -> transform
				if (i == includedPackages.size() - 1) 
					return classfile;	//not included -> do not transform
			}
			
			ClassPool cp = ClassPool.getDefault();
			CtClass clazz = cp.makeClass(new ByteArrayInputStream(classfile));

			for (CtMethod method : clazz.getDeclaredMethods()) {
				transform(method); // transform methods
			}
			
			for (CtConstructor constructor : clazz.getDeclaredConstructors()) {
				transform(constructor.toMethod(constructor.getName(), clazz)); // transform constructors
			}

			return clazz.toBytecode(); // return transformed class
		} catch (IOException | RuntimeException | CannotCompileException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void transform(CtMethod method) {
		if (isNative(method) || isAbstract(method))
			return;
		System.out.printf("----- TRANSFORMING %s -----%n", method.getName());
		MethodInfo methodInfo = method.getMethodInfo();
		CodeIterator codeIterator = methodInfo.getCodeAttribute().iterator();
		
		try {
			method.instrument(new ExprEditor() {
				@Override
				public void edit(MethodCall mc) throws CannotCompileException {
					int byteCodeIndex = mc.indexOfBytecode();
					
					System.out.printf("%s wird von %s von Zeile %d aufgerufen. [bytecode index=%d]%n", 
							mc.getMethodName(), method.getName(), mc.getLineNumber(), byteCodeIndex);
					
					//insert after-method-bytecode first so that byteCodeIndex does not change
					codeIterator.move(byteCodeIndex);
					while (codeIterator.hasNext()) {
						try {
							int curIndex = codeIterator.next();
							int opcode = codeIterator.byteAt(curIndex);
							
							if (opcode == Opcode.RETURN || opcode == Opcode.ATHROW || opcode == Opcode.IRETURN || 
								opcode == Opcode.LRETURN || opcode == Opcode.FRETURN || opcode == Opcode.DRETURN) 
							{
								System.out.printf("%s returns at bytecode index %d.%n", mc.getMethodName(), curIndex);
//								codeIterator.insert(getCodeAfter(methodInfo));	//produce Bytecode after method and insert it
								break;
							}
						} catch (BadBytecode e) {
							e.printStackTrace();
						}
					}
					
//					try {
//						//produce Bytecode before method and insert it at byteCodeIndex
//						codeIterator.insertAt(byteCodeIndex, getCodeBefore(methodInfo));
//					} catch (BadBytecode e) {
//						e.printStackTrace();
//					}
				}
			});
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}

	private boolean isNative(CtMethod method) {
		return Modifier.isNative(method.getModifiers());
	}

	private boolean isAbstract(CtMethod method) {
		return Modifier.isAbstract(method.getModifiers());
	}
	
	private byte[] getCodeAfter(MethodInfo method) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected byte[] getCodeBefore(MethodInfo method) {
		// TODO Auto-generated method stub
		return null;
	}

}
