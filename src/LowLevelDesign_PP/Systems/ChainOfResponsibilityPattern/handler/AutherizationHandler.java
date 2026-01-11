package withoutcor.handler;

import withoutcor.data.Request;
import withoutcor.manager.UserManager;

public class AutherizationHandler implements RequestHandler {
	
	private final RequestHandler nextHandler;
    private final UserManager userManager;
	
	public AutherizationHandler(RequestHandler nextHandler,UserManager userManager) {
		super();
		this.nextHandler = nextHandler;
		this.userManager = userManager;
	}


	@Override
	public void handle(Request request) {
		// TODO Auto-generated method stub
		if(!this.userManager.isSubscribed(request.getToken())) {
			throw new RuntimeException("Authorization failed");
		}
		System.out.println("Autherisation Passed");
		this.nextHandler.handle(request);
	}

}
