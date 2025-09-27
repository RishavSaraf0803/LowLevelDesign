package AdapterPattern;

// Target interface
interface MediaPlayer {
    void play(String audioType, String fileName);
}

// Adaptee interface
interface AdvancedMediaPlayer {
    void playVlc(String fileName);
    void playMp4(String fileName);
    void playAvi(String fileName);
}

// Concrete Adaptee classes
class VlcPlayer implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file: " + fileName);
    }
    
    @Override
    public void playMp4(String fileName) {
        // VLC player doesn't support MP4 directly
        System.out.println("VLC player cannot play MP4 files directly");
    }
    
    @Override
    public void playAvi(String fileName) {
        System.out.println("Playing avi file: " + fileName);
    }
}

class Mp4Player implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        // MP4 player doesn't support VLC directly
        System.out.println("MP4 player cannot play VLC files directly");
    }
    
    @Override
    public void playMp4(String fileName) {
        System.out.println("Playing mp4 file: " + fileName);
    }
    
    @Override
    public void playAvi(String fileName) {
        System.out.println("Playing avi file: " + fileName);
    }
}

// Adapter class
class MediaAdapter implements MediaPlayer {
    private AdvancedMediaPlayer advancedMusicPlayer;
    
    public MediaAdapter(String audioType) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer = new VlcPlayer();
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer = new Mp4Player();
        }
    }
    
    @Override
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer.playVlc(fileName);
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer.playMp4(fileName);
        } else if (audioType.equalsIgnoreCase("avi")) {
            // Both players support AVI, use VLC as default
            advancedMusicPlayer.playAvi(fileName);
        }
    }
}

// Concrete Target class
class AudioPlayer implements MediaPlayer {
    private MediaAdapter mediaAdapter;
    
    @Override
    public void play(String audioType, String fileName) {
        // Built-in support for MP3
        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing mp3 file: " + fileName);
        }
        // MediaAdapter provides support for other formats
        else if (audioType.equalsIgnoreCase("vlc") || 
                 audioType.equalsIgnoreCase("mp4") || 
                 audioType.equalsIgnoreCase("avi")) {
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, fileName);
        } else {
            System.out.println("Invalid media type: " + audioType + " format not supported");
        }
    }
}

// Object Adapter Pattern Example
interface Database {
    void connect();
    void query(String sql);
    void disconnect();
}

// Adaptee - Legacy Database
class LegacyDatabase {
    public void openConnection() {
        System.out.println("Legacy database connection opened");
    }
    
    public void executeQuery(String sql) {
        System.out.println("Executing legacy query: " + sql);
    }
    
    public void closeConnection() {
        System.out.println("Legacy database connection closed");
    }
}

// Object Adapter
class DatabaseAdapter implements Database {
    private LegacyDatabase legacyDatabase;
    
    public DatabaseAdapter(LegacyDatabase legacyDatabase) {
        this.legacyDatabase = legacyDatabase;
    }
    
    @Override
    public void connect() {
        legacyDatabase.openConnection();
    }
    
    @Override
    public void query(String sql) {
        legacyDatabase.executeQuery(sql);
    }
    
    @Override
    public void disconnect() {
        legacyDatabase.closeConnection();
    }
}

// Class Adapter Pattern Example (using inheritance)
interface Shape {
    void draw();
}

class Rectangle {
    public void drawRectangle() {
        System.out.println("Drawing Rectangle");
    }
}

class Circle {
    public void drawCircle() {
        System.out.println("Drawing Circle");
    }
}

// Class Adapter for Rectangle
class RectangleAdapter implements Shape {
    private Rectangle rectangle;
    
    public RectangleAdapter(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
    
    @Override
    public void draw() {
        rectangle.drawRectangle();
    }
}

// Class Adapter for Circle
class CircleAdapter implements Shape {
    private Circle circle;
    
    public CircleAdapter(Circle circle) {
        this.circle = circle;
    }
    
    @Override
    public void draw() {
        circle.drawCircle();
    }
}

// Test class
class AdapterPatternTest {
    public static void main(String[] args) {
        System.out.println("=== Media Player Adapter Demo ===\n");
        
        AudioPlayer audioPlayer = new AudioPlayer();
        
        audioPlayer.play("mp3", "song.mp3");
        audioPlayer.play("mp4", "movie.mp4");
        audioPlayer.play("vlc", "video.vlc");
        audioPlayer.play("avi", "clip.avi");
        audioPlayer.play("wav", "sound.wav");
        
        System.out.println("\n=== Database Adapter Demo ===\n");
        
        LegacyDatabase legacyDb = new LegacyDatabase();
        DatabaseAdapter dbAdapter = new DatabaseAdapter(legacyDb);
        
        dbAdapter.connect();
        dbAdapter.query("SELECT * FROM users");
        dbAdapter.disconnect();
        
        System.out.println("\n=== Shape Adapter Demo ===\n");
        
        Rectangle rectangle = new Rectangle();
        Circle circle = new Circle();
        
        Shape rectangleShape = new RectangleAdapter(rectangle);
        Shape circleShape = new CircleAdapter(circle);
        
        rectangleShape.draw();
        circleShape.draw();
    }
}
