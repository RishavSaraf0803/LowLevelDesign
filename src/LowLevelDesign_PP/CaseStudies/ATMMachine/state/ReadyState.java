package atmmachine.state;

import atmmachine.ATM;
import atmmachine.Card.CardDetails;

public class ReadyState implements State {

	private final ATM atm;
	
	
	
	public ReadyState(ATM atm) {
		super();
		this.atm = atm;
	}

	@Override
	public int init() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean readCard(CardDetails cardDetails) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelTransaction(int transId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean readWithdrawalDetails(CardDetails cardDetails, int transId, float amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float dispenceCash(int transId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void ejectCard() {
		// TODO Auto-generated method stub
		
	}

}
