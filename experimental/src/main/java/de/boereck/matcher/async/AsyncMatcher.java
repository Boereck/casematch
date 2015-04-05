package de.boereck.matcher.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class provides factory methods for CaseMatchers that perform the retrieval of
 * objects to match asynchronously. Therefore the CaseMatcher implementation cannot work
 * eager and evaluate the cases at the time they are defined, but are deferred until the
 * value is received. The cases are then checked in the order they were specified.
 */
public final class AsyncMatcher {

    private AsyncMatcher() {
        throw new IllegalStateException("Class AsyncMatcher must not me instantiated");
    }
    
    public static <I> NoResultFutureCaseMatcher<I> matchAsync(Supplier<I> supplier) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    } 
    
    public static <I> NoResultFutureCaseMatcher<I> matchAsync(Executor ex, Supplier<I> supplier) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
    public static <I> NoResultFutureCaseMatcher<Object> matchAnyAsync(Supplier<?>... supplier) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    } 
    
    public static <I> NoResultFutureCaseMatcher<Object> matchAnyAsync(Executor ex, Supplier<?>... supplier) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    } 
    
    public static <I> NoResultFutureCaseMatcher<I> matchAsync(Consumer<CompletableFuture<I>> action) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
    public static <I> NoResultFutureCaseMatcher<I> matchAsync(Executor ex, Consumer<CompletableFuture<I>> action) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
    public static <I> NoResultFutureCaseMatcher<I> matchAsync(CompletableFuture<I> future) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
    public static <I> NoResultFutureCaseMatcher<I> match(Supplier<CompletableFuture<I>> supplier) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
}
