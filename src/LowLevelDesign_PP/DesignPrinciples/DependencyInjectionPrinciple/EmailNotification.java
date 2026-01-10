package DependencyInjectionPrinciple;

public class EmailNotification implements NotificationSender{

	public void sendNotification(int notificationId, int customerId) {
		System.out.println("Notification"+ notificationId+ "send vias email to customer" + customerId);
	}
	
}
