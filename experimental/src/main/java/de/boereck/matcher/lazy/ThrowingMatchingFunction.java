package de.boereck.matcher.lazy;

import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.function.throwing.ThrowingFunction;

import java.util.function.Function;

public interface ThrowingMatchingFunction<I,O,X extends Exception> {

    /**
     * Has the same effect as calling {@link #apply(Object)}. All contracts from apply are valid for this function as well.
     * @param i input to the function
     * @return output of the function
     * @see #apply(Object)
     */
    default O match(I i) throws X {
        return apply(i);
    }

    O apply(I i) throws X;

    @SuppressWarnings("unchecked")// Cast is safe, we checked it
    default MatchingFunction<I,O> recoverWith(Class<X> xClass, Function<X, O> recovery) {
        return i -> {
            try {
                return apply(i);
            } catch(Exception e) {
                if(xClass.isInstance(e)) {
                    return recovery.apply((X)e);
                } else {
                    ThrowingFunction.Uncheck.rethrowUnchecked(e);
                    // will never be reached, since above will always throw
                    return null;
                }
            }
        };
    }

    default MatchingFunction<I,O> cloak() {
        return i -> {
            try {
                return apply(i);
            } catch (Exception x) {
                ThrowingFunction.Uncheck.rethrowUnchecked(x);
                // will never be reached, since above will always throw
                return null;
            }
        };
    }

}
