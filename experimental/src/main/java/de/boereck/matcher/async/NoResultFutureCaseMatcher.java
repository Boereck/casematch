package de.boereck.matcher.async;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.*;

import de.boereck.matcher.NoResultCaseMatcher;

/**
 * Match function takes CompletableFuture<T>, maybe functions 
 *   
 * @author Max Bureck
 * @param <I>
 */
public interface NoResultFutureCaseMatcher<I> extends NoResultCaseMatcher<I> {

    /**
     * {@inheritDoc}
     */
    @Override
    <T> NoResultFutureCaseMatcher<I> caseOf(Class<T> clazz, Consumer<? super T> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    NoResultFutureCaseMatcher<I> caseOf(Predicate<? super I> p, Consumer<? super I> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    NoResultFutureCaseMatcher<I> caseOf(BooleanSupplier s, Consumer<? super I> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    NoResultFutureCaseMatcher<I> caseOf(boolean test, Consumer<? super I> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    <T> NoResultFutureCaseMatcher<I> caseObj(Function<? super I, Optional<T>> p, Consumer<? super T> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    <T> NoResultFutureCaseMatcher<I> caseInt(Function<? super I, OptionalInt> p, IntConsumer consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    <T> NoResultFutureCaseMatcher<I> caseLong(Function<? super I, OptionalLong> p, LongConsumer consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    <T> NoResultFutureCaseMatcher<I> caseDouble(Function<? super I, OptionalDouble> p, DoubleConsumer consumer);
    
    // TODO versions of case methods taking executor
    
    
    //http://www.nurkiewicz.com/2014/12/asynchronous-timeouts-with.html
    
    // TODO timeout versions with ScheduledExecutorService?
    
    <X extends Throwable> NoResultFutureCaseMatcher<I> caseExceptionally(Class<X> exClass, Consumer<X> consumer);
    
    NoResultFutureCaseMatcher<I> caseExceptionally(Predicate<Throwable> p, Consumer<Throwable> consumer);
    
    void caseTimeout(long time, TimeUnit unit, Runnable onTimeOut);
    
    void caseTimeout(long time, TimeUnit unit, Runnable onTimeOut, Executor ex);

    /**
     * The given consumer will be called if all cases were checked and none of them matched. This is a closing method, some
     * implementations of the interface may require an closing method to be called after a sequence of case definitions. The
     * consumer will be called with the input object of the case found.
     *
     * @param consumer will be called with the input object if there was no matching case
     * @throws NullPointerException might be thrown if either parameter {@code consumer} is {@code null}.
     */
    CompletableFuture<Void> otherwise(Consumer<? super I> consumer) throws NullPointerException;

    /**
     * If all cases were checked and there was no found so far, the given supplier will be called and the given throwable
     * will be thrown. This is a closing method, some implementations of the interface may require an closing method to be
     * called after a sequence of case definitions. The consumer will be called with the input object of the case found.
     *
     * @param exSupplier supplier of the exception to be thrown. For exceptions with parameterless constructors a method reference
     *                   can be used. E.g. {@code MyException::new}.
     * @throws NullPointerException might be thrown if parameter {@code exSupplier} is {@code null} or if it provides {@code null} as a value.
     */
    <X extends Throwable> CompletableFuture<Void> otherwiseThrow(Supplier<X> exSupplier) throws X, NullPointerException;
}
