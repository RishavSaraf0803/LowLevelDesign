package atmmachine.DataBase;

import atmmachine.state.ATMState;

public class DBAccessor {

	
	private DBAccessor() {}
	
	public static ATMState getATMState(String machineId) {
		return ATMState.READY;
	}
	
}
