package logframework.factory;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import logframework.ConsoleSubscriber;
import logframework.FileSubscriber;
import logframework.LogPublisher;
import logframework.Subscriber;
import logframework.logger.DebugLogger;
import logframework.logger.ErrorLogger;
import logframework.logger.FatalLogger;
import logframework.logger.IdleLogger;
import logframework.logger.InfoLogger;
import logframework.logger.Logger;
import logframework.logger.WarnLogger;

public class LoggerFactory {

	
	private LoggerFactory() {
		
	}
	
	public static Logger getLogger() {
		//subscribers.add(new FileSubscriber(new FileWriter()));
		LogPublisher logPublisher = new LogPublisher();
		logPublisher.subscribe(new ConsoleSubscriber());
		return new DebugLogger(new InfoLogger(new WarnLogger(new ErrorLogger(new FatalLogger(new IdleLogger(),
				logPublisher),logPublisher),logPublisher),logPublisher),logPublisher);
	}
}
