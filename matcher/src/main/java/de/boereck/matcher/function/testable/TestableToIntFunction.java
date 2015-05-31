package de.boereck.matcher.function.testable;

import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.*;

import de.boereck.matcher.function.optionalmap.OptionalDoubleMapper;
import de.boereck.matcher.function.optionalmap.OptionalIntMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

@FunctionalInterface
public interface TestableToIntFunction<I> extends ToIntFunction<I> {

    default AdvPredicate<I> thenTest(IntPredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> test.test(applyAsInt(i));
    }

    default OptionalIntMapper<I> filter(IntPredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final int result = this.applyAsInt(i);
            return test.test(result) ? OptionalInt.of(result) : OptionalInt.empty();
        };
    }

    default Consumer<I> thenDo(IntConsumer consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.applyAsInt(i));
    }

    default OptionalIntMapper<I> requires(Predicate<? super I> precondition) throws NullPointerException {
        Objects.requireNonNull(precondition);
        return i -> precondition.test(i) ? OptionalInt.of(this.applyAsInt(i)) : OptionalInt.empty();
    }

    /**
     * The returned function will execute this TestableToIntFunction and if it executes without any problem, the result
     * will be returned as an OptionalInt. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalInt in this case. It is recommended to handle the exception, e.g. by using  method
     * {@link TestableToIntFunction#withCatch(Class, Consumer) &lt;E extends Throwable&gt; withCatch(Class&lt;E&gt;, Consumer&lt;E&gt;)}
     * or
     * {@link TestableToIntFunction#withCatch(Consumer) withCatch(Consumer&lt;Exception&gt;)} instead.
     *
     * @return function that will execute this TestableToIntFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalInt in this case.
     */
    default OptionalIntMapper<I> withCatch() {
        return i -> {
            try {
                return OptionalInt.of(this.applyAsInt(i));
            } catch (Exception e) {
                return OptionalInt.empty();
            }
        };
    }

    /**
     * The returned function will execute this TestableToIntFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an throwable of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the Exception; the function will return an empty OptionalInt in this case.
     * Throwables of other types will be re-thrown.
     * In both cases an empty OptionalInt is returned.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz   type of throwable to be caught
     * @param handler consumer handling the throwable
     * @param <E>     type of throwable to be caught
     * @return function that will execute this TestableToIntFunction and potentially handling exceptions of type {@code clazz}
     * with {@code handler}. After handling exceptions, the function will return an empty OptionalInt.
     * @throws NullPointerException if {@code clazz} or {@code handler} is {@code null}.
     */
    default <E extends Throwable> OptionalIntMapper<I> withCatch(Class<E> clazz, Consumer<E> handler) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return OptionalInt.of(this.applyAsInt(i));
            } catch (Throwable t) {
                if (clazz.isInstance(t)) {
                    handler.accept((E) t);
                    return OptionalInt.empty();
                } else {
                    throw t;
                }
            }
        };
    }

    /**
     * The returned function will execute this TestableToIntFunction and if it executes without any problem, the result
     * will be returned. If the execution throws an exception, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalInt in this case.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param handler will handle exceptions being thrown during the execution of this TestableToIntFunction
     * @return function execution this TestableToIntFunction and if execution performs without any problem, the result
     * will be returned. If the execution throws an exception of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalInt in this case.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    default OptionalIntMapper<I> withCatch(Consumer<Exception> handler) throws NullPointerException {
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return OptionalInt.of(this.applyAsInt(i));
            } catch (Exception e) {
                handler.accept(e);
                return OptionalInt.empty();
            }
        };
    }

    /**
     * Returns a function executing this TestableToIntFunction and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalInt should be
     * returned, consider using method {@link TestableToIntFunction#withCatch() withCatch()} instead.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param recovery method providing a regular result value if the TestableToIntFunction throws an exception.
     * @return function executing this TestableToIntFunction and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalInt should be
     * returned, consider using method {@link TestableToIntFunction#withCatch() withCatch()} instead.
     * @throws NullPointerException if {@code recovery} is {@code null}.
     */
    default OptionalIntMapper<I> recoverWith(Function<? super Throwable, OptionalInt> recovery) throws NullPointerException {
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return OptionalInt.of(this.applyAsInt(i));
            } catch (Throwable t) {
                return recovery.apply(t);
            }
        };
    }

    /**
     * Returns a function executing this TestableToIntFunction and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz class of exceptions to be caught and recovered from.
     * @param recovery function recovering from an exception providing a regular value to return from the returned function.
     * @param <E> Type of exceptions to be caught and recovered from.
     * @return function executing this TestableToIntFunction and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * @throws NullPointerException if {@code clazz} or {@code recovery} is {@code null}.
     */
    default <E extends Throwable> OptionalIntMapper<I> recoverWith(Class<E> clazz, Function<? super E, OptionalInt> recovery) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(recovery);
        return i -> {
            try {
                return OptionalInt.of(this.applyAsInt(i));
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
