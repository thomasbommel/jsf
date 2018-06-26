package agent;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

public class HotMethodAgent {
	
	protected static final boolean DEBUG = false;
	
	public static void premain(String args, Instrumentation instrumentation) {
		//filter out classes to transform from args-String
		String[] packageStrings = args.split(";");
		List<String> includedClasses = new ArrayList<>();
		for (String p : packageStrings) {
			int colonIndex = p.indexOf(':');
			String packageName = p.substring(0, colonIndex);
			String classesString = p.substring(colonIndex + 1);
			for (String className : classesString.split(",")) {
				includedClasses.add(packageName + "." + className);
			}
		}
		//add a transformer with the specified classes (automatically transforms classes that are being loaded)
		instrumentation.addTransformer(new MixedTransformer(includedClasses));
		
		//load all classes that should be transformed
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		for (String className : includedClasses) {
			try {
				if (DEBUG) System.out.printf("------------------------------------> LOADING CLASS \"%s\" <------------------------------------%n", className);
				Class.forName(className, true, loader);		//load and therefore transform class
			} catch (ClassNotFoundException e) { e.printStackTrace(); }
		}
	}
}
