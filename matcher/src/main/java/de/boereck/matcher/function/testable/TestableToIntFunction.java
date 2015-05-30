package de.boereck.matcher.function.testable;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.ToIntFunction;

import de.boereck.matcher.function.optionalmap.OptionalIntMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

@FunctionalInterface
public interface TestableToIntFunction<I> extends ToIntFunction<I> {

    default AdvPredicate<I> thenTest(IntPredicate test) {
        Objects.requireNonNull(test);
        return i -> test.test(applyAsInt(i));
    }

    default OptionalIntMapper<I> filter(IntPredicate test) {
        Objects.requireNonNull(test);
        return i -> {
            final int result = this.applyAsInt(i);
            return test.test(result) ? OptionalInt.of(result) : OptionalInt.empty();
        };
    }

    default Consumer<I> thenDo(IntConsumer consumer) {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.applyAsInt(i));
    }
}
