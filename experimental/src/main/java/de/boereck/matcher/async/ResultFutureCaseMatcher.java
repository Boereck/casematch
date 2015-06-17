package de.boereck.matcher.async;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.*;

import de.boereck.matcher.ResultCaseMatcher;

public interface ResultFutureCaseMatcher<I, O> extends ResultCaseMatcher<I, O> {

    /**
     * {@inheritDoc}
     */
    @Override
    <T> ResultFutureCaseMatcher<I, O> caseOf(Class<T> clazz, Function<? super T, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    ResultFutureCaseMatcher<I, O> caseOf(Predicate<? super I> p, Function<? super I, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    ResultFutureCaseMatcher<I, O> caseOf(BooleanSupplier s, Function<? super I, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    ResultFutureCaseMatcher<I, O> caseOf(boolean test, Function<? super I, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    <T> ResultFutureCaseMatcher<I, O> caseObj(Function<? super I, Optional<T>> p, Function<? super T, ? extends O> consumer);


    /**
     * {@inheritDoc}
     */
    @Override
    ResultFutureCaseMatcher<I, O> caseInt(Function<? super I, OptionalInt> p, IntFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    ResultFutureCaseMatcher<I, O> caseLong(Function<? super I, OptionalLong> p, LongFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    ResultFutureCaseMatcher<I, O> caseDouble(Function<? super I, OptionalDouble> p, DoubleFunction<? extends O> f) throws NullPointerException;


    // TODO versions of case methods taking Executor to say where to execute case actions

    // TODO timeout versions with ScheduledExecutorService?

    <X extends Throwable> ResultFutureCaseMatcher<I, O> caseExceptionally(Class<X> exClass, Function<Throwable, O> f);

    ResultFutureCaseMatcher<I, O> caseExceptionally(Predicate<Throwable> p, Function<Throwable, O> f);

    ResultFutureCaseMatcher<I, O> caseExceptionForward();

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
     * @param onTimeOutException
     * @return
     */
    <X extends Throwable> CompletableFuture<O> caseTimeoutException(long time, TimeUnit unit, Supplier<X> onTimeOutException);

    ////////////////////
    // Closing Method //
    ////////////////////

    CompletableFuture<Optional<O>> result();

    CompletableFuture<O> otherwise(O o);

    CompletableFuture<O> otherwise(Function<? super I, ? extends O> supplier) throws NullPointerException;

    /**
     * If there was a case found and the result of the found is not {@code null} the given {@code consumer} is called with
     * the result value.
     *
     * @param consumer will be called with the result of the case found if the result was not {@code null}.
     * @throws NullPointerException might be thrown if parameter {@code consumer} is {@code null}.
     */
    void ifResult(Consumer<? super O> consumer) throws NullPointerException;

    CompletableFuture<O> orElse(O alternative) throws NullPointerException;

    CompletableFuture<O> orElse(Supplier<O> elseSupply) throws NullPointerException;

    <X extends Throwable> CompletableFuture<O> otherwiseThrow(Supplier<X> exSupplier) throws NullPointerException;

    /**
     * If there was a case found and the result of the found is not {@code null} the given callback {@code onResult} is called with
     * the result value. If no result was found or the result is {@code null}, the callback {@code onAbsent} is called.
     *
     * @param onResult will be called with the result of the case found if the result was not {@code null}.
     * @param onError  will be called if a throwable was thrown during evaluation of case matches.
     * @param onAbsent will be called if no case matched or the match returned {@code null}.
     * @throws NullPointerException might be thrown if parameter {@code onResult} or {@code onAbsent} is {@code null}.
     */
    void then(Consumer<? super O> onResult, Consumer<? super Throwable> onError, Runnable onAbsent) throws NullPointerException;
}
