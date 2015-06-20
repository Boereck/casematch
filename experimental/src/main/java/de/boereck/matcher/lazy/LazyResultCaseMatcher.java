package de.boereck.matcher.lazy;

import de.boereck.matcher.ResultCaseMatcher;
import de.boereck.matcher.function.optionalmap.OptionalMapper;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;

/**
 * Match that will be performed <em>after</em> defining all cases to be matched on, so the cases will be checked lazily.
 * After all cases are defined using the caseOf/caseIs/caseInt/caseDouble/caseLong methods, either method
 * {@link #match(Object)} or {@link #apply(Object)} (must have same behavior) can be called to start the matching.
 * match/apply are therefore the closing methods for this case matcher.
 * <p>The cases are guaranteed to be evaluated in the order they were defined. Cases and associated actions will all be
 * evaluated on the thread that calls the match/apply method. One of the consequences is that exceptions during case
 * evaluation or execution of the associated action will be thrown at the user calling the match/apply method.</p>
 * <p>The advantage of this lazy type of matching is that the objects describing the cases only have to be defined
 * once and can be reused whenever the matching is needed. The other advantage is that the case matcher can be used
 * whenever a function is needed, e.g. in a higher level map method, such as
 * {@link java.util.stream.Stream#map(Function) Stream.map(Function)}<p/>
 */
public interface LazyResultCaseMatcher<I,O> extends ResultCaseMatcher<I,O>, MatchingFunction<I, Optional<O>> {

    /**
     * {@inheritDoc}
     */
    @Override
    <T> LazyResultCaseMatcher<I, O> caseOf(Class<T> clazz, Function<? super T, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    <T> LazyResultCaseMatcher<I, O> caseOf(Class<T> clazz, Predicate<? super T> condition, Function<? super T, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    LazyResultCaseMatcher<I, O> caseOf(Predicate<? super I> p, Function<? super I, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    LazyResultCaseMatcher<I, O> caseOf(BooleanSupplier s, Function<? super I, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    LazyResultCaseMatcher<I, O> caseOf(boolean test, Function<? super I, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    LazyResultCaseMatcher<I,O> caseIs(Predicate<? super I> p, Supplier<? extends O> supplier) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    LazyResultCaseMatcher<I,O> caseIs(boolean test, Supplier<? extends O> supplier) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    <T> LazyResultCaseMatcher<I, O> caseObj(Function<? super I, Optional<T>> p, Function<? super T, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    LazyResultCaseMatcher<I, O> caseInt(Function<? super I, OptionalInt> p, IntFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    LazyResultCaseMatcher<I, O> caseLong(Function<? super I, OptionalLong> p, LongFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    LazyResultCaseMatcher<I, O> caseDouble(Function<? super I, OptionalDouble> p, DoubleFunction<? extends O> f) throws NullPointerException;

    /**
     * Starts the matching process on the given input object {@code i}.
     * @param i parameter to match on.
     * @return match result.
     * @see #match(Object)
     */
    @Override
    Optional<O> apply(I i);

    MatchingFunction<I,O> otherwise(O o);

    MatchingFunction<I,O> orElse(O o);

    <X extends Exception> ThrowingMatchingFunction<I,O,X> otherwiseThrow(Supplier<X> exceptionSupplier);

    MatchingFunction<I,O> otherwiseThrowRuntime(Supplier<? extends RuntimeException> exceptionSupplier);

    /**
     * Returns a function that will perform matches and if there is a result available will return it, if there was no
     * match, the function will throw a {@link java.util.NoSuchElementException}.
     *
     * @return function not defined on inputs that do not have a matching case defined for this input. This means that
     * if there is a matching case the result of this match is returned, otherwise a {@link java.util.NoSuchElementException} is
     * thrown.
     */
    MatchingFunction<I,O> partial();
}
