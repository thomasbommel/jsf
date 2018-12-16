package app;

import factory.shared.FactoryApplication;

/**
 * Simple entry point for the application -- starts GUI, monitoring system and with it the rest of the factory.
 */
public class Main {
	
	public static void main(String[] args) {
		FactoryApplication factory = new FactoryApplication();
		factory.start();
	}
	
}
