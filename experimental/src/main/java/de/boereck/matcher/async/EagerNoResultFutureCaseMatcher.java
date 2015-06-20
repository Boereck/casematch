package de.boereck.matcher.async;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface EagerNoResultFutureCaseMatcher<I> extends NoResultFutureCaseMatcher<I> {

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
