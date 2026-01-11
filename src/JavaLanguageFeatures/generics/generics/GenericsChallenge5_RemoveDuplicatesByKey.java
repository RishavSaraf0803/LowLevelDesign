import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Challenge 5: Remove duplicates by key
 * Task: Given a list of items and a key extractor, return a list with first occurrences only.
 */
public class GenericsChallenge5_RemoveDuplicatesByKey {
    public static <T> List<T> uniqueByKey(List<T> items, Function<? super T, ?> keyExtractor) {
        return GenericUtils.uniqueByKey(items, keyExtractor);
    }

    public static void main(String[] args) {
        List<String> data = Arrays.asList("aa", "ab", "ba", "bb", "ac");
        // Keep first string for each first-character key
        List<String> unique = uniqueByKey(data, s -> s.charAt(0));
        System.out.println(unique);
    }
}


