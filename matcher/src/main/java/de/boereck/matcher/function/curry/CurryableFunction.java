package de.boereck.matcher.function.curry;

import de.boereck.matcher.function.testable.TestableFunction;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 */
@FunctionalInterface
public interface CurryableFunction<I,O> extends TestableFunction<I,O> {

    default Supplier<O> _1(I i) {
        return () -> apply(i);
    }

    default Supplier<O> _1(Supplier<I> inputSupplier) throws NullPointerException {
        Objects.requireNonNull(inputSupplier);
        return () -> apply(inputSupplier.get());
    }

    static <I, O> CurryableFunction<I, O> bnd(Function<I, O> f) {
        return f::apply;
    }

    static <I, O> CurryableFunction<I, O> λ(Function<I, O> f) {
        return bnd(f);
    }
}
