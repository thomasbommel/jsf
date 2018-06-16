package agent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class HotMethodFinder implements ClassFileTransformer {

	public final String[] includedPackages;

	private final Map<String, Integer> methodCounter;
	public static final HashMap<String, Long> methodTimer = new HashMap<>();

	public static void startTrackingMethod(String key) {

		methodTimer.put(key.trim(), System.nanoTime());

	}

	public static void stopTrackingMethod(String key) {
		Long startTime = methodTimer.get(key.trim());
		long timeNeededForExecution = System.nanoTime() - startTime;
		System.out.println(key + " " + timeNeededForExecution);

	}

	public HotMethodFinder(String[] includedPackages) {
		this.includedPackages = includedPackages;
		methodCounter = new HashMap<>();
	}

	@Override
	public byte[] transform(ClassLoader loader, String name, Class<?> ___, ProtectionDomain __, byte[] classfile)
			throws IllegalClassFormatException {
		try {
			ClassPool pool = ClassPool.getDefault(); // TODO
			CtClass clazz = pool.makeClass(new ByteArrayInputStream(classfile));

			boolean isIncluded = false;
			for (String packageName : includedPackages) {
				if (clazz.getPackageName().startsWith(packageName)) {
					isIncluded = true;
					break;
				}
			}
			if (!isIncluded)
				return classfile; // only transform classes in included packages

			for (CtMethod method : clazz.getDeclaredMethods()) {
				transform(method); // transform methods
			}
			for (CtConstructor constructor : clazz.getDeclaredConstructors()) {
				transform(constructor.toMethod(constructor.getName(), clazz)); // transform constructors
			}
			return clazz.toBytecode();
		} catch (IOException | RuntimeException | CannotCompileException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void transform(CtMethod method) {
		if (isNative(method) || isAbstract(method))
			return;
		System.out.println("TRANSFORMING " + method.getName());

		try {
			method.instrument(new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					// methodCounter.put(m.getMethodName(), methodCounter.get(m.getMethodName()) ==
					// null ? 1 : methodCounter.get(m.getMethodName()) + 1);
					method.insertAt(m.getLineNumber(),
							"agent.HotMethodFinder.startTrackingMethod( java.lang.Thread.currentThread().getId() + \"_"
									+ m.getMethodName() + "\" );");

					method.insertAt(m.getLineNumber() + 1,
							"agent.HotMethodFinder.stopTrackingMethod( java.lang.Thread.currentThread().getId() + \"_"
									+ m.getMethodName() + "\" );");

					// method.insertAt(m.getLineNumber(), String.format(
					// "agent.HotMethodFinder.getMethodTimer().put(\"%s\"+Thread.currentThread().getId(),
					// new Long(System.nanoTime()));",
					// m.getMethodName())
					// );
					// method.insertAt(m.getLineNumber()+1, String.format(
					// "System.out.println(new Long(System.nanoTime()) -
					// agent.HotMethodFinder.getMethodTimer().get(\"%s\"+Thread.currentThread().getId()));",
					// m.getMethodName())
					// );
					System.out.printf("%s wird von %s von Zeile %d aufgerufen.%n", m.getMethodName(), method.getName(),
							m.getLineNumber());
				}
			});
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
