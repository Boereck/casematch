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

public interface FutureCaseMatcher<I, O> extends ResultCaseMatcher<I, O> {

    /**
     * {@inheritDoc}
     */
    @Override
    <T> FutureCaseMatcher<I, O> caseOf(Class<T> clazz, Function<? super T, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    FutureCaseMatcher<I, O> caseOf(Predicate<? super I> p, Function<? super I, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    FutureCaseMatcher<I, O> caseOf(BooleanSupplier s, Function<? super I, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    FutureCaseMatcher<I, O> caseOf(boolean test, Function<? super I, ? extends O> f);

    /**
     * {@inheritDoc}
     */
    @Override
    <T> FutureCaseMatcher<I, O> caseObj(Function<? super I, Optional<T>> p, Function<? super T, ? extends O> consumer);


    /**
     * {@inheritDoc}
     */
    @Override
    FutureCaseMatcher<I, O> caseInt(Function<? super I, OptionalInt> p, IntFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    FutureCaseMatcher<I, O> caseLong(Function<? super I, OptionalLong> p, LongFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    FutureCaseMatcher<I, O> caseDouble(Function<? super I, OptionalDouble> p, DoubleFunction<? extends O> f) throws NullPointerException;


    // TODO versions of case methods taking Executor to say where to execute case actions

    // TODO timeout versions with ScheduledExecutorService?

    <X extends Throwable> FutureCaseMatcher<I, O> caseExceptionally(Class<X> exClass, Function<Throwable, O> f);

    FutureCaseMatcher<I, O> caseExceptionally(Predicate<Throwable> p, Function<Throwable, O> f);

    FutureCaseMatcher<I, O> caseExceptionForward();

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
    FutureCaseMatcher<I, O> caseTimeout(long time, TimeUnit unit, Runnable onTimeOut);

    FutureCaseMatcher<I, O> caseTimeout(long time, TimeUnit unit, Runnable onTimeOut, Executor ex);

    FutureCaseMatcher<I, O> caseTimeoutRecover(long time, TimeUnit unit, Supplier<O> onTimeOutSupply);

    FutureCaseMatcher<I, O> caseTimeoutRecover(long time, TimeUnit unit, Supplier<O> onTimeOutSupply, Executor ex);

    /**
     * Will complete result exceptionally with a custom exception, provided by the given {@code onTimeOutException} supplier.
     *
     * @param time
     * @param unit
     * @param onTimeOutException
     * @return
     */
    <X extends Throwable> FutureCaseMatcher<I, O> caseTimeoutException(long time, TimeUnit unit, Supplier<X> onTimeOutException);


}
