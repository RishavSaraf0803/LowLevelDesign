package interview.practice.functional.lambdas.challenges;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Challenge 4: Custom Stream Implementation.
 *
 * Minimal lazy stream that supports filter, map, forEach.
 */
public class LambdaChallenge4_MyStreamImplementation {

    public static void main(String[] args) {
        MyStream.of(1, 2, 3, 4, 5)
                .filter(n -> n % 2 == 0)
                .map(n -> "Value-" + n)
                .forEach(System.out::println);
    }
}

final class MyStream<T> implements Iterable<T> {
    private final Iterable<T> source;

    private MyStream(Iterable<T> source) {
        this.source = source;
    }

    @SafeVarargs
    static <T> MyStream<T> of(T... values) {
        return new MyStream<>(java.util.List.of(values));
    }

    MyStream<T> filter(Predicate<? super T> predicate) {
        return new MyStream<>(() -> new Iterator<>() {
            final Iterator<T> it = source.iterator();
            T next;
            boolean nextReady = false;

            private void advance() {
                while (it.hasNext()) {
                    T candidate = it.next();
                    if (predicate.test(candidate)) {
                        next = candidate;
                        nextReady = true;
                        return;
                    }
                }
                nextReady = false;
            }

            @Override
            public boolean hasNext() {
                if (!nextReady) {
                    advance();
                }
                return nextReady;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                nextReady = false;
                return next;
            }
        });
    }

    <R> MyStream<R> map(Function<? super T, ? extends R> mapper) {
        return new MyStream<>(() -> new Iterator<>() {
            final Iterator<T> it = source.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public R next() {
                return mapper.apply(it.next());
            }
        });
    }

    public void forEach(Consumer<? super T> action) {
        source.forEach(action);
    }

    @Override
    public Iterator<T> iterator() {
        return source.iterator();
    }
}
