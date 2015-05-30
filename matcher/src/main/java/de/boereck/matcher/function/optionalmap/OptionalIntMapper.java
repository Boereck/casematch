package de.boereck.matcher.function.optionalmap;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

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

    default Predicate<I> hasResultAnd(IntPredicate test) {
        return i -> {
            final OptionalInt result = apply(i);
            return result.isPresent() && test.test(result.getAsInt());
        };
    }
}
