package test2;

public class Test_Uninstrumented {

	public static void uninstrumentedSleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
