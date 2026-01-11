package interview.practice.functional.streams.challenges;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Challenge 4: First Non-Repeated Character
 * 
 * Task: Find the first non-repeated character in a string using streams.
 * 
 * Requirements:
 * - Use stream operations to find the first character that appears exactly once
 * - Return the character or null if no such character exists
 * - Consider case sensitivity
 * - Handle edge cases (empty string, all characters repeated)
 */
public class StreamChallenge4_FirstNonRepeatedChar {
    
    public static void main(String[] args) {
        String input = "aabbccdefg";
        
        // TODO: Implement the solution
        Character first = Arrays.stream(input.split(""))
                .collect(Collectors.groupingBy(s -> s, LinkedHashMap::new, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(Map.Entry::getKey)
                .map(s -> s.charAt(0))
                .findFirst()
                .orElse(null);
        System.out.println(first);
                               // Hint: You can convert the inpu string to a stream of characters, then use grouping to count their occurrences:
                               // Arrays.stream(input.split("")).collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
                               // Finally, find the first character with a count of 1 using .entrySet().stream().filter(e -> e.getValue() == 1)
    }
}
