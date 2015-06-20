package de.boereck.matcher.function.testable;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.*;

import de.boereck.matcher.function.optionalmap.OptionalDoubleMapper;
import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

/**
 * To double function that provides more combinators than {@link ToDoubleFunction}. Especially interesting are function
 * {@link #thenTest(DoublePredicate) thenTest(DoublePredicate)} to create a predicate based on the output of the function
 * and the filter method {@link #filter(DoublePredicate) filter(DoublePredicate)}
 * to create a function providing an optional, depending if the output of this function passes the given filter criterion
 * or not.
 *
 * @param <I> type of input to function
 */
@FunctionalInterface
public interface TestableToDoubleFunction<I> extends ToDoubleFunction<I> {

    /**
     * Returns a function first executing this function and then calling a test predicate on the result of this function.
     * <p>If this function or {@code test} will throw an exception when called by the returned predicate, this exception will be thrown
     * to the caller of the returned predicate. If this function throws an exception the predicate {@code test} will
     * not be evaluated.</p>
     *
     * @param test predicate that will be used on the result of this function by the returned predicate. Must not be {@code null}
     * @return predicate calling this function on its input and call predicate {@code test} with the output. The result
     * of the call to {@code test} will be returned as the result.
     * @throws NullPointerException will be thrown if {@code test} is {@code null}.
     */
    default AdvPredicate<I> thenTest(DoublePredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> test.test(applyAsDouble(i));
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either this function or {@code after }throws an exception, it is thrown to
     * the caller of the composed function.
     *
     * @param <V> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException thrown if {@code after} is {@code null}
     *
     * @see #compose(Function)
     */
    default <V> TestableFunction<I, V> andThen(DoubleFunction<? extends V> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> after.apply(applyAsDouble(i));
    }

    /**
     * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     * If evaluation of either this function or {@code before} throws an exception, it is thrown to
     * the caller of the composed function.
     *
     * @param <V> the type of input to the {@code before} function, and to the
     *           composed function
     * @param before the function to apply before this function is applied
     * @return a composed function that first applies the {@code before}
     * function and then applies this function
     * @throws NullPointerException thrown if {@code before} is {@code null}
     *
     * @see #andThen(DoubleFunction)
     */
    default <V> TestableToDoubleFunction<V> compose(Function<? super V, ? extends I> before) {
        Objects.requireNonNull(before);
        return (V v) -> applyAsDouble(before.apply(v));
    }

    /**
     * Returns a function that will call this function with its input and then check if the output fulfills predicate {@code test}.
     * If the predicate is fulfilled (returned {@code true}), an optional containing the output if this function will be
     * returned; if the predicate is not fulfilled, an empty optional will be returned.
     * <p>If this function or {@code test} will throw an exception when called by the returned function, this exception will be thrown
     * to the caller of the returned function. If this function throws an exception the predicate {@code test} will
     * not be evaluated.</p>
     *
     * @param test predicate checking if the result of this function should be returned in an optional by the returned
     *             function, or if an empty optional should be returned.
     * @return function filtering the result of this function, based on predicate {@code test}.
     * @throws NullPointerException will be thrown if {@code test} is {@code null}.
     */
    default OptionalDoubleMapper<I> filter(DoublePredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final double result = this.applyAsDouble(i);
            return test.test(result) ? OptionalDouble.of(result) : OptionalDouble.empty();
        };
    }

