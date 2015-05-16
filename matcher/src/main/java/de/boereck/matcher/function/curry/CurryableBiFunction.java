package de.boereck.matcher.function.curry;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import de.boereck.matcher.function.testable.TestableFunction;

@FunctionalInterface
public interface CurryableBiFunction<I1, I2, O> extends BiFunction<I1, I2, O> {

    default CurryableFunction<I2, O> curry(I1 i1) {
        return _1(i1);
    }

    default CurryableFunction<I2, O> _1(I1 i1) {
        return i2 -> this.apply(i1, i2);
    }

    default CurryableFunction<I2, O> _1(Supplier<I1> i1supplier) {
        return i2 -> this.apply(i1supplier.get(), i2);
    }

    default CurryableFunction<I1, O> rcurry(I2 i2) {
        return _2(i2);
    }

    default CurryableFunction<I1, O> _2(I2 i2) {
        return i1 -> this.apply(i1, i2);
    }

    default CurryableFunction<I1, O> _2(Supplier<I2> i2supplier) {
        return i1 -> this.apply(i1, i2supplier.get());
    }

    static <I1, I2, O> CurryableBiFunction<I1, I2, O> bnd(BiFunction<I1, I2, O> f) {
        return f::apply;
    }

    static <I1, I2, O> CurryableBiFunction<I1, I2, O> λ(BiFunction<I1, I2, O> f) {
        return bnd(f);
    }
}
