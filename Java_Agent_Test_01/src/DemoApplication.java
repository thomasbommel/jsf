public class DemoApplication {
	public static void main(String[] args) {
		System.out.println("[Application - Main] Start application");
		suspend(1000);
		String value = "Demonstration of Java bytecode manipulation capabilities";
		Text text = new Text();
		System.out.println("[Application - Main] Value passed to text display: " + value);
		text.display(value);
		System.out.println("[Application - Main] Complete application");
	}

	private static void suspend(long sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();

		}
	}
}