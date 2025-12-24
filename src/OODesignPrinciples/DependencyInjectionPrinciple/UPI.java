package DependencyInjectionPrinciple;

public class UPI implements PaymentProcessor {

	public void processPayment(int productId, int customerId) {
		System.out.println(" Process payment "+productId+"for customer"+customerId+ "via UPI");
	}
}
