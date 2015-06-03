package de.boereck.matcher.eager;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Supplier;

import de.boereck.matcher.NoResultDoubleCaseMatcher;

/**
 * This class represents a {@link NoResultDoubleCaseMatcher} that already had a matching case. The
 * {@link de.boereck.matcher.eager.NoResultDoubleCaseMatcherFinished#instance() singleton instance} is provided by
 * {@link NoResultDoubleCaseMatcherUnfinished} whenever a matching case is found. This class will never check a case since the
 * matching case was already found.
 *
 * @author Max Bureck
 */
final class NoResultDoubleCaseMatcherFinished implements NoResultDoubleCaseMatcher {

    /**
     * Singleton instance
     */
    private static final NoResultDoubleCaseMatcherFinished INSTANCE = new NoResultDoubleCaseMatcherFinished();

    /**
     * Access to singleton instance
     *
     * @return singleton instance
     */
    static NoResultDoubleCaseMatcherFinished instance() {
        return INSTANCE;
    }

    /**
     * Should only be used from within this class. From the outside the singleton accessor
     * {@link de.boereck.matcher.eager.NoResultDoubleCaseMatcherFinished#instance() instance()} should be used.
     */
    private NoResultDoubleCaseMatcherFinished() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultDoubleCaseMatcher caseOf(DoublePredicate p, DoubleConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultDoubleCaseMatcher caseOf(BooleanSupplier s, DoubleConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultDoubleCaseMatcher caseOf(boolean test, DoubleConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    @Override
    public NoResultDoubleCaseMatcher caseIs(DoublePredicate p, Runnable then) throws NullPointerException {
        return this;
    }

    @Override
    public NoResultDoubleCaseMatcher caseIs(boolean test, Runnable then) throws NullPointerException {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultDoubleCaseMatcher caseObj(DoubleFunction<Optional<T>> f, Consumer<T> consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultDoubleCaseMatcher caseInt(DoubleFunction<OptionalInt> f, IntConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultDoubleCaseMatcher caseLong(DoubleFunction<OptionalLong> f, LongConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultDoubleCaseMatcher caseDouble(DoubleFunction<OptionalDouble> f, DoubleConsumer consumer) {
        // do nothing, we already had a matching case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void otherwise(DoubleConsumer consumer) {
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
