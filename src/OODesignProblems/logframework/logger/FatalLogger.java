package logframework.logger;

import logframework.Publisher;
import logframework.data.LogLevel;

public class FatalLogger implements Logger {

	private final Logger nextLogger;
	private final Publisher logPublisher;
	public FatalLogger(Logger nextLogger, Publisher publisher) {
		super();
		this.nextLogger = nextLogger;
		this.logPublisher = publisher;
	}

	@Override
	public void log(LogLevel logLevel, String message) {
		// TODO Auto-generated method stub
		if(logLevel.getLevel() == LogLevel.FATAL.getLevel()) {
			logPublisher.notify(LogLevel.FATAL + " " + message);
		}
		nextLogger.log(logLevel, message);
	}

}
