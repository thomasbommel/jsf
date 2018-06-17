package agent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HotMethodLogger {
	
	private static final Map<String, Integer> methodCounter = new HashMap<>();
	public static final Map<String, Long> methodTimer = new ConcurrentHashMap<>();

	public static void startTrackingMethod(String key) {
		key = key.trim();

		methodTimer.put(key, System.nanoTime());
		// System.out.println("Thread id: "+Thread.currentThread().getId());

		System.out.printf("-> %-20s%n", key);

	}

	public static void stopTrackingMethod(String key) {
		key = key.trim();

		Long startTime = methodTimer.get(key);
		long timeNeededForExecution = System.nanoTime() - startTime;
		System.out.printf("<- %-20s: %10.2f [ms]%n", key, (float) timeNeededForExecution / 1000000.0f);

	}
	
}
