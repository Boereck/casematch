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
 * @author Max Bureck
 * @param <I>
 *            type of the input object
 */
final class NoResultCaseMatcherFinished<I> implements NoResultCaseMatcher<I> {

    /**
     * Singleton instance
     */
    private static final NoResultCaseMatcherFinished<?> INSTANCE = new NoResultCaseMatcherFinished<Object>();

    /**
     * Generics aware access to singleton instance
     * @return singleton instance
     */
    @SuppressWarnings("unchecked")
    // cast is safe
    static <T> NoResultCaseMatcherFinished<T> instance() {
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
    public <T> NoResultCaseMatcher<I> caseOf(Class<T> clazz, Consumer<? super T> consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultCaseMatcher<I> caseOf(Predicate<? super I> p, Consumer<? super I> consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultCaseMatcher<I> caseOf(BooleanSupplier s, Consumer<? super I> consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultCaseMatcher<I> caseObj(Function<? super I, Optional<T>> p, Consumer<? super T> consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultCaseMatcher<I> caseInt(Function<? super I, OptionalInt> p, IntConsumer consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultCaseMatcher<I> caseLong(Function<? super I, OptionalLong> p, LongConsumer consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultCaseMatcher<I> caseDouble(Function<? super I, OptionalDouble> p, DoubleConsumer consumer) {
        // we already have a result, so nothing to check
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultCaseMatcher<I> caseOf(boolean test, Consumer<? super I> consumer) {
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
