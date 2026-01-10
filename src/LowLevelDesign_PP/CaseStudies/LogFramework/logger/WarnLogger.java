package logframework.logger;

import logframework.Publisher;
import logframework.data.LogLevel;

public class WarnLogger implements Logger {

	private final Logger nextLogger;
	private final Publisher logPublisher;
	
	public WarnLogger(Logger nextLogger, Publisher publisher) {
		super();
		this.nextLogger = nextLogger;
		this.logPublisher = publisher;
	}


	@Override
	public void log(LogLevel logLevel, String message) {
		// TODO Auto-generated method stub
		if(logLevel.getLevel() == LogLevel.WARN.getLevel()) {
			logPublisher.notify(LogLevel.WARN + " " + message);
		}
		
		nextLogger.log(logLevel, message);
			
	}

}
