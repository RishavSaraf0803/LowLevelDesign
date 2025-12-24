package atmmachine;

import atmmachine.factory.StateFactory;
import atmmachine.state.State;

public class ATM {

	private State state;
	private final String machineId;
	public ATM (String machineId) {
		this.machineId = machineId;
		this.state = StateFactory.getState(DBAcessor.getATMState(this), null);
	}
	
	public int init() {
		return this.state.beginTransaction();
	}
	public boolean cancel(int transId) {
		return this.state.cancelTransaction(transId);
	}
	
}
