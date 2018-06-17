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
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import test.Test_1;

public class HotMethodFinder implements ClassFileTransformer {

	private static final String thisClassName;
	private static CtClass thisClass;
	
	static {
		thisClassName = HotMethodFinder.class.getName();
		try {
			thisClass = ClassPool.getDefault().get(thisClassName);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public final List<String> includedPackages;

	private final Map<String, Integer> methodCounter;
	public static final Map<String, Long> methodTimer = new ConcurrentHashMap<>();

	public static void startTrackingMethod(String key) {
		key = key.trim();
		
		methodTimer.put(key, System.nanoTime());
		
		System.out.printf("-> %-20s%n", key);
		
	}

	public static void stopTrackingMethod(String key) {
		key = key.trim();
		
		Long startTime = methodTimer.get(key);
		long timeNeededForExecution = System.nanoTime() - startTime;
		System.out.printf("<- %-20s: %10.2f [ms]%n", key, (float) timeNeededForExecution / 1000000.0f);
		
	}

	public HotMethodFinder(String[] includedPackages) {
		this.includedPackages = Arrays.asList(includedPackages);
		methodCounter = new HashMap<>();
	}

	@Override
	public byte[] transform(ClassLoader loader, String name, Class<?> ___, ProtectionDomain __, byte[] classfile)
			throws IllegalClassFormatException {
		try {
			ClassPool pool = ClassPool.getDefault(); // TODO
			CtClass clazz = pool.makeClass(new ByteArrayInputStream(classfile));
			
			if (!includedPackages.contains(clazz.getPackageName()))
				return classfile; // only transform classes in included packages
			
			transformClass(clazz);	//TODO: needed?
			
			for (CtMethod method : clazz.getDeclaredMethods()) {
				transform(method); // transform methods
			}
			
			for (CtConstructor constructor : clazz.getDeclaredConstructors()) {
				transform(constructor.toMethod(constructor.getName(), clazz)); // transform constructors
			}
			
			return clazz.toBytecode();	//return transformed class
		} catch (IOException | RuntimeException | CannotCompileException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void transformClass(CtClass clazz) {
//		try {
//			
//			
//			clazz.addMethod(CtMethod.make(String.format(
//					"public static void "
//					),
//				clazz)
//			);
//		} catch (CannotCompileException e) {
//			e.printStackTrace();
//		}
	}

	private void transform(CtMethod method) {
		if (isNative(method) || isAbstract(method))
			return;
		System.out.println("TRANSFORMING " + method.getName());
		CodeConverter conv = new CodeConverter();
		
		try {
			method.instrument(new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					// methodCounter.put(m.getMethodName(), methodCounter.get(m.getMethodName()) ==
					// null ? 1 : methodCounter.get(m.getMethodName()) + 1);
					
					method.insertAt(m.getLineNumber(),String.format(
							"%s.startTrackingMethod( java.lang.Thread.currentThread().getId() + \"_%s\" );", 
							thisClassName, m.getMethodName())
					);

					method.insertAt(m.getLineNumber() + 1, String.format(
							"%s.stopTrackingMethod( java.lang.Thread.currentThread().getId() + \"_%s\" );",
							thisClassName, m.getMethodName())
					);
					
//					try {
//						System.err.println(thisClass.getDeclaredMethod("startTrackingMethod").getSignature());
//						
//						conv.insertBeforeMethod(m.getMethod(), thisClass.getDeclaredMethod("startTrackingMethod"));
//						//conv.insertAfterMethod(m.getMethod(), thisClass.getDeclaredMethod("stopTrackingMethod"));
//						
//					} catch (NotFoundException e) {
//						e.printStackTrace();
//					}

					System.out.printf("%s wird von %s von Zeile %d aufgerufen.%n", 
							m.getMethodName(), method.getName(), m.getLineNumber());
				}
			});
			
			method.instrument(conv);
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}

	}

	public static Map<String, Long> getMethodTimer() {
		return methodTimer;
	}

	private static boolean isNative(CtMethod method) {
		return Modifier.isNative(method.getModifiers());
	}

	private static boolean isAbstract(CtMethod method) {
		return Modifier.isAbstract(method.getModifiers());
	}

}
