package EnquiryHandler.handler;

import EnquiryHandler.Data.EnquiryType;

public class SubscriptionEnquiryHandler implements EnquiryHandler {

	EnquiryHandler nextHandler;
	
	
	public SubscriptionEnquiryHandler(EnquiryHandler nextHandler) {
		super();
		this.nextHandler = nextHandler;
	}


	@Override
	public EnquiryType hanlde(String enquiry) {
		// TODO Auto-generated method stub

		System.out.println("Subscription Handler");
		if(enquiry.contains("Subscription")) {
			return EnquiryType.SUBSCRIPTION;
		}
		return nextHandler.hanlde(enquiry);
		
	}

}
