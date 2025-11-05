
package interview.practice.functional.streams.challenges;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Challenge 1: Find Even Numbers
 * 
 * Task: Filter and collect all even numbers from a list, then calculate their sum.
 * 
 * Requirements:
 * - Use stream operations only
 * - Filter even numbers
 * - Calculate sum using stream reduction
 * - Return both the filtered list and the sum
 */
public class StreamChallenge1_FindEvenNumbers {
    
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // TODO: Implement the solution
        List<Integer> evens = numbers.stream()
                .filter(i -> i % 2 == 0)
                .collect(Collectors.toList());
        print(evens);
        int sum = evens.stream()
                .mapToInt(Integer::intValue)
                .sum();
        print(sum);
    }
    private static void print(Object object) {
        System.out.println(object);
    }
}
