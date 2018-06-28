package agent;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class HotMethodStatistics {

	public static final long nanosToMillis = 1000000;
	public static final long nanosToSeconds = 1000000000;
	
	private final PrintStream outStream;
	private StatisticsThread statisticsThread = null;
	
	private long totalWallTimePassed = 0, totalCPUTimePassed = 0;
	private QueueMap<String, MethodCall> methodCalls;
	
	public HotMethodStatistics() {
		outStream = HotMethodLogger.statisticsLoggingStream;
		methodCalls = new QueueMap<>();
	}
	
	/**
	 * Records a method call for the statisticsThread.
	 */
	public void recordMethodCall(MethodCall mc) {
		if (statisticsThread == null) {
			statisticsThread = new StatisticsThread(Thread.currentThread());
			statisticsThread.start();
		}
		totalWallTimePassed += mc.wallTimePassedNanos;
		totalCPUTimePassed += mc.cpuTimePassedNanos;
		methodCalls.add(mc.callee, mc);
	}
	
	private class StatisticsThread extends Thread {
		
		private final Thread instrumentedThread;
		
		public StatisticsThread(Thread instrumentedThread) {
			this.instrumentedThread = instrumentedThread;
		}
		
		@Override
		public void run() {
			super.run();
			try {
				instrumentedThread.join();	//wait for instrumented program to finish
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			double wallTime = totalWallTimePassed;
			double cpuTime = totalCPUTimePassed;
			
			//then evaluate and print gathered statistics
			outStream.printf("-------> Total times:%n"
					+ "-------> Wallclock:  %8.2fs    (%.2fms),%n"
					+ "-------> CPU time:   %8.2fs    (%.2fms)%n", 
					wallTime / nanosToSeconds, wallTime / nanosToMillis,
					cpuTime / nanosToSeconds, cpuTime / nanosToMillis);
			outStream.println();
			
			for (String method : methodCalls.keySet()) {
				//calculate data
				int nrCalls = methodCalls.getQueue(method).size();
				Map<String, Integer> callers = new HashMap<>();
				
				double minWallTime = Integer.MAX_VALUE, maxWallTime = Integer.MIN_VALUE, sumWallTime = 0;
				double minCPUTime = Integer.MAX_VALUE, maxCPUTime = Integer.MIN_VALUE, sumCPUTime = 0;
				
				for (MethodCall mc = methodCalls.remove(method); mc != null; mc = methodCalls.remove(method)) {
					if (mc.wallTimePassedNanos < minWallTime)
						minWallTime = mc.wallTimePassedNanos;
					if (mc.wallTimePassedNanos > maxWallTime)
						maxWallTime = mc.wallTimePassedNanos;
					
					if (mc.cpuTimePassedNanos < minCPUTime)
						minCPUTime = mc.cpuTimePassedNanos;
					if (mc.cpuTimePassedNanos > maxCPUTime)
						maxCPUTime = mc.cpuTimePassedNanos;
					
					sumWallTime += mc.wallTimePassedNanos;
					sumCPUTime += mc.cpuTimePassedNanos;
					
					Integer callsFromCaller = callers.get(mc.caller);
					if (callsFromCaller == null)
						callers.put(mc.caller, 1);
					else
						callers.put(mc.caller, callsFromCaller + 1);
				}
				
				//print data
				outStream.printf("-------------------------- Method: %s --------------------------%n", method);
				outStream.printf("-> Total Calls: %d%n", nrCalls); 
				outStream.printf("-> Sum walltime (%% of total):  %8.2fms (%.2f%%)%n", sumWallTime/nanosToMillis, 100*sumWallTime/wallTime);
				outStream.printf("-> Sum cputime  (%% of total):  %8.2fms (%.2f%%)%n", sumCPUTime/nanosToMillis, 100*sumCPUTime/cpuTime);
				outStream.println();
				outStream.printf("-> Avg walltime (arithmetic):  %8.2fms%n", (sumWallTime/nrCalls)/nanosToMillis);
				outStream.printf("-> Avg cputime  (arithmetic):  %8.2fms%n", (sumCPUTime/nrCalls)/nanosToMillis);
				outStream.println();
				outStream.printf("-> Min / Max walltime:  %8.2fms     /  %8.2fms%n", minWallTime/nanosToMillis, maxWallTime/nanosToMillis);
				outStream.printf("-> Min / Max cputime:   %8.2fms     /  %8.2fms%n", minCPUTime/nanosToMillis, maxCPUTime/nanosToMillis);
				
				outStream.println();
				outStream.printf("-> Callers:%n");
				for (String caller : callers.keySet()) {
					Integer callsFromCaller = callers.get(caller);
					outStream.printf("---> %4d calls", callsFromCaller);
					outStream.printf(" (%.2f%%)", (double) (100*callsFromCaller) / nrCalls);
					outStream.printf(" from \"%s\"%n", caller);
				}
				outStream.println();
				outStream.println();
			}
		}
		
	}
	
}
