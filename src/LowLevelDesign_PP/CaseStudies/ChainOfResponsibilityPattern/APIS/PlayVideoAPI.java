package withoutcor.APIS;

import java.util.List;

import withoutcor.data.Request;
import withoutcor.data.Response;
import withoutcor.factory.RequestHandlerFactory;
import withoutcor.handler.RequestHandler;

public class PlayVideoAPI {

	RequestHandler requestHandler;
	
	
	public PlayVideoAPI() {
		super();
		
	}

	public Response playVideo(Request request) {
		
		handle(request);
		return null;
	}
	
	private void handle(Request request) {
		//Validation
		//Authentication
		//Autherisation
		

		 requestHandler = RequestHandlerFactory.getRequestHandler();
		 requestHandler.handle(request);
	
	}
}
