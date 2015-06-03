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
 * This class represents a {@link ResultIntCaseMatcher} that already had a matching case. A new instance holding the result
 * value will be created by {@link ResultIntCaseMatcherUnfinished} whenever a matching case is found.
 *
 * @param <O> type of the output/return object
 * @author Max Bureck
 */
final class ResultIntCaseMatcherFinished<O> implements ResultIntCaseMatcher<O> {

    /**
     * Holds object returned by the function of a matching case.
     */
    private final Optional<O> result;

    /**
     * Package private constructor, will be called from {@link ResultIntCaseMatcherUnfinished} with the result of a function
     * defined for the matching case.
     *
     * @param result result object (may be null)
     */
    public ResultIntCaseMatcherFinished(O result) {
        this.result = Optional.ofNullable(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultIntCaseMatcher<O> caseOf(int i, IntFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultIntCaseMatcher<O> caseOf(IntPredicate p, IntFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultIntCaseMatcher<O> caseOf(BooleanSupplier s, IntFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultIntCaseMatcher<O> caseOf(boolean test, IntFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    @Override
    public ResultIntCaseMatcher<O> caseIs(IntPredicate p, Supplier<? extends O> supplier) throws NullPointerException {
        // we already have the result and don't need to check case
        return this;
    }

    @Override
    public ResultIntCaseMatcher<O> caseIs(boolean test, Supplier<? extends O> supplier) throws NullPointerException {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultIntCaseMatcher<O> caseObj(IntFunction<Optional<T>> p, Function<? super T, ? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultIntCaseMatcher<O> caseInt(IntFunction<OptionalInt> p, IntFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultIntCaseMatcher<O> caseLong(IntFunction<OptionalLong> p, LongFunction<? extends O> consumer) {
        // we already have the result and don't need to check case
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ResultIntCaseMatcher<O> caseDouble(IntFunction<OptionalDouble> p, DoubleFunction<? extends O> consumer) {
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
    public O otherwise(IntFunction<? extends O> supplier) {
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
