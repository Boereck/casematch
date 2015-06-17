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
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.Supplier;

import de.boereck.matcher.NoResultLongCaseMatcher;

/**
 * Eager implementation of {@link NoResultLongCaseMatcher}. To instantiate use static method {@link EagerMatcher#match(long)}.
 * This class holds the long value that is about to be checked for cases. It will evaluate predicates as soon as case methods are
 * called (and in the order they were called). When a case does not found it will return itself, when case matches
 * {@link NoResultLongCaseMatcherFinished} will be returned. This will not evaluate further predicate on cases, since the
 * matching case was already found.
 *
 * @author Max Bureck
 */
final class NoResultLongCaseMatcherUnfinished implements EagerNoResultLongCaseMatcher {

    /**
     * Value cases are defined for
     */
    private final long toCheck;

    /**
     * Package private constructor. Should only be called from {@link EagerMatcher#match(long)}.
     *
     * @param toCheck long value cases are defined for.
     */
    NoResultLongCaseMatcherUnfinished(long toCheck) {
        this.toCheck = toCheck;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseOf(long reference, LongConsumer consumer) {
        Objects.requireNonNull(consumer);
        // check if case matches
        if (reference == toCheck) {
            consumer.accept(toCheck);
            return NoResultLongCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseOf(LongPredicate p, LongConsumer consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        // check if case matches
        final long toCheck = this.toCheck;
        if (p.test(toCheck)) {
            consumer.accept(toCheck);
            return NoResultLongCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseOf(BooleanSupplier s, LongConsumer consumer) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(consumer);
        // check if case matches
        if (s.getAsBoolean()) {
            consumer.accept(toCheck);
            return NoResultLongCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseOf(boolean test, LongConsumer consumer) {
        Objects.requireNonNull(consumer);
        // check if case matches
        if (test) {
            consumer.accept(toCheck);
            return NoResultLongCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    @Override
    public EagerNoResultLongCaseMatcher caseIs(LongPredicate p, Runnable then) throws NullPointerException {
        Objects.requireNonNull(p);
        Objects.requireNonNull(then);
        final long toCheck = this.toCheck;
        if (p.test(toCheck)) {
            then.run();
            return NoResultLongCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    @Override
    public EagerNoResultLongCaseMatcher caseIs(boolean test, Runnable then) throws NullPointerException {
        Objects.requireNonNull(then);
        if (test) {
            then.run();
            return NoResultLongCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> EagerNoResultLongCaseMatcher caseObj(LongFunction<Optional<T>> f, Consumer<T> consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final Optional<T> opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.get());
            return NoResultLongCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseInt(LongFunction<OptionalInt> f, IntConsumer consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalInt opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsInt());
            return NoResultLongCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseLong(LongFunction<OptionalLong> f, LongConsumer consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalLong opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsLong());
            return NoResultLongCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerNoResultLongCaseMatcher caseDouble(LongFunction<OptionalDouble> f, DoubleConsumer consumer) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalDouble opt = f.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsDouble());
            return NoResultLongCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void otherwise(LongConsumer consumer) {
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
