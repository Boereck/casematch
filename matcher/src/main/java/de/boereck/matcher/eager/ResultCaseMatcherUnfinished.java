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
import java.util.function.Predicate;
import java.util.function.Supplier;

import de.boereck.matcher.ResultCaseMatcher;

/**
 * Eager implementation of {@link ResultCaseMatcher}. To instantiate use static method {@link EagerMatcher#resultMatch(Object)}.
 * This class holds the object that is about to be checked for cases. It will evaluate predicates as soon as case methods are
 * called (and in the order they were called). When a case does not found it will return itself, when case matches
 * {@link NoResultCaseMatcherFinished} will be returned. This will not evaluate further predicate on cases, since the
 * matching case was already found.
 *
 * @param <I> type of the input object
 * @author Max Bureck
 */
final class ResultCaseMatcherUnfinished<I, O> implements ResultCaseMatcher<I, O> {

    /**
     * Value cases are defined for
     */
    private final I toCheck;

    /**
     * Package private constructor. Should only be called from {@link EagerMatcher#resultMatch(Object)}.
     *
     * @param toCheck element cases are defined for.
     */
    ResultCaseMatcherUnfinished(I toCheck) {
        this.toCheck = toCheck;
    }

    /**
     * If the {@code condition} is {@code true}, the {@code consumer} function will be called
     * with the object {@link de.boereck.matcher.eager.ResultCaseMatcherUnfinished#toCheck toCheck}. The result will be
     * passed to a new instance of {@link ResultCaseMatcherFinished}, which will be returned.
     * Otherwise ({@code condition == false}) the method returns {@code this}.
     *
     * @param condition determines if {@code consumer} is called with {@link de.boereck.matcher.eager.ResultCaseMatcherUnfinished#toCheck toCheck}
     * @param consumer  will be called if {@code condition} is true.
     * @return either a new instance of {@link ResultCaseMatcherFinished} holding the result or {@code this}.
     */
    private <T> ResultCaseMatcher<I, O> finishedOrSelf(boolean condition, Function<? super I, ? extends O> consumer) {
        Objects.requireNonNull(consumer);
        if (condition) {
            final O result = consumer.apply(toCheck);
            return new ResultCaseMatcherFinished<I, O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseOf(Class<T> clazz, Function<? super T, ? extends O> consumer) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(consumer);
        // check if input object instance of clazz
        final I toCheck = this.toCheck;
        if (clazz.isInstance(toCheck)) {
            @SuppressWarnings("unchecked")
            // cast is safe, we checked if toCheck is instance of compatible type
            final O result = consumer.apply((T) toCheck);
            return new ResultCaseMatcherFinished<I, O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseOf(Class<T> clazz, Predicate<? super T> condition, Function<? super T, ? extends O> f) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(condition);
        Objects.requireNonNull(f);
        // check if input object instance of clazz
        final I toCheck = this.toCheck;
        if (clazz.isInstance(toCheck)) {
            final T casted = (T) toCheck;
            if (condition.test(casted)) {
                @SuppressWarnings("unchecked")
                // cast is safe, we checked if toCheck is instance of compatible type
                final O result = f.apply(casted);
                return new ResultCaseMatcherFinished<I, O>(result);
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
    public ResultCaseMatcher<I, O> caseOf(Predicate<? super I> p, Function<? super I, ? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        return finishedOrSelf(p.test(toCheck), consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultCaseMatcher<I, O> caseOf(BooleanSupplier s, Function<? super I, ? extends O> consumer) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(consumer);
        return finishedOrSelf(s.getAsBoolean(), consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultCaseMatcher<I, O> caseOf(boolean test, Function<? super I, ? extends O> consumer) {
        Objects.requireNonNull(consumer);
        return finishedOrSelf(test, consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseObj(Function<? super I, Optional<T>> p, Function<? super T, ? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        final Optional<T> opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.get());
            return new ResultCaseMatcherFinished<I, O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseInt(Function<? super I, OptionalInt> p, IntFunction<? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        final OptionalInt opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.getAsInt());
            return new ResultCaseMatcherFinished<I, O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseLong(Function<? super I, OptionalLong> p, LongFunction<? extends O> consumer) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(consumer);
        final OptionalLong opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.getAsLong());
            return new ResultCaseMatcherFinished<I, O>(result);
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseDouble(Function<? super I, OptionalDouble> p, DoubleFunction<? extends O> consumer) {
        Objects.requireNonNull(p);
        final OptionalDouble opt = p.apply(toCheck);
        if (opt.isPresent()) {
            final O result = consumer.apply(opt.getAsDouble());
            return new ResultCaseMatcherFinished<I, O>(result);
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
    public O otherwise(Function<? super I, ? extends O> supplier) {
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
        // we don't have a result, so we have to throw the exception
        throw Objects.requireNonNull(exSupplier.get());
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
        Objects.requireNonNull(supplier);
        // we don't have a result, so we return a provided alternative
        return Objects.requireNonNull(supplier.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <X extends Throwable> O orElseThrow(Supplier<X> exSupplier) throws X {
        Objects.requireNonNull(exSupplier);
        // we don't have a result, so we have to throw the exception
        throw Objects.requireNonNull(exSupplier.get());
    }

}
