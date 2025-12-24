package ElevatorSystem.data;

public class Floor {
    private final int level;
    private Gate gate;
    // private boolean isAccessible;

    public Floor(int level) {
        this.level = level;
        this.gate = new Gate();
    }

    public int getLevel() {
        return level;
    }

    public Gate getGate() {
        return gate;
    }
    
}
