import java.util.ArrayList;
import java.util.List;

public class PECSExamples {
    // Producer Extends: read-only from producer
    public static double total(List<? extends Number> numbers) {
        double sum = 0.0;
        for (Number n : numbers) {
            sum += n.doubleValue();
        }
        return sum;
    }

    // Consumer Super: safe to add into consumer
    public static void addIntegers(List<? super Integer> destination, List<Integer> source) {
        destination.addAll(source);
    }

    public static void demo() {
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        List<Number> nums = new ArrayList<>();
        addIntegers(nums, ints); // OK due to <? super Integer>
        double t = total(nums);  // OK due to <? extends Number>
        System.out.println("Total: " + t);
    }
}


