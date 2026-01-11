import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Challenge 3: Implement a generic stack
 */
public class GenericsChallenge3_GenericStack {

    public static class GenericStack<T> {
        private final Deque<T> elements = new ArrayDeque<>();

        public void push(T value) {
            elements.push(value);
        }

        public T pop() {
            if (elements.isEmpty()) throw new IllegalStateException("Stack is empty");
            return elements.pop();
        }

        public T peek() {
            if (elements.isEmpty()) throw new IllegalStateException("Stack is empty");
            return elements.peek();
        }

        public boolean isEmpty() {
            return elements.isEmpty();
        }

        public int size() {
            return elements.size();
        }
    }

    public static void main(String[] args) {
        GenericStack<String> stack = new GenericStack<>();
        stack.push("a");
        stack.push("b");
        stack.push("c");
        System.out.println(stack.peek());
        System.out.println(stack.pop());
        System.out.println(stack.size());
    }
}


