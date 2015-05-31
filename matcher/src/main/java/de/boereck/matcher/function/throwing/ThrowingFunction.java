package de.boereck.matcher.function.throwing;

import de.boereck.matcher.function.testable.TestableFunction;

/**
 * It is <em>not</em> recommended overriding the default apply method. It is responsible to call
 * the {@link ThrowingFunction#applyThrowing(Object) applyThrowing} method and disguising
 * the possibly thrown exception as a {@link RuntimeException}.
 */
@FunctionalInterface
public interface ThrowingFunction<I,O,E extends Exception> extends TestableFunction<I,O> {

    static <I,O,E extends Exception> TestableFunction<I,O> cloak(ThrowingFunction<I, O, E> f) {
        return f;
    }

    /**
     * Class for internal use in {@link ThrowingFunction}.
     */
    class Uncheck {
        private Uncheck() {
            throw new IllegalStateException();
        }
       private static <T extends Exception> void rethrowUnchecked(Exception t) throws T {
           throw (T)t;
       }
    }

    @Override
    default O apply(I i) {
        try {
            return applyThrowing(i);
        } catch(Exception t) {
            Uncheck.<RuntimeException>rethrowUnchecked(t);
            // the following line is actually dead code.
            // It is just there because we had to re-throw the exception in Uncheck#rethrowUnchecked(Exception)
            return null;
        }
    }

    O applyThrowing(I i) throws E;
}
