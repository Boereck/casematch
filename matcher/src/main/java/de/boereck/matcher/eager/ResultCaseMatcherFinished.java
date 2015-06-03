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

import de.boereck.matcher.NoResultCaseMatcher;
import de.boereck.matcher.ResultCaseMatcher;

/**
 * This class represents a {@link ResultCaseMatcher} that already had a matching case. A new instance holding the result
 * value will be created by {@link ResultCaseMatcherUnfinished} whenever a matching case is found.
 *
 * @param <I> type of the input object
 * @param <O> type of the output/return object
 * @author Max Bureck
 */
final class ResultCaseMatcherFinished<I, O> implements ResultCaseMatcher<I, O> {

    /**
     * Holds object returned by the function of a matching case.
     */
    private final Optional<O> result;

    /**
     * Package private constructor, will be called from {@link ResultCaseMatcherUnfinished} with the result of a function
     * defined for the matching case.
     *
     * @param result result object (may be null)
     */
    ResultCaseMatcherFinished(O result) {
        this.result = Optional.ofNullable(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseOf(Class<T> clazz, Function<? super T, ? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseOf(Class<T> clazz, Predicate<? super T> condition, Function<? super T, ? extends O> f) throws NullPointerException {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultCaseMatcher<I, O> caseOf(Predicate<? super I> p, Function<? super I, ? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultCaseMatcher<I, O> caseOf(BooleanSupplier s, Function<? super I, ? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultCaseMatcher<I, O> caseOf(boolean test, Function<? super I, ? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    @Override
    public ResultCaseMatcher<I, O> caseIs(Predicate<? super I> p, Supplier<? extends O> supplier) throws NullPointerException {
        // we already have the result and don't need to check case
        return this;
    }

    @Override
    public ResultCaseMatcher<I,O> caseIs(boolean test, Supplier<? extends O> supplier) throws NullPointerException {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseObj(Function<? super I, Optional<T>> p, Function<? super T, ? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseInt(Function<? super I, OptionalInt> p, IntFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseLong(Function<? super I, OptionalLong> p, LongFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultCaseMatcher<I, O> caseDouble(Function<? super I, OptionalDouble> p, DoubleFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<O> result() {
        // we do have the result, so we simply return it
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ifResult(Consumer<? super O> consumer) {
        Objects.requireNonNull(consumer);
        result.ifPresent(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void then(Consumer<? super O> onResult, Runnable onAbsent) throws NullPointerException {
        Objects.requireNonNull(onResult);
        Objects.requireNonNull(onAbsent);
        if(result.isPresent()) {
            onResult.accept(result.get());
        } else {
            onAbsent.run();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public O otherwise(O o) {
        // we do have a result. if it is null, return null
        return result.orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public O otherwise(Function<? super I, ? extends O> supplier) {
        // we do have a result. if it is null, return null
        return result.orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <X extends Throwable> O otherwiseThrow(Supplier<X> exSupplier) throws X {
        // we do have a result. if it is null, return null
        return result.orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public O orElse(O o) throws NullPointerException {
        // if result is empty supply alternative, if this is not null
        return result.orElse(Objects.requireNonNull(o));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public O orElse(Supplier<? extends O> supplier) throws NullPointerException {
        Objects.requireNonNull(supplier);
        // supply alternative if result is empty, but only if supplied object is not null
        return result.orElseGet(() -> Objects.requireNonNull(supplier.get()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <X extends Throwable> O orElseThrow(Supplier<X> exSupplier) throws X {
        Objects.requireNonNull(exSupplier);
        return result.orElseThrow(exSupplier);
    }
}
