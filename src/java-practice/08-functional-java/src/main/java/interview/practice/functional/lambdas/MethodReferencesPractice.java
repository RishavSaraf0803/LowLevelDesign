package interview.practice.functional.lambdas;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Practice: Method References
 *
 * Do the following using method references where appropriate:
 * - Static method reference: ClassName::staticMethod
 * - Bound instance method reference: instance::instanceMethod
 * - Unbound instance method reference: ClassName::instanceMethod
 * - Constructor reference: ClassName::new
 */
public class MethodReferencesPractice {

    public static void main(String[] args) {
        List<String> rawNames = Arrays.asList("  alice  ", " ", "Bob", null, "  Charlie", "david  ", "", "  Eve");

        // TODO:
        // 1) Create a Function<String,String> using a static method reference to safely trim input
        //    Hint: ClassName::staticMethod
        Function<String, String> safeTrim = null; // TODO replace with MethodReferencesPractice::safeTrim

        // 2) Build a pipeline over rawNames using method references:
        //    - map: safeTrim
        //    - filter: notBlank (static ref)
        //    - map: Person constructor reference
        //    - sort: Comparator.comparing with unbound ref Person::name
        //    - collect to List<Person>
        List<Person> people = null; // TODO implement stream pipeline

        // 3) Print each person using a bound method reference to System.out
        //    Hint: System.out::println
        // TODO print people

        // 4) Create a BiFunction<Integer,Integer,Integer> using a static method reference to compare two integers
        //    and assert that compare(7, 11) < 0
        // TODO create comparator via Integer::compare and assert

        // Simple scaffolding assertions (adjust expected as needed)
        // Expected names after processing: ["alice", "Bob", "Charlie", "david", "Eve"] (case preserved from input)
        // because we only trim/filter and map to Person without changing case.
        assertTrue(people == null || people.size() == 5, "people size should be 5 after filtering blanks and nulls");
    }

    private static String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    static final class Person {
        private final String name;

        Person(String name) { this.name = name; }
        String name() { return name; }

        @Override public String toString() { return "Person{" + "name='" + name + '\'' + '}'; }
    }

    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("PASS: " + message);
        } else {
            System.out.println("FAIL: " + message);
        }
    }
}


