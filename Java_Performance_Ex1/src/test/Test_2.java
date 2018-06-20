package test;

public class Test_2 extends Test_1 {

	@Override
	public String toString() {
		return "This is a subclass";
	}
	
	public void testmethod3() {
		
		System.out.println("Subclass methodcall ;; " + super.return5());
		
		super.nonStaticEmpty("super call");
		
	}
	
}
