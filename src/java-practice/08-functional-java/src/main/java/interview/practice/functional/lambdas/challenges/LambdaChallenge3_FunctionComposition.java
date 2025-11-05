package interview.practice.functional.lambdas.challenges;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Challenge 3: Function Composition
 * 
 * Task: Compose multiple Function operations to transform data through a pipeline.
 * 
 * Requirements:
 * - Start with a list of strings
 * - Apply: trim -> toUpperCase -> addPrefix -> length
 * - Use Function.andThen() and Function.compose()
 * - Chain multiple transformations
 * - Handle edge cases (null, empty strings)
 */
public class LambdaChallenge3_FunctionComposition {

    public static void main(String[] args) {
        List<String> values = Arrays.asList("Rishav", "Saraf", "Anamika", null, "  ", "");

        Function<String, String> safeTrim = value -> value == null ? "" : value.trim();
        Function<String, String> toUpperCase = value -> value.toUpperCase();
        Function<String, String> addPrefix = value -> "pre" + value;
        Function<String, Integer> length = value -> value.length();

        Function<String, Integer> pipeline = safeTrim
                .andThen(toUpperCase)
                .andThen(addPrefix)
                .andThen(length);

        values.forEach(value ->
                System.out.println("Input: '" + value + "', Output: " + pipeline.apply(value)));
    }
}

