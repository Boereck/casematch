package de.boereck.matcher.function.optionalmap;

import de.boereck.matcher.NoResultCaseMatcher;
import de.boereck.matcher.function.testable.TestableToIntFunction;
import de.boereck.matcher.function.testable.TestableToLongFunction;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;

/**
 * {@link Function} mapping an input of type I to an {@link OptionalInt}. The result of this function should never
 * be {@code null}, even though most combinator methods of this interface are prepared for this.
 * This interface is especially useful when working with {@link NoResultCaseMatcher#caseInt(Function, IntConsumer)}, or
 * {@link de.boereck.matcher.ResultCaseMatcher#caseInt(Function, IntFunction)}.
 *
 * @param <I> Type of input to the function
 * @author Max Bureck
 */
@FunctionalInterface
public interface OptionalIntMapper<I> extends Function<I, OptionalInt> {

    /**
     * Returns a function that will first call this OptionalIntMapper and afterwards calls
     * checks if the OptionalInt contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalIntMapper is {@code null}, an empty {@code OptionalInt} will be returned.
     *
     * @param after mapping function that will be called on the value of the returned {@code OptionalInt}
     *              of this OptionalIntMapper. Must not be {@code null}.
     * @return function mapping the result value of this OptionalIntMapper (provided the OptionalInt holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will first call this OptionalIntMapper and afterwards calls
     * checks if the OptionalInt contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalIntMapper is {@code null}, an empty {@code OptionalInt} will be returned.
     *
     * @param after mapping function that will be called on the value of the returned {@code OptionalInt}
     *              of this OptionalIntMapper. Must not be {@code null}.
     * @return function mapping the result value of this OptionalIntMapper (provided the OptionalInt holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will first call this OptionalIntMapper and afterwards calls
     * checks if the OptionalInt contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalIntMapper is {@code null}, an empty {@code OptionalInt} will be returned.
     *
     * @param after mapping function that will be called on the value of the returned {@code OptionalInt}
     *              of this OptionalIntMapper. Must not be {@code null}.
     * @return function mapping the result value of this OptionalIntMapper (provided the OptionalInt holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
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


    /**
     * Returns a function that will first call this OptionalIntMapper and afterwards calls
     * checks if the OptionalInt contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalIntMapper is {@code null}, an empty {@code OptionalInt} will be returned.
     *
     * @param after mapping function that will be called on the value of the returned {@code OptionalInt}
     *              of this OptionalIntMapper. Must not be {@code null}.
     * @return function mapping the result value of this OptionalIntMapper (provided the OptionalInt holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will call this OptionalIntMapper and afterwards, if the returned OptionalInt contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalIntMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalInt} returned by
     *              this OptionalIntMapper. Must not be {@code null}.
     * @param <V>   result type of optional returned by {@after} mapper function.
     * @return function that will call this OptionalIntMapper and afterwards, if the returned OptionalInt contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalIntMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will call this OptionalIntMapper and afterwards, if the returned OptionalInt contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalIntMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalInt} returned by
     *              this OptionalIntMapper. Must not be {@code null}.
     * @param <V>   result type of optional returned by {@after} mapper function.
     * @return function that will call this OptionalIntMapper and afterwards, if the returned OptionalInt contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalIntMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will call this OptionalIntMapper and afterwards, if the returned OptionalInt contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalIntMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalInt} returned by
     *              this OptionalIntMapper. Must not be {@code null}.
     * @param <V>   result type of optional returned by {@after} mapper function.
     * @return function that will call this OptionalIntMapper and afterwards, if the returned OptionalInt contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalIntMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will call this OptionalIntMapper and afterwards, if the returned OptionalInt contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalIntMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalInt} returned by
     *              this OptionalIntMapper. Must not be {@code null}.
     * @param <V>   result type of optional returned by {@after} mapper function.
     * @return function that will call this OptionalIntMapper and afterwards, if the returned OptionalInt contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalIntMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
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

    /**
     * Returns an optional mapper filtering the {@code OptionalInt} returned by this OptionalIntMapper
     * with the given predicate {@code after}. This means that function returns an empty OptionalInt
     * if either this OptionalIntMapper returns {@code null}, or an empty {@code OptionalInt}, or if the
     * OptionalInt returned from this OptionalIntMapper holds a value, but the predicate {@code after} returns
     * {@code false} for the value held in the OptionalInt. If the predicate returns {@code true}, the function
     * will return the OptionalInt returned from this OptionalIntMapper.
     *
     * @param after function being used to filter on the OptionalInt returned
     *              from this OptionalIntMapper. This parameter must not be {@code null}.
     * @return optional mapper filtering the {@code OptionalInt} returned by this OptionalIntMapper
     * with the given predicate {@code after}.
     * @throws NullPointerException will be thrown if {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will call this OptionalIntMapper and checks if the returned OptionalInt contains
     * a value. If this OptionalIntMapper returns {@code null}, the predicate will return {@code false}.
     * @return function checking if the result of this OptionalIntMapper is not {@code null} and holds a value
     */
    default Predicate<I> hasResult() {
        return i -> {
            final OptionalInt result = this.apply(i);
            return result != null && result.isPresent();
        };
    }

