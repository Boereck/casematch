package de.boereck.matcher.function.optionalmap;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

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

    default Predicate<I> hasResultAnd(DoublePredicate test) {
        return i -> {
            final OptionalDouble result = apply(i);
            return result.isPresent() && test.test(result.getAsDouble());
        };
    }
}
