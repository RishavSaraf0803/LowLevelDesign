package EnquiryHandler.handler;

public class EnquiryHandleFactory {

	
	private EnquiryHandleFactory() {
		
	}
	
	public static EnquiryHandler getEnquiryHandler() {
		return new LogHandler(new SubscriptionEnquiryHandler(new ProjectEnquiryHandler(new AcademicEnquiryHandler(new IdleHandler()))));
	}
}
