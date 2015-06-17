package de.boereck.matcher.function.optionalmap;

import de.boereck.matcher.NoResultCaseMatcher;
import de.boereck.matcher.ResultCaseMatcher;
import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.function.testable.TestableToLongFunction;

import java.util.*;
import java.util.function.*;

/**
 * {@link Function} mapping an input of type I to an {@link OptionalLong}. The result of this function should never
 * be {@code null}, even though most combinator methods of this interface are prepared for this.
 * This interface is especially useful when working with {@link NoResultCaseMatcher#caseLong(Function, LongConsumer)}, or
 * {@link ResultCaseMatcher#caseLong(Function, LongFunction)}.
 *
 * @param <I> Type of input to the function
 * @author Max Bureck
 */
@FunctionalInterface
public interface OptionalLongMapper<I> extends Function<I, OptionalLong> {

    /**
     * Returns a function that will first call this OptionalLongMapper and afterwards
     * checks if the OptionalLong contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalLongMapper is {@code null}, an empty {@code Optional} will be returned.
     *
     * @param after mapping function that will be called on the value of the returned {@code OptionalLong}
     *              of this OptionalLongMapper. Must not be {@code null}.
     * @param <V>   result type of {@code after} mapping function.
     * @return function mapping the result value of this OptionalLongMapper (provided the OptionalLong holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default <V> OptionalMapper<I, V> map(LongFunction<? extends V> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final V afterResult = after.apply(thisResult.getAsLong());
                return Optional.ofNullable(afterResult);
            } else {
                return Optional.empty();
            }
        };
    }

    /**
     * Returns a function that will first call this OptionalLongMapper and afterwards calls
     * the checks if the OptionalLong contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalLongMapper is {@code null}, an empty {@code Optional} will be returned.
     *
     * @param after mapping function that will be called on the value of the returned {@code OptionalLong}
     *              of this OptionalLongMapper. Must not be {@code null}.
     * @return function mapping the result value of this OptionalLongMapper (provided the OptionalLong holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default OptionalIntMapper<I> mapI(LongToIntFunction after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final int afterResult = after.applyAsInt(thisResult.getAsLong());
                return OptionalInt.of(afterResult);
            } else {
                return OptionalInt.empty();
            }
        };
    }

    /**
     * Returns a function that will first call this OptionalLongMapper and afterwards calls
     * the checks if the OptionalLong contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalLongMapper is {@code null}, an empty {@code Optional} will be returned.
     *
     * @param after mapping function that will be called on the value of the returned {@code OptionalLong}
     *              of this OptionalLongMapper. Must not be {@code null}.
     * @return function mapping the result value of this OptionalLongMapper (provided the OptionalLong holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default OptionalLongMapper<I> mapL(LongUnaryOperator after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult!=null && thisResult.isPresent()) {
                final long afterResult = after.applyAsLong(thisResult.getAsLong());
                return OptionalLong.of(afterResult);
            } else {
                return thisResult;
            }
        };
    }

    /**
     * Returns a function that will first call this OptionalLongMapper and afterwards calls
     * the checks if the OptionalLong contains a value. If so, the value in the optional will be used as an input to
     * the given mapping function {@code after} and returned in an optional, otherwise an empty optional will be returned.
     * If the result of this OptionalLongMapper is {@code null}, an empty {@code Optional} will be returned.
     *
     * @param after mapping function that will be called on the value of the returned {@code OptionalLong}
     *              of this OptionalLongMapper. Must not be {@code null}.
     * @return function mapping the result value of this OptionalLongMapper (provided the OptionalLong holds a value).
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default OptionalDoubleMapper<I> mapD(LongToDoubleFunction after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final double afterResult = after.applyAsDouble(thisResult.getAsLong());
                return OptionalDouble.of(afterResult);
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * Returns a function that will call this OptionalLongMapper and afterwards, if the returned OptionalLong contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalLongMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalLong} returned by
     *              this OptionalLongMapper. Must not be {@code null}.
     * @param <V>   result type of optional returned by {@code after} mapper function.
     * @return function that will call this OptionalLongMapper and afterwards, if the returned OptionalLong contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalLongMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
    default <V> OptionalMapper<I, V> flatMap(LongFunction<Optional<V>> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                return after.apply(thisResult.getAsLong());
            } else {
                return Optional.empty();
            }
        };
    }

    /**
     * Returns a function that will call this OptionalLongMapper and afterwards, if the returned OptionalLong contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalLongMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalLong} returned by
     *              this OptionalLongMapper. Must not be {@code null}.
     * @return function that will call this OptionalLongMapper and afterwards, if the returned OptionalLong contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalLongMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
    default OptionalIntMapper<I> flatMapI(LongFunction<OptionalInt> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                return after.apply(thisResult.getAsLong());
            } else {
                return OptionalInt.empty();
            }
        };
    }

    /**
     * Returns a function that will call this OptionalLongMapper and afterwards, if the returned OptionalLong contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalLongMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalLong} returned by
     *              this OptionalLongMapper. Must not be {@code null}.
     * @return function that will call this OptionalLongMapper and afterwards, if the returned OptionalLong contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalLongMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
    default OptionalLongMapper<I> flatMapL(LongFunction<OptionalLong> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                return after.apply(thisResult.getAsLong());
            } else {
                return thisResult;
            }
        };
    }

    /**
     * Returns a function that will call this OptionalLongMapper and afterwards, if the returned OptionalLong contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalLongMapper is {@code null} or empty, the resulting function will return an
     * empty optional.
     *
     * @param after mapping function that will be called with the value of the {@code OptionalLong} returned by
     *              this OptionalLongMapper. Must not be {@code null}.
     * @return function that will call this OptionalLongMapper and afterwards, if the returned OptionalLong contains a
     * value, calls the {@code after} mapping function with that value and returns the resulting optional. If the
     * optional returned by this OptionalLongMapper is empty or {@code null}, the resulting function will
     * return an empty optional.
     * @throws NullPointerException will the thrown if {@code after} is {@code null}.
     */
    default OptionalDoubleMapper<I> flatMapD(LongFunction<OptionalDouble> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                return after.apply(thisResult.getAsLong());
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * Returns an optional mapper filtering the {@code OptionalLong} returned by this OptionalLongMapper
     * with the given predicate {@code after}. This means that function returns an empty OptionalLong
     * if either this OptionalLongMapper returns {@code null}, or an empty {@code OptionalLong}, or if the
     * OptionalLong returned from this OptionalLongMapper holds a value, but the predicate {@code after} returns
     * {@code false} for the value held in the OptionalLong. If the predicate returns {@code true}, the function
     * will return the OptionalLong returned from this OptionalLongMapper.
     *
     * @param after function being used to filter on the OptionalLong returned
     *              from this OptionalLongMapper. This parameter must not be {@code null}.
     * @return optional mapper filtering the {@code OptionalLong} returned by this OptionalLongMapper
     * with the given predicate {@code after}.
     * @throws NullPointerException will be thrown if {@code after} is {@code null}.
     */
    default OptionalLongMapper<I> filter(LongPredicate after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent() && after.test(thisResult.getAsLong())) {
                return thisResult;
            } else {
                return OptionalLong.empty();
            }
        };
    }

    /**
     * This method returns a predicate checking if the optional returned by this OptionalLongMapper
     * is not {@code null} and holds a value.
     *
     * @return predicate checking if optional returned by this OptionalLongMapper
     * is not {@code null} and holds a value.
     */
    default Predicate<I> hasResult() {
        return i -> {
            final OptionalLong result = this.apply(i);
            return result != null && result.isPresent();
        };
    }

    /**
     * This method returns a predicate checking if the optional returned by this OptionalLongMapper
     * is not {@code null}, holds a value, and if the predicate {@code test} returns {@code true} for this value.
     *
     * @param test predicate checking if the result value returned by this OptionalLongMapper
     * @return predicate checking if optional value returned by this OptionalLongMapper is tested positive.
     * is not {@code null}, holds a value, and if the predicate {@code test} returns {@code true} for this value.
     * @throws NullPointerException will be thrown if {@code test} is {@code null}.
     */
    default Predicate<I> hasResultAnd(LongPredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final OptionalLong result = apply(i);
            return result != null && result.isPresent() && test.test(result.getAsLong());
        };
    }

    /**
     * Returns a consumer that takes an input value, calls this OptionalLongMapper with it, and feeds the result to
     * the given {@code consumer}.
     *
     * @param consumer consumes the result of this OptionalLongMapper, when the returned Consumer is invoked.
     * @return consumer that takes an input value, calls this OptionalLongMapper with it, and feeds the result to
     * the parameter {@code consumer}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default Consumer<I> thenDo(Consumer<OptionalLong> consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.apply(i));
    }

    /**
     * Returns a consumer that takes an input value, calls this OptionalLongMapper with it, if the returned {@code Optional}
     * holds a value, feeds this value to the given {@code consumer}.
     *
     * @param consumer consumes the result value of the Optional returned by this OptionalLongMapper (if value is present),
     *                 when the returned Consumer is invoked.
     * @return consumer that takes an input value, calls this OptionalLongMapper with it, and feeds the content of the
     * resulting Optional to the parameter {@code consumer} (if value exists).
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default Consumer<I> thenIfPresent(LongConsumer consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> {
            final OptionalLong result = this.apply(i);
            if (result != null) {
                result.ifPresent(consumer);
            }
        };
    }

    /**
     * Returns a function that takes an input value, calls this OptionalLongMapper with it, and calls
     * {@link Optional#orElse(Object) orElse} on the returned Optional with the given value {@code o}. If
     * the Optional returned by this OptionalLongMapper is {@code null}, the function will return value {@code o}.
     *
     * @param o value that will be returned from the function returned by this method if either this OptionalLongMapper
     *          returns {@code null}, or an empty Optional.
     * @return function that takes an input value, calls this OptionalLongMapper with it, and calls
     * {@link Optional#orElse(Object) orElse} on the returned Optional with the given value {@code o}. If
     * the Optional returned by this OptionalLongMapper is {@code null}, the function will return value {@code o}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default TestableToLongFunction<I> orElse(long o) {
        return i -> {
            final OptionalLong result = this.apply(i);
            if (result != null) {
                return result.orElse(o);
            } else {
                return o;
            }
        };
    }

    /**
     * Returns a function that takes an input value, calls this OptionalLongMapper with it, and calls
     * {@link OptionalLong#orElseGet(LongSupplier) orElseGet}  on the returned OptionalLong with the given value {@code supplier}. If
     * the OptionalLong returned by this OptionalLongMapper is {@code null}, the function will return the value provided
     * by {@code supplier}.
     *
     * @param supplier supplies value that will be returned from the function returned by this method if either this
     *                 OptionalLongMapper returns {@code null}, or an empty OptionalLong.
     * @return function taking an input value, calls this OptionalLongMapper with it, and calls
     * {@link OptionalLong#orElseGet(LongSupplier) orElseGet} on the returned OptionalLong with the given value {@code supplier}. If
     * the OptionalLong returned by this OptionalLongMapper is {@code null}, the function will return the value provided
     * by {@code supplier}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default TestableToLongFunction<I> orElseGet(LongSupplier supplier) throws NullPointerException {
        Objects.requireNonNull(supplier);
        return i -> {
            final OptionalLong result = this.apply(i);
            if (result != null) {
                return result.orElseGet(supplier);
            } else {
                return supplier.getAsLong();
            }
        };
    }

    /**
     * This method will return a function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this OptionalLongMapper and if the result is not {@code null}, returns it.
     * If the result is {@code null}, an empty OptionalLong will be returned. So effectively {@code null} checks will be
     * performed on input and output of the function. Exceptions being thrown during the execution of this
     * OptionalLongMapper will also be thrown at the caller of the returned function.
     *
     * @return function that checks if the input is {@code null} and if so returns an empty
     * optional. Otherwise it will call this OptionalLongMapper and if the result is not {@code null}, returns it.
     * If the result is {@code null}, an empty OptionalLong will be returned.
     */
    default OptionalLongMapper<I> nullAware() {
        return i -> {
            // check if input is null
            if (i == null) {
                return OptionalLong.empty();
            }
            final OptionalLong result = this.apply(i);
            // check if output is null
            if (result != null) {
                return result;
            } else {
                return OptionalLong.empty();
            }
        };
    }

    /**
     * The returned function will execute this OptionalLongMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalLong in this case. It is recommended to handle the exception, e.g. by using  method
     * {@link OptionalLongMapper#withCatch(Class, Consumer) &lt;E extends Throwable&gt; withCatch(Class&lt;E&gt;, Consumer&lt;E&gt;)}
     * or
     * {@link OptionalLongMapper#withCatch(Consumer) withCatch(Consumer&lt;Exception&gt;)} instead.
     *
     * @return function that will execute this OptionalLongMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an {@link Exception Exception}, this will be caught and swallowed(!);
     * the function will return an empty OptionalLong in this case.
     */
    default OptionalLongMapper<I> withCatch() {
        return i -> {
            try {
                return this.apply(i);
            } catch (Exception e) {
                return OptionalLong.empty();
            }
        };
    }

    /**
     * The returned function will execute this OptionalLongMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an throwable of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the Exception; the function will return an empty OptionalLong in this case.
     * Throwables of other types will be re-thrown.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz   type of throwable to be caught
     * @param handler consumer handling the throwable
     * @param <E>     type of throwable to be caught
     * @return function that will execute this OptionalLongMapper and potentially handling exceptions of type {@code clazz}
     * with {@code handler}. After handling exceptions, the function will return an empty OptionalLong.
     * @throws NullPointerException if {@code clazz} or {@code handler} are {@code null}.
     */
    @SuppressWarnings("unchecked") // Safe cast, checked if t is instance of E
    default <E extends Throwable> OptionalLongMapper<I> withCatch(Class<E> clazz, Consumer<E> handler) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return this.apply(i);
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
     * The returned function will execute this OptionalLongMapper and if it executes without any problem, the result
     * will be returned. If the execution throws an exception, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalLong in this case.
     * If the handler itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param handler will handle exceptions being thrown during the execution of this OptionalLongMapper
     * @return function execution this OptionalLongMapper and if execution performs without any problem, the result
     * will be returned. If the execution throws an exception of type {@code clazz}, it will be caught and the consumer
     * {@code handler} will be called with the exception; the function will return an empty OptionalLong in this case.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    default OptionalLongMapper<I> withCatch(Consumer<Exception> handler) throws NullPointerException {
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return this.apply(i);
            } catch (Exception e) {
                handler.accept(e);
                return OptionalLong.empty();
            }
        };
    }

    /**
     * Returns a function executing this OptionalLongMapper and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalLong should be
     * returned, consider using method {@link OptionalLongMapper#withCatch() withCatch()} instead.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param recovery method providing a regular result value if the OptionalLongMapper throws an exception.
     * @return function executing this OptionalLongMapper and if a throwable is thrown during the execution, the
     * given {@code recovery} function is used to provide a value to be returned. If an empty OptionalLong should be
     * returned, consider using method {@link OptionalLongMapper#withCatch() withCatch()} instead.
     * @throws NullPointerException if {@code recovery} is {@code null}.
     */
    default OptionalLongMapper<I> recoverWith(Function<? super Throwable, OptionalLong> recovery) throws NullPointerException {
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
     * Returns a function executing this OptionalLongMapper and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * If the recovery method itself will throw an exception, it will not be caught and propagated to the caller of the function.
     *
     * @param clazz class of exceptions to be caught and recovered from.
     * @param recovery function recovering from an exception providing a regular value to return from the returned function.
     * @param <E> Type of exceptions to be caught and recovered from.
     * @return function executing this OptionalLongMapper and if a an exception of type {@code E} is thrown during the execution,
     * the given {@code recovery} function is used to provide a value to be returned. Exceptions of other types will be
     * re-thrown to the caller.
     * @throws NullPointerException if {@code clazz} or {@code recovery} are {@code null}.
     */
    @SuppressWarnings("unchecked") // Safe cast, checked if t is instance of E
    default <E extends Throwable> OptionalLongMapper<I> recoverWith(Class<E> clazz, Function<? super E, OptionalLong> recovery) {
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
    default  TestableToLongFunction<I> partial() {
        return i -> {
            OptionalLong result = apply(i);
            if(result == null) {
                throw  new NoSuchElementException();
            }
            return result.getAsLong();
        };
    }
}
