package de.boereck.matcher.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class provides factory methods for CaseMatchers that perform the retrieval of
 * objects to found asynchronously. Therefore the CaseMatcher implementation cannot work
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

    /**
     * This function will create a CompletableFuture and calls the consumer {@code action} asynchronously
     * using the common ForkJoin pool. The consumer is expected to complete the given CompletableFuture either
     * with a result value or exceptionally. The matching methods on the returned NoResultFutureCaseMatcher
     * will evaluate on the provided result.
     * @param action
     * @param <I>
     * @return
     */
    public static <I> NoResultFutureCaseMatcher<I> matchAsync(Consumer<CompletableFuture<I>> action) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    /**
     * This function will create a CompletableFuture and calls the consumer {@code action} asynchronously
     * using the given Executor {@code ex}. The consumer is expected to complete the given CompletableFuture either
     * with a result value or exceptionally. The matching methods on the returned NoResultFutureCaseMatcher
     * will evaluate on the provided result.
     * @param ex
     * @param action
     * @param <I>
     * @return
     */
    public static <I> NoResultFutureCaseMatcher<I> matchAsync(Executor ex, Consumer<CompletableFuture<I>> action) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    public static <I> NoResultFutureCaseMatcher<I> matchAnyAsync(Executor ex, Consumer<CompletableFuture<I>>... action) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    public static <I> NoResultFutureCaseMatcher<I> matchAnyCancelRestAsync(Executor ex, Consumer<CompletableFuture<I>>... action) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    public static <I> NoResultFutureCaseMatcher<I> matchAsync(CompletableFuture<I> future) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
    public static <I> NoResultFutureCaseMatcher<I> match(Supplier<CompletableFuture<I>> supplier) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
}
