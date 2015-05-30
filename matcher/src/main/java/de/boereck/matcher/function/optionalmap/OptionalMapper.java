package de.boereck.matcher.function.optionalmap;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;

import de.boereck.matcher.NoResultCaseMatcher;
import de.boereck.matcher.ResultCaseMatcher;
import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.helpers.MatchHelpers;

/**
 * {@link Function} mapping an input of type I to an Optional&lt;O&gt;.
 * This interface is especially useful when working with {@link NoResultCaseMatcher#caseObj(java.util.function.Function, java.util.function.Consumer)}
 * or {@link ResultCaseMatcher#caseOf(java.util.function.BooleanSupplier, java.util.function.Function)}.
 *
 * @param <I> type of input to function
 * @param <O> type of optional output of function
 * @author Max Bureck
 * @see MatchHelpers#cast(Class)
 */
@FunctionalInterface
public interface OptionalMapper<I, O> extends Function<I, Optional<O>> {

    /**
     * Returns a {@code Function} that will first call this OptionalMapper and afterwards calls
     * the {@link Optional#map(Function) map} function of the returned {@code Optional}
     * with the given mapping function {@code after}. If the result of this OptionalMapper
     * is {@code null}, an empty {@code Optional} will be returned.
     *
     * @param after mapping function that will be called on the returned {@code Optional}
     *              of this OptionalMapper. Must not be {@code null}.
     * @param <V>   result type of {@code after} mapping function.
     * @return method that will first call this OptionalMapper and afterwards calls
     * the {@link Optional#map(Function) map} function of the returned {@code Optional}
     * with the given mapping function {@code after}.
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default <V> OptionalMapper<I, V> map(Function<? super O, ? extends V> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            return thisResult != null ? thisResult.map(after) : Optional.empty();
        };
    }

    /**
     * Returns a function that will first call this OptionalMapper and afterwards calls
     * function {@code after} on the result. If this OptionalMapper returns an optional that holds a
     * value an optional holding the result will be returned, otherwise an empty optional will be returned.
     * If the result of this OptionalMapper is {@code null}, an empty {@code Optional} will be returned from
     * the resulting function being returned.
     *
     * @param after mapping function that will be called on the returned {@code Optional}
     *              of this OptionalMapper. Must not be {@code null}.
     * @return function that will first call this OptionalMapper and afterwards calls
     * function {@code after} on the result, if this OptionalMapper returns an optional that holds a
     * value an optional holding the result will be returned, otherwise an empty optional will be returned.
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default OptionalIntMapper<I> mapI(ToIntFunction<? super O> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final O val = thisResult.get();
                return OptionalInt.of(after.applyAsInt(val));
            } else {
                return OptionalInt.empty();
            }
        };
    }

    /**
     * Returns a function that will first call this OptionalMapper and afterwards calls
     * function {@code after} on the result. If this OptionalMapper returns an optional that holds a
     * value an optional holding the result will be returned, otherwise an empty optional will be returned.
     * If the result of this OptionalMapper is {@code null}, an empty {@code Optional} will be returned from
     * the resulting function being returned.
     *
     * @param after mapping function that will be called on the returned {@code Optional}
     *              of this OptionalMapper. Must not be {@code null}.
     * @return function that will first call this OptionalMapper and afterwards calls
     * function {@code after} on the result, if this OptionalMapper returns an optional that holds a
     * value an optional holding the result will be returned, otherwise an empty optional will be returned.
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default OptionalLongMapper<I> mapL(ToLongFunction<? super O> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final O val = thisResult.get();
                return OptionalLong.of(after.applyAsLong(val));
            } else {
                return OptionalLong.empty();
            }
        };
    }

    /**
     * Returns a function that will first call this OptionalMapper and afterwards calls
     * function {@code after} on the result. If this OptionalMapper returns an optional that holds a
     * value an optional holding the result will be returned, otherwise an empty optional will be returned.
     * If the result of this OptionalMapper is {@code null}, an empty {@code Optional} will be returned from
     * the resulting function being returned.
     *
     * @param after mapping function that will be called on the returned {@code Optional}
     *              of this OptionalMapper. Must not be {@code null}.
     * @return function that will first call this OptionalMapper and afterwards calls
     * function {@code after} on the result, if this OptionalMapper returns an optional that holds a
     * value an optional holding the result will be returned, otherwise an empty optional will be returned.
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default OptionalDoubleMapper<I> mapD(ToDoubleFunction<? super O> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final O val = thisResult.get();
                return OptionalDouble.of(after.applyAsDouble(val));
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * Returns a function that will first call this OptionalMapper and afterwards calls
     * the {@link Optional#flatMap(Function) flatMap} method of the returned {@code Optional}
     * with the given mapping function {@code after}. If the result of this OptionalMapper
     * is {@code null}, an empty {@code Optional} will be returned.
     *
     * @param after mapping function that will be called on the returned {@code Optional}
     *              of this OptionalMapper. Must not be {@code null}.
     * @param <V>   type of element held by the returned optional, which is the result type of
     *              the returned function
     * @return function that will first call this OptionalMapper and afterwards calls
     * the {@link Optional#flatMap(Function) flatMap} method of the returned {@code Optional}
     * with the given mapping function {@code after}.
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default <V> OptionalMapper<I, V> flatMap(Function<? super O, Optional<V>> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            return (thisResult != null) ? thisResult.flatMap(after) : Optional.empty();
        };
    }

    /**
     * Returns a function that will first call this OptionalMapper and afterwards calls
     * function {@code after} on the optional result. Meaning, if this OptionalMapper returns an optional holding a
     * value, the result of {@code after} (called with the content of the option) is returned. Otherwise an empty
     * optional will be returned. If the result of this OptionalMapper is {@code null}, an empty {@code Optional} will be
     * returned from the resulting function.
     *
     * @param after function being called on the result value of {@code this::apply}, if the optional result exists.
     *              Must not be {@code null}.
     * @return function that will first call this OptionalMapper and afterwards calls function {@code after} on the
     * optional result.
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default OptionalIntMapper<I> flatMapI(Function<? super O, OptionalInt> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final O val = thisResult.get();
                return after.apply(val);
            } else {
                return OptionalInt.empty();
            }
        };
    }

    /**
     * Returns a function that will first call this OptionalMapper and afterwards calls
     * function {@code after} on the optional result. Meaning, if this OptionalMapper returns an optional holding a
     * value, the result of {@code after} (called with the content of the option) is returned. Otherwise an empty
     * optional will be returned. If the result of this OptionalMapper is {@code null}, an empty {@code Optional} will be
     * returned from the resulting function.
     *
     * @param after function being called on the result value of {@code this::apply}, if the optional result exists.
     *              Must not be {@code null}.
     * @return function that will first call this OptionalMapper and afterwards calls function {@code after} on the
     * optional result.
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default <V> OptionalLongMapper<I> flatMapL(Function<? super O, OptionalLong> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final O val = thisResult.get();
                return after.apply(val);
            } else {
                return OptionalLong.empty();
            }
        };
    }

    /**
     * Returns a function that will first call this OptionalMapper and afterwards calls
     * function {@code after} on the optional result. Meaning, if this OptionalMapper returns an optional holding a
     * value, the result of {@code after} (called with the content of the option) is returned. Otherwise an empty
     * optional will be returned. If the result of this OptionalMapper is {@code null}, an empty {@code Optional} will be
     * returned from the resulting function.
     *
     * @param after function being called on the result value of {@code this::apply}, if the optional result exists.
     *              Must not be {@code null}.
     * @return function that will first call this OptionalMapper and afterwards calls function {@code after} on the
     * optional result.
     * @throws NullPointerException if parameter {@code after} is {@code null}.
     */
    default <V> OptionalDoubleMapper<I> flatMapD(Function<? super O, OptionalDouble> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            if (thisResult != null && thisResult.isPresent()) {
                final O val = thisResult.get();
                return after.apply(val);
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * Returns an optional mapper filtering the {@code Optional} returned by this OptionalMapper
     * with the given predicate {@code after}. This means that function returns an empty Optional
     * if either this OptionalMapper returns {@code null}, or an empty {@code Optional}, or if the
     * Optional returned from this OptionalMapper holds a value, but the predicate {@code after} returns
     * {@code false} for the value held in the Optional. If the predicate reutrns {@code true}, the function
     * will return the Optional returned from this OptionalMapper.
     *
     * @param after function being used to call {@link Optional#filter(Predicate) filter} on the Optional returned
     *              from this OptionalMapper. This parameter must not be {@code null}.
     * @return optional mapper filtering the {@code Optional} returned by this OptionalMapper
     * with the given predicate {@code after}.
     * @throws NullPointerException will be thrown if {@code after} is {@code null}.
     */
    default OptionalMapper<I, O> filter(Predicate<O> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            return (thisResult != null) ? thisResult.filter(after) : Optional.empty();
        };
    }

    /**
     * Returns an optional mapper filtering the {@code Optional} returned by this OptionalMapper
     * with the given class {@code clazz}. This means that function returns an empty Optional
     * if either this OptionalMapper returns {@code null}, or an empty {@code Optional}, or if the
     * Optional returned from this OptionalMapper holds an object not instance of {@code clazz}.
     * If the object in the Optional is instance of {@code clazz}, the function will return an optional
     * holding the object of type {@code clazz}.
     *
     * @param <R>   Type of object held in optional returned by the function, returned by this method.
     * @param clazz Class the object contained in the Optional returned by this OptionalMapper is checked for.
     * @return optional mapper filtering the {@code Optional} returned by this OptionalMapper
     * by class {@code clazz}.
     * @throws NullPointerException will be thrown if {@code clazz} is {@code null}.
     */
    @SuppressWarnings("unchecked") // we know that Optional can only contain instance of R or be empty.
    default <R> OptionalMapper<I, R> filter(Class<R> clazz) throws NullPointerException {
        Objects.requireNonNull(clazz);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            if (thisResult != null) {
                return (Optional<R>) thisResult.filter(o -> clazz.isInstance(o));
            } else {
                return Optional.empty();
            }
        };
    }

    /**
     * This method returns a predicate checking if the optional returned by this OptionalMapper
     * is not {@code null} and holds a value.
     *
     * @return predicate checking if optional returned by this OptionalMapper
     * is not {@code null} and holds a value.
     */
    default Predicate<I> hasResult() {
        return i -> {
            final Optional<O> thisResult = this.apply(i);
            return (thisResult != null) && thisResult.isPresent();
        };
    }

    /**
     * This method returns a predicate checking if the optional returned by this OptionalMapper
     * is not {@code null}, holds a value, and if the predicate {@code test} returns {@code true} for this value.
     *
     * @param test predicate checking if the result value returned by this OptionalMapper
     * @return predicate checking if optional value returned by this OptionalMapper is tested positive.
     * is not {@code null}, holds a value, and if the predicate {@code test} returns {@code true} for this value.
     * @throws NullPointerException will be thrown if {@code test} is {@code null}.
     */
    default Predicate<I> hasResultAnd(Predicate<O> test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final Optional<O> result = apply(i);
            return result != null && result.isPresent() && test.test(result.get());
        };
    }

