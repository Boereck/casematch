package de.boereck.matcher.eager;

import java.util.Objects;
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
 * Eager implementation of {@link NoResultCaseMatcher}. To instantiate use static method {@link EagerMatcher#match(Object)}.
 * This class holds the object that is about to be checked for cases. It will evaluate predicates as soon as case methods are
 * called (and in the order they were called). When a case does not found it will return itself, when case matches
 * {@link NoResultCaseMatcherFinished} will be returned. This will not evaluate further predicate on cases, since the
 * matching case was already found.
 *
 * @param <I> type of the input object
 * @author Max Bureck
 */
final class NoResultCaseMatcherUnfinished<I> implements NoResultCaseMatcher<I> {

    /**
     * Value cases are defined for
     */
    private final I toCheck;

    /**
     * Package private constructor. Should only be called from {@link EagerMatcher#match(Object)}.
     *
     * @param toCheck element cases are defined for.
     */
    NoResultCaseMatcherUnfinished(I toCheck) {
        this.toCheck = toCheck;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    // we checked for type safety before cast
    @Override
    public <T> NoResultCaseMatcher<I> caseOf(Class<T> clazz, Consumer<? super T> consumer) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(consumer);
        // check if case matches
        final I toCheck = this.toCheck;
        if (clazz.isInstance(toCheck)) {
            consumer.accept((T) toCheck);
            return NoResultCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultCaseMatcher<I> caseOf(Class<T> clazz, Predicate<? super T> condition, Consumer<? super T> consumer) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(condition);
        Objects.requireNonNull(consumer);
        final I toCheck = this.toCheck;
        if (clazz.isInstance(toCheck)) {
            T casted = (T) toCheck;
            if (condition.test(casted)) {
                consumer.accept(casted);
                return NoResultCaseMatcherFinished.instance();
            } else {
                return this;
            }
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultCaseMatcher<I> caseOf(Predicate<? super I> p, Consumer<? super I> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        // check if case matches
        final I toCheck = this.toCheck;
        if (p.test(toCheck)) {
            consumer.accept(toCheck);
            return NoResultCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultCaseMatcher<I> caseOf(BooleanSupplier s, Consumer<? super I> consumer) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(consumer);
        // check if case matches
        if (s.getAsBoolean()) {
            consumer.accept(toCheck);
            return NoResultCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoResultCaseMatcher<I> caseOf(boolean test, Consumer<? super I> consumer) {
        Objects.requireNonNull(consumer);
        // check if case matches
        if (test) {
            consumer.accept(toCheck);
            return NoResultCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultCaseMatcher<I> caseObj(Function<? super I, Optional<T>> p, Consumer<? super T> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        // check if case matches
        final Optional<T> opt = p.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.get());
            return NoResultCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultCaseMatcher<I> caseInt(Function<? super I, OptionalInt> p, IntConsumer consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalInt opt = p.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsInt());
            return NoResultCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultCaseMatcher<I> caseLong(Function<? super I, OptionalLong> p, LongConsumer consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalLong opt = p.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsLong());
            return NoResultCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> NoResultCaseMatcher<I> caseDouble(Function<? super I, OptionalDouble> p, DoubleConsumer consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        // check if case matches
        final OptionalDouble opt = p.apply(toCheck);
        if (opt.isPresent()) {
            consumer.accept(opt.getAsDouble());
            return NoResultCaseMatcherFinished.instance();
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void otherwise(Consumer<? super I> consumer) {
        Objects.requireNonNull(consumer);
        // no result available consumer must be called
        consumer.accept(toCheck);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <X extends Throwable> void otherwiseThrow(Supplier<X> exSupplier) throws X {
        Objects.requireNonNull(exSupplier);
        // no result available, throw exception
        throw Objects.requireNonNull(exSupplier.get());
    }

}
