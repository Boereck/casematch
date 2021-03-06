package de.boereck.matcher.function.optionalmap;

import de.boereck.matcher.NoResultCaseMatcher;
import de.boereck.matcher.function.testable.TestableToDoubleFunction;
import de.boereck.matcher.function.testable.TestableToIntFunction;

import java.util.*;
import java.util.function.*;

/**
 * {@link Function} mapping an input of type I to an {@link OptionalDouble}. The result of this function should never
 * be {@code null}, even though most combinator methods of this interface are prepared for this.
 * This interface is especially useful when working with {@link NoResultCaseMatcher#caseDouble(Function, DoubleConsumer)}, or
 * {@link de.boereck.matcher.ResultCaseMatcher#caseDouble(Function, DoubleFunction)}.
 *
 * @param <I> Type of input to the function
 * @author Max Bureck
 */
@FunctionalInterface
public interface OptionalDoubleMapper<I> extends Function<I, OptionalDouble> {

    /**
     * Returns a function that will first call this OptionalDoubleMapper and afterwards calls
     * checks if the OptionalDouble contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalDoubleMapper is {@code null}, an empty {@code OptionalDouble} will be returned.
     *
     * @param <V> Type of element held by output optional
     * @param after mapping function that will be called on the value of the returned {@code OptionalDouble}
     *              of this OptionalDoubleMapper. Must not be {@code null}.
     * @return function mapping the result value of this OptionalDoubleMapper (provided the OptionalDouble holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will first call this OptionalDoubleMapper and afterwards calls
     * checks if the OptionalDouble contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalDoubleMapper is {@code null}, an empty {@code OptionalInt} will be returned.
     *
     * @param after mapping function that will be called on the value of the returned {@code OptionalDouble}
     *              of this OptionalDoubleMapper. Must not be {@code null}.
     * @return function mapping the result value of this OptionalDoubleMapper (provided the OptionalDouble holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will first call this OptionalDoubleMapper and afterwards calls
     * checks if the OptionalDouble contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalDoubleMapper is {@code null}, an empty {@code OptionalLong} will be returned.
     *
     * @param after mapping function that will be called on the value of the returned {@code OptionalDouble}
     *              of this OptionalDoubleMapper. Must not be {@code null}.
     * @return function mapping the result value of this OptionalDoubleMapper (provided the OptionalDouble holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will first call this OptionalDoubleMapper and afterwards calls
     * checks if the OptionalDouble contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalDoubleMapper is {@code null}, an empty {@code OptionalDouble} will be returned.
     *
     * @param after mapping function that will be called on the value of the returned {@code OptionalDouble}
     *              of this OptionalDoubleMapper. Must not be {@code null}.
     * @return function mapping the result value of this OptionalDoubleMapper (provided the OptionalDouble holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default OptionalDoubleMapper<I> mapD(DoubleUnaryOperator after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalDouble thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final double afterResult = after.applyAsDouble(thisResult.getAsDouble());
                return OptionalDouble.of(afterResult);
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * Returns a function that will call this OptionalDoubleMapper and afterwards, if the returned OptionalDouble contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalDoubleMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalDouble} returned by
     *              this OptionalDoubleMapper. Must not be {@code null}.
     * @param <V>   result type of optional returned by {@code after} mapper function.
     * @return function that will call this OptionalDoubleMapper and afterwards, if the returned OptionalDouble contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalDoubleMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will call this OptionalDoubleMapper and afterwards, if the returned OptionalDouble contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalDoubleMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalDouble} returned by
     *              this OptionalDoubleMapper. Must not be {@code null}.
     * @return function that will call this OptionalDoubleMapper and afterwards, if the returned OptionalDouble contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalDoubleMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will call this OptionalDoubleMapper and afterwards, if the returned OptionalDouble contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalDoubleMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalDouble} returned by
     *              this OptionalDoubleMapper. Must not be {@code null}.
     * @return function that will call this OptionalDoubleMapper and afterwards, if the returned OptionalDouble contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalDoubleMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will call this OptionalDoubleMapper and afterwards, if the returned OptionalDouble contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalDoubleMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalDouble} returned by
     *              this OptionalDoubleMapper. Must not be {@code null}.
     * @return function that will call this OptionalDoubleMapper and afterwards, if the returned OptionalDouble contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalDoubleMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
    default OptionalDoubleMapper<I> flatMapD(DoubleFunction<OptionalDouble> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalDouble thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                return after.apply(thisResult.getAsDouble());
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * Returns an optional mapper filtering the {@code OptionalDouble} returned by this OptionalDoubleMapper
     * with the given predicate {@code after}. This means that function returns an empty OptionalDouble
     * if either this OptionalDoubleMapper returns {@code null}, or an empty {@code OptionalDouble}, or if the
     * OptionalDouble returned from this OptionalDoubleMapper holds a value, but the predicate {@code after} returns
     * {@code false} for the value held in the OptionalDouble. If the predicate returns {@code true}, the function
     * will return the OptionalDouble returned from this OptionalDoubleMapper.
     *
     * @param after function being used to filter on the OptionalLong returned
     *              from this OptionalLongMapper. This parameter must not be {@code null}.
     * @return optional mapper filtering the {@code OptionalLong} returned by this OptionalLongMapper
     * with the given predicate {@code after}.
     * @throws NullPointerException will be thrown if {@code after} is {@code null}.
     */
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

