package logframework.logger;

import logframework.Publisher;
import logframework.data.LogLevel;

public class DebugLogger implements Logger {

	private Logger nextLogger;
	private final Publisher logPublisher;
	
	public DebugLogger(Logger nextLogger,Publisher publisher) {
		super();
		this.nextLogger = nextLogger;
		this.logPublisher = publisher;
	}

	@Override
	public void log(LogLevel logLevel, String message) {
		// TODO Auto-generated method stub
		if(logLevel.getLevel() == LogLevel.DEBUG.getLevel()) {
			logPublisher.notify(LogLevel.DEBUG + " " + message);
		}
		
		nextLogger.log(logLevel, message);

	}

}
