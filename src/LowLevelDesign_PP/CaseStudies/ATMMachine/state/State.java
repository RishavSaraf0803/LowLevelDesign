package atmmachine.state;

import atmmachine.Card.CardDetails;

public interface State {

	int init();
	boolean readCard(CardDetails cardDetails);
	boolean cancelTransaction(int transId);
	boolean readWithdrawalDetails(CardDetails cardDetails, int transId, float amount);
	
	float dispenceCash(int transId);
	void ejectCard();
	
}
