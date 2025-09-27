package MementoPattern;

import java.util.ArrayList;
import java.util.List;

// Memento class
class TextMemento {
    private final String content;
    private final String timestamp;
    
    public TextMemento(String content, String timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
}

// Originator class
class TextEditor {
    private String content;
    
    public TextEditor() {
        this.content = "";
    }
    
    public void write(String text) {
        this.content += text;
        System.out.println("Content updated: " + this.content);
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }
    
    // Create memento
    public TextMemento save() {
        String timestamp = java.time.LocalDateTime.now().toString();
        System.out.println("Saving state at: " + timestamp);
        return new TextMemento(this.content, timestamp);
    }
    
    // Restore from memento
    public void restore(TextMemento memento) {
        this.content = memento.getContent();
        System.out.println("Restored to state from: " + memento.getTimestamp());
        System.out.println("Current content: " + this.content);
    }
}

// Caretaker class
class TextEditorHistory {
    private final List<TextMemento> history;
    private int currentIndex;
    
    public TextEditorHistory() {
        this.history = new ArrayList<>();
        this.currentIndex = -1;
    }
    
    public void saveState(TextEditor editor) {
        // Remove any states after current index (when branching)
        if (currentIndex < history.size() - 1) {
            history.subList(currentIndex + 1, history.size()).clear();
        }
        
        TextMemento memento = editor.save();
        history.add(memento);
        currentIndex = history.size() - 1;
        
        // Limit history size
        if (history.size() > 10) {
            history.remove(0);
            currentIndex--;
        }
    }
    
    public void undo(TextEditor editor) {
        if (currentIndex > 0) {
            currentIndex--;
            TextMemento memento = history.get(currentIndex);
            editor.restore(memento);
        } else {
            System.out.println("Nothing to undo!");
        }
    }
    
    public void redo(TextEditor editor) {
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            TextMemento memento = history.get(currentIndex);
            editor.restore(memento);
        } else {
            System.out.println("Nothing to redo!");
        }
    }
    
    public void showHistory() {
        System.out.println("\n--- History ---");
        for (int i = 0; i < history.size(); i++) {
            TextMemento memento = history.get(i);
            String marker = (i == currentIndex) ? " <-- Current" : "";
            System.out.println(i + ": " + memento.getTimestamp() + " - " + 
                             memento.getContent().substring(0, Math.min(20, memento.getContent().length())) + 
                             (memento.getContent().length() > 20 ? "..." : "") + marker);
        }
        System.out.println("---------------\n");
    }
}

// Test class
class TextEditorTest {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        TextEditorHistory history = new TextEditorHistory();
        
        // Initial state
        history.saveState(editor);
        
        // Make changes
        editor.write("Hello ");
        history.saveState(editor);
        
        editor.write("World ");
        history.saveState(editor);
        
        editor.write("from Java!");
        history.saveState(editor);
        
        history.showHistory();
        
        // Undo operations
        System.out.println("--- Undoing ---");
        history.undo(editor);
        history.undo(editor);
        history.undo(editor);
        
        history.showHistory();
        
        // Redo operations
        System.out.println("--- Redoing ---");
        history.redo(editor);
        history.redo(editor);
        
        history.showHistory();
        
        // Branching scenario
        System.out.println("--- Branching ---");
        editor.write(" Branch!");
        history.saveState(editor);
        
        history.showHistory();
    }
}
