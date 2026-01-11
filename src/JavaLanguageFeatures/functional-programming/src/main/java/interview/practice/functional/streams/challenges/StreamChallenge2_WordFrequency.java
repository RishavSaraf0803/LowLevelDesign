
package interview.practice.functional.streams.challenges;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Challenge 2: Word Frequency
 * 
 * Task: Count the frequency of each word in a list of sentences.
 * 
 * Requirements:
 * - Split sentences into words
 * - Count occurrence of each word (case-insensitive)
 * - Return a Map<String, Long> with word frequencies
 * - Use Collectors.groupingBy() or Collectors.toMap()
 * - Handle punctuation appropriately
 */
public class StreamChallenge2_WordFrequency {
    
    public static void main(String[] args) {
        List<String> sentences = Arrays.asList(
            "Hello world hello",
            "Java is great java is fun",
            "Stream API stream api is powerful"
        );
        
        // TODO: Implement the solution

        Map<String, Long> frequency = sentences.stream()
                .flatMap(s -> Arrays.stream(s.toLowerCase().split("\\W+")))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

        System.out.println(frequency);

        // map transforms each element and wraps the result once
        // flatMap transforms each element into a stream and flattens them into one stream
    }
}
