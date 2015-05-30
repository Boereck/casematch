package de.boereck.matcher.function.testable;

import java.util.Objects;
import java.util.OptionalLong;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.ToLongFunction;

import de.boereck.matcher.function.optionalmap.OptionalLongMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

@FunctionalInterface
public interface TestableToLongFunction<I> extends ToLongFunction<I> {

    default AdvPredicate<I> thenTest(LongPredicate test) {
        Objects.requireNonNull(test);
        return i -> test.test(applyAsLong(i));
    }

    default OptionalLongMapper<I> filter(LongPredicate test) {
        Objects.requireNonNull(test);
        return i -> {
            final long result = this.applyAsLong(i);
            return test.test(result) ? OptionalLong.of(result) : OptionalLong.empty();
        };
    }

    default Consumer<I> thenDo(LongConsumer consumer) {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.applyAsLong(i));
    }
}
