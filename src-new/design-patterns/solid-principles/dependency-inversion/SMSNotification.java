package DependencyInjectionPrinciple;

public class SMSNotification implements NotificationSender {

	public void sendNotification(int notificationId, int customerId) {
		System.out.println("Notification"+ notificationId+ "send vias SMS to customer" + customerId);
	}
	
}