    /**
     * Returns a function that will call this OptionalDoubleMapper and checks if the returned OptionalDouble contains
     * a value. If this OptionalDoubleMapper returns {@code null}, the predicate will return {@code false}.
     * @return function checking if the result of this OptionalDoubleMapper is not {@code null} and holds a value
     */
    default Predicate<I> hasResult() {
        return i -> {
            final OptionalDouble result = this.apply(i);
            return result != null && result.isPresent();
        };
    }

    /**
     * Returns a predicate checking if the optional returned by this OptionalDoubleMapper
     * is not {@code null}, holds a value, and if the predicate {@code test} returns {@code true} for this value.
     *
     * @param test predicate checking if the result value returned by this OptionalDoubleMapper
     * @return predicate checking if optional value returned by this OptionalDoubleMapper is tested positive.
     * is not {@code null}, holds a value, and if the predicate {@code test} returns {@code true} for this value.
     * @throws NullPointerException will be thrown if {@code test} is {@code null}.
     */
    default Predicate<I> hasResultAnd(DoublePredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final OptionalDouble result = apply(i);
            return result != null && result.isPresent() && test.test(result.getAsDouble());
        };
    }

    /**
     * Returns a consumer that takes an input value, calls this OptionalDoubleMapper with it, and feeds the result to
     * the given {@code consumer}.
     *
     * @param consumer consumes the result of this OptionalDoubleMapper, when the returned Consumer is invoked.
     * @return consumer that takes an input value, calls this OptionalDoubleMapper with it, and feeds the result to
     * the parameter {@code consumer}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default Consumer<I> thenDo(Consumer<OptionalDouble> consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> {
            OptionalDouble res = this.apply(i);
            if(res == null) {
                res = OptionalDouble.empty();
            }
            consumer.accept(res);
        };
    }

    /**
     * Returns a consumer that takes an input value, calls this OptionalDoubleMapper with it, if the returned {@code Optional}
     * holds a value, feeds this value to the given {@code consumer}.
     *
     * @param consumer consumes the result value of the Optional returned by this OptionalDoubleMapper (if value is present),
     *                 when the returned Consumer is invoked.
     * @return consumer that takes an input value, calls this OptionalDoubleMapper with it, and feeds the content of the
     * resulting Optional to the parameter {@code consumer} (if value exists).
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default Consumer<I> thenIfPresent(DoubleConsumer consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> {
            final OptionalDouble result = this.apply(i);
            if (result != null) {
                result.ifPresent(consumer);
            }
        };
    }

    /**
     * Returns a function that takes an input value, calls this OptionalDoubleMapper with it, and calls
     * {@link Optional#orElse(Object) orElse} on the returned Optional with the given value {@code o}. If
     * the Optional returned by this OptionalDoubleMapper is {@code null}, the function will return value {@code o}.
     *
     * @param o value that will be returned from the function returned by this method if either this OptionalDoubleMapper
     *          returns {@code null}, or an empty OptionalDouble.
     * @return function that takes an input value, calls this OptionalDoubleMapper with it, and calls
     * {@link OptionalDouble#orElse(double) orElse} on the returned OptionalDouble with the given value {@code o}. If
     * the OptionalDouble returned by this OptionalDoubleMapper is {@code null}, the function will return value {@code o}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default TestableToDoubleFunction<I> orElse(double o) {
        return i -> {
            final OptionalDouble result = this.apply(i);
            if (result != null) {
                return result.orElse(o);
            } else {
                return o;
            }
        };
    }

    /**
     * Returns a function that takes an input value, calls this OptionalDoubleMapper with it, and calls
     * {@link OptionalDouble#orElseGet(DoubleSupplier) orElseGet} on the returned OptionalDouble with the given value {@code supplier}. If
     * the Optional returned by this OptionalDoubleMapper is {@code null}, the function will return the value provided
     * by {@code supplier}.
     *
     * @param supplier supplies value that will be returned from the function returned by this method if either this
     *                 OptionalDoubleMapper returns {@code null}, or an empty OptionalDouble.
     * @return function taking an input value, calls this OptionalDoubleMapper with it, and calls
     * {@link OptionalDouble#orElseGet(DoubleSupplier) orElseGet} on the returned OptionalDouble with the given value {@code supplier}. If
     * the OptionalDouble returned by this OptionalDoubleMapper is {@code null}, the function will return the value provided
     * by {@code supplier}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default TestableToDoubleFunction<I> orElseGet(DoubleSupplier supplier) throws NullPointerException {
        Objects.requireNonNull(supplier);
        return i -> {
            final OptionalDouble result = this.apply(i);
            if (result != null) {
                return result.orElseGet(supplier);
            } else {
                return supplier.getAsDouble();
            }
        };
    }

    /**
     * This method will return a function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this OptionalDoubleMapper and if the result is not {@code null}, returns it.
     * If the result is {@code null}, an empty OptionalDouble will be returned. So effectively {@code null} checks will be
     * performed on input and output of the function. Exceptions being thrown during the execution of this
     * OptionalDoubleMapper will also be thrown at the caller of the returned function.
     *
     * @return function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this OptionalDoubleMapper and if the result is not {@code null}, returns it.
     * If the result is {@code null}, an empty OptionalDouble will be returned.
     */
    default OptionalDoubleMapper<I> nullAware() {
        return i -> {
            // check if input is null
            if (i == null) {
                return OptionalDouble.empty();
            }
            final OptionalDouble result = this.apply(i);
            // check if output is null
            if (result != null) {
                return result;
            } else {
                return OptionalDouble.empty();
            }
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
    @SuppressWarnings("unchecked") // Safe cast, checked if t is instance of E
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
    @SuppressWarnings("unchecked") // Safe cast, checked if t is instance of E
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

    /**
     * Converts this optional mapper to a function that will perform the mapper and check if there is a result available
     * and return it. If the returned optional is {@code null} or empty, the function will throw a {@link java.util.NoSuchElementException}.
     *
     * @return function not defined on inputs produce empty optionals. This means that
     * if there this mapper produces an optional holding a value, this will be returned. Otherwise a
     * {@link java.util.NoSuchElementException} is thrown.
     */
    default  TestableToDoubleFunction<I> partial() {
        return i -> {
            OptionalDouble result = apply(i);
            if(result == null) {
                throw  new NoSuchElementException();
            }
            return result.getAsDouble();
        };
    }
}
