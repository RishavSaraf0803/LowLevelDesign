package DependencyInjectionPrinciple;

public class NetBanking implements PaymentProcessor {
	
	public void processPayment(int productId, int customerId) {
		System.out.println(" Payment Process by Netbaking"+ productId + " for customer"+customerId);
	}

}
