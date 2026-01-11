package logframework.logger;

import logframework.Publisher;
import logframework.data.LogLevel;

public class ErrorLogger implements Logger {

	private final Logger nextLogger;
	private final Publisher logPublisher;
	
	public ErrorLogger(Logger nextLogger,Publisher publisher) {
		super();
		this.nextLogger = nextLogger;
		this.logPublisher = publisher;
	}
	


	@Override
	public void log(LogLevel logLevel, String message) {
		// TODO Auto-generated method stub

		if(logLevel.getLevel() == LogLevel.ERROR.getLevel()) {
			logPublisher.notify(LogLevel.ERROR + " " + message);
			
		}
		nextLogger.log(logLevel, message);
	}

}
