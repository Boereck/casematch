package de.boereck.matcher.function.throwing;

import de.boereck.matcher.function.testable.TestableFunction;

/**
 * This function is a special version of a {@link java.util.function.Function}, where the apply method is already implemented
 * and calling the method {@link ThrowingFunction#applyThrowing(Object) applyThrowing} (which may throw a checked  exception
 * of type {@code E}) and if the checked exception is thrown, re-throws the exception as if it is a {@link RuntimeException}.
 *
 * It is <em>not</em> recommended overriding the default apply method. It is responsible to call
 * the {@link ThrowingFunction#applyThrowing(Object) applyThrowing} method and disguising
 * the possibly thrown exception as a {@link RuntimeException}.
 */
@FunctionalInterface
public interface ThrowingFunction<I,O,E extends Exception> extends TestableFunction<I,O> {

    /**
     * Cloaks a {@link ThrowingFunction} as a {@link TestableFunction}. Since ThrowingFunction extends
     * TestableFunction, this method is actually an identity function, such as
     * {@link ThrowingFunction#throwing(ThrowingFunction) throwing}, but it makes clear, that it is used to turn
     * a checked exception into a runtime exception and not be visible in the method declaration.
     *
     * @param f
     * @param <I>
     * @param <O>
     * @param <E>
     * @return
     */
    static <I,O,E extends Exception> TestableFunction<I,O> cloak(ThrowingFunction<I, O, E> f) {
        return f;
    }

    /**
     * Function that can be used to make a {@link ThrowingFunction} from a method reference. This is basically an
     * identity function.
     * @param f function that will be returned as result of this method.
     * @param <I> type of input to the ThrowingFunction
     * @param <O> type of output of the ThrowingFunction
     * @param <E> exception being thrown from the ThrowingFunction
     * @return same object being passed to the method as parameter {@code f}.
     */
    static <I,O,E extends Exception> ThrowingFunction<I,O,E> throwing(ThrowingFunction<I, O, E> f) {
        return f;
    }

    /**
     * Class for internal use in {@link ThrowingFunction}.
     */
    class Uncheck {
        private Uncheck() {
            throw new IllegalStateException();
        }

        /**
         * Rethrows exceptions as if they were RuntimeExceptions, therefore being unchecked.
         * @param e exception rethrown disguised RuntimeException
         * @throws RuntimeException given exception {@code e} rethrown as RuntimeException
         */
        public static void rethrowUnchecked(Exception e) throws RuntimeException {
            Uncheck.<RuntimeException>rethrowCasted(e);
        }

       @SuppressWarnings("unchecked") // Cast works, since we only use it for RuntimeException
       private static <T extends Exception> void rethrowCasted(Exception t) throws T {
           throw (T)t;
       }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default O apply(I i) {
        try {
            return applyThrowing(i);
        } catch(Exception t) {
            Uncheck.rethrowUnchecked(t);
            // the following line is actually dead code.
            // It is just there because we had to re-throw the exception in Uncheck#rethrowUnchecked(Exception)
            return null;
        }
    }

    O applyThrowing(I i) throws E;
}
