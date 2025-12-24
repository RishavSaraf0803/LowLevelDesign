package ElevatorSystem.data;

import java.util.List;

public class Elevator {
    
    private List<Floor> floors;
    private int currentLevel;
    private int minlevel;
    private int maxlevel;



    public Elevator(int minfloor,int maxFloor) {
        this.minlevel = minfloor;
        this.maxlevel = maxFloor;
        this.currentLevel = minfloor;
        this.floors = new java.util.ArrayList<>();
        for (int i = minfloor; i <= maxFloor; i++) {
            this.floors.add(new Floor(i));
        }
    }

    private void opengate(int level){
        floors.get(level).getGate().open();
    }
    private void closegate(int level){
        floors.get(level).getGate().close();

    }
    public int moveElevator(int level){
        // First, close the gate of the *current* floor before moving
        closegate(currentLevel);

        // Move elevator to target level (if within range)
        if (level < minlevel || level > maxlevel) {
            System.out.println("Requested level out of bounds.");
            return currentLevel;
        }

        // Simulate elevator moving
        System.out.println("Elevator moving from floor " + currentLevel + " to floor " + level);

        // Arrive at new floor and open gate
        currentLevel = level;
        opengate(currentLevel);
        System.out.println("Elevator arrived at floor " + currentLevel + ", gate opened.");

        return currentLevel;
    }

    public Floor getCurrentFloor() {
        // TODO Auto-generated method stub
        return floors.get(currentLevel);
    }

    public Floor getFloor(int i) {
        // TODO Auto-generated method stub
        return floors.get(i);
    }

    


   
}
