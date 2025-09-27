package TemplateMethodPattern;

import java.util.List;
import java.util.ArrayList;

// Abstract Template Class
abstract class DataProcessor {
    
    // Template method - defines the algorithm skeleton
    public final void processData(List<String> data) {
        System.out.println("=== Starting Data Processing ===");
        
        // Step 1: Validate data
        if (!validateData(data)) {
            System.out.println("Data validation failed!");
            return;
        }
        
        // Step 2: Preprocess data
        List<String> processedData = preprocessData(data);
        
        // Step 3: Process data (abstract method - must be implemented by subclasses)
        List<String> result = doProcessing(processedData);
        
        // Step 4: Postprocess data
        List<String> finalResult = postprocessData(result);
        
        // Step 5: Save results
        saveResults(finalResult);
        
        System.out.println("=== Data Processing Complete ===\n");
    }
    
    // Concrete methods with default implementations
    protected boolean validateData(List<String> data) {
        System.out.println("Validating data...");
        return data != null && !data.isEmpty();
    }
    
    protected List<String> preprocessData(List<String> data) {
        System.out.println("Preprocessing data...");
        List<String> processed = new ArrayList<>();
        for (String item : data) {
            processed.add(item.trim().toLowerCase());
        }
        return processed;
    }
    
    protected List<String> postprocessData(List<String> data) {
        System.out.println("Postprocessing data...");
        List<String> result = new ArrayList<>();
        for (String item : data) {
            result.add(item.toUpperCase());
        }
        return result;
    }
    
    protected void saveResults(List<String> results) {
        System.out.println("Saving results...");
        System.out.println("Results: " + results);
    }
    
    // Abstract method - must be implemented by subclasses
    protected abstract List<String> doProcessing(List<String> data);
    
    // Hook method - can be overridden by subclasses
    protected void onProcessingStart() {
        // Default empty implementation
    }
    
    protected void onProcessingEnd() {
        // Default empty implementation
    }
}

// Concrete implementations
class TextProcessor extends DataProcessor {
    
    @Override
    protected List<String> doProcessing(List<String> data) {
        System.out.println("Processing text data...");
        List<String> result = new ArrayList<>();
        
        for (String text : data) {
            // Remove special characters and numbers
            String processed = text.replaceAll("[^a-zA-Z\\s]", "");
            if (!processed.trim().isEmpty()) {
                result.add(processed);
            }
        }
        
        return result;
    }
    
    @Override
    protected void onProcessingStart() {
        System.out.println("Text processor initialized");
    }
}

class NumberProcessor extends DataProcessor {
    
    @Override
    protected List<String> doProcessing(List<String> data) {
        System.out.println("Processing numeric data...");
        List<String> result = new ArrayList<>();
        
        for (String item : data) {
            try {
                double number = Double.parseDouble(item);
                // Square the number
                double squared = number * number;
                result.add(String.valueOf(squared));
            } catch (NumberFormatException e) {
                System.out.println("Skipping non-numeric value: " + item);
            }
        }
        
        return result;
    }
    
    @Override
    protected void onProcessingEnd() {
        System.out.println("Number processing completed successfully");
    }
}

class EmailProcessor extends DataProcessor {
    
    @Override
    protected List<String> doProcessing(List<String> data) {
        System.out.println("Processing email data...");
        List<String> result = new ArrayList<>();
        
        for (String email : data) {
            if (isValidEmail(email)) {
                // Extract domain
                String domain = email.substring(email.indexOf('@') + 1);
                result.add(domain);
            }
        }
        
        return result;
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    
    @Override
    protected boolean validateData(List<String> data) {
        System.out.println("Validating email data...");
        boolean isValid = super.validateData(data);
        
        if (isValid) {
            for (String email : data) {
                if (!email.contains("@")) {
                    System.out.println("Invalid email format found: " + email);
                    return false;
                }
            }
        }
        
        return isValid;
    }
}

// Test class
class DataProcessorTest {
    public static void main(String[] args) {
        List<String> textData = List.of("Hello World!", "Java Programming", "123 Test");
        List<String> numberData = List.of("5", "10", "15", "invalid", "20");
        List<String> emailData = List.of("user1@example.com", "user2@test.org", "invalid-email");
        
        System.out.println("=== Text Processing ===");
        DataProcessor textProcessor = new TextProcessor();
        textProcessor.processData(textData);
        
        System.out.println("=== Number Processing ===");
        DataProcessor numberProcessor = new NumberProcessor();
        numberProcessor.processData(numberData);
        
        System.out.println("=== Email Processing ===");
        DataProcessor emailProcessor = new EmailProcessor();
        emailProcessor.processData(emailData);
    }
}
