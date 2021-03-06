package de.boereck.matcher.eager;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import de.boereck.matcher.NoResultCaseMatcher;


/**
 * This class represents a {@link NoResultCaseMatcher} that already had a matching case. The
 * {@link de.boereck.matcher.eager.NoResultCaseMatcherFinished#instance() singleton instance} is provided by {@link NoResultCaseMatcherUnfinished}
 * whenever a matching case is found.
 *
 * @param <I> type of the input object
 * @author Max Bureck
 */
final class NoResultCaseMatcherFinished<I> implements EagerNoResultCaseMatcher<I> {

    /**
     * Singleton instance
     */
    private static final EagerNoResultCaseMatcher<?> INSTANCE = new NoResultCaseMatcherFinished<Object>();

    /**
     * Generics aware access to singleton instance
     *
     * @return singleton instance
     */
    @SuppressWarnings("unchecked")
    // cast is safe
    static <T> EagerNoResultCaseMatcher<T> instance() {
        return (NoResultCaseMatcherFinished<T>) INSTANCE;
    }

    /**
     * Should only be used from within this class. From the outside the singleton
     * accessor {@link de.boereck.matcher.eager.NoResultCaseMatcherFinished#instance() instance()} should be used.
     */
    private NoResultCaseMatcherFinished() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> EagerNoResultCaseMatcher<I> caseOf(Class<T> clazz, Consumer<? super T> consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> EagerNoResultCaseMatcher<I> caseOf(Class<T> clazz, Predicate<? super T> condition, Consumer<? super T> consumer) throws NullPointerException {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultCaseMatcher<I> caseOf(Predicate<? super I> p, Consumer<? super I> consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    @Override
    public EagerNoResultCaseMatcher<I> caseIs(Predicate<? super I> p, Runnable then) throws NullPointerException {
        // we already have a result, so nothing to check
        return this;
    }

    @Override
    public EagerNoResultCaseMatcher<I> caseIs(boolean test, Runnable then) throws NullPointerException {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultCaseMatcher<I> caseOf(BooleanSupplier s, Consumer<? super I> consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> EagerNoResultCaseMatcher<I> caseObj(Function<? super I, Optional<T>> p, Consumer<? super T> consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultCaseMatcher<I> caseInt(Function<? super I, OptionalInt> p, IntConsumer consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultCaseMatcher<I> caseLong(Function<? super I, OptionalLong> p, LongConsumer consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultCaseMatcher<I> caseDouble(Function<? super I, OptionalDouble> p, DoubleConsumer consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultCaseMatcher<I> caseOf(boolean test, Consumer<? super I> consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void otherwise(Consumer<? super I> consumer) {
        // do nothing, because earlier case already matched
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <X extends Throwable> void otherwiseThrow(Supplier<X> exSupplier) throws X {
        // do nothing, because earlier case already matched
    }

}
