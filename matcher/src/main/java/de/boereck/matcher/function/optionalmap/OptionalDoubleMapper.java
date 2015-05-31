package de.boereck.matcher.function.optionalmap;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;

@FunctionalInterface
public interface OptionalDoubleMapper<I> extends Function<I, OptionalDouble> {

    default <V> OptionalMapper<I, V> map(DoubleFunction<? extends V> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalDouble thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final V afterResult = after.apply(thisResult.getAsDouble());
                return Optional.ofNullable(afterResult);
            } else {
                return Optional.empty();
            }
        };
    }

    default OptionalIntMapper<I> mapI(DoubleToIntFunction after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalDouble thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final int afterResult = after.applyAsInt(thisResult.getAsDouble());
                return OptionalInt.of(afterResult);
            } else {
                return OptionalInt.empty();
            }
        };
    }

    default OptionalLongMapper<I> mapL(DoubleToLongFunction after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalDouble thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final long afterResult = after.applyAsLong(thisResult.getAsDouble());
                return OptionalLong.of(afterResult);
            } else {
                return OptionalLong.empty();
            }
        };
    }

    default OptionalDoubleMapper<I> mapD(DoubleUnaryOperator after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalDouble thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final double afterResult = after.applyAsDouble(thisResult.getAsDouble());
                return OptionalDouble.of(afterResult);
            } else {
                return thisResult;
            }
        };
    }


    default <V> OptionalMapper<I, V> flatMap(DoubleFunction<Optional<V>> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalDouble thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                return after.apply(thisResult.getAsDouble());
            } else {
                return Optional.empty();
            }
        };
    }

    default OptionalIntMapper<I> flatMapI(DoubleFunction<OptionalInt> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalDouble thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                return after.apply(thisResult.getAsDouble());
            } else {
                return OptionalInt.empty();
            }
        };
    }

    default OptionalLongMapper<I> flatMapL(DoubleFunction<OptionalLong> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalDouble thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                return after.apply(thisResult.getAsDouble());
            } else {
                return OptionalLong.empty();
            }
        };
    }

    default OptionalDoubleMapper<I> flatMapD(DoubleFunction<OptionalDouble> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalDouble thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                return after.apply(thisResult.getAsDouble());
            } else {
                return thisResult;
            }
        };
    }

    default OptionalDoubleMapper<I> filter(DoublePredicate after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalDouble thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent() && after.test(thisResult.getAsDouble())) {
                return thisResult;
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    default Predicate<I> hasResult() {
        return i -> this.apply(i).isPresent();
    }

    default Predicate<I> hasResultAnd(DoublePredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final OptionalDouble result = apply(i);
            return result.isPresent() && test.test(result.getAsDouble());
        };
    }

    /**
     * The returned function will execute this OptionalDoubleMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalDouble in this case. It is recommended to handle the exception, e.g. by using  method
     * {@link OptionalDoubleMapper#withCatch(Class, Consumer) &lt;E extends Throwable&gt; withCatch(Class&lt;E&gt;, Consumer&lt;E&gt;)}
     * or
     * {@link OptionalDoubleMapper#withCatch(Consumer) withCatch(Consumer&lt;Exception&gt;)} instead.
     *
     * @return function that will execute this OptionalDoubleMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalDouble in this case.
     */
    default OptionalDoubleMapper<I> withCatch() {
        return i -> {
            try {
                return this.apply(i);
            } catch (Exception e) {
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * The returned function will execute this OptionalDoubleMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an throwable of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the Exception; the function will return an empty OptionalDouble in this case.
     * Throwables of other types will be re-thrown.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz   type of throwable to be caught
     * @param handler consumer handling the throwable
     * @param <E>     type of throwable to be caught
     * @return function that will execute this OptionalDoubleMapper and potentially handling exceptions of type {@code clazz}
     * with {@code handler}. After handling exceptions, the function will return an empty OptionalDouble.
     * @throws NullPointerException if {@code clazz} or {@code handler} are {@code null}.
     */
    default <E extends Throwable> OptionalDoubleMapper<I> withCatch(Class<E> clazz, Consumer<E> handler) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return this.apply(i);
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
     * The returned function will execute this OptionalDoubleMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an exception, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalDouble in this case.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param handler will handle exceptions being thrown during the execution of this OptionalDoubleMapper
     * @return function execution this OptionalDoubleMapper and if execution performs without any problem, the result
     * will be returned. If the execution throws an exception of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalDouble in this case.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    default OptionalDoubleMapper<I> withCatch(Consumer<Exception> handler) throws NullPointerException {
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return this.apply(i);
            } catch (Exception e) {
                handler.accept(e);
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * Returns a function executing this OptionalDoubleMapper and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalDouble should be
     * returned, consider using method {@link OptionalDoubleMapper#withCatch() withCatch()} instead.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param recovery method providing a regular result value if the OptionalDoubleMapper throws an exception.
     * @return function executing this OptionalDoubleMapper and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalDouble should be
     * returned, consider using method {@link OptionalDoubleMapper#withCatch() withCatch()} instead.
     * @throws NullPointerException if {@code recovery} is {@code null}.
     */
    default OptionalDoubleMapper<I> recoverWith(Function<? super Throwable, OptionalDouble> recovery) throws NullPointerException {
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
     * Returns a function executing this OptionalDoubleMapper and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz class of exceptions to be caught and recovered from.
     * @param recovery function recovering from an exception providing a regular value to return from the returned function.
     * @param <E> Type of exceptions to be caught and recovered from.
     * @return function executing this OptionalDoubleMapper and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * @throws NullPointerException if {@code clazz} or {@code recovery} are {@code null}.
     */
    default <E extends Throwable> OptionalDoubleMapper<I> recoverWith(Class<E> clazz, Function<? super E, OptionalDouble> recovery) {
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
