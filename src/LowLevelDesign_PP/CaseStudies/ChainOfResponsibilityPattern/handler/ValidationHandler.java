package withoutcor.handler;

import withoutcor.data.Request;

public class ValidationHandler implements RequestHandler {
	
	private final RequestHandler nextHandler;
	
	

	public ValidationHandler(RequestHandler nextHandler) {
		super();
		this.nextHandler = nextHandler;
	}



	@Override
	public void handle(Request request) {
		// TODO Auto-generated method stub
		if(request.getHeader() == null || request.getHeader().isEmpty()) {
			throw new IllegalArgumentException("empty Request");
		}
		if(request.getBody() == null || request.getBody().isEmpty()) {
			throw new IllegalArgumentException("empty Body");
		}
		System.out.println("Validation Passed");
		nextHandler.handle(request);
	}

}
