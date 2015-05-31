package de.boereck.matcher.function.optionalmap;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;

@FunctionalInterface
public interface OptionalIntMapper<I> extends Function<I, OptionalInt> {

    default <V> OptionalMapper<I, V> map(IntFunction<? extends V> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalInt thisResult = apply(i);
            if (thisResult.isPresent()) {
                final V afterResult = after.apply(thisResult.getAsInt());
                return Optional.ofNullable(afterResult);
            } else {
                return Optional.empty();
            }
        };
    }

    default OptionalIntMapper<I> mapI(IntUnaryOperator after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalInt thisResult = apply(i);
            if (thisResult.isPresent()) {
                final int afterResult = after.applyAsInt(thisResult.getAsInt());
                return OptionalInt.of(afterResult);
            } else {
                return thisResult;
            }
        };
    }

    default OptionalLongMapper<I> mapL(IntToLongFunction after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalInt thisResult = apply(i);
            if (thisResult.isPresent()) {
                final long afterResult = after.applyAsLong(thisResult.getAsInt());
                return OptionalLong.of(afterResult);
            } else {
                return OptionalLong.empty();
            }
        };
    }


    default OptionalDoubleMapper<I> mapD(IntToDoubleFunction after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalInt thisResult = apply(i);
            if (thisResult.isPresent()) {
                final double afterResult = after.applyAsDouble(thisResult.getAsInt());
                return OptionalDouble.of(afterResult);
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    default <V> OptionalMapper<I, V> flatMap(IntFunction<Optional<V>> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalInt thisResult = apply(i);
            if (thisResult.isPresent()) {
                return after.apply(thisResult.getAsInt());
            } else {
                return Optional.empty();
            }
        };
    }

    default OptionalIntMapper<I> flatMapI(IntFunction<OptionalInt> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalInt thisResult = apply(i);
            if (thisResult.isPresent()) {
                return after.apply(thisResult.getAsInt());
            } else {
                return OptionalInt.empty();
            }
        };
    }

    default OptionalLongMapper<I> flatMapL(IntFunction<OptionalLong> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalInt thisResult = apply(i);
            if (thisResult.isPresent()) {
                return after.apply(thisResult.getAsInt());
            } else {
                return OptionalLong.empty();
            }
        };
    }

    default OptionalDoubleMapper<I> flatMapD(IntFunction<OptionalDouble> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalInt thisResult = apply(i);
            if (thisResult.isPresent()) {
                return after.apply(thisResult.getAsInt());
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    default OptionalIntMapper<I> filter(IntPredicate after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalInt thisResult = apply(i);
            if (thisResult.isPresent() && after.test(thisResult.getAsInt())) {
                return thisResult;
            } else {
                return OptionalInt.empty();
            }
        };
    }

    default Predicate<I> hasResult() {
        return i -> this.apply(i).isPresent();
    }

    default Predicate<I> hasResultAnd(IntPredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final OptionalInt result = apply(i);
            return result.isPresent() && test.test(result.getAsInt());
        };
    }

    /**
     * The returned function will execute this OptionalIntMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalInt in this case. It is recommended to handle the exception, e.g. by using  method
     * {@link OptionalIntMapper#withCatch(Class, Consumer) &lt;E extends Throwable&gt; withCatch(Class&lt;E&gt;, Consumer&lt;E&gt;)}
     * or
     * {@link OptionalIntMapper#withCatch(Consumer) withCatch(Consumer&lt;Exception&gt;)} instead.
     *
     * @return function that will execute this OptionalIntMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalInt in this case.
     */
    default OptionalIntMapper<I> withCatch() {
        return i -> {
            try {
                return this.apply(i);
            } catch (Exception e) {
                return OptionalInt.empty();
            }
        };
    }

    /**
     * The returned function will execute this OptionalIntMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an throwable of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the Exception; the function will return an empty OptionalInt in this case.
     * Throwables of other types will be re-thrown.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz   type of throwable to be caught
     * @param handler consumer handling the throwable
     * @param <E>     type of throwable to be caught
     * @return function that will execute this OptionalIntMapper and potentially handling exceptions of type {@code clazz}
     * with {@code handler}. After handling exceptions, the function will return an empty OptionalInt.
     * @throws NullPointerException if {@code clazz} or {@code handler} are {@code null}.
     */
    default <E extends Throwable> OptionalIntMapper<I> withCatch(Class<E> clazz, Consumer<E> handler) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return this.apply(i);
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
     * The returned function will execute this OptionalIntMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an exception, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalInt in this case.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param handler will handle exceptions being thrown during the execution of this OptionalIntMapper
     * @return function execution this OptionalIntMapper and if execution performs without any problem, the result
     * will be returned. If the execution throws an exception of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalInt in this case.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    default OptionalIntMapper<I> withCatch(Consumer<Exception> handler) throws NullPointerException {
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return this.apply(i);
            } catch (Exception e) {
                handler.accept(e);
                return OptionalInt.empty();
            }
        };
    }

    /**
     * Returns a function executing this OptionalIntMapper and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalInt should be
     * returned, consider using method {@link OptionalIntMapper#withCatch() withCatch()} instead.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param recovery method providing a regular result value if the OptionalIntMapper throws an exception.
     * @return function executing this OptionalIntMapper and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalInt should be
     * returned, consider using method {@link OptionalIntMapper#withCatch() withCatch()} instead.
     * @throws NullPointerException if {@code recovery} is {@code null}.
     */
    default OptionalIntMapper<I> recoverWith(Function<? super Throwable, OptionalInt> recovery) throws NullPointerException {
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
     * Returns a function executing this OptionalIntMapper and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz class of exceptions to be caught and recovered from.
     * @param recovery function recovering from an exception providing a regular value to return from the returned function.
     * @param <E> Type of exceptions to be caught and recovered from.
     * @return function executing this OptionalIntMapper and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * @throws NullPointerException if {@code clazz} or {@code recovery} are {@code null}.
     */
    default <E extends Throwable> OptionalIntMapper<I> recoverWith(Class<E> clazz, Function<? super E, OptionalInt> recovery) {
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
