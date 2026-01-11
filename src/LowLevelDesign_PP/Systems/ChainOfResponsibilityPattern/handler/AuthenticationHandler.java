package withoutcor.handler;

import withoutcor.data.Request;
import withoutcor.manager.TokenManager;

public class AuthenticationHandler  implements RequestHandler{
	
	private final RequestHandler nextHandler;
	
	private final TokenManager tokenManager;
	public AuthenticationHandler(RequestHandler nextHandler, TokenManager tokenManager) {
		super();
		this.nextHandler = nextHandler;
		this.tokenManager = tokenManager;
		
	}


	@Override
	public void handle(Request request) {
		// TODO Auto-generated method stub
		
		String email = this.tokenManager.getEmailFromToken(request.getToken());
		if(!isValidEmail(email)) {
			throw new RuntimeException("Authentication Failed");
		}
		System.out.println("Authentication Passed");
		nextHandler.handle(request);
	}
	
	
	private boolean isValidEmail(String email) {
		return true;
	}

}
