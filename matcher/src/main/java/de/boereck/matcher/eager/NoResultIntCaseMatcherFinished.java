package de.boereck.matcher.eager;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.LongConsumer;
import java.util.function.Supplier;

import de.boereck.matcher.NoResultIntCaseMatcher;

/**
 * This class represents a {@link NoResultIntCaseMatcher} that already had a matching case. The
 * {@link de.boereck.matcher.eager.NoResultIntCaseMatcherFinished#instance() singleton instance} is provided by {@link NoResultIntCaseMatcherUnfinished}
 * whenever a matching case is found. This class will never check a case since the matching case was
 * already found.
 *
 * @author Max Bureck
 */
final class NoResultIntCaseMatcherFinished implements EagerNoResultIntCaseMatcher {

    /**
     * Singleton instance
     */
    private static final NoResultIntCaseMatcherFinished INSTANCE = new NoResultIntCaseMatcherFinished();

    /**
     * Access to singleton instance
     *
     * @return singleton instance
     */
    static NoResultIntCaseMatcherFinished instance() {
        return INSTANCE;
    }

    /**
     * Should only be used from within this class. From the outside the singleton
     * accessor {@link de.boereck.matcher.eager.NoResultIntCaseMatcherFinished#instance() instance()} should be used.
     */
    private NoResultIntCaseMatcherFinished() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultIntCaseMatcher caseOf(int reference, IntConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultIntCaseMatcher caseOf(IntPredicate p, IntConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultIntCaseMatcher caseOf(BooleanSupplier s, IntConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultIntCaseMatcher caseOf(boolean test, IntConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    @Override
    public EagerNoResultIntCaseMatcher caseIs(IntPredicate p, Runnable then) throws NullPointerException {
        // do nothing, we already had a matching case
        return this;
    }

    @Override
    public EagerNoResultIntCaseMatcher caseIs(boolean test, Runnable then) throws NullPointerException {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> EagerNoResultIntCaseMatcher caseObj(IntFunction<Optional<T>> f, Consumer<T> consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultIntCaseMatcher caseInt(IntFunction<OptionalInt> f, IntConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultIntCaseMatcher caseLong(IntFunction<OptionalLong> f, LongConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultIntCaseMatcher caseDouble(IntFunction<OptionalDouble> f, DoubleConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void otherwise(IntConsumer consumer) {
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
