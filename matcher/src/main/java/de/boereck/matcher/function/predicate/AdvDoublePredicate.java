package de.boereck.matcher.function.predicate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface AdvDoublePredicate extends DoublePredicate {

    default <O> DoubleFunction<Optional<O>> optionally(DoubleFunction<O> f) {
        Objects.requireNonNull(f);
        return i -> this.test(i) ? Optional.ofNullable(f.apply(i)) : Optional.empty();
    }
    
    default AdvDoublePredicate xor(DoublePredicate that) {
        Objects.requireNonNull(that);
        return i -> this.test(i) ^ that.test(i);
    }
    
    default AdvDoublePredicate nor(DoublePredicate that) {
        Objects.requireNonNull(that);
        return i -> !this.test(i) && !that.test(i);
    }
    
    /**
     * Logical equality of two predicates
     * @param that
     * @return
     */
    default AdvDoublePredicate xnor(DoublePredicate that) {
        Objects.requireNonNull(that);
        return i -> this.test(i) == that.test(i);
    }
    
    default AdvDoublePredicate implies(DoublePredicate that) {
        Objects.requireNonNull(that);
        return i -> !this.test(i) || that.test(i);
    }
    
    @Override
    default AdvDoublePredicate and(DoublePredicate that) {
        Objects.requireNonNull(that);
        return i -> test(i) && that.test(i);
    }
    
    @Override
    default AdvDoublePredicate negate() {
        return i -> !test(i);
    }
    
    /**
     * Shortcut for {@link de.boereck.matcher.function.predicate.AdvDoublePredicate#negate()}
     * @return
     */
    default AdvDoublePredicate not() {
        return negate();
    }
    
    @Override
    default AdvDoublePredicate or(DoublePredicate that) {
        Objects.requireNonNull(that);
        return i -> test(i) || that.test(i);
    }
    
    /**
     * Defines precondition that has to hold before checking
     * this predicate.
     * @param that
     * @return
     */
    default DoublePredicate requires(DoublePredicate that) {
        return that.and(this)::test;
    }
}
