package EnquiryHandler.handler;

import EnquiryHandler.Data.EnquiryType;

public class AcademicEnquiryHandler implements EnquiryHandler {

	
	private final EnquiryHandler nextHandler;
	
	
	public AcademicEnquiryHandler(EnquiryHandler nextHandler) {
		super();
		this.nextHandler = nextHandler;
	}


	@Override
	public EnquiryType hanlde(String enquiry) {
		// TODO Auto-generated method stub
		System.out.println("Academic Handler");
		if(enquiry.contains("DS Algo") || enquiry.contains("Design")) {
			return EnquiryType.ACADEMIC;
		}
		return nextHandler.hanlde(enquiry);
	}

}
