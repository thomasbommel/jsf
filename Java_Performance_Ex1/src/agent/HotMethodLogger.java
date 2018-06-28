package agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;

public class HotMethodLogger {
	
	protected static PrintStream runtimeLoggingStream, statisticsLoggingStream;
	static {
		runtimeLoggingStream = System.err;		//Default console stream
		statisticsLoggingStream = System.err;
		try {
			runtimeLoggingStream = new PrintStream(new File("log.txt"));
			statisticsLoggingStream = new PrintStream(new File("statistics.txt"));
		} catch (FileNotFoundException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	private static final String methodTimerKeyFormat = "%s_%s_%d";
	private static final StackMap<String, MethodCall> methodTimer = new StackMap<>();
	private static final HotMethodStatistics methodStatistics = new HotMethodStatistics();
	
	//                --- Runtime Logging ---
	
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
		
		runtimeLoggingStream.printf("%-50s ----> %50s%n", callSite, String.format("%s(%s)", getSimpleMethodName(calledMethod), argString));
		
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
		
		runtimeLoggingStream.printf("%-50s <---- %50s [wall = %.2fms, cpu = %.2fms%s]%n",
				callSite, String.format("%s(%s)", getSimpleMethodName(calledMethod), args), 
				(double) wallTimePassed / HotMethodStatistics.nanosToMillis, (double) cpuTimePassed / HotMethodStatistics.nanosToMillis,
				retVal == null ? "" : ", ret=" + retVal.toString()
		);
		
		call.wallTimePassedNanos = wallTimePassed;
		call.cpuTimePassedNanos = cpuTimePassed;
		methodStatistics.recordMethodCall(call);
	}
	
	private static String getSimpleMethodName(String methodNameWithPackages) {
		return methodNameWithPackages.substring(methodNameWithPackages.lastIndexOf('.') + 1);
	}
	
}
