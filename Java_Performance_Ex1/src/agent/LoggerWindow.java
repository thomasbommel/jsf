package agent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class LoggerWindow extends JFrame implements Logger {
	
	static {
		System.out.println("static constr");
	}
	
	private LoggerModel x;
	
	public LoggerWindow() {
		x = new LoggerModel();
	}
	
	@Override
	public void log(String message) {
		System.out.println(message);
	}
	
	
	
}
