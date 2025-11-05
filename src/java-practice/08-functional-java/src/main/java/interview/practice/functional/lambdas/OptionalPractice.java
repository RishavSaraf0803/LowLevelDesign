package interview.practice.functional.lambdas;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * Practice: Optional
 *
 * Build safe pipelines using Optional without explicit null checks.
 * Demonstrate: of, ofNullable, empty, map, flatMap, filter, orElse,
 * orElseGet, orElseThrow.
 */
public class OptionalPractice {

    public static void main(String[] args) {
        List<String> inputs = List.of("  4 ", "abc", null, " ", "7", "16");

        // TODO:
        // 1) Create a sanitize function that trims strings safely (treat null as empty)
        Function<String, String> sanitize = null; // TODO implement

        // 2) For each raw input, build an Optional pipeline:
        //    - ofNullable(raw) -> map(sanitize) -> filter(not blank)
        //    - flatMap(parseInt) -> filter(n >= 0) -> map(square)
        //    - map to display string: "Value is <n>" -> orElse("No value")
        //    Print: Input='<raw>' -> <display>
        // TODO implement loop and pipeline

        // 3) Demonstrate orElseGet vs orElse by producing a default via computeExpensiveDefault only when empty
        // TODO implement demonstration

        // 4) Demonstrate orElseThrow by throwing NoSuchElementException when Optional<Integer> is empty
        // TODO implement try/catch demonstration

        // Simple scaffolding assertion for parseInt
        assertEquals(Optional.of(42), parseInt("42"), "parseInt should parse valid numbers");
        assertEquals(Optional.empty(), parseInt("xyz"), "parseInt should return empty for invalid numbers");
    }

    static Optional<Integer> parseInt(String text) {
        try {
            return Optional.of(Integer.parseInt(text));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static String computeExpensiveDefault() {
        return "Computed Default";
    }

    private static void assertEquals(Object expected, Object actual, String label) {
        boolean ok = (expected == null ? actual == null : expected.equals(actual));
        System.out.println((ok ? "PASS" : "FAIL") + ": " + label +
                " | expected=" + expected + " actual=" + actual);
    }
}


