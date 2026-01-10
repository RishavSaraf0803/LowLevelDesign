
package interview.practice.functional.lambdas.challenges;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Challenge 2: Predicate Filter
 *
 * - Filter numbers that are greater than 10 AND even
 * - Filter numbers that are divisible by 3 OR divisible by 5
 * - Use Predicate.and(), Predicate.or(), Predicate.negate()
 * - Create a reusable filter method that accepts a Predicate
 */
public class LambdaChallenge2_PredicateFilter {

    public static void main(String[] args) {
        List<Integer> data = Arrays.asList(3, 5, 6, 9, 10, 12, 14, 15, 18, 20, 21, 25, 30, 33, 40);

        Predicate<Integer> greaterThan10 = n -> n > 10;
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> divisibleBy3 = n -> n % 3 == 0;
        Predicate<Integer> divisibleBy5 = n -> n % 5 == 0;

        // 1) >10 AND even
        List<Integer> gt10AndEven = filter(data, greaterThan10.and(isEven));
        System.out.println("gt10 AND even: " + gt10AndEven);

        // 2) divisible by 3 OR 5
        List<Integer> div3Or5 = filter(data, divisibleBy3.or(divisibleBy5));
        System.out.println("divisible by 3 OR 5: " + div3Or5);

        // 3) negate(): NOT (divisible by 3 OR 5)
        List<Integer> notDiv3Or5 = filter(data, divisibleBy3.or(divisibleBy5).negate());
        System.out.println("NOT (divisible by 3 OR 5): " + notDiv3Or5);

        // Simple checks (prints PASS/FAIL)
        assertList(Arrays.asList(12, 14, 18, 20, 30, 40), gt10AndEven, ">10 AND even");
        assertList(Arrays.asList(3, 5, 6, 9, 10, 12, 15, 18, 20, 21, 25, 30, 33, 40), div3Or5, "divisible by 3 OR 5");
        assertList(Arrays.asList(14), notDiv3Or5, "NOT (divisible by 3 OR 5)");
    }

    private static <T> List<T> filter(List<T> list, Predicate<T> condition) {
        return list.stream().filter(condition).collect(Collectors.toList());
    }

    private static <T> void assertList(List<T> expected, List<T> actual, String label) {
        boolean ok = expected.equals(actual);
        System.out.println((ok ? "PASS" : "FAIL") + ": " + label +
                " | expected=" + expected + " actual=" + actual);
    }
}
