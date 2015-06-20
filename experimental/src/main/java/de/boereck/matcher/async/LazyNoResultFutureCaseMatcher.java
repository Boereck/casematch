package de.boereck.matcher.async;

import de.boereck.matcher.lazy.MatchingConsumer;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;


public interface LazyNoResultFutureCaseMatcher<I> extends NoResultFutureCaseMatcher<I>, MatchingConsumer<I> {

    /**
     * The given consumer will be called if all cases were checked and none of them matched. This is a closing method, some
     * implementations of the interface may require an closing method to be called after a sequence of case definitions. The
     * consumer will be called with the input object of the case found.
     *
     * @param consumer will be called with the input object if there was no matching case
     * @throws NullPointerException might be thrown if either parameter {@code consumer} is {@code null}.
     */
    MatchingConsumer<I> otherwise(Consumer<? super I> consumer) throws NullPointerException;

}
