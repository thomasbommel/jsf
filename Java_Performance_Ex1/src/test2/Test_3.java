package test2;

public class Test_3 {

	//this method was intented to have a "double" parameter and "long" return value
	public static int otherPackageStaticMethod(float floatToRound) {
		
		//passing Double causes JVM to crash -- why? 
		return Math.round(floatToRound);
		
	}
	
	public int otherPackageMethod(float floatToRound) {
		return Math.round(floatToRound);
	}
	
}
