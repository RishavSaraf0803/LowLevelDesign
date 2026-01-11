package withoutcor.factory;

import withoutcor.handler.AuthenticationHandler;
import withoutcor.handler.AutherizationHandler;
import withoutcor.handler.IdleHandler;
import withoutcor.handler.RequestHandler;
import withoutcor.handler.ValidationHandler;
import withoutcor.manager.TokenManager;
import withoutcor.manager.UserManager;

public class RequestHandlerFactory {

	private RequestHandlerFactory() {
		
	}
	
	public static RequestHandler getRequestHandler() {
		return createValidationHandler();
	}
	
	private static RequestHandler createValidationHandler() {
		return new ValidationHandler(createAuthenticationHandler());
	}
	
	private static RequestHandler createIdleHandler() {
		return new IdleHandler();
	}
	
	private static RequestHandler createAutherizationHandler() {
		return new AutherizationHandler(createIdleHandler(), new UserManager());
	}
	
	private static RequestHandler createAuthenticationHandler() {
		return new AuthenticationHandler(createAutherizationHandler(), new TokenManager());
	}
	
	
}
