package de.boereck.matcher.function.optionalmap;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;

@FunctionalInterface
public interface OptionalLongMapper<I> extends Function<I, OptionalLong> {

    default <V> OptionalMapper<I, V> map(LongFunction<? extends V> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult.isPresent()) {
                final V afterResult = after.apply(thisResult.getAsLong());
                return Optional.ofNullable(afterResult);
            } else {
                return Optional.empty();
            }
        };
    }

    default OptionalIntMapper<I> mapI(LongToIntFunction after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult.isPresent()) {
                final int afterResult = after.applyAsInt(thisResult.getAsLong());
                return OptionalInt.of(afterResult);
            } else {
                return OptionalInt.empty();
            }
        };
    }

    default OptionalLongMapper<I> mapL(LongUnaryOperator after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult.isPresent()) {
                final long afterResult = after.applyAsLong(thisResult.getAsLong());
                return OptionalLong.of(afterResult);
            } else {
                return thisResult;
            }
        };
    }

    default OptionalDoubleMapper<I> mapD(LongToDoubleFunction after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult.isPresent()) {
                final double afterResult = after.applyAsDouble(thisResult.getAsLong());
                return OptionalDouble.of(afterResult);
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    default <V> OptionalMapper<I, V> flatMap(LongFunction<Optional<V>> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult.isPresent()) {
                return after.apply(thisResult.getAsLong());
            } else {
                return Optional.empty();
            }
        };
    }

    default OptionalIntMapper<I> flatMapI(LongFunction<OptionalInt> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult.isPresent()) {
                return after.apply(thisResult.getAsLong());
            } else {
                return OptionalInt.empty();
            }
        };
    }

    default OptionalLongMapper<I> flatMapL(LongFunction<OptionalLong> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult.isPresent()) {
                return after.apply(thisResult.getAsLong());
            } else {
                return thisResult;
            }
        };
    }

    default OptionalDoubleMapper<I> flatMapD(LongFunction<OptionalDouble> after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult.isPresent()) {
                return after.apply(thisResult.getAsLong());
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    default OptionalLongMapper<I> filter(LongPredicate after) throws NullPointerException {
        Objects.requireNonNull(after);
        return (I i) -> {
            final OptionalLong thisResult = apply(i);
            if (thisResult.isPresent() && after.test(thisResult.getAsLong())) {
                return thisResult;
            } else {
                return OptionalLong.empty();
            }
        };
    }

    default Predicate<I> hasResult() {
        return i -> this.apply(i).isPresent();
    }

    default Predicate<I> hasResultAnd(LongPredicate test) {
        return i -> {
            final OptionalLong result = apply(i);
            return result.isPresent() && test.test(result.getAsLong());
        };
    }
}
