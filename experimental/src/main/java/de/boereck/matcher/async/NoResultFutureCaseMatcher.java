package de.boereck.matcher.async;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Predicate;

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
    public abstract <T> NoResultFutureCaseMatcher<I> caseOf(Class<T> clazz, Consumer<? super T> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract NoResultFutureCaseMatcher<I> caseOf(Predicate<? super I> p, Consumer<? super I> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract NoResultFutureCaseMatcher<I> caseOf(BooleanSupplier s, Consumer<? super I> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract NoResultFutureCaseMatcher<I> caseOf(boolean test, Consumer<? super I> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> NoResultFutureCaseMatcher<I> caseObj(Function<? super I, Optional<T>> p, Consumer<? super T> consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> NoResultFutureCaseMatcher<I> caseInt(Function<? super I, OptionalInt> p, IntConsumer consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> NoResultFutureCaseMatcher<I> caseLong(Function<? super I, OptionalLong> p, LongConsumer consumer);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> NoResultFutureCaseMatcher<I> caseDouble(Function<? super I, OptionalDouble> p, DoubleConsumer consumer);
    
    // TODO versions of case methods taking executor
    
    
    //http://www.nurkiewicz.com/2014/12/asynchronous-timeouts-with.html
    
    // TODO timeout versions with ScheduledExecutorService?
    
    <X extends Throwable> NoResultFutureCaseMatcher<I> caseExceptionally(Class<X> exClass, Consumer<X> consumer);
    
    NoResultFutureCaseMatcher<I> caseExceptionally(Predicate<Throwable> p, Consumer<Throwable> consumer);
    
    void caseTimeout(long time, TimeUnit unit, Runnable onTimeOut);
    
    void caseTimeout(long time, TimeUnit unit, Runnable onTimeOut, Executor ex);
}
