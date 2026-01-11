package EnquiryHandler.handler;

import EnquiryHandler.Data.EnquiryType;

public class ProjectEnquiryHandler implements EnquiryHandler {

	EnquiryHandler nextHandler;
	
	public ProjectEnquiryHandler(EnquiryHandler nextHandler) {
		super();
		this.nextHandler = nextHandler;
	}

	@Override
	public EnquiryType hanlde(String enquiry) {
		// TODO Auto-generated method stub

		System.out.println("Project Handler");
		if(enquiry.contains("Project")) {
			return EnquiryType.PROJECTS;
		}
		return  nextHandler.hanlde(enquiry);
	}

}
