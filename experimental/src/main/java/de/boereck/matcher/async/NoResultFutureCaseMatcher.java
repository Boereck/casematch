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
 * Some cases are not evaluated in order of their definition. The {@code caseTimeout} cases are always evaluated when
 * a timeout occurred and no result was set so far (even when other cases are still being evaluated concurrently).
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
    
    // TODO versions of case methods taking executor
    
    
    //http://www.nurkiewicz.com/2014/12/asynchronous-timeouts-with.html
    
    // TODO timeout versions with ScheduledExecutorService?
    
    <X extends Throwable> NoResultFutureCaseMatcher<I> caseExceptionally(Class<X> exClass, Consumer<X> consumer);
    
    NoResultFutureCaseMatcher<I> caseExceptionally(Predicate<Throwable> p, Consumer<Throwable> consumer);

    NoResultFutureCaseMatcher<I> caseTimeout(long time, TimeUnit unit, Runnable onTimeOut);

    NoResultFutureCaseMatcher<I> caseTimeout(long time, TimeUnit unit, Runnable onTimeOut, Executor ex);
}
