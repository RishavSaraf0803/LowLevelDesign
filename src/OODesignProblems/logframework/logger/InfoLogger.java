package logframework.logger;

import logframework.Publisher;
import logframework.data.LogLevel;

public class InfoLogger implements Logger {

	private final Logger nextLogger;
	private final Publisher logPublisher;
	
	public InfoLogger(Logger nextLogger, Publisher logPublisher) {
		super();
		this.nextLogger = nextLogger;
		this.logPublisher = logPublisher;
	}

	@Override
	public void log(LogLevel logLevel, String message) {
		// TODO Auto-generated method stub

		if(logLevel.getLevel() == LogLevel.INFO.getLevel()) {
			logPublisher.notify(LogLevel.INFO + " " + message);
			
		}
		nextLogger.log(logLevel, message);
	}

}
