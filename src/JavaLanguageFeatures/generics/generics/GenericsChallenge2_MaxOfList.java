import java.util.Arrays;
import java.util.List;

/**
 * Challenge 2: Max of a list
 * Task: Implement a method to find the max element in a list of Comparable items.
 */
public class GenericsChallenge2_MaxOfList {
    public static void main(String[] args) {
        List<Integer> ints = Arrays.asList(5, 1, 9, 2, 7);
        List<String> strings = Arrays.asList("alpha", "gamma", "beta");

        System.out.println("Max int: " + GenericUtils.max(ints));
        System.out.println("Max string: " + GenericUtils.max(strings));
    }
}


