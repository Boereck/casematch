package de.boereck.matcher.lazy;

import de.boereck.matcher.ResultCaseMatcher;
import de.boereck.matcher.function.optionalmap.OptionalMapper;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;

/**
 * Match that will be performed after defining all cases to be matched on, so the cases will be checked lazily.
 */
public interface LazyResultCaseMatcher<I,O> extends ResultCaseMatcher<I,O>, Function<I, Optional<O>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> LazyResultCaseMatcher<I, O> caseOf(Class<T> clazz, Function<? super T, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> LazyResultCaseMatcher<I, O> caseOf(Class<T> clazz, Predicate<? super T> condition, Function<? super T, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract LazyResultCaseMatcher<I, O> caseOf(Predicate<? super I> p, Function<? super I, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract LazyResultCaseMatcher<I, O> caseOf(BooleanSupplier s, Function<? super I, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract LazyResultCaseMatcher<I, O> caseOf(boolean test, Function<? super I, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract LazyResultCaseMatcher<I,O> caseIs(Predicate<? super I> p, Supplier<? extends O> supplier) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract LazyResultCaseMatcher<I,O> caseIs(boolean test, Supplier<? extends O> supplier) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> LazyResultCaseMatcher<I, O> caseObj(Function<? super I, Optional<T>> p, Function<? super T, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract LazyResultCaseMatcher<I, O> caseInt(Function<? super I, OptionalInt> p, IntFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract LazyResultCaseMatcher<I, O> caseLong(Function<? super I, OptionalLong> p, LongFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract LazyResultCaseMatcher<I, O> caseDouble(Function<? super I, OptionalDouble> p, DoubleFunction<? extends O> f) throws NullPointerException;


    // TODO more closing methods

    /**
     * Starts the matching process on the given input object {@code i}.
     * @param i parameter to match on.
     * @return match result.
     */
    @Override
    Optional<O> apply(I i);

    Function<I,O> otherwise(O o);

    /**
     * Returns a function that will perform matches and if there is a result available will return it, if there was no
     * match, the function will throw a {@link java.util.NoSuchElementException}.
     *
     * @return function not defined on inputs that do not have a matching case defined for this input. This means that
     * if there is a matching case the result of this match is returned, otherwise a {@link java.util.NoSuchElementException} is
     * thrown.
     */
    Function<I,O> partial();
}
