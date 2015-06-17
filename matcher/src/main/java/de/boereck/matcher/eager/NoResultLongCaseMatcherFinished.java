package de.boereck.matcher.eager;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.Supplier;

import de.boereck.matcher.NoResultLongCaseMatcher;

/**
 * This class represents a {@link NoResultLongCaseMatcher} that already had a matching case. The
 * {@link de.boereck.matcher.eager.NoResultLongCaseMatcherFinished#instance() singleton instance} is provided by
 * {@link NoResultLongCaseMatcherUnfinished} whenever a matching case is found. This class will never check a case since the
 * matching case was already found.
 *
 * @author Max Bureck
 */
final class NoResultLongCaseMatcherFinished implements EagerNoResultLongCaseMatcher {

    /**
     * Singleton instance
     */
    private static final NoResultLongCaseMatcherFinished INSTANCE = new NoResultLongCaseMatcherFinished();

    /**
     * Access to singleton instance
     *
     * @return singleton instance
     */
    static NoResultLongCaseMatcherFinished instance() {
        return INSTANCE;
    }

    /**
     * Should only be used from within this class. From the outside the singleton accessor
     * {@link de.boereck.matcher.eager.NoResultLongCaseMatcherFinished#instance() instance()} should be used.
     */
    private NoResultLongCaseMatcherFinished() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> EagerNoResultLongCaseMatcher caseOf(long reference, LongConsumer consumer) throws NullPointerException {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseOf(LongPredicate p, LongConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseOf(BooleanSupplier s, LongConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseOf(boolean test, LongConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    @Override
    public EagerNoResultLongCaseMatcher caseIs(LongPredicate p, Runnable then) throws NullPointerException {
        // do nothing, we already had a matching case
        return this;
    }

    @Override
    public EagerNoResultLongCaseMatcher caseIs(boolean test, Runnable then) throws NullPointerException {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> EagerNoResultLongCaseMatcher caseObj(LongFunction<Optional<T>> f, Consumer<T> consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseInt(LongFunction<OptionalInt> f, IntConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseLong(LongFunction<OptionalLong> f, LongConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseDouble(LongFunction<OptionalDouble> f, DoubleConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void otherwise(LongConsumer consumer) {
        // do nothing, we already had a matching case
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <X extends Throwable> void otherwiseThrow(Supplier<X> exSupplier) throws X {
        // do nothing, we already had a matching case
    }

}
