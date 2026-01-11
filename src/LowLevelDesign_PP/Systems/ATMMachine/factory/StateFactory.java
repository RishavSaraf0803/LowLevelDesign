package atmmachine.factory;

public class StateFactory {

	private StateFactory() {
		
	}
	public static State getState(ATMState atmState, ATM atm) {
		State state = null;
		if(atmState.equals(ATMState.READY)) {
			state = new ReadyState(atm);
		}
		else if(atmState.equals(ATMState.CARD_READING)) {
			state = new CardReadingState(atm);
		}
		else if(atmState.equals(ATMState.WITHDRAWAL_DETAIL)) {
			state = new WithdrawalDetailReadingState(atm);
		}
		else if(atmState.equals(ATMState.CADR_READING)) {
			state = new CardReadingState(atm);
		}
		else if(atmState.equals(ATMState.CASH_DISPENCING)) {
			state = new CashDispencingState(atm);
		}
		else if(atmState.equals(ATMState.CADR_EJECTING)) {
			state = new CardEjectingState(atm);
		}
		else {
			
		}
	}
}
