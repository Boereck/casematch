package de.boereck.matcher.function.predicate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

@FunctionalInterface
public interface AdvIntPredicate extends IntPredicate {

    default <O> IntFunction<Optional<O>> optionally(IntFunction<O> f) {
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
     * @param that
     * @return
     */
    default AdvIntPredicate requires(AdvIntPredicate that) {
        return that.and(this)::test;
    }
    
    default Predicate<Integer> forInteger() {
        return i -> i == null ? false : this.test(i.intValue());
    }
}