    /**
     * Returns a consumer that takes an input value, calls this OptionalMapper with it, and feeds the result to
     * the given {@code consumer}.
     *
     * @param consumer consumes the result of this OptionalMapper, when the returned Consumer is invoked.
     * @return consumer that takes an input value, calls this OptionalMapper with it, and feeds the result to
     * the parameter {@code consumer}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default Consumer<I> thenDo(Consumer<Optional<? super O>> consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.apply(i));
    }

    /**
     * Returns a consumer that takes an input value, calls this OptionalMapper with it, if the returned {@code Optional}
     * holds a value, feeds this value to the given {@code consumer}.
     *
     * @param consumer consumes the result value of the Optional returned by this OptionalMapper (if value is present),
     *                 when the returned Consumer is invoked.
     * @return consumer that takes an input value, calls this OptionalMapper with it, and feeds the content of the
     * resulting Optional to the parameter {@code consumer} (if value exists).
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default Consumer<I> thenIfPresent(Consumer<? super O> consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> {
            Optional<O> result = this.apply(i);
            if (result != null) {
                result.ifPresent(consumer);
            }
        };
    }

    /**
     * Returns a function that takes an input value, calls this OptionalMapper with it, and calls
     * {@link Optional#orElse(Object) orElse} on the returned Optional with the given value {@code o}. If
     * the Optional returned by this OptionalMapper is {@code null}, the function will return value {@code o}.
     *
     * @param o value that will be returned from the function returned by this method if either this OptionalMapper
     *          returns {@code null}, or an empty Optional.
     * @return function that takes an input value, calls this OptionalMapper with it, and calls
     * {@link Optional#orElse(Object) orElse} on the returned Optional with the given value {@code o}. If
     * the Optional returned by this OptionalMapper is {@code null}, the function will return value {@code o}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default TestableFunction<I, O> orElse(O o) {
        return i -> {
            Optional<O> result = this.apply(i);
            if (result != null) {
                return result.orElse(o);
            } else {
                return o;
            }
        };
    }

    /**
     * Returns a function that takes an input value, calls this OptionalMapper with it, and calls
     * {@link Optional#orElseGet(Supplier) orElseGet} on the returned Optional with the given value {@code supplier}. If
     * the Optional returned by this OptionalMapper is {@code null}, the function will return the value provided
     * by {@code supplier}.
     *
     * @param supplier supplies value that will be returned from the function returned by this method if either this
     *                 OptionalMapper returns {@code null}, or an empty Optional.
     * @return function taking an input value, calls this OptionalMapper with it, and calls
     * {@link Optional#orElseGet(Supplier) orElseGet} on the returned Optional with the given value {@code supplier}. If
     * the Optional returned by this OptionalMapper is {@code null}, the function will return the value provided
     * by {@code supplier}.
     * @throws NullPointerException if {@code consumer} is {@code null}.
     */
    default TestableFunction<I, O> orElseGet(Supplier<O> supplier) throws NullPointerException {
        Objects.requireNonNull(supplier);
        return i -> {
            Optional<O> result = this.apply(i);
            if (result != null) {
                return result.orElseGet(supplier);
            } else {
                return supplier.get();
            }
        };
    }
}