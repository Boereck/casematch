package de.boereck.matcher.helpers;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

/**
 * Function checking if an instance of I is either instance of or exactly of type O. It is essential for this interface, that
 * if {@link de.boereck.matcher.helpers.TypeCheck#test(Object)} returns true, the tested object is assignable to type O.
 *
 * @param <I> type of objects that can be checked for it's type
 * @param <O> type the tested object can be assigned to.
 * @author Max Bureck
 */
@FunctionalInterface
public interface TypeCheck<I, O> extends AdvPredicate<I> {

    /**
     * This is a lazy concatenation of tests. The returned TypeCheck will first test this predicate and only if this returns
     * true will test the given otherTest. The result will be the logical short-circuiting AND operation.
     * <p>
     * <p>
     * Of any exceptions are thrown during evaluation of either predicate are passed on to the caller of the composed
     * predicate. If this predicate throws an exception, the {@code otherTest} predicate will not be called.
     * </p>
     *
     * @param otherTest will be evaluated if this predicate evaluates to true and throws no exception. The result of the given
     *                  {@code otherTest} predicate will be combined with logical AND with the result of this predicate. Since
     *                  otherTest will only be evaluated if this predicate evaluates to {@code true}, the tested object will
     *                  automatically casted to the tested type.
     * @return a composed predicate, representing the lazily evaluate logical
     * @throws NullPointerException if predicate {@code otherTest} is null.
     * @see de.boereck.matcher.helpers.TypeCheck#and(java.util.function.Predicate) for composition without casting of tested object. AND of this predicate and the
     * {@code otherTest} predicate.
     */
    @SuppressWarnings("unchecked")
    // we checked the type, so cast is safe
    default TypeCheck<I, O> andTest(Predicate<O> otherTest) throws NullPointerException {
        Objects.requireNonNull(otherTest);
        return (t) -> test(t) && otherTest.test((O) t);
    }

    /**
     * This is a lazy concatenation of tests. The returned TypeCheck will first test this predicate and only if this returns
     * true will test the given otherTest. The result will be the logical short-circuiting IMPLIES operation.
     * <p>
     * <p>
     * Of any exceptions are thrown during evaluation of either predicate are passed on to the caller of the composed
     * predicate. If this predicate throws an exception, the {@code otherTest} predicate will not be called.
     * </p>
     *
     * @param otherTest will be evaluated if this predicate evaluates to true and throws no exception. The result of the given
     *                  {@code otherTest} predicate will be combined with logical IMPLIES with the result of this predicate. Since
     *                  otherTest will only be evaluated if this predicate evaluates to {@code true}, the tested object will
     *                  automatically casted to the tested type.
     * @return a composed predicate, representing the lazily evaluate logical
     * @throws NullPointerException if predicate {@code otherTest} is null.
     * @see de.boereck.matcher.helpers.TypeCheck#and(java.util.function.Predicate) for composition without casting of tested object. AND of this predicate and the
     * {@code otherTest} predicate.
     */
    @SuppressWarnings("unchecked") // cast is only performed if type check was successful, so cast is safe
    default AdvPredicate<I> impliesTest(Predicate<? super O> otherTest) throws NullPointerException {
        Objects.requireNonNull(otherTest);
        return i -> !this.test(i) || otherTest.test((O) i);
    }

    /**
     * Returns a function that performs a map operation after this type check, if this type check
     * succeeds in the first place. If the type checks succeeds the returned Optional will hold the
     * result of the call of mapping function {@code f}, otherwise the function will return an empty
     * Optional.
     * @param f mapping function that will take the casted output of this TypeCheck and called with
     *          the result of the cast. The result of this mapping function will be returned in an
     *          Optional object from the function returned by this method.
     * @param <R> target type of the mapping operation.
     * @return function that performs a map operation after this type check, if this type check
     * succeeds in the first place.
     * @throws NullPointerException if mapping function {@code f} is {@code null}.
     */
    @SuppressWarnings("unchecked") // we checked type before cast, so cast is safe
    default <R> OptionalMapper<I, R> thenMap(Function<? super O, ? extends R> f) throws NullPointerException {
        Objects.requireNonNull(f);
        return i -> this.test(i) ? Optional.ofNullable(f.apply((O) i)) : Optional.empty();
    }

    /**
     * Returns a function that performs a map operation after this type check, if this type check
     * succeeds in the first place. If the type checks succeeds the returned Optional will be the
     * result of the call of mapping function {@code f}, otherwise the function will return an empty
     * Optional.
     * @param f mapping function that will take the casted output of this TypeCheck and called with
     *          the result of the cast. The result of this mapping function will be returned as the result
     *          of the function returned by this method.
     * @param <R> target type of the mapping operation.
     * @return function that performs a map operation after this type check, if this type check
     * succeeds in the first place.
     * @throws NullPointerException if mapping function {@code f} is {@code null}.
     */
    default <R> OptionalMapper<I,R> thenFlatMap(Function<? super O, Optional<R>> f) throws NullPointerException {
        Objects.requireNonNull(f);
        return i -> this.test(i) ? f.apply((O) i) : Optional.empty();
    }
}