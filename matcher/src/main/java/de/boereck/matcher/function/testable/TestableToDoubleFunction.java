package de.boereck.matcher.function.testable;

import java.util.Objects;
import java.util.OptionalDouble;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.ToDoubleFunction;

import de.boereck.matcher.function.optionalmap.OptionalDoubleMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

@FunctionalInterface
public interface TestableToDoubleFunction<I> extends ToDoubleFunction<I> {

    default AdvPredicate<I> thenTest(DoublePredicate test) {
        Objects.requireNonNull(test);
        return i -> test.test(applyAsDouble(i));
    }

    default OptionalDoubleMapper<I> filter(DoublePredicate test) {
        Objects.requireNonNull(test);
        return i -> {
            final double result = this.applyAsDouble(i);
            return test.test(result) ? OptionalDouble.of(result) : OptionalDouble.empty();
        };
    }

    default Consumer<I> thenDo(DoubleConsumer consumer) {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.applyAsDouble(i));
    }
}
