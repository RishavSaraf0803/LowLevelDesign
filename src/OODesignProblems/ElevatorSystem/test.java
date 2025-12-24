package ElevatorSystem;

import ElevatorSystem.data.Elevator;

public class test {

    public static void main(String[] args) {
        Elevator elevator = new Elevator(0,20);
        elevator.moveElevator(5);
        elevator.moveElevator(10);
       System.out.print(elevator.getCurrentFloor().getGate().isOpen());
       System.out.print(elevator.getFloor(5).getGate().isOpen());
        
        
       
    }
    
}
