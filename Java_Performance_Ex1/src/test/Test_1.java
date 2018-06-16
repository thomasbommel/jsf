package test;

public class Test_1 {

	public static void main(String[] args) {
		//for (int i = 0; i < 1000000; i++);
		
		
		System.currentTimeMillis();

		testmethod();
		
		Test_1 x = new Test_1("");
		
	}
	
	public static void testmethod() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String text = new String("test");
		
		System.out.println("yolo " + text);
	}
	
	public Test_1() {
		
		System.out.println("test constr");
		
	}
	
	public Test_1(String x) {
		
		System.out.println("test constr2");
		
	}
	
}
