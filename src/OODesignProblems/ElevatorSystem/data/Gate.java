package ElevatorSystem.data;

public class Gate {
    private boolean isOpen;

    public Gate() {
        this.isOpen = false;
    }

    public void open(){
        if (!isOpen) {
            isOpen = true;
            System.out.println("Gate is now open.");
        }
    }

    public void close(){
        if (isOpen) {
            isOpen = false;
            System.out.println("Gate is now closed.");
        }
    }
    public boolean isOpen(){
        return this.isOpen;
    }
    
}
