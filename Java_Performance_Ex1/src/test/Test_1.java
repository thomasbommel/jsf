package test;

public class Test_1 {

	public static void main(String[] args) {
		//for (int i = 0; i < 1000000; i++);
		
		testmethod2();
		//System.currentTimeMillis();

		testmethod();
		
		//Test_1 x = new Test_1("");
		
		//testmethod2();
	}
	
	public static void testmethod() {
		
		String text = new String("test");
		
		System.err.println("yolo " + text);
	}
	
	public Test_1() {
		
		System.err.println("test constr");
		
	}
	
	public Test_1(String x) {
		
		System.err.println("test constr2");
		
	}
	
	private static void testmethod2() {
		System.err.println("hi");
	}
	
}
