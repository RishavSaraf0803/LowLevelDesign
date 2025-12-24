package microprocessor;

import java.util.List;

public class Microprocessor {

	private List<Register> registers;



	public Microprocessor(List<Register> registers) {
		super();
		this.registers = registers;
	}
	
	public List<Register> getRegisters() {
		return this.registers;
	}



	public void set(Register register , int value) {
		for(Register mRegister : registers)
			if(register.equals(mRegister)) {
				mRegister.setValue(value);
		}
		
		
	}
	public void increment(Register A) {
		for(Register register : registers)
			if(A.equals(register)) {
				register.setValue(register.getValue()+1);
		}
	}
	public void decrement(Register A) {
		for(Register register : registers)
			if(A.equals(register)) {
				register.setValue(register.getValue()-1);
		}
	}
	public void reset() {
		for(Register register : registers) {
			register.setValue(0);
		}
	}
	public void add(Register A, int value) {
		for(Register register : registers)
			if(A.equals(register)) {
				register.setValue(register.getValue() + value);
		}
	}
	public void adr(Register A, Register B) {
		for(Register register: registers) {
			if(A.equals(register)) {
				register.setValue(register.getValue() + B.getValue());
			}
		}
	}
	public void move(Register A, Register B) {
		for(Register register: registers) {
			if(A.equals(register)) {
				register.setValue(B.getValue());
			}
		}
	}
	
	
}
