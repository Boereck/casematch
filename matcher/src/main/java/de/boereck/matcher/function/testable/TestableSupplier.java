package de.boereck.matcher.function.testable;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface TestableSupplier<O> extends Supplier<O> {

    default BooleanSupplier thenTest(Predicate<O> test) {
        Objects.requireNonNull(test);
        return () -> test.test(get());
    }

    default TestableSupplier<Optional<O>> optional() {
        return () -> Optional.ofNullable(get());
    }

    default Runnable thenDo(Consumer<O> consumer) {
        Objects.requireNonNull(consumer);
        return () -> consumer.accept(get());
    }

    default TestableSupplier<Optional<O>> filter(Predicate<O> test) {
        Objects.requireNonNull(test);
        return () -> {
            final O result = get();
            return test.test(result) ? Optional.ofNullable(result) : Optional.empty();
        };
    }
}
