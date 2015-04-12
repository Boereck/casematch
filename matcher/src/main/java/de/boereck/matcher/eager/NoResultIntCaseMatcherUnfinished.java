package de.boereck.matcher.eager;

import java.util.Objects;
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
 * Eager implementation of {@link NoResultIntCaseMatcher}. To instantiate use static method {@link EagerMatcher#match(int)}.
 * This class holds the int value that is about to be checked for cases. It will evaluate predicates as soon as case methods are
 * called (and in the order they were called). When a case does not match it will return itself, when case matches
 * {@link NoResultIntCaseMatcherFinished} will be returned. This will not evaluate further predicate on cases, since the
 * matching case was already found.
 *
 * @author Max Bureck
 */
final class NoResultIntCaseMatcherUnfinished implements NoResultIntCaseMatcher {

    /**
     * Value cases are defined for
     */
    private final int toCheck;

    /**
     * Package private constructor. Should only be called from {@link EagerMatcher#match(int)}.
     *
     * @param toCheck int value cases are defined for.
     */
    NoResultIntCaseMatcherUnfinished(int toCheck) {
        this.toCheck = toCheck;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultIntCaseMatcher caseOf(int reference, IntConsumer consumer) {
        Objects.requireNonNull(consumer);
        // check if case matches
        if (reference == toCheck) {
            consumer.accept(toCheck);
            return NoResultIntCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultIntCaseMatcher caseOf(IntPredicate p, IntConsumer consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        // check if case matches
        final int toCheck = this.toCheck;
        if (p.test(toCheck)) {
            consumer.accept(toCheck);
            return NoResultIntCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultIntCaseMatcher caseOf(BooleanSupplier s, IntConsumer consumer) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(consumer);
        // check if case matches
        if (s.getAsBoolean()) {
            consumer.accept(toCheck);
            return NoResultIntCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultIntCaseMatcher caseOf(boolean test, IntConsumer consumer) {
        Objects.requireNonNull(consumer);
        // check if case matches
        if (test) {
            consumer.accept(toCheck);
            return NoResultIntCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultIntCaseMatcher caseObj(IntFunction<Optional<T>> f, Consumer<T> consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final Optional<T> opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.get());
            return NoResultIntCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultIntCaseMatcher caseInt(IntFunction<OptionalInt> f, IntConsumer consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalInt opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsInt());
            return NoResultIntCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultIntCaseMatcher caseLong(IntFunction<OptionalLong> f, LongConsumer consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalLong opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsLong());
            return NoResultIntCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultIntCaseMatcher caseDouble(IntFunction<OptionalDouble> f, DoubleConsumer consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalDouble opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsDouble());
            return NoResultIntCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void otherwise(IntConsumer consumer) {
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
