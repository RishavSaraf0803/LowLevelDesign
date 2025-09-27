package FacadePattern;

// Subsystem classes
class CPU {
    public void freeze() {
        System.out.println("CPU: Freezing...");
    }
    
    public void jump(long position) {
        System.out.println("CPU: Jumping to position " + position);
    }
    
    public void execute() {
        System.out.println("CPU: Executing instructions...");
    }
}

class Memory {
    public void load(long position, byte[] data) {
        System.out.println("Memory: Loading data at position " + position);
    }
}

class HardDrive {
    public byte[] read(long lba, int size) {
        System.out.println("HardDrive: Reading " + size + " bytes from LBA " + lba);
        return new byte[size];
    }
}

class GraphicsCard {
    public void initialize() {
        System.out.println("GraphicsCard: Initializing...");
    }
    
    public void render(String data) {
        System.out.println("GraphicsCard: Rendering " + data);
    }
}

class NetworkCard {
    public void connect() {
        System.out.println("NetworkCard: Connecting to network...");
    }
    
    public void sendData(String data) {
        System.out.println("NetworkCard: Sending data: " + data);
    }
}

class SoundCard {
    public void initialize() {
        System.out.println("SoundCard: Initializing...");
    }
    
    public void playSound(String sound) {
        System.out.println("SoundCard: Playing sound: " + sound);
    }
}

// Facade class
class ComputerFacade {
    private final CPU cpu;
    private final Memory memory;
    private final HardDrive hardDrive;
    private final GraphicsCard graphicsCard;
    private final NetworkCard networkCard;
    private final SoundCard soundCard;
    
    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
        this.graphicsCard = new GraphicsCard();
        this.networkCard = new NetworkCard();
        this.soundCard = new SoundCard();
    }
    
    public void startComputer() {
        System.out.println("=== Starting Computer ===");
        cpu.freeze();
        memory.load(0, hardDrive.read(0, 1024));
        cpu.jump(0);
        cpu.execute();
        graphicsCard.initialize();
        soundCard.initialize();
        networkCard.connect();
        System.out.println("Computer started successfully!\n");
    }
    
    public void shutdownComputer() {
        System.out.println("=== Shutting Down Computer ===");
        System.out.println("Saving all data...");
        System.out.println("Closing all applications...");
        System.out.println("Disconnecting from network...");
        System.out.println("Computer shutdown complete!\n");
    }
    
    public void playGame(String gameName) {
        System.out.println("=== Starting Game: " + gameName + " ===");
        graphicsCard.render("Game graphics for " + gameName);
        soundCard.playSound("Game music for " + gameName);
        System.out.println("Game " + gameName + " is now running!\n");
    }
    
    public void browseInternet(String url) {
        System.out.println("=== Browsing Internet ===");
        networkCard.sendData("GET " + url);
        graphicsCard.render("Web page content");
        System.out.println("Successfully loaded: " + url + "\n");
    }
    
    public void playMusic(String songName) {
        System.out.println("=== Playing Music ===");
        soundCard.playSound(songName);
        System.out.println("Now playing: " + songName + "\n");
    }
}

// Another Facade example - Order Processing System
class InventoryService {
    public boolean checkAvailability(String productId, int quantity) {
        System.out.println("Checking inventory for product " + productId + " (qty: " + quantity + ")");
        return true; // Simplified
    }
    
    public void reserveItems(String productId, int quantity) {
        System.out.println("Reserving " + quantity + " units of product " + productId);
    }
}

class PaymentService {
    public boolean processPayment(String paymentMethod, double amount) {
        System.out.println("Processing payment of $" + amount + " via " + paymentMethod);
        return true; // Simplified
    }
}

class ShippingService {
    public void shipOrder(String orderId, String address) {
        System.out.println("Shipping order " + orderId + " to " + address);
    }
}

class NotificationService {
    public void sendConfirmation(String email, String orderId) {
        System.out.println("Sending confirmation email to " + email + " for order " + orderId);
    }
}

class OrderFacade {
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final NotificationService notificationService;
    
    public OrderFacade() {
        this.inventoryService = new InventoryService();
        this.paymentService = new PaymentService();
        this.shippingService = new ShippingService();
        this.notificationService = new NotificationService();
    }
    
    public void placeOrder(String productId, int quantity, String paymentMethod, 
                          double amount, String email, String address) {
        System.out.println("=== Processing Order ===");
        
        // Check inventory
        if (!inventoryService.checkAvailability(productId, quantity)) {
            System.out.println("Order failed: Insufficient inventory");
            return;
        }
        
        // Process payment
        if (!paymentService.processPayment(paymentMethod, amount)) {
            System.out.println("Order failed: Payment processing failed");
            return;
        }
        
        // Reserve items
        inventoryService.reserveItems(productId, quantity);
        
        // Ship order
        String orderId = "ORD-" + System.currentTimeMillis();
        shippingService.shipOrder(orderId, address);
        
        // Send confirmation
        notificationService.sendConfirmation(email, orderId);
        
        System.out.println("Order " + orderId + " processed successfully!\n");
    }
}

// Test class
class FacadePatternTest {
    public static void main(String[] args) {
        System.out.println("=== Computer Facade Demo ===\n");
        
        ComputerFacade computer = new ComputerFacade();
        computer.startComputer();
        computer.playGame("Super Mario");
        computer.browseInternet("https://www.google.com");
        computer.playMusic("Bohemian Rhapsody");
        computer.shutdownComputer();
        
        System.out.println("=== Order Processing Facade Demo ===\n");
        
        OrderFacade orderFacade = new OrderFacade();
        orderFacade.placeOrder("LAPTOP-001", 1, "Credit Card", 999.99, 
                              "customer@example.com", "123 Main St, City");
    }
}
