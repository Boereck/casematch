package de.boereck.matcher.eager;

import java.util.Objects;
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
 * Eager implementation of {@link NoResultDoubleCaseMatcher}. To instantiate use static method {@link EagerMatcher#match(double)}.
 * This class holds the double value that is about to be checked for cases. It will evaluate predicates as soon as case methods are
 * called (and in the order they were called). When a case does not found it will return itself, when case matches
 * {@link NoResultDoubleCaseMatcherFinished} will be returned. This will not evaluate further predicate on cases, since the
 * matching case was already found.
 *
 * @author Max Bureck
 */
final class NoResultDoubleCaseMatcherUnfinished implements EagerNoResultDoubleCaseMatcher {

    /**
     * Value cases are defined for
     */
    private final double toCheck;

    /**
     * Package private constructor. Should only be called from {@link EagerMatcher#match(double)}.
     *
     * @param toCheck double value cases are defined for.
     */
    NoResultDoubleCaseMatcherUnfinished(double toCheck) {
        this.toCheck = toCheck;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultDoubleCaseMatcher caseOf(DoublePredicate p, DoubleConsumer consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        // check if case matches
        final double toCheck = this.toCheck;
        if (p.test(toCheck)) {
            consumer.accept(toCheck);
            return NoResultDoubleCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultDoubleCaseMatcher caseOf(BooleanSupplier s, DoubleConsumer consumer) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(consumer);
        // check if case matches
        if (s.getAsBoolean()) {
            consumer.accept(toCheck);
            return NoResultDoubleCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultDoubleCaseMatcher caseOf(boolean test, DoubleConsumer consumer) {
        Objects.requireNonNull(consumer);
        // check if case matches
        if (test) {
            consumer.accept(toCheck);
            return NoResultDoubleCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    @Override
    public EagerNoResultDoubleCaseMatcher caseIs(DoublePredicate p, Runnable then) throws NullPointerException {
        Objects.requireNonNull(p);
        Objects.requireNonNull(then);
        final double toCheck = this.toCheck;
        if (p.test(toCheck)) {
            then.run();
            return NoResultDoubleCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    @Override
    public EagerNoResultDoubleCaseMatcher caseIs(boolean test, Runnable then) throws NullPointerException {
        Objects.requireNonNull(then);
        if (test) {
            then.run();
            return NoResultDoubleCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> EagerNoResultDoubleCaseMatcher caseObj(DoubleFunction<Optional<T>> f, Consumer<T> consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final Optional<T> opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.get());
            return NoResultDoubleCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultDoubleCaseMatcher caseInt(DoubleFunction<OptionalInt> f, IntConsumer consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalInt opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsInt());
            return NoResultDoubleCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultDoubleCaseMatcher caseLong(DoubleFunction<OptionalLong> f, LongConsumer consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalLong opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsLong());
            return NoResultDoubleCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultDoubleCaseMatcher caseDouble(DoubleFunction<OptionalDouble> f, DoubleConsumer consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalDouble opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsDouble());
            return NoResultDoubleCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void otherwise(DoubleConsumer consumer) {
        Objects.requireNonNull(consumer);
        // no cases matched so far, so we have to use the otherwise consumer
        consumer.accept(toCheck);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <X extends Throwable> void otherwiseThrow(Supplier<X> exSupplier) throws X {
        Objects.requireNonNull(exSupplier);
        // no cases matched so far, so we have throw a supplied exception
        throw Objects.requireNonNull(exSupplier.get());
    }

}
