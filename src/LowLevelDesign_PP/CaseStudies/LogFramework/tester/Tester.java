package logframework.tester;

import logframework.data.LogLevel;
import logframework.factory.LoggerFactory;
import logframework.logger.Logger;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Logger logger = LoggerFactory.getLogger();
		logger.log(LogLevel.ERROR, "Error is logged");
	}

}
