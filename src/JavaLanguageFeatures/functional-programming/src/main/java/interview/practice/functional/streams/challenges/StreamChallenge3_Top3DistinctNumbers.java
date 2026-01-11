
package interview.practice.functional.streams.challenges;

import java.util.Arrays;
import java.util.List;

/**
 * Challenge 3: Top 3 Distinct Numbers
 * 
 * Task: Find the top 3 distinct largest numbers from a list.
 * 
 * Requirements:
 * - Remove duplicates
 * - Sort in descending order
 * - Get top 3 elements
 * - Return as a List<Integer>
 * - Handle edge cases (list with less than 3 distinct numbers)
 */
public class StreamChallenge3_Top3DistinctNumbers {
    
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 3, 8, 7, 6, 9, 4);
        
        // TODO: Implement the solution

        List<Integer> top3 = numbers.stream()
                .distinct()
                .sorted((a, b) -> Integer.compare(b, a))
                .limit(3)
                .toList();
        System.out.println(top3);
    }
}
