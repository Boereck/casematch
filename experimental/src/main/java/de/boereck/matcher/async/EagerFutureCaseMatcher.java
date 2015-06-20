package de.boereck.matcher.async;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Eager in this case means that the input is known upfront, not that the evaluation of cases is performed imediately
 * when the cases are defined.
 */
public interface EagerFutureCaseMatcher<I,O> extends FutureCaseMatcher<I,O> {

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
