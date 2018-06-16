package ue01;

import java.lang.instrument.Instrumentation;

public class SpecialAgent {
	
	public static void premain(String args, Instrumentation instrumentation) {
		/*String classname = args.substring(0, args.indexOf("::")).replace('.', '/');
		String methodname = args.substring(args.indexOf("::") + 2);
		
		instrumentation.addTransformer(new MyHighLevelTransformer(classname, methodname), true);
		//instrumentation.addTransformer(new MyLowLevelTransformer(classname, methodname), true);
		Class<?>[] classes = instrumentation.getAllLoadedClasses();
		
		for(Class<?> clazz : classes) {
			if(!instrumentation.isModifiableClass(clazz)) {
				continue;
			}
			instrumentation.retransformClasses(clazz);
		}	*/
		
		
		System.out.println(args);
	}

}
