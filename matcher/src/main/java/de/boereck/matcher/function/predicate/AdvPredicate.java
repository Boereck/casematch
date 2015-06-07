package de.boereck.matcher.function.predicate;

import de.boereck.matcher.function.optionalmap.OptionalMapper;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface AdvPredicate<I> extends Predicate<I> {

    default <O> OptionalMapper<I,O> preFor(Function<? super I, ? extends O> f) throws NullPointerException {
        Objects.requireNonNull(f);
        return i -> this.test(i) ? Optional.ofNullable(f.apply(i)) : Optional.empty();
    }

    default <O> OptionalMapper<I,O> then(Function<? super I, ? extends O> f) throws NullPointerException {
        Objects.requireNonNull(f);
        return i -> this.test(i) ? Optional.ofNullable(f.apply(i)) : Optional.empty();
    }

    default <O> OptionalMapper<I,O> thenFlat(Function<? super I, Optional<O>> f)throws NullPointerException {
        Objects.requireNonNull(f);
        return i -> this.test(i) ? f.apply(i) : Optional.empty();
    }

    default AdvPredicate<I> xor(Predicate<? super I> that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> this.test(i) ^ that.test(i);
    }

    default AdvPredicate<I> nor(Predicate<? super I> that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> !this.test(i) && !that.test(i);
    }

    /**
     * Logical equality of two predicates
     *
     * @param that
     * @return
     */
    default AdvPredicate<I> xnor(Predicate<? super I> that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> this.test(i) == that.test(i);
    }

    default AdvPredicate<I> implies(Predicate<? super I> that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> !this.test(i) || that.test(i);
    }

    @Override
    default AdvPredicate<I> and(Predicate<? super I> that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> test(i) && that.test(i);
    }

    @Override
    default AdvPredicate<I> negate() {
        return i -> !test(i);
    }

    /**
     * Shortcut for {@link de.boereck.matcher.function.predicate.AdvPredicate#negate()}
     *
     * @return
     */
    default AdvPredicate<I> not() {
        return negate();
    }

    @Override
    default AdvPredicate<I> or(Predicate<? super I> that) throws NullPointerException {
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
    default AdvPredicate<I> requires(Predicate<? super I> that) {
        return i -> that.test(i) && this.test(i);
    }

    /**
     * The returned predicate will check if the input is {@code null} and return {@code false}, if so. Otherwise it
     * will call this AdvPredicate and return the result.
     * @return predicate returning {@code false} if the input is {@code null}, otherwise the result of this predicate
     *  with the same input.
     */
    default AdvPredicate<I> nullToFalse() {
        return i -> i != null && this.test(i);
    }

    /**
     * The returned predicate will check if the input is {@code null} and return {@code true}, if so. Otherwise it
     * will call this AdvPredicate and return the result.
     * @return predicate returning {@code true} if the input is {@code null}, otherwise the result of this predicate
     *  with the same input.
     */
    default AdvPredicate<I> nullToTrue() {
        return i -> i == null || this.test(i);
    }
}
