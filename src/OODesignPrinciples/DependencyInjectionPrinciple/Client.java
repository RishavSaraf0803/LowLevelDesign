package DependencyInjectionPrinciple;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PurchaseFlowManager pfm = new PurchaseFlowManager(new NetBanking(), new EmailNotification());
		pfm.triggerPaymentProcessor(0, 0);
		
		PurchaseFlowManager altpfm = new PurchaseFlowManager(new UPI(), new SMSNotification());
		altpfm.triggerPaymentProcessor(0, 0);

	}

}
