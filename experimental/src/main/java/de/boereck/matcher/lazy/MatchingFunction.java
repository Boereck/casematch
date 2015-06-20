package de.boereck.matcher.lazy;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Version of {@link Function} that additionally specifies {@link #match(Object) match(Object)} method which has
 * the same effect as calling {@link #apply(Object) apply(Object)}. All contracts from function apply to this function.
 * @see
 */
public interface MatchingFunction<I,O> extends Function<I,O> {

    /**
     * {@inheritDoc}
     */
    @Override
    default <V> MatchingFunction<V, O> compose(Function<? super V, ? extends I> before) {
        Objects.requireNonNull(before);
        return v -> apply(before.apply(v));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <V> MatchingFunction<I, V> andThen(Function<? super O, ? extends V> after) {
        Objects.requireNonNull(after);
        return t -> after.apply(apply(t));
    }

    /**
     * Has the same effect as calling {@link #apply(Object)}. All contracts from apply are valid for this function as well.
     * @param i input to the function
     * @return output of the function
     * @see #apply(Object)
     */
    default O match(I i) {
        return apply(i);
    }

    default O match(Supplier<I> inputSupplier) throws NullPointerException {
        Objects.requireNonNull(inputSupplier);
        return match(inputSupplier.get());
    }
}
