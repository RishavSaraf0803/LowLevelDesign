package DependencyInjectionPrinciple;

public class PurchaseFlowManager {

	
	private final PaymentProcessor paymentProcessor;
	private final NotificationSender notificationSender;
	
	public PurchaseFlowManager( PaymentProcessor paymentProcessor, NotificationSender notificationSender) {
		this.paymentProcessor = paymentProcessor;
		this.notificationSender = notificationSender;
	}
	
	


	public void triggerPaymentProcessor(int pid, int cid) {
		paymentProcessor.processPayment(pid, cid);
		
		notificationSender.sendNotification(pid, cid);
	}
}
