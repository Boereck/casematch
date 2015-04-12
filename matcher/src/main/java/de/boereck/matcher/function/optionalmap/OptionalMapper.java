package de.boereck.matcher.function.optionalmap;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import de.boereck.matcher.NoResultCaseMatcher;
import de.boereck.matcher.ResultCaseMatcher;
import de.boereck.matcher.helpers.MatchHelpers;

/**
 * Signature: <code> I -&gt; Optional&lt;O&gt; </code>  </br>
 * Function mapping an input of type I to an Optional&lt;O&gt;.
 * This interface is especially useful when working with {@link NoResultCaseMatcher#caseObj(java.util.function.Function, java.util.function.Consumer)}
 * or {@link ResultCaseMatcher#caseOf(java.util.function.BooleanSupplier, java.util.function.Function)}.
 *
 * @param <I> type of input to function
 * @param <O> type of optional output of function
 * @author Max Bureck
 * @see MatchHelpers#optional(java.util.function.Function)
 * @see MatchHelpers#cast(Class)
 */
@FunctionalInterface
public interface OptionalMapper<I, O> extends Function<I, Optional<O>> {

    default <V> OptionalMapper<I, V> map(Function<? super O, ? extends V> after) {
        Objects.requireNonNull(after);
        return (I i) -> apply(i).map(after);
    }

    default <V> OptionalIntMapper<I> mapI(ToIntFunction<? super O> after) {
        Objects.requireNonNull(after);
        return (I i) -> {
            Optional<O> thisResult = apply(i);
            if (thisResult.isPresent()) {
                final O val = thisResult.get();
                return OptionalInt.of(after.applyAsInt(val));
            } else {
                return OptionalInt.empty();
            }
        };
    }

    default <V> OptionalLongMapper<I> mapL(ToLongFunction<? super O> after) {
        Objects.requireNonNull(after);
        return (I i) -> {
            Optional<O> thisResult = apply(i);
            if (thisResult.isPresent()) {
                final O val = thisResult.get();
                return OptionalLong.of(after.applyAsLong(val));
            } else {
                return OptionalLong.empty();
            }
        };
    }

    default <V> OptionalDoubleMapper<I> mapD(ToDoubleFunction<? super O> after) {
        Objects.requireNonNull(after);
        return (I i) -> {
            Optional<O> thisResult = apply(i);
            if (thisResult.isPresent()) {
                final O val = thisResult.get();
                return OptionalDouble.of(after.applyAsDouble(val));
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    default <V> OptionalMapper<I, V> flatMap(Function<? super O, Optional<V>> after) {
        Objects.requireNonNull(after);
        return (I i) -> apply(i).flatMap(after);
    }

    default <V> OptionalIntMapper<I> flatMapI(Function<? super O, OptionalInt> after) {
        Objects.requireNonNull(after);
        return (I i) -> {
            Optional<O> thisResult = apply(i);
            if (thisResult.isPresent()) {
                final O val = thisResult.get();
                return after.apply(val);
            } else {
                return OptionalInt.empty();
            }
        };
    }

    default <V> OptionalLongMapper<I> flatMapL(Function<? super O, OptionalLong> after) {
        Objects.requireNonNull(after);
        return (I i) -> {
            Optional<O> thisResult = apply(i);
            if (thisResult.isPresent()) {
                final O val = thisResult.get();
                return after.apply(val);
            } else {
                return OptionalLong.empty();
            }
        };
    }

    default <V> OptionalDoubleMapper<I> flatMapD(Function<? super O, OptionalDouble> after) {
        Objects.requireNonNull(after);
        return (I i) -> {
            Optional<O> thisResult = apply(i);
            if (thisResult.isPresent()) {
                final O val = thisResult.get();
                return after.apply(val);
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    default OptionalMapper<I, O> filter(Predicate<O> after) {
        Objects.requireNonNull(after);
        return (I i) -> apply(i).filter(after);
    }

    default Predicate<I> hasResult() {
        return i -> this.apply(i).isPresent();
    }

    default Predicate<I> hasResultAnd(Predicate<O> test) {
        return i -> {
            final Optional<O> result = apply(i);
            return result.isPresent() && test.test(result.get());
        };
    }
}