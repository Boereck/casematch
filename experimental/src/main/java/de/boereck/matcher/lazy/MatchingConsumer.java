package de.boereck.matcher.lazy;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Version of {@link Consumer} that additionally specifies {@link #match(Object) match(Object)} method which has
 * the same effect as calling {@link #accept(Object) accept(Object)}. All contracts from function apply to this function.
 * @see
 */
public interface MatchingConsumer<T> extends Consumer<T> {

    /**
     * Has the same effect as calling {@link #accept(Object)}. All contracts from apply are valid for this consumer as well.
     * @param t input to the function
     * @return output of the function
     * @see #accept(Object)
     */
    default void match(T t) {
        accept(t);
    }

    /**
     * Performs matching with one object supplied by the given supplier.
     * @param inputSupplier provides the object matching is performed on.
     * @throws NullPointerException if {@code inputSupplier} is {@code null}.
     */
    default void match(Supplier<T> inputSupplier) throws NullPointerException {
        Objects.requireNonNull(inputSupplier);
        accept(inputSupplier.get());
    }
}
