package de.boereck.matcher.async;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import de.boereck.matcher.ResultCaseMatcher;

public interface ResultFutureCaseMatcher<I, O> extends ResultCaseMatcher<I, O> {

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> ResultFutureCaseMatcher<I, O> caseOf(Class<T> clazz, Function<? super T, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract ResultFutureCaseMatcher<I, O> caseOf(Predicate<? super I> p, Function<? super I, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract ResultFutureCaseMatcher<I, O> caseOf(BooleanSupplier s, Function<? super I, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract ResultFutureCaseMatcher<I, O> caseOf(boolean test, Function<? super I, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> ResultFutureCaseMatcher<I, O> caseObj(Function<? super I, Optional<T>> p, Function<? super T, ? extends O> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> ResultFutureCaseMatcher<I, O> caseInt(Function<? super I, OptionalInt> p, IntFunction<? extends O> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> ResultFutureCaseMatcher<I, O> caseLong(Function<? super I, OptionalLong> p, LongFunction<? extends O> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> ResultFutureCaseMatcher<I, O> caseDouble(Function<? super I, OptionalDouble> p, DoubleFunction<? extends O> consumer);

    
    // TODO versions of case methods taking Executor to say where to execute case actions

    // TODO versions of otherwise and orElse returning CompletableFuture

    // TODO timeout versions with ScheduledExecutorService?

    <X extends Throwable> ResultFutureCaseMatcher<I, O> caseExceptionally(Class<X> exClass, Function<Throwable, O> f);

    ResultFutureCaseMatcher<I, O> caseExceptionally(Predicate<Throwable> p, Function<Throwable, O> f);

    ResultFutureCaseMatcher<I, O> caseExceptionForward();

    CompletableFuture<Optional<O>> resultAsync();

    /**
     * Result will contain {@link java.util.concurrent.TimeoutException} and runnable will be executed.
     * <p>
     * Timeout case declaration do not depend on any order of case check by implementations of this interface. Meaning that
     * the timeout can be detected, even when previous cases were not evaluated yet.
     * </p>
     * 
     * @param time
     * @param unit
     * @param onTimeOut
     * @return
     */
    ResultFutureCaseMatcher<I, O> caseTimeout(long time, TimeUnit unit, Runnable onTimeOut);

    ResultFutureCaseMatcher<I, O> caseTimeout(long time, TimeUnit unit, Runnable onTimeOut, Executor ex);

    ResultFutureCaseMatcher<I, O> caseTimeoutRecover(long time, TimeUnit unit, Supplier<O> onTimeOutSupply);

    ResultFutureCaseMatcher<I, O> caseTimeoutRecover(long time, TimeUnit unit, Supplier<O> onTimeOutSupply, Executor ex);

    /**
     * Will complete result exceptionally with a custom exception, provided by the given {@code onTimeOutException} supplier.
     * 
     * @param time
     * @param unit
     * @param onTimeOutSupply
     * @return
     */
    <X extends Throwable> CompletableFuture<O> caseTimeoutException(long time, TimeUnit unit, Supplier<X> onTimeOutException);
}
