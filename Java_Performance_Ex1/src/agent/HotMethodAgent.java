package agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class HotMethodAgent {
	
	//CPU time: ManagementFactory.getThreadMXBean().getThreadCpuTime(new Thread().getId());
	
	public static void premain(String args, Instrumentation instrumentation) {
		String[] includedPackages = args.split(";");
		//instrumentation.addTransformer(new HotMethodFinder(includedPackages));
		//instrumentation.addTransformer(new LowLvlTransformer(includedPackages));
		instrumentation.addTransformer(new MixedTransformer(includedPackages));
		
		Class<?>[] classes = instrumentation.getAllLoadedClasses();
		for(Class<?> clazz : classes) {
			if(instrumentation.isModifiableClass(clazz)) {
				try {
					instrumentation.retransformClasses(clazz);
				} catch (UnmodifiableClassException e) { e.printStackTrace(); }
			}
		}
		
		//TODO: swing Window: LoggerWindow();
	}
}
