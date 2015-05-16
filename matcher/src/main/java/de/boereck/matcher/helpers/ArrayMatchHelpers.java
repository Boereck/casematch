package de.boereck.matcher.helpers;


import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ArrayMatchHelpers {

    private ArrayMatchHelpers() {
        throw new IllegalStateException();
    }

    public static final Predicate<Object> isArray = o -> (o == null) ? false : o.getClass().isArray();

    public static <T> Stream<T> $(T[] arr) {
        if(arr == null) {
            return Stream.empty();
        }
        return Arrays.stream(arr);
    }

    public static IntStream $(int[] arr) {
        if(arr == null) {
            return IntStream.empty();
        }
        return Arrays.stream(arr);
    }

    public static LongStream $(long[] arr) {
        if(arr == null) {
            return LongStream.empty();
        }
        return Arrays.stream(arr);
    }

    public static DoubleStream $(double[] arr) {
        if(arr == null) {
            return DoubleStream.empty();
        }
        return Arrays.stream(arr);
    }

    public static <I, O> OptionalMapper<I, O[]> toArrayOf(Class<O> clazz) {
        return i -> {
            if (i == null) {
                return Optional.empty();
            } // else
            if (i instanceof Object[]) {
                final Class<?> arrayType = i.getClass().getComponentType();
                if (clazz.isAssignableFrom(arrayType)) {
                    final O[] out = (O[]) i;
                    return Optional.of(out);
                } else {
                    return Optional.empty();
                }
            } //else
            return Optional.empty();
        };
    }

    public static <I,O> TypeCheck<I,O[]> isArrayOf(Class<O> clazz) {
        return i -> {
            if (i == null) {
                return false;
            } // else
            if (i instanceof Object[]) {
                final Class<?> arrayType = i.getClass().getComponentType();
                if (clazz.isAssignableFrom(arrayType)) {
                    return true;
                } else {
                    return false;
                }
            } //else
            return false;
        };
    }
}
