package de.boereck.matcher.function.testable;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;

import de.boereck.matcher.function.optionalmap.OptionalIntMapper;
import de.boereck.matcher.function.optionalmap.OptionalLongMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

@FunctionalInterface
public interface TestableToLongFunction<I> extends ToLongFunction<I> {

    default AdvPredicate<I> thenTest(LongPredicate test) {
        Objects.requireNonNull(test);
        return i -> test.test(applyAsLong(i));
    }

    default OptionalLongMapper<I> filter(LongPredicate test) {
        Objects.requireNonNull(test);
        return i -> {
            final long result = this.applyAsLong(i);
            return test.test(result) ? OptionalLong.of(result) : OptionalLong.empty();
        };
    }

    default Consumer<I> thenDo(LongConsumer consumer) {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.applyAsLong(i));
    }

    default OptionalLongMapper<I> requires(Predicate<? super I> precondition) {
        Objects.requireNonNull(precondition);
        return i -> precondition.test(i) ? OptionalLong.of(this.applyAsLong(i)) : OptionalLong.empty();
    }

    /**
     * This method will return a function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this TestableToLongFunction and wrap the result in an OptionalInt.
     * Exceptions being thrown during the execution of this TestableToLongFunction will also be thrown at the caller of the
     * returned function.
     *
     * @return function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this TestableToLongFunction and wrap the result in an OptionalLong.
     */
    default OptionalLongMapper<I> nullAware() {
        return i -> {
            // check if input is null
            if (i == null) {
                return OptionalLong.empty();
            }
            return OptionalLong.of(this.applyAsLong(i));
        };
    }

    /**
     * The returned function will execute this TestableToLongFunction and if it executes without any problem, the result
     * will be returned as an OptionalLong. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalLong in this case. It is recommended to handle the exception, e.g. by using  method
     * {@link TestableToLongFunction#withCatch(Class, Consumer) &lt;E extends Throwable&gt; withCatch(Class&lt;E&gt;, Consumer&lt;E&gt;)}
     * or
     * {@link TestableToLongFunction#withCatch(Consumer) withCatch(Consumer&lt;Exception&gt;)} instead.
     *
     * @return function that will execute this TestableToLongFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalLong in this case.
     */
    default OptionalLongMapper<I> withCatch() {
        return i -> {
            try {
                return OptionalLong.of(this.applyAsLong(i));
            } catch (Exception e) {
                return OptionalLong.empty();
            }
        };
    }

    /**
     * The returned function will execute this TestableToLongFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an throwable of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the Exception; the function will return an empty OptionalLong in this case.
     * Throwables of other types will be re-thrown.
     * In both cases an empty OptionalLong is returned.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz   type of throwable to be caught
     * @param handler consumer handling the throwable
     * @param <E>     type of throwable to be caught
     * @return function that will execute this TestableToLongFunction and potentially handling exceptions of type {@code clazz}
     * with {@code handler}. After handling exceptions, the function will return an empty OptionalLong.
     * @throws NullPointerException if {@code clazz} or {@code handler} is {@code null}.
     */
    default <E extends Throwable> OptionalLongMapper<I> withCatch(Class<E> clazz, Consumer<E> handler) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return OptionalLong.of(this.applyAsLong(i));
            } catch (Throwable t) {
                if (clazz.isInstance(t)) {
                    handler.accept((E) t);
                    return OptionalLong.empty();
                } else {
                    throw t;
                }
            }
        };
    }

    /**
     * The returned function will execute this TestableToLongFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an exception, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalLong in this case.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param handler will handle exceptions being thrown during the execution of this TestableToLongFunction
     * @return function execution this TestableToLongFunction and if execution performs without any problem, the result
     * will be returned. If the execution throws an exception of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalLong in this case.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    default OptionalLongMapper<I> withCatch(Consumer<Exception> handler) throws NullPointerException {
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return OptionalLong.of(this.applyAsLong(i));
            } catch (Exception e) {
                handler.accept(e);
                return OptionalLong.empty();
            }
        };
    }

    /**
     * Returns a function executing this TestableToLongFunction and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalLong should be
     * returned, consider using method {@link TestableToLongFunction#withCatch() withCatch()} instead.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param recovery method providing a regular result value if the TestableToLongFunction throws an exception.
     * @return function executing this TestableToLongFunction and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalLong should be
     * returned, consider using method {@link TestableToLongFunction#withCatch() withCatch()} instead.
     * @throws NullPointerException if {@code recovery} is {@code null}.
     */
    default OptionalLongMapper<I> recoverWith(Function<? super Throwable, OptionalLong> recovery) throws NullPointerException {
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return OptionalLong.of(this.applyAsLong(i));
            } catch (Throwable t) {
                return recovery.apply(t);
            }
        };
    }

    /**
     * Returns a function executing this TestableToLongFunction and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz class of exceptions to be caught and recovered from.
     * @param recovery function recovering from an exception providing a regular value to return from the returned function.
     * @param <E> Type of exceptions to be caught and recovered from.
     * @return function executing this TestableToLongFunction and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * @throws NullPointerException if {@code clazz} or {@code recovery} is {@code null}.
     */
    default <E extends Throwable> OptionalLongMapper<I> recoverWith(Class<E> clazz, Function<? super E, OptionalLong> recovery) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return OptionalLong.of(this.applyAsLong(i));
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
