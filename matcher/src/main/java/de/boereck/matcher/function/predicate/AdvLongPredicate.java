package de.boereck.matcher.function.predicate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;

@FunctionalInterface
public interface AdvLongPredicate extends LongPredicate {

    default <O> LongFunction<Optional<O>> preOf(LongFunction<? extends O> f) {
        Objects.requireNonNull(f);
        return i -> this.test(i) ? Optional.ofNullable(f.apply(i)) : Optional.empty();
    }

    default AdvLongPredicate xor(LongPredicate that) {
        Objects.requireNonNull(that);
        return i -> this.test(i) ^ that.test(i);
    }

    default AdvLongPredicate nor(LongPredicate that) {
        Objects.requireNonNull(that);
        return i -> !this.test(i) && !that.test(i);
    }

    /**
     * Logical equality of two predicates
     *
     * @param that
     * @return
     */
    default AdvLongPredicate xnor(LongPredicate that) {
        Objects.requireNonNull(that);
        return i -> this.test(i) == that.test(i);
    }

    default AdvLongPredicate implies(LongPredicate that) {
        Objects.requireNonNull(that);
        return i -> !this.test(i) || that.test(i);
    }

    @Override
    default AdvLongPredicate and(LongPredicate that) {
        Objects.requireNonNull(that);
        return i -> test(i) && that.test(i);
    }

    @Override
    default AdvLongPredicate negate() {
        return i -> !test(i);
    }

    /**
     * Shortcut for {@link de.boereck.matcher.function.predicate.AdvLongPredicate#negate()}
     *
     * @return
     */
    default AdvLongPredicate not() {
        return negate();
    }

    @Override
    default AdvLongPredicate or(LongPredicate that) {
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
    default AdvLongPredicate requires(AdvLongPredicate that) {
        return that.and(this)::test;
    }
}
