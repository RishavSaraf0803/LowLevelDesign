import java.util.Optional;
import java.util.function.Function;

public class Box<T> {
    private T value;

    public Box() {
    }

    public Box(T value) {
        this.value = value;
    }

    public static <T> Box<T> of(T value) {
        return new Box<>(value);
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public <R> Box<R> map(Function<? super T, ? extends R> mapper) {
        if (!isPresent()) {
            return new Box<>(null);
        }
        return new Box<>(mapper.apply(value));
    }

    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }
}


