package de.boereck.matcher.function.testable;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.*;

import de.boereck.matcher.function.optionalmap.OptionalDoubleMapper;
import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

@FunctionalInterface
public interface TestableToDoubleFunction<I> extends ToDoubleFunction<I> {

    default AdvPredicate<I> thenTest(DoublePredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> test.test(applyAsDouble(i));
    }

    default OptionalDoubleMapper<I> filter(DoublePredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final double result = this.applyAsDouble(i);
            return test.test(result) ? OptionalDouble.of(result) : OptionalDouble.empty();
        };
    }

    default Consumer<I> thenDo(DoubleConsumer consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.applyAsDouble(i));
    }

    default OptionalDoubleMapper<I> requires(Predicate<? super I> precondition) throws NullPointerException {
        Objects.requireNonNull(precondition);
        return i -> precondition.test(i) ? OptionalDouble.of(this.applyAsDouble(i)) : OptionalDouble.empty();
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
    default OptionalDoubleMapper<I> recoverWith(Function<? super Throwable, OptionalDouble> recovery) throws NullPointerException {
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return OptionalDouble.of(this.applyAsDouble(i));
            } catch (Throwable t) {
                return recovery.apply(t);
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
    default <E extends Throwable> OptionalDoubleMapper<I> recoverWith(Class<E> clazz, Function<? super E, OptionalDouble> recovery) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return OptionalDouble.of(this.applyAsDouble(i));
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
