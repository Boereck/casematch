package de.boereck.matcher.async;

import de.boereck.matcher.lazy.MatchingConsumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Created by mbu on 18.06.2015.
 */
public interface AsyncMatchingConsumer<T> extends MatchingConsumer<T> {

    void match(Executor ex, Supplier<T> inputSupplier);

    void match(CompletableFuture<T> future);
}
