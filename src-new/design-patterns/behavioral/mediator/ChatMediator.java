package MediatorPattern;

import java.util.ArrayList;
import java.util.List;

// Mediator interface
public interface ChatMediator {
    void sendMessage(String message, User user);
    void addUser(User user);
    void removeUser(User user);
}

// Concrete Mediator
public class ChatRoom implements ChatMediator {
    private final List<User> users;
    
    public ChatRoom() {
        this.users = new ArrayList<>();
    }
    
    @Override
    public void sendMessage(String message, User user) {
        System.out.println(user.getName() + " sends: " + message);
        // Broadcast message to all users except sender
        for (User u : users) {
            if (u != user) {
                u.receive(message);
            }
        }
    }
    
    @Override
    public void addUser(User user) {
        users.add(user);
        System.out.println(user.getName() + " joined the chat room");
    }
    
    @Override
    public void removeUser(User user) {
        users.remove(user);
        System.out.println(user.getName() + " left the chat room");
    }
}

// Colleague interface
public abstract class User {
    protected ChatMediator mediator;
    protected String name;
    
    public User(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }
    
    public abstract void send(String message);
    public abstract void receive(String message);
    
    public String getName() {
        return name;
    }
}

// Concrete Colleague
public class ChatUser extends User {
    
    public ChatUser(ChatMediator mediator, String name) {
        super(mediator, name);
    }
    
    @Override
    public void send(String message) {
        System.out.println(this.name + " is sending message...");
        mediator.sendMessage(message, this);
    }
    
    @Override
    public void receive(String message) {
        System.out.println(this.name + " received: " + message);
    }
}

// Test class
class ChatRoomTest {
    public static void main(String[] args) {
        ChatMediator chatRoom = new ChatRoom();
        
        User alice = new ChatUser(chatRoom, "Alice");
        User bob = new ChatUser(chatRoom, "Bob");
        User charlie = new ChatUser(chatRoom, "Charlie");
        
        chatRoom.addUser(alice);
        chatRoom.addUser(bob);
        chatRoom.addUser(charlie);
        
        alice.send("Hello everyone!");
        bob.send("Hi Alice!");
        charlie.send("Good morning!");
        
        chatRoom.removeUser(bob);
        alice.send("Bob left the chat");
    }
}
