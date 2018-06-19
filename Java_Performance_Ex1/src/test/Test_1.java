package test;

public class Test_1 {

	public static void main(String[] args) {
		for (int i = 0; i < 1000000; i++);
		
		Test_1 x = new Test_1("hello", ": hello 2");
		x.nonStaticEmpty("objectref call");
		
		return5();
		
		testmethod2('k');
		System.currentTimeMillis();
		
		recursive(3);

		System.out.println(new StringBuilder("tezzt").append(" test"));
		
		new Test_1();
		
		testmethod2('x');
	}
	
	private static int recursive(int n) {
		if (n > 0) {
			recursive(n-1);
		}
		return n;
	}
	
	public static int return5() {
		return 5;
	}
	
	public Test_1() {
		
		try {
			//only allowed since not transformed for some reason?
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("test constr");
		nonStaticEmpty("call nonStaticEmpty");
		
	}
	
	public Test_1(String x, String y) {
		
		System.out.println("test constr2: " + x + y);
		
	}
	
	protected static void testmethod2(char c) {
		
		
		System.out.println(c);
	}
	
	private void nonStaticEmpty(String x) {
		
	}
	
}
