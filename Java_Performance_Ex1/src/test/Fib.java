package test;

public class Fib {
	
	public static void main(String[] args) {
		System.out.println(fib(6));	//Fib(5) = 5, Fib(6) = 8
	}

	private static int fib(int n) {
		if (n <= 1) {
			return n;
		} else {
			return fib(n - 1) + fib(n - 2);
		}
	}
	
}











