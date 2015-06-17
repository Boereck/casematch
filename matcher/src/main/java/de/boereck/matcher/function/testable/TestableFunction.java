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
 * and the filter methods ({@link TestableFunction#filter(Class) filter(Class)} and {@link TestableFunction#filter(Predicate) filter(Predicate)})
 * to create a function providing an Optional, depending if the output of this function passes the given filter criterion
 * or not.
 * @param <I>
 * @param <O>
 */
@FunctionalInterface
public interface TestableFunction<I, O> extends Function<I, O> {

    /**
     * Provides a function that first calls this function with its input and then
     * {@link Optional#ofNullable(Object)} with the result and returns the resulting
     * optional.
     * <p>The difference to {@link TestableFunction#nullAware() nullAware()} is that
     * the input to the function will not be checked for {@code null}, which is don in {@code nullAware()}.</p>
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

    default AdvPredicate<I> thenTest(Predicate<? super O> test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> test.test(apply(i));
    }

    default Consumer<I> thenDo(Consumer<? super O> consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.apply(i));
    }

    default OptionalMapper<I, O> filter(Predicate<? super O> test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final O result = this.apply(i);
            return test.test(result) ? Optional.ofNullable(result) : Optional.empty();
        };
    }

    default <R> OptionalMapper<I, R> filter(Class<R> clazz) throws NullPointerException {
        Objects.requireNonNull(clazz);
        return i -> {
            final O result = this.apply(i);
            return (clazz.isInstance(result)) ? Optional.of((R)result) : Optional.empty();
        };
    }

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
     * returned, consider using method {@link TestableFunction#withCatch() withCatch()} instead.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param recovery method providing a regular result value if the TestableFunction throws an exception.
     * @return function executing this TestableFunction and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty Optional should be
     * returned, consider using method {@link TestableFunction#withCatch() withCatch()} instead.
     * @throws NullPointerException if {@code recovery} is {@code null}.
     */
    default OptionalMapper<I, O> recoverWith(Function<? super Throwable, Optional<O>> recovery) {
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return Optional.ofNullable(this.apply(i));
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
     * @param clazz class of exceptions to be caught and recovered from.
     * @param recovery function recovering from an exception providing a regular value to return from the returned function.
     * @param <E> Type of exceptions to be caught and recovered from.
     * @return function executing this TestableFunction and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * @throws NullPointerException if {@code clazz} or {@code recovery} is {@code null}.
     */
    default <E extends Throwable> OptionalMapper<I, O> recoverWith(Class<E> clazz, Function<? super E, Optional<O>> recovery) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return Optional.ofNullable(this.apply(i));
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
