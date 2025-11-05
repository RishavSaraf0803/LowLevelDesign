import java.util.Arrays;
import java.util.List;

/**
 * Challenge 1: Print any list
 * Task: Implement a method that prints elements of any kind of list using wildcards.
 */
public class GenericsChallenge1_PrintAnyList {
    public static void main(String[] args) {
        List<Integer> ints = Arrays.asList(1, 2, 3);
        List<String> strings = Arrays.asList("a", "b", "c");

       printList(ints);
        printList(strings);
    }
    private static <T> void printList(List<T> list){
        for(T t : list){
            System.out.print(t);
        }
    }
}


