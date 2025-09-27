package EnquiryHandler.apis;

import EnquiryHandler.handler.EnquiryHandleFactory;
import EnquiryHandler.handler.EnquiryHandler;

public class HandleEnquiryAPI {

	
	public void handleEnquiry(String enquiry) {
		EnquiryHandler enquiryHandler = EnquiryHandleFactory.getEnquiryHandler();
		enquiryHandler.hanlde(enquiry);
	}
}
