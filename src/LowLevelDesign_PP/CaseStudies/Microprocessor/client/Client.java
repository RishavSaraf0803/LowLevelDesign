package microprocessor.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import microprocessor.Microprocessor;
import microprocessor.Register;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        Register A = new Register();
        Register B = new Register();
        Register C = new Register();
        Register D = new Register();
        List<Register> list = Arrays.asList(A,B,C,D);
        
		Microprocessor microprocessor = new Microprocessor(list);
		microprocessor.set(A,15);
		microprocessor.decrement(B);
		microprocessor.move(A, B);
		for(Register register: microprocessor.getRegisters()) {
			System.out.println(register.getValue());
		}
	
		
	}

}
