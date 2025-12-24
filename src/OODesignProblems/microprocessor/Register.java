package microprocessor;

public class Register {

	private int value;
	public Register() {
		this.value = 0;
	}

	public int getValue() {
		return value;
	}

	protected void setValue(int value) {
		this.value = value;
	}
	
}
