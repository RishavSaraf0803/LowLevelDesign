package interview.practice.functional.lambdas;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Study guide: functional interfaces and lambdas.
 * 
 * Functional interfaces model a single behaviour. Lambdas and method references
 * let us pass that behaviour around without anonymous classes.
 */
public class FunctionalInterfaces {

    public static void main(String[] args) {
        new FunctionalInterfaces().runStudyGuide();
    }

    private void runStudyGuide() {
        printSectionTitle("1. Built-in functional interfaces");
        demonstrateBuiltInInterfaces();

        printSectionTitle("2. Custom functional interfaces");
        demonstrateCustomInterfaces();

        printSectionTitle("3. Method references");
        demonstrateMethodReferences();

        printSectionTitle("4. Composition and real-world patterns");
        demonstrateCompositionPatterns();
    }

    private void printSectionTitle(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }

    private void demonstrateBuiltInInterfaces() {
        Predicate<Integer> isEven = number -> number % 2 == 0;
        System.out.println("Predicate -> 42 is even? " + isEven.test(42));

        Function<String, Integer> stringLength = text -> text == null ? 0 : text.length();
        System.out.println("Function -> length of 'lambda' = " + stringLength.apply("lambda"));

        Consumer<String> consoleLogger = message -> System.out.println("Consumer -> " + message);
        consoleLogger.accept("log from Consumer accepts behaviour, not data structures.");

        Supplier<LocalDateTime> clock = LocalDateTime::now;
        System.out.println("Supplier -> current time: " + clock.get());

        BiFunction<Integer, Integer, Integer> maxFinder = Integer::max;
        System.out.println("BiFunction -> max of 7 and 11 = " + maxFinder.apply(7, 11));
    }

    private void demonstrateCustomInterfaces() {
        Greet greet = name -> "Hello, " + name + "!";
        System.out.println("Greeting example -> " + greet.sayHello("reader"));

        RetryableTask task = () -> {
            System.out.println("RetryableTask -> attempt executed");
            return true;
        };
        System.out.println("Did task succeed? " + task.execute());
    }

    private void demonstrateMethodReferences() {
        Function<String, Integer> parseInt = Integer::parseInt;
        System.out.println("Static method reference -> " + parseInt.apply("123"));

        Function<String, String> toUpper = String::toUpperCase;
        System.out.println("Instance method reference -> " + toUpper.apply("method references"));

        Consumer<String> sysOut = System.out::println;
        sysOut.accept("Specific object method reference -> System.out::println");
    }

    private void demonstrateCompositionPatterns() {
        Function<String, String> trim = String::trim;
        Function<String, String> toUpper = String::toUpperCase;
        Function<String, String> prepareInput = trim.andThen(toUpper);
        System.out.println("Function composition -> '" + prepareInput.apply("  space -> uppercase  ") + "'");

        Predicate<String> notBlank = text -> text != null && !text.isBlank();
        Predicate<String> isShort = text -> text.length() < 10;
        Predicate<String> shortAndPresent = notBlank.and(isShort);
        System.out.println("Predicate composition -> 'lambda' matches? " + shortAndPresent.test("lambda"));

        Processor pipeline = input -> Map.of(
                "original", input,
                "sanitized", prepareInput.apply(input),
                "valid", shortAndPresent.test(input)
        );
        System.out.println("Real-world pipeline -> " + pipeline.process("  lambdas  "));

        List<String> languages = List.of("Java", "Kotlin", "Scala");
        languages.forEach(System.out::println);
    }
}

@FunctionalInterface
interface Greet {
    String sayHello(String name);
}

@FunctionalInterface
interface RetryableTask {
    boolean execute();
}

@FunctionalInterface
interface Processor {
    Map<String, Object> process(String value);
}
