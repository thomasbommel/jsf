import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;

public class DemoClassFileTransformer implements ClassFileTransformer {
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		String instrumentedClassName = "vadim.demo.jvm.app.Text";
		String instrumentedMethodName = "display";
		byte[] bytecode = classfileBuffer;
		try {
			ClassPool cPool = ClassPool.getDefault();
			CtClass ctClass = cPool.makeClass(new ByteArrayInputStream(bytecode));
			CtMethod[] ctClassMethods = ctClass.getDeclaredMethods();
			for (CtMethod ctClassMethod : ctClassMethods) {
				if (ctClassMethod.getDeclaringClass().getName().equals(instrumentedClassName)
						&& ctClassMethod.getName().equals(instrumentedMethodName)) {
					ctClassMethod.insertBefore("System.out.println(\"[Instrumentation] Entering method\");");
					ctClassMethod.insertAfter("System.out.println(\"[Instrumentation] Exiting method\");");
					ctClassMethod.insertAt(24, true,
							"text = \"Original text was replaced with instrumentation by agent\";");
					ExprEditor instrumentationExpressionEditor = new DemoExpressionEditor();
					ctClassMethod.instrument(instrumentationExpressionEditor);
					bytecode = ctClass.toBytecode();
				}
			}
		} catch (IOException e) {
			throw new IllegalClassFormatException(e.getMessage());
		} catch (RuntimeException e) {
			throw new IllegalClassFormatException(e.getMessage());
		} catch (CannotCompileException e) {
			throw new IllegalClassFormatException(e.getMessage());
		}
		return bytecode;
	}
}
