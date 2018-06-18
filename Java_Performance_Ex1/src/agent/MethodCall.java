package agent;

public class MethodCall {

	protected final String caller, callee, args;
	protected final long threadID, wallClockTimeNanos, cpuTimeNanos;
	
	public MethodCall(String caller, String callee, String args, long threadID, long callTimeNanoSeconds, long cpuTime) {
		super();
		this.caller = caller;
		this.callee = callee;
		this.args = args;
		this.threadID = threadID;
		this.wallClockTimeNanos = callTimeNanoSeconds;
		this.cpuTimeNanos = cpuTime;
	}

	@Override
	public String toString() {
		return "MethodCall [caller=" + caller + ", callee=" + callee + ", args=" + args + ", threadID=" + threadID
				+ ", wallClockTime=" + wallClockTimeNanos + ", cpuTime=" + cpuTimeNanos + "]";
	}
	
}
