package de.boereck.matcher.function.testable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

import javax.swing.text.html.Option;

/**
 * Function that provides more combinators than {@link Function}. Especially interesting are function
 * {@link TestableFunction#thenTest(Predicate) thenTest(Predicate)} to create a predicate based on the output of the function
 * and the filter methods ({@link #filter(Class) filter(Class)} and {@link #filter(Predicate) filter(Predicate)})
 * to create a function providing an Optional, depending if the output of this function passes the given filter criterion
 * or not.
 *
 * @param <I> type of input to function
 * @param <O> type of output of function
 */
@FunctionalInterface
public interface TestableFunction<I, O> extends Function<I, O> {

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
    default AdvPredicate<I> thenTest(Predicate<? super O> test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> test.test(apply(i));
    }

    /**
     * Provides a function that first calls this function with its input and then
     * {@link Optional#ofNullable(Object)} with the result and returns the resulting
     * optional.
     * <p>The difference to {@link #nullAware() nullAware()} is that
     * the input to the function will not be checked for {@code null}, which is don in {@code nullAware()}.</p>
     * <p>If during the call of the returned method the call to this method throws an exception, this exception
     * will be thrown to the caller of the returned method.</p>
     *
     * @return function wrapping the result of this function into an optional.
     */
    default OptionalMapper<I, O> optional() {
        return i -> Optional.ofNullable(apply(i));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <V> TestableFunction<I, V> andThen(Function<? super O, ? extends V> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> after.apply(apply(i));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <V> TestableFunction<V, O> compose(Function<? super V, ? extends I> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
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
    default Consumer<I> thenDo(Consumer<? super O> consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.apply(i));
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
    default OptionalMapper<I, O> filter(Predicate<? super O> test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final O result = this.apply(i);
            return test.test(result) ? Optional.ofNullable(result) : Optional.empty();
        };
    }

    /**
     * Returns a function that will call this function with its input and then check if the output is instance of {@code clazz}.
     * If the object is instance of {@code clazz}, an optional containing the output if this function will be
     * returned; if not, an empty optional will be returned.
     * <p>If this function will throw an exception when called by the returned function, this exception will be thrown
     * to the caller of the returned function.</p>
     *
     * @param clazz Class the output of this function is checked for in the returned function
     * @param <R>   Type the output of this function is checked for in the returned function
     * @return function filtering the result of this function, based on the type of the output object.
     * @throws NullPointerException will be thrown if {@code clazz} is {@code null}.
     */
    @SuppressWarnings("unchecked") // cast is safe, we checked if result is instance of type R
    default <R> OptionalMapper<I, R> filter(Class<R> clazz) throws NullPointerException {
        Objects.requireNonNull(clazz);
        return i -> {
            final O result = this.apply(i);
            return (clazz.isInstance(result)) ? Optional.of((R) result) : Optional.empty();
        };
    }

    /**
     * Returns a function that will first check if its input fulfills {@code precondition} and if so returns an
     * optional holding the result of calling this function with the input. If the precondition is not fulfilled,
     * an empty optional will be returned without evaluating this function. Be aware that the returned function will
     * yield an empty optional in two cases:
     * <ul>
     * <li>If the precondition is not fulfilled</li>
     * <li>If this function returns a {@code null} value</li>
     * </ul>
     * <p>If either this function or {@code precondition} will throw an exception when called by the returned function,
     * this exception will be thrown to the caller of the returned function. If {@code predicate} throws an exception
     * this function will not be called.</p>
     *
     * @param precondition will be used in the returned function to check weather or not to call this function.
     * @return function using {@code precondition} to decide weather or not to call this function with its input and
     * return the result of that call.
     */
    default OptionalMapper<I, O> requires(Predicate<? super I> precondition) {
        Objects.requireNonNull(precondition);
        return i -> precondition.test(i) ? Optional.ofNullable(this.apply(i)) : Optional.empty();
    }

    /**
     * This method will return a function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this TestableFunction and if the result is not {@code null}, returns it.
     * If the result is {@code null}, an empty Optional will be returned. So effectively {@code null} checks will be
     * performed on input and output of the function. Exceptions being thrown during the execution of this
     * TestableFunction will also be thrown at the caller of the returned function.
     * <p>The difference to {@link TestableFunction#optional() optional()} is that
     * the input to the function be checked for {@code null}, which is not done in {@code optional()}.</p>
     *
     * @return function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this TestableFunction and if the result is not {@code null}, returns it.
     * If the result is {@code null}, an empty Optional will be returned.
     */
    default OptionalMapper<I, O> nullAware() {
        return i -> {
            // check if input is null
            if (i == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(this.apply(i));
        };
    }

    /**
     * The returned function will execute this TestableFunction and if it executes without any problem, the result
     * will be returned as an Optional. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty Optional in this case. It is recommended to handle the exception, e.g. by using  method
     * {@link TestableFunction#withCatch(Class, Consumer) &lt;E extends Throwable&gt; withCatch(Class&lt;E&gt;, Consumer&lt;E&gt;)}
     * or
     * {@link TestableFunction#withCatch(Consumer) withCatch(Consumer&lt;Exception&gt;)} instead.
     * It cannot be distinguished if the function executed successfully and returned {@code null}, or an exception occurred.
     * In both cases an empty Optional is returned.
     *
     * @return function that will execute this TestableFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty Optional in this case.
     */
    default OptionalMapper<I, O> withCatch() {
        return i -> {
            try {
                return Optional.ofNullable(this.apply(i));
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }

    /**
     * The returned function will execute this TestableFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an throwable of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the Exception; the function will return an empty Optional in this case.
     * Throwables of other types will be re-thrown.
     * From the result it cannot be distinguished if the function executed successfully and returned {@code null}, or an exception occurred.
     * In both cases an empty Optional is returned.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz   type of throwable to be caught
     * @param handler consumer handling the throwable
     * @param <E>     type of throwable to be caught
     * @return function that will execute this TestableFunction and potentially handling exceptions of type {@code clazz}
     * with {@code handler}. After handling exceptions, the function will return an empty Optional.
     * @throws NullPointerException if {@code handler} or {@code handler} is {@code null}.
     */
    @SuppressWarnings("unchecked")// we know cast to E is safe, we checked if t is instance of E
    default <E extends Throwable> OptionalMapper<I, O> withCatch(Class<E> clazz, Consumer<E> handler) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return Optional.ofNullable(this.apply(i));
            } catch (Throwable t) {
                if (clazz.isInstance(t)) {
                    handler.accept((E) t);
                    return Optional.empty();
                } else {
                    throw t;
                }
            }
        };
    }

    /**
     * The returned function will execute this TestableFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an exception, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty Optional in this case.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param handler will handle exceptions being thrown during the execution of this TestableFunction
     * @return function execution this TestableFunction and if execution performs without any problem, the result
     * will be returned. If the execution throws an exception of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty Optional in this case.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    default OptionalMapper<I, O> withCatch(Consumer<Exception> handler) {
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return Optional.ofNullable(this.apply(i));
            } catch (Exception e) {
                handler.accept(e);
                return Optional.empty();
            }
        };
    }

    /**
     * Returns a function executing this TestableFunction and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty Optional should be
     * returned, consider using method {@link #withCatch() withCatch()} instead.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param recovery method providing a regular result value if the TestableFunction throws an exception.
     * @return function executing this TestableFunction and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty Optional should be
     * returned, consider using method {@link #withCatch() withCatch()} instead.
     * @throws NullPointerException if {@code recovery} is {@code null}.
     */
    default TestableFunction<I, O> recoverWith(Function<? super Throwable, O> recovery) {
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return this.apply(i);
            } catch (Throwable t) {
                return recovery.apply(t);
            }
        };
    }

    /**
     * Returns a function executing this TestableFunction and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz    class of exceptions to be caught and recovered from.
     * @param recovery function recovering from an exception providing a regular value to return from the returned function.
     * @param <E>      Type of exceptions to be caught and recovered from.
     * @return function executing this TestableFunction and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * @throws NullPointerException if {@code clazz} or {@code recovery} is {@code null}.
     */
    @SuppressWarnings("unchecked") // we know cast is safe, we checked if t is instance of E
    default <E extends Throwable> TestableFunction<I, O> recoverWith(Class<E> clazz, Function<? super E, O> recovery) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return this.apply(i);
            } catch (Throwable t) {
                if (clazz.isInstance(t)) {
                    return recovery.apply((E) t);
                } else {
                    throw t;
                }
            }
        };
    }
}
