package agent;

import java.lang.management.ManagementFactory;

public class HotMethodLogger {
	
	private static final String methodTimerKeyFormat = "%s_%s_%d";
	private static final StackMap<String, MethodCall> methodTimer = new StackMap<>();
	
	public static void startTrackingMethod(Object[] argsInReverseOrder, String callSite, String calledMethod) {
		int i = 0;
		String args[] = new String[argsInReverseOrder.length];		
		for (Object o : argsInReverseOrder) {
			String format = "%s";
			if (o instanceof String) {
				format = "\"%s\"";
			}
			else if (o instanceof Character) {
				format = "'%s'";
			}
			args[i++] = String.format(format, o.toString());
		}
		
		String argString = "";
		for (i -= 1; i >= 0; i--) {
			argString += args[i];
			if (i > 0) argString += ", ";
		}
		
		System.err.printf("%-40s ----> %40s%n", callSite, String.format("%s(%s)", calledMethod, argString));
		
		long curThreadID = Thread.currentThread().getId();
		methodTimer.add(String.format(methodTimerKeyFormat, callSite, calledMethod, curThreadID),
				new MethodCall(callSite, calledMethod, argString, curThreadID, 
						System.nanoTime(), ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime())
		);
	}
	
	public static void stopTrackingMethod(Object retVal, String callSite, String calledMethod) {
		MethodCall call = methodTimer.remove(String.format(methodTimerKeyFormat, callSite, calledMethod, Thread.currentThread().getId()));
		
		String args = call.args;
		long wallTimePassed = System.nanoTime() - call.wallClockTimeNanos;
		long cpuTimePassed = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - call.cpuTimeNanos;
		double nanosToMillis = 1000000.0;
		
		System.err.printf("%-40s <---- %40s [wall = %.2f ms, cpu = %.2f ms, ret=%s]%n", 
				callSite, String.format("%s(%s)", calledMethod, args), 
				(double) wallTimePassed / nanosToMillis, cpuTimePassed / nanosToMillis, 
				retVal == null ? "" : retVal.toString()
		);
	}
	
}
