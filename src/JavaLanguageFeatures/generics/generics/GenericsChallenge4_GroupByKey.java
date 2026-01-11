import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Challenge 4: Group by key
 * Task: Implement a generic groupBy that groups items by a derived key.
 */
public class GenericsChallenge4_GroupByKey {
    public static <T, K> Map<K, List<T>> groupBy(List<T> items, Function<? super T, ? extends K> keyExtractor) {
        Map<K, List<T>> result = new HashMap<>();
        for (T item : items) {
            K key = keyExtractor.apply(item);
            result.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> words = Arrays.asList("ant", "bat", "apple", "ball", "cat");
        Map<Integer, List<String>> byLength = groupBy(words, String::length);
        System.out.println(byLength);
    }
}


