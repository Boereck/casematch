package de.boereck.matcher.async;

import de.boereck.matcher.lazy.MatchingFunction;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Created by mbu on 18.06.2015.
 */
public interface LazyFutureCaseMatcher<I,O> extends FutureCaseMatcher<I,O>, MatchingFunction<I, CompletableFuture<Optional<O>>> {

    MatchingFunction<I, CompletableFuture<O>> otherwise(O o);

    MatchingFunction<I, CompletableFuture<O>> otherwise(Function<? super I, ? extends O> supplier) throws NullPointerException;
}
