package test2;

public class Test_3 {

	//this method was intented to have a "double" parameter and "long" return value
	public static long otherPackageStaticMethod(Double doubleToRound) {
		
		//passing Double causes JVM to crash -- why? 
		return Math.round(doubleToRound);
		
	}
	
	public int otherPackageMethod(float floatToRound) {
		return Math.round(floatToRound);
	}
	
}
