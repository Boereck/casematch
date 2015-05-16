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

    default <V> TestableFunction<I, V> andThen(Function<? super O, ? extends V> after) {
        Objects.requireNonNull(after);
        return (I i) -> after.apply(apply(i));
    }

    default AdvPredicate<I> thenTest(Predicate<? super O> test) {
        Objects.requireNonNull(test);
        return i -> test.test(apply(i));
    }

    default Consumer<I> thenDo(Consumer<? super O> consumer) {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.apply(i));
    }

    default OptionalMapper<I, O> filter(Predicate<? super O> test) {
        Objects.requireNonNull(test);
        return i -> {
            final O result = this.apply(i);
            return test.test(result) ? Optional.ofNullable(result) : Optional.empty();
        };
    }


}
