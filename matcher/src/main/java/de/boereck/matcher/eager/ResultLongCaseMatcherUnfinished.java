package de.boereck.matcher.eager;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.Supplier;

import de.boereck.matcher.ResultLongCaseMatcher;

/**
 * Eager implementation of {@link ResultLongCaseMatcher}. To instantiate use static method
 * {@link EagerMatcher#resultMatch(long)}. This class holds the long value that is about to be checked for cases. It will
 * evaluate predicates as soon as case methods are called (and in the order they were called). When a case does not match it
 * will return itself, when case matches {@link NoResultLongCaseMatcherFinished} will be returned. This will not evaluate
 * further predicate on cases, since the matching case was already found.
 *
 * @param <I> type of the input object
 * @author Max Bureck
 */
final class ResultLongCaseMatcherUnfinished<O> implements ResultLongCaseMatcher<O> {

    /**
     * Value cases are defined for
     */
    private final long toCheck;

    /**
     * Package private constructor. Should only be called from {@link EagerMatcher#resultMatch(long)}.
     *
     * @param toCheck element cases are defined for.
     */
    ResultLongCaseMatcherUnfinished(long toCheck) {
        this.toCheck = toCheck;
    }

    /**
     * If the {@code condition} is {@code true}, the {@code consumer} function will be called with the value
     * {@link ResultCaseMatcherUnfinished#toCheck toCheck}. The result will be passed to a new instance of
     * {@link ResultCaseMatcherFinished}, which will be returned. Otherwise ({@code condition == false}) the method returns
     * {@code this}.
     *
     * @param condition determines if {@code consumer} is called with {@link ResultCaseMatcherUnfinished#toCheck toCheck}
     * @param consumer  will be called if {@code condition} is true.
     * @return either a new instance of {@link ResultCaseMatcherFinished} holding the result or {@code this}.
     */
    private ResultLongCaseMatcher<O> completeOrSelf(boolean condition, LongFunction<? extends O> consumer) {
        Objects.requireNonNull(consumer);
        if (condition) {
            final O result = consumer.apply(toCheck);
            return new ResultLongCaseMatcherFinished<O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultLongCaseMatcher<O> caseOf(long i, LongFunction<? extends O> consumer) {
        Objects.requireNonNull(consumer);
        return completeOrSelf(i == toCheck, consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultLongCaseMatcher<O> caseOf(LongPredicate p, LongFunction<? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        return completeOrSelf(p.test(toCheck), consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultLongCaseMatcher<O> caseOf(BooleanSupplier s, LongFunction<? extends O> consumer) {
        Objects.requireNonNull(consumer);
        return completeOrSelf(s.getAsBoolean(), consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultLongCaseMatcher<O> caseOf(boolean test, LongFunction<? extends O> consumer) {
        Objects.requireNonNull(consumer);
        return completeOrSelf(test, consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultLongCaseMatcher<O> caseObj(LongFunction<Optional<T>> p, Function<? super T, ? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        final Optional<T> opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.get());
            return new ResultLongCaseMatcherFinished<O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultLongCaseMatcher<O> caseInt(LongFunction<OptionalInt> p, IntFunction<? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        final OptionalInt opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.getAsInt());
            return new ResultLongCaseMatcherFinished<O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultLongCaseMatcher<O> caseLong(LongFunction<OptionalLong> p, LongFunction<? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        final OptionalLong opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.getAsLong());
            return new ResultLongCaseMatcherFinished<O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultLongCaseMatcher<O> caseDouble(LongFunction<OptionalDouble> p, DoubleFunction<? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        final OptionalDouble opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.getAsDouble());
            return new ResultLongCaseMatcherFinished<O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<O> result() {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ifResult(Consumer<? super O> consumer) {
        // No result present. Nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public O otherwise(O o) {
        // we don't have a result, so we return the alternative value
        return o;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public O otherwise(LongFunction<? extends O> supplier) {
        Objects.requireNonNull(supplier);
        // we don't have a result, so we return a provided alternative
        return supplier.apply(toCheck);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <X extends Throwable> O otherwiseThrow(Supplier<X> exSupplier) throws X {
        Objects.requireNonNull(exSupplier);
        throw exSupplier.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public O orElse(O o) throws NullPointerException {
        // we don't have a result, so we return the alternative value
        return Objects.requireNonNull(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public O orElse(Supplier<? extends O> supplier) throws NullPointerException {
        // we don't have a result, so we return a provided alternative
        Objects.requireNonNull(supplier);
        return Objects.requireNonNull(supplier.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <X extends Throwable> O orElseThrow(Supplier<X> exSupplier) throws X {
        Objects.requireNonNull(exSupplier);
        throw exSupplier.get();
    }

}
