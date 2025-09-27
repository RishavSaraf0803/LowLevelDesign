package EnquiryHandler.handler;

import EnquiryHandler.Data.EnquiryType;

public class LogHandler implements EnquiryHandler {

	private final EnquiryHandler nextHandler;
	
	
	public LogHandler(EnquiryHandler nextHandler) {
		super();
		this.nextHandler = nextHandler;
	}


	@Override
	public EnquiryType hanlde(String enquiry) {
		// TODO Auto-generated method stub
		System.out.println(enquiry);
		EnquiryType enquiryType = this.nextHandler.hanlde(enquiry);
		System.out.println("Inside log Handler");
		System.out.println(enquiryType);
		return null;
	}

}
