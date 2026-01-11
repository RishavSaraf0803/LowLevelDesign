package logframework;

public class ConsoleSubscriber implements Subscriber {

	@Override
	public void update(String message) {
		// TODO Auto-generated method stub

		System.out.println(message);
	}

}
