public class Text {
	public void display(String text) {
		long sleepTime = 1000;
		long sleepStartTime;
		long sleepEndTime;
		System.out.println("[Application - Text display] Text display is going to sleep for " + sleepTime + " ms");
		sleepStartTime = System.nanoTime();
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sleepEndTime = System.nanoTime();
		System.out.println("[Application - Text display] Text display wakes up");
		System.out.println("[Application - Text display] Text display sleep time: "
				+ ((sleepEndTime - sleepStartTime) / 1000000) + " ms");
		System.out.println("[Application - Text display] Output: " + text);
	}
}