    /**
     * Returns a consumer that will take an input, call this function with it, and then call the given
     * {@code consumer} with the result.
     * <p>If this function or {@code consumer} will throw an exception when called by the returned consumer, this exception will be thrown
     * to the caller of the returned consumer. If this function throws an exception {@code consumer} will
     * not be evaluated.</p>
     *
     * @param consumer will be called by the returned consumer with the result of applying this function on its input.
     * @return Consumer calling this function with its input and calling {@code consumer} with the result. Must not be
     * {@code null}.
     * @throws NullPointerException will be thrown if {@code consumer} is {@code null}.
     */
    default Consumer<I> thenDo(DoubleConsumer consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.applyAsDouble(i));
    }

    /**
     * Returns a function that will first check if its input fulfills {@code precondition} and if so returns an
     * optional holding the result of calling this function with the input. If the precondition is not fulfilled,
     * an empty optional will be returned without evaluating this function.
     * <p>If either this function or {@code precondition} will throw an exception when called by the returned function,
     * this exception will be thrown to the caller of the returned function. If {@code predicate} throws an exception
     * this function will not be called.</p>
     *
     * @param precondition will be used in the returned function to check weather or not to call this function.
     * @return function using {@code precondition} to decide weather or not to call this function with its input and
     * return the result of that call.
     */
    default OptionalDoubleMapper<I> requires(Predicate<? super I> precondition) throws NullPointerException {
        Objects.requireNonNull(precondition);
        return i -> precondition.test(i) ? OptionalDouble.of(this.applyAsDouble(i)) : OptionalDouble.empty();
    }

    /**
     * This method will return a function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this TestableToDoubleFunction and wrap the result in an OptionalDouble.
     * Exceptions being thrown during the execution of this TestableToDoubleFunction will also be thrown at the caller of the
     * returned function.
     *
     * @return function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this TestableToDoubleFunction and wrap the result in an OptionalDouble.
     */
    default OptionalDoubleMapper<I> nullAware() {
        return i -> {
            // check if input is null
            if (i == null) {
                return OptionalDouble.empty();
            }
            return OptionalDouble.of(this.applyAsDouble(i));
        };
    }

    /**
     * The returned function will execute this TestableToDoubleFunction and if it executes without any problem, the result
     * will be returned as an OptionalDouble. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalDouble in this case. It is recommended to handle the exception, e.g. by using  method
     * {@link TestableToDoubleFunction#withCatch(Class, Consumer) &lt;E extends Throwable&gt; withCatch(Class&lt;E&gt;, Consumer&lt;E&gt;)}
     * or
     * {@link TestableToDoubleFunction#withCatch(Consumer) withCatch(Consumer&lt;Exception&gt;)} instead.
     * In both cases an empty OptionalDouble is returned.
     *
     * @return function that will execute this TestableToDoubleFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalDouble in this case.
     */
    default OptionalDoubleMapper<I> withCatch() {
        return i -> {
            try {
                return OptionalDouble.of(this.applyAsDouble(i));
            } catch (Exception e) {
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * The returned function will execute this TestableToDoubleFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an throwable of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the Exception; the function will return an empty OptionalDouble in this case.
     * Throwables of other types will be re-thrown.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz   type of throwable to be caught
     * @param handler consumer handling the throwable
     * @param <E>     type of throwable to be caught
     * @return function that will execute this TestableToDoubleFunction and potentially handling exceptions of type {@code clazz}
     * with {@code handler}. After handling exceptions, the function will return an empty OptionalDouble.
     * @throws NullPointerException if {@code clazz} or {@code handler} is {@code null}.
     */
    @SuppressWarnings("unchecked") // Cast is safe, we checked if t is instance of E
    default <E extends Throwable> OptionalDoubleMapper<I> withCatch(Class<E> clazz, Consumer<E> handler) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return OptionalDouble.of(this.applyAsDouble(i));
            } catch (Throwable t) {
                if (clazz.isInstance(t)) {
                    handler.accept((E) t);
                    return OptionalDouble.empty();
                } else {
                    throw t;
                }
            }
        };
    }

    /**
     * The returned function will execute this TestableToDoubleFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an exception, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalDouble in this case.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param handler will handle exceptions being thrown during the execution of this TestableToDoubleFunction
     * @return function execution this TestableToDoubleFunction and if execution performs without any problem, the result
     * will be returned. If the execution throws an exception of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalDouble in this case.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    default OptionalDoubleMapper<I> withCatch(Consumer<Exception> handler) throws NullPointerException {
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return OptionalDouble.of(this.applyAsDouble(i));
            } catch (Exception e) {
                handler.accept(e);
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * Returns a function executing this TestableToDoubleFunction and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalDouble should be
     * returned, consider using method {@link TestableToDoubleFunction#withCatch() withCatch()} instead.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param recovery method providing a regular result value if the TestableToDoubleFunction throws an exception.
     * @return function executing this TestableToDoubleFunction and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalDouble should be
     * returned, consider using method {@link TestableToDoubleFunction#withCatch() withCatch()} instead.
     * @throws NullPointerException if {@code recovery} is {@code null}.
     */
    default TestableToDoubleFunction<I> recoverWith(ToDoubleFunction<? super Throwable> recovery) throws NullPointerException {
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return this.applyAsDouble(i);
            } catch (Throwable t) {
                return recovery.applyAsDouble(t);
            }
        };
    }

    /**
     * Returns a function executing this TestableToDoubleFunction and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz class of exceptions to be caught and recovered from.
     * @param recovery function recovering from an exception providing a regular value to return from the returned function.
     * @param <E> Type of exceptions to be caught and recovered from.
     * @return function executing this TestableToDoubleFunction and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * @throws NullPointerException if {@code clazz} or {@code recovery} is {@code null}.
     */
    @SuppressWarnings("unchecked") // Cast is safe, t is checked to be instance of E
    default <E extends Throwable> TestableToDoubleFunction<I> recoverWith(Class<E> clazz, ToDoubleFunction<? super E> recovery) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return this.applyAsDouble(i);
            } catch (Throwable t) {
                if (clazz.isInstance(t)) {
                    return recovery.applyAsDouble((E) t);
                } else {
                    throw t;
                }
            }
        };
    }
}
