package de.boereck.matcher.helpers;


import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;
import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.function.testable.TestableToLongFunction;
import de.boereck.matcher.helpers.found.Found;
import de.boereck.matcher.helpers.found.FoundNone;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Provides static helper functions for defining matches in CaseMatchers based on array values.
 * <p>
 * This class is not intended to be instantiated or sub-classed.
 * </p>
 *
 * @author Max Bureck
 */
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

    /**
     * This method traverses the given array {@code arr} and checks how many elements in that array
     * are tested positive with the given predicate. Depending on the result, a subclass of
     * {@link Found} will be returned. This will either be {@link de.boereck.matcher.helpers.found.FoundAll},
     * {@link de.boereck.matcher.helpers.found.FoundNone} or {@link de.boereck.matcher.helpers.found.FoundSome}.
     * Be aware that under the covers java stream APIs may be used.
     *
     * @param arr   array that will be traversed and elements checked for predicate {@code p}.
     * @param p   Predicate, every element from {@code arr} is checked with. Must not be {@code null}.
     * @param <T> Type of elements of {@code arr}.
     * @return Found instance, based on how many elements in {@code arr} pass predicate {@code p}.
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static <T> Found findCount(T[] arr, Predicate<? super T> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return $(arr).collect(CollectionMatchHelpers.findCollector(p));
    }

    /**
     * Returns a function that counts how many elements of an input array are tested positive with the given
     * predicate {@code p}. If the input array is {@code null}, the returned count will be 0.
     * @param p used to check how many elements in the input array match this predicate
     * @param <I> Type of elements in the input collection
     * @return function that counts how many elements of an input collection are tested positive with the given
     * predicate {@code p}. If the input collection is {@code null}, the returned count will be 0.
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static <I> TestableToLongFunction<I[]> countInArray(Predicate<? super I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return arr -> arr == null ? 0 : $(arr).filter(p).count();
    }

    /**
     * This function returns a function that tests if and how many elements in an array match
     * the given predicate {@code p}.
     * @param p Predicate that is used to check for elements in a given array. Must not be {@code null}.
     * @param <I> Type of elements in input collection
     * @return Function counting the elements of an input collection that are tested
     * positive with the given predicate {@code p}.
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public static <I> TestableFunction<I[], Found> findCountInArray(Predicate<? super I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return c -> findCount(c, p);
    }

    /**
     * This method returns a function that returns an optional of {@code Found}, that holds a value, if elements
     * were found, and an empty optional, if no elements were found that match predicate {@code p}.
     * @param p used to check how many elements match this predicate. Must not be {@code null}.
     * @param <I> Type of elements of input collection to be checked.
     * @return Function that returns an optional of {@code Found}, that holds a value, if elements
     * were found, and an empty optional, if no elements were found that match predicate {@code p}.
     * @throws NullPointerException if {@code p} is {@code null}.
     */
    public static  <I> OptionalMapper<I[], Found> findCountExistingInArray(Predicate<? super I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return findCountInArray(p).filter(f -> !(f instanceof FoundNone));
    }

    /**
     * Returns a predicate that checks if an array is not {@code null} and contains an element that matches the predicate
     * {@code p}.
     *
     * @param p   predicate that will be used to check if an element of a given array
     *            matches. Must not be {@code null}.
     * @param <I> Types of elements in the array to be checked by the returned predicate.
     * @return Predicate that checks if an array is not {@code null} and contains an element matching the predicate {@code p}.
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public static <I> AdvPredicate<I[]> existsInArray(Predicate<? super I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return c -> c != null && $(c).anyMatch(p);
    }

    /**
     * Returns a predicate that checks if an array is not {@code null} and all its elements found the predicate
     * {@code p}.
     *
     * @param p   predicate that will be used to check if all elements of a given array
     *            matches. Must not be {@code null}
     * @param <I> Types of elements in the array to be checked by the returned predicate.
     * @return Predicate that checks if an array is not {@code null} and contains an element matching the predicate {@code p}.
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public static <I> AdvPredicate<I[]> forAllInArray(Predicate<? super I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return c -> c != null && $(c).allMatch(p);
    }

    /**
     * Returns a predicate checking if the given {@code key} element exists in an array of elements
     * ordered ascending by the ordering defined in comparator {@code cmp}. The predicate will return
     * {@code false} if the input array is {@code null}.
     * @param key the element that is searched in input arrays.
     * @param cmp the comparator defining the order on type {@code I}.
     * @param <I> The type of the input array.
     * @return Predicate checking if element {@code key} is element in input arrays, ordered by the ordering defined
     * by {@code cmp}.
     * @throws NullPointerException if {@code cmp} is {@code null}.
     */
    public static <I> Predicate<I[]> containsOrdered(I key, Comparator<? super I> cmp) throws NullPointerException {
        Objects.requireNonNull(cmp);
        return arr -> arr != null && Arrays.binarySearch(arr, key, cmp) >= 0;
    }

    /**
     * Returns a predicate checking if the given {@code key} element exists in an array of elements
     * ordered ascending by the natural ordering of type {@code I}. The predicate will return
     * {@code false} if the input array is {@code null}.
     * @param key the element that is searched in input arrays.
     * @param <I> The type of the input array.
     * @return Predicate checking if element {@code key} is element in input arrays, ordered by the natural ordering
     * of type {@code I}.
     */
    public static <I> Predicate<I[]> containsOrdered(I key) {
        return arr -> arr != null && Arrays.binarySearch(arr, key) >= 0;
    }

    /**
     * Returns a function that checks if the input object is an array of type {@code O}, if so
     * it will return an optional with the input object casted to an array of {@code O}, otherwise
     * an empty optional will be returned.
     * @param clazz element type of the array
     * @param <O> Element type of array
     * @return function that checks if the input object is an array of type {@code O}, if so
     * it will return an optional with the input object casted to an array of {@code O}, otherwise
     * an empty optional will be returned.
     * @throws NullPointerException if clazz is {@code null}
     */
    @SuppressWarnings("unchecked") // we know the cast is safe, before we checked type first
    public static <O> OptionalMapper<Object, O[]> toArrayOf(Class<O> clazz) throws NullPointerException {
        Objects.requireNonNull(clazz);
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

    /**
     * Returns a function that will check if an input object is type of array of the type
     * defined by parameter {@code clazz}.
     * @param clazz element type of the array
     * @param <O> Element type of array
     * @return function that will check if an input object is type of array of the type
     * defined by parameter {@code clazz}.
     * @throws NullPointerException if clazz is {@code null}
     */
    public static <O> TypeCheck<Object,O[]> isArrayOf(Class<O> clazz) throws NullPointerException {
        Objects.requireNonNull(clazz);
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
