package de.boereck.matcher.eager;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Supplier;

import de.boereck.matcher.ResultDoubleCaseMatcher;

/**
 * This class represents a {@link ResultDoubleCaseMatcher} that already had a matching case. A new instance holding the result
 * value will be created by {@link ResultDoubleCaseMatcherUnfinished} whenever a matching case is found.
 * 
 * @author Max Bureck
 * @param <O>
 *            type of the output/return object
 */
final class ResultDoubleCaseMatcherFinished<O> implements ResultDoubleCaseMatcher<O> {

    /**
     * Holds object returned by the function of a matching case.
     */
    private final Optional<O> result;

    /**
     * Package private constructor, will be called from {@link ResultDoubleCaseMatcherUnfinished} with the result of a function
     * defined for the matching case.
     * 
     * @param result
     *            result object (may be null)
     */
    public ResultDoubleCaseMatcherFinished(O result) {
        this.result = Optional.ofNullable(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultDoubleCaseMatcher<O> caseOf(DoublePredicate p, DoubleFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultDoubleCaseMatcher<O> caseOf(BooleanSupplier s, DoubleFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultDoubleCaseMatcher<O> caseOf(boolean test, DoubleFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultDoubleCaseMatcher<O> caseObj(DoubleFunction<Optional<T>> p, Function<? super T, ? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultDoubleCaseMatcher<O> caseInt(DoubleFunction<OptionalInt> p, IntFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultDoubleCaseMatcher<O> caseLong(DoubleFunction<OptionalLong> p, LongFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultDoubleCaseMatcher<O> caseDouble(DoubleFunction<OptionalDouble> p, DoubleFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<O> result() {
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ifResult(Consumer<? super O> consumer) {
        result.ifPresent(consumer);
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
    public O otherwise(DoubleFunction<? extends O> supplier) {
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
        return result.orElse(Objects.requireNonNull(o));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public O orElse(Supplier<? extends O> supplier) throws NullPointerException {
        return result.orElseGet(() -> Objects.requireNonNull(supplier.get()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <X extends Throwable> O orElseThrow(Supplier<X> exSupplier) throws X {
        return result.orElseThrow(exSupplier);
    }
}
