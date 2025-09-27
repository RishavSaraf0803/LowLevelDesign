package BuilderPattern;

// Complex Builder Pattern Example - Computer Builder
public class Computer {
    private final String cpu;
    private final String ram;
    private final String storage;
    private final String graphicsCard;
    private final String motherboard;
    private final String powerSupply;
    private final boolean hasWifi;
    private final boolean hasBluetooth;
    private final String operatingSystem;
    
    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.storage = builder.storage;
        this.graphicsCard = builder.graphicsCard;
        this.motherboard = builder.motherboard;
        this.powerSupply = builder.powerSupply;
        this.hasWifi = builder.hasWifi;
        this.hasBluetooth = builder.hasBluetooth;
        this.operatingSystem = builder.operatingSystem;
    }
    
    // Getters
    public String getCpu() { return cpu; }
    public String getRam() { return ram; }
    public String getStorage() { return storage; }
    public String getGraphicsCard() { return graphicsCard; }
    public String getMotherboard() { return motherboard; }
    public String getPowerSupply() { return powerSupply; }
    public boolean hasWifi() { return hasWifi; }
    public boolean hasBluetooth() { return hasBluetooth; }
    public String getOperatingSystem() { return operatingSystem; }
    
    @Override
    public String toString() {
        return "Computer{" +
                "cpu='" + cpu + '\'' +
                ", ram='" + ram + '\'' +
                ", storage='" + storage + '\'' +
                ", graphicsCard='" + graphicsCard + '\'' +
                ", motherboard='" + motherboard + '\'' +
                ", powerSupply='" + powerSupply + '\'' +
                ", hasWifi=" + hasWifi +
                ", hasBluetooth=" + hasBluetooth +
                ", operatingSystem='" + operatingSystem + '\'' +
                '}';
    }
    
    public static class Builder {
        // Required fields
        private final String cpu;
        private final String ram;
        private final String storage;
        
        // Optional fields with defaults
        private String graphicsCard = "Integrated";
        private String motherboard = "Standard";
        private String powerSupply = "500W";
        private boolean hasWifi = false;
        private boolean hasBluetooth = false;
        private String operatingSystem = "Windows 10";
        
        public Builder(String cpu, String ram, String storage) {
            if (cpu == null || cpu.trim().isEmpty()) {
                throw new IllegalArgumentException("CPU is required");
            }
            if (ram == null || ram.trim().isEmpty()) {
                throw new IllegalArgumentException("RAM is required");
            }
            if (storage == null || storage.trim().isEmpty()) {
                throw new IllegalArgumentException("Storage is required");
            }
            
            this.cpu = cpu;
            this.ram = ram;
            this.storage = storage;
        }
        
        public Builder setGraphicsCard(String graphicsCard) {
            this.graphicsCard = graphicsCard;
            return this;
        }
        
        public Builder setMotherboard(String motherboard) {
            this.motherboard = motherboard;
            return this;
        }
        
        public Builder setPowerSupply(String powerSupply) {
            this.powerSupply = powerSupply;
            return this;
        }
        
        public Builder setWifi(boolean hasWifi) {
            this.hasWifi = hasWifi;
            return this;
        }
        
        public Builder setBluetooth(boolean hasBluetooth) {
            this.hasBluetooth = hasBluetooth;
            return this;
        }
        
        public Builder setOperatingSystem(String operatingSystem) {
            this.operatingSystem = operatingSystem;
            return this;
        }
        
        public Computer build() {
            return new Computer(this);
        }
    }
    
    // Static factory methods for common configurations
    public static Computer gamingComputer() {
        return new Builder("Intel i7", "16GB DDR4", "1TB SSD")
                .setGraphicsCard("RTX 3080")
                .setMotherboard("Gaming Motherboard")
                .setPowerSupply("750W")
                .setWifi(true)
                .setBluetooth(true)
                .setOperatingSystem("Windows 11")
                .build();
    }
    
    public static Computer officeComputer() {
        return new Builder("Intel i5", "8GB DDR4", "512GB SSD")
                .setGraphicsCard("Integrated")
                .setMotherboard("Business Motherboard")
                .setPowerSupply("400W")
                .setWifi(true)
                .setOperatingSystem("Windows 10")
                .build();
    }
    
    public static Computer budgetComputer() {
        return new Builder("AMD Ryzen 3", "4GB DDR4", "256GB SSD")
                .setGraphicsCard("Integrated")
                .setPowerSupply("300W")
                .setOperatingSystem("Linux")
                .build();
    }
}


// Test class
class BuilderPatternTest {
    public static void main(String[] args) {
        System.out.println("=== Computer Builder Demo ===\n");
        
        // Custom computer
        Computer customComputer = new Computer.Builder("AMD Ryzen 7", "32GB DDR4", "2TB NVMe SSD")
                .setGraphicsCard("RTX 4070")
                .setMotherboard("X570 Gaming")
                .setPowerSupply("850W")
                .setWifi(true)
                .setBluetooth(true)
                .setOperatingSystem("Windows 11 Pro")
                .build();
        
        System.out.println("Custom Computer: " + customComputer);
        
        // Pre-configured computers
        System.out.println("\nGaming Computer: " + Computer.gamingComputer());
        System.out.println("Office Computer: " + Computer.officeComputer());
        System.out.println("Budget Computer: " + Computer.budgetComputer());
        
        System.out.println("\n=== Pizza Builder Demo ===\n");
        
        Pizza margherita = new PizzaBuilder()
                .dough("Thin Crust")
                .sauce("Tomato")
                .cheese("Mozzarella")
                .addTopping("Basil")
                .size("Large")
                .build();
        
        System.out.println("Margherita Pizza: " + margherita);
        
        Pizza deluxe = new PizzaBuilder()
                .dough("Thick Crust")
                .sauce("BBQ")
                .cheese("Cheddar")
                .addTopping("Pepperoni")
                .addTopping("Mushrooms")
                .addTopping("Onions")
                .addTopping("Bell Peppers")
                .size("Extra Large")
                .extraCheese()
                .extraSauce()
                .build();
        
        System.out.println("Deluxe Pizza: " + deluxe);
    }
}