    /**
     * Returns a predicate checking if the optional returned by this OptionalIntMapper
     * is not {@code null}, holds a value, and if the predicate {@code test} returns {@code true} for this value.
     *
     * @param test predicate checking if the result value returned by this OptionalIntMapper
     * @return predicate checking if optional value returned by this OptionalIntMapper is tested positive.
     * is not {@code null}, holds a value, and if the predicate {@code test} returns {@code true} for this value.
     * @throws NullPointerException will be thrown if {@code test} is {@code null}.
     */
    default Predicate<I> hasResultAnd(IntPredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final OptionalInt result = apply(i);
            return result.isPresent() && test.test(result.getAsInt());
        };
    }

    /**
     * Returns a consumer that takes an input value, calls this OptionalIntMapper with it, and feeds the result to
     * the given {@code consumer}.
     *
     * @param consumer consumes the result of this OptionalIntMapper, when the returned Consumer is invoked.
     * @return consumer that takes an input value, calls this OptionalIntMapper with it, and feeds the result to
     * the parameter {@code consumer}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default Consumer<I> thenDo(Consumer<OptionalInt> consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.apply(i));
    }

    /**
     * Returns a consumer that takes an input value, calls this OptionalIntMapper with it, if the returned {@code Optional}
     * holds a value, feeds this value to the given {@code consumer}.
     *
     * @param consumer consumes the result value of the Optional returned by this OptionalIntMapper (if value is present),
     *                 when the returned Consumer is invoked.
     * @return consumer that takes an input value, calls this OptionalIntMapper with it, and feeds the content of the
     * resulting Optional to the parameter {@code consumer} (if value exists).
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default Consumer<I> thenIfPresent(IntConsumer consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> {
            final OptionalInt result = this.apply(i);
            if (result != null && result != null) {
                result.ifPresent(consumer);
            }
        };
    }

    /**
     * Returns a function that takes an input value, calls this OptionalIntMapper with it, and calls
     * {@link Optional#orElse(Object) orElse} on the returned Optional with the given value {@code o}. If
     * the Optional returned by this OptionalIntMapper is {@code null}, the function will return value {@code o}.
     *
     * @param o value that will be returned from the function returned by this method if either this OptionalIntMapper
     *          returns {@code null}, or an empty Optional.
     * @return function that takes an input value, calls this OptionalIntMapper with it, and calls
     * {@link OptionalInt#orElse(int) orElse} on the returned OptionalInt with the given value {@code o}. If
     * the OptionalInt returned by this OptionalIntMapper is {@code null}, the function will return value {@code o}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default TestableToIntFunction<I> orElse(int o) {
        return i -> {
            final OptionalInt result = this.apply(i);
            if (result != null && result != null) {
                return result.orElse(o);
            } else {
                return o;
            }
        };
    }

    /**
     * Returns a function that takes an input value, calls this OptionalIntMapper with it, and calls
     * {@link OptionalInt#orElseGet(IntSupplier) orElseGet} on the returned OptionalInt with the given value {@code supplier}. If
     * the Optional returned by this OptionalIntMapper is {@code null}, the function will return the value provided
     * by {@code supplier}.
     *
     * @param supplier supplies value that will be returned from the function returned by this method if either this
     *                 OptionalIntMapper returns {@code null}, or an empty OptionalInt.
     * @return function taking an input value, calls this OptionalIntMapper with it, and calls
     * {@link OptionalInt#orElseGet(IntSupplier) orElseGet} on the returned OptionalInt with the given value {@code supplier}. If
     * the OptionalInt returned by this OptionalIntMapper is {@code null}, the function will return the value provided
     * by {@code supplier}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default TestableToIntFunction<I> orElseGet(IntSupplier supplier) throws NullPointerException {
        Objects.requireNonNull(supplier);
        return i -> {
            final OptionalInt result = this.apply(i);
            if (result != null) {
                return result.orElseGet(supplier);
            } else {
                return supplier.getAsInt();
            }
        };
    }

    /**
     * This method will return a function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this OptionalIntMapper and if the result is not {@code null}, returns it.
     * If the result is {@code null}, an empty OptionalInt will be returned. So effectively {@code null} checks will be
     * performed on input and output of the function. Exceptions being thrown during the execution of this
     * OptionalIntMapper will also be thrown at the caller of the returned function.
     *
     * @return function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this OptionalIntMapper and if the result is not {@code null}, returns it.
     * If the result is {@code null}, an empty OptionalInt will be returned.
     */
    default OptionalIntMapper<I> nullAware() {
        return i -> {
            // check if input is null
            if (i == null) {
                return OptionalInt.empty();
            }
            final OptionalInt result = this.apply(i);
            // check if output is null
            if (result != null) {
                return result;
            } else {
                return OptionalInt.empty();
            }
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
