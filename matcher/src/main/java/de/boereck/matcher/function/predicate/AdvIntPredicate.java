package de.boereck.matcher.function.predicate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * This extended IntPredicate provides more default methods as boolean operators.
 * <style type="text/css">
 *     .truthtable, .truthtable td, .truthtable th {
 *         border: 1px solid black;
 *         border-collapse: collapse;
 *     }
 *     .false {
 *         background-color:LightPink;
 *     }
 *     .false::after {
 *         content: 'false';
 *     }
 *     .true {
 *         background-color:LightGreen;
 *     }
 *     .true::after {
 *         content: 'true';
 *     }
 * </style>
 */
@FunctionalInterface
public interface AdvIntPredicate extends IntPredicate {

    /**
     * Applies this predicate as a precondition for the given function {@code f}. The returned function will return an empty
     * optional if this predicate evaluates to {@code false} for an input. Otherwise the returned function will return
     * an optional holding the output of the wrapped function {@code f} for the given input.
     * @param f wrapped mapping function. It will only be called if the predicate returns {@code true} for a given input.
     * @param <O> Type of output object of the wrapped function.
     * @return Function that will either return an empty optional (if this predicate returns {@code false} for an input),
     *  or an optional holding the output of function {@code f} (if this predicate returns {@code true} for an input).
     * @throws NullPointerException if {@code f} is {@code null}.
     */
    default <O> IntFunction<Optional<O>> preFor(IntFunction<O> f) {
        Objects.requireNonNull(f);
        return i -> this.test(i) ? Optional.ofNullable(f.apply(i)) : Optional.empty();
    }

    default AdvIntPredicate xor(IntPredicate that) {
        Objects.requireNonNull(that);
        return i -> this.test(i) ^ that.test(i);
    }

    default AdvIntPredicate nor(IntPredicate that) {
        Objects.requireNonNull(that);
        return i -> !this.test(i) && !that.test(i);
    }

    /**
     * Logical equality of two predicates
     *
     * @param that
     * @return
     */
    default AdvIntPredicate xnor(IntPredicate that) {
        Objects.requireNonNull(that);
        return i -> this.test(i) == that.test(i);
    }

    default AdvIntPredicate implies(IntPredicate that) {
        Objects.requireNonNull(that);
        return i -> !this.test(i) || that.test(i);
    }

    @Override
    default AdvIntPredicate and(IntPredicate that) {
        Objects.requireNonNull(that);
        return i -> test(i) && that.test(i);
    }

    @Override
    default AdvIntPredicate negate() {
        return i -> !test(i);
    }

    /**
     * Shortcut for {@link de.boereck.matcher.function.predicate.AdvIntPredicate#negate()}
     *
     * @return
     */
    default AdvIntPredicate not() {
        return negate();
    }

    @Override
    default AdvIntPredicate or(IntPredicate that) {
        Objects.requireNonNull(that);
        return i -> test(i) || that.test(i);
    }

    /**
     * Defines precondition that has to hold before checking
     * this predicate.
     *
     * @param that
     * @return
     */
    default AdvIntPredicate requires(IntPredicate that) {
        return that.and(this)::test;
    }

    default Predicate<Integer> forInteger() {
        return i -> i != null && this.test(i);
    }
}
