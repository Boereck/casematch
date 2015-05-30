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
     * Returns a {@code Function} that will first call this function and afterwards calls
     * the {@link Optional#map(Function) map} function of the returned {@code Optional}
     * with the given mapping function {@code after}. If the result of this function
     * is {@code null}, an empty {@code Optional} will be returned.
     *
     * @param after mapping function that will be called on the returned {@code Optional}
     *              of this function. Must not be {@code null}.
     * @param <V>   result type of {@code after} mapping function.
     * @return method that will first call this function and afterwards calls
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

    default <V> OptionalIntMapper<I> mapI(ToIntFunction<? super O> after) throws NullPointerException {
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

    default <V> OptionalLongMapper<I> mapL(ToLongFunction<? super O> after) throws NullPointerException {
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

    default <V> OptionalDoubleMapper<I> mapD(ToDoubleFunction<? super O> after) throws NullPointerException {
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

    default <V> OptionalMapper<I, V> flatMap(Function<? super O, Optional<V>> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            return (thisResult != null) ? thisResult.flatMap(after) : Optional.empty();
        };
    }

    default <V> OptionalIntMapper<I> flatMapI(Function<? super O, OptionalInt> after) throws NullPointerException {
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

    default OptionalMapper<I, O> filter(Predicate<O> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final Optional<O> thisResult = apply(i);
            return (thisResult != null) ? thisResult.filter(after) : Optional.empty();
        };
    }

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

    default Predicate<I> hasResult() {
        return i -> {
            final Optional<O> thisResult = this.apply(i);
            return (thisResult != null) && thisResult.isPresent();
        };
    }

    default Predicate<I> hasResultAnd(Predicate<O> test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> {
            final Optional<O> result = apply(i);
            return result != null && result.isPresent() && test.test(result.get());
        };
    }

    default Consumer<I> thenDo(Consumer<Optional<? super O>> consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> consumer.accept(this.apply(i));
    }

    default Consumer<I> thenIfPresent(Consumer<? super O> consumer) throws NullPointerException {
        Objects.requireNonNull(consumer);
        return i -> {
            Optional<O> result = this.apply(i);
            if (result != null) {
                result.ifPresent(consumer);
            }
        };
    }

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