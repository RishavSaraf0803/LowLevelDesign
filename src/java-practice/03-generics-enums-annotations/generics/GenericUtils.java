import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class GenericUtils {
    private GenericUtils() {}

    public static <T> void copy(List<? super T> destination, List<? extends T> source) {
        for (T item : source) {
            destination.add(item);
        }
    }

    public static void printList(List<?> items) {
        for (Object item : items) {
            System.out.println(item);
        }
    }

    public static double sumNumbers(List<? extends Number> numbers) {
        double sum = 0.0;
        for (Number n : numbers) {
            sum += n.doubleValue();
        }
        return sum;
    }

    public static <T extends Comparable<? super T>> T max(List<T> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items must not be null or empty");
        }
        T currentMax = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            T candidate = items.get(i);
            if (candidate.compareTo(currentMax) > 0) {
                currentMax = candidate;
            }
        }
        return currentMax;
    }

    public static <T> List<T> uniqueByKey(List<T> items, Function<? super T, ?> keyExtractor) {
        if (items == null) return new ArrayList<>();
        Map<Object, T> seen = new LinkedHashMap<>();
        for (T item : items) {
            Object key = keyExtractor.apply(item);
            // Keep the first occurrence
            seen.putIfAbsent(key, item);
        }
        return new ArrayList<>(seen.values());
    }

    public static <T> List<T> filter(List<T> items, Function<? super T, Boolean> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (Boolean.TRUE.equals(predicate.apply(item))) {
                result.add(item);
            }
        }
        return result;
    }
}


