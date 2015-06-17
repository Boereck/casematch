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
import java.util.function.IntPredicate;
import java.util.function.LongFunction;
import java.util.function.Supplier;

import de.boereck.matcher.ResultIntCaseMatcher;

/**
 * Eager implementation of {@link ResultIntCaseMatcher}. To instantiate use static method
 * {@link EagerMatcher#resultMatch(int)}. This class holds the int value that is about to be checked for cases. It will
 * evaluate predicates as soon as case methods are called (and in the order they were called). When a case does not found it
 * will return itself, when case matches {@link NoResultIntCaseMatcherFinished} will be returned. This will not evaluate
 * further predicate on cases, since the matching case was already found.
 *
 * @param <O> type of the output object
 * @author Max Bureck
 */
final class ResultIntCaseMatcherUnfinished<O> implements EagerResultIntCaseMatcher<O> {

    /**
     * Value cases are defined for
     */
    private final int toCheck;

    /**
     * Package private constructor. Should only be called from {@link EagerMatcher#resultMatch(int)}.
     *
     * @param toCheck element cases are defined for.
     */
    ResultIntCaseMatcherUnfinished(int toCheck) {
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
    private EagerResultIntCaseMatcher<O> completeOrSelf(boolean condition, IntFunction<? extends O> consumer) {
        Objects.requireNonNull(consumer);
        if (condition) {
            final O result = consumer.apply(toCheck);
            return new ResultIntCaseMatcherFinished<O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerResultIntCaseMatcher<O> caseOf(int i, IntFunction<? extends O> consumer) {
        Objects.requireNonNull(consumer);
        return completeOrSelf(i == toCheck, consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerResultIntCaseMatcher<O> caseOf(IntPredicate p, IntFunction<? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        return completeOrSelf(p.test(toCheck), consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerResultIntCaseMatcher<O> caseOf(BooleanSupplier s, IntFunction<? extends O> consumer) {
        Objects.requireNonNull(consumer);
        return completeOrSelf(s.getAsBoolean(), consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerResultIntCaseMatcher<O> caseOf(boolean test, IntFunction<? extends O> consumer) {
        Objects.requireNonNull(consumer);
        return completeOrSelf(test, consumer);
    }

    @Override
    public EagerResultIntCaseMatcher<O> caseIs(IntPredicate p, Supplier<? extends O> supplier) throws NullPointerException {
        Objects.requireNonNull(p);
        return caseIs(p.test(toCheck), supplier);
    }

    @Override
    public EagerResultIntCaseMatcher<O> caseIs(boolean test, Supplier<? extends O> supplier) throws NullPointerException {
        Objects.requireNonNull(supplier);
        if (test) {
            final O result = supplier.get();
            return new ResultIntCaseMatcherFinished<>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> EagerResultIntCaseMatcher<O> caseObj(IntFunction<Optional<T>> p, Function<? super T, ? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        final Optional<T> opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.get());
            return new ResultIntCaseMatcherFinished<O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerResultIntCaseMatcher<O> caseInt(IntFunction<OptionalInt> p, IntFunction<? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        final OptionalInt opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.getAsInt());
            return new ResultIntCaseMatcherFinished<O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerResultIntCaseMatcher<O> caseLong(IntFunction<OptionalLong> p, LongFunction<? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        final OptionalLong opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.getAsLong());
            return new ResultIntCaseMatcherFinished<O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EagerResultIntCaseMatcher<O> caseDouble(IntFunction<OptionalDouble> p, DoubleFunction<? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        final OptionalDouble opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.getAsDouble());
            return new ResultIntCaseMatcherFinished<O>(result);
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
    public void then(Consumer<? super O> onResult, Runnable onAbsent) throws NullPointerException {
        Objects.requireNonNull(onAbsent);
        onAbsent.run();
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
    public O otherwise(IntFunction<? extends O> supplier) {
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
