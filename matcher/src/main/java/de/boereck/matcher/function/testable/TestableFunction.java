package de.boereck.matcher.function.testable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

@FunctionalInterface
public interface TestableFunction<I, O> extends Function<I, O> {

    default OptionalMapper<I, O> optional() {
        return i -> Optional.ofNullable(apply(i));
    }

    default <V> TestableFunction<I, V> andThen(Function<? super O, ? extends V> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> after.apply(apply(i));
    }

    default AdvPredicate<I> thenTest(Predicate<? super O> test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> test.test(apply(i));
    }

    default Consumer<I> thenDo(Consumer<? super O> consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.apply(i));
    }

    default OptionalMapper<I, O> filter(Predicate<? super O> test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final O result = this.apply(i);
            return test.test(result) ? Optional.ofNullable(result) : Optional.empty();
        };
    }

    default <R> OptionalMapper<I, R> filter(Class<R> clazz) throws NullPointerException {
        Objects.requireNonNull(clazz);
        return i -> {
            final O result = this.apply(i);
            return (clazz.isInstance(result)) ? Optional.of((R)result) : Optional.empty();
        };
    }

}
