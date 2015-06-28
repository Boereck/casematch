package de.boereck.matcher.helpers;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

import de.boereck.matcher.function.optionalmap.OptionalIntMapper;
import de.boereck.matcher.function.predicate.AdvIntPredicate;
import de.boereck.matcher.function.predicate.AdvPredicate;

/**
 * This class provides static methods and final fields holding references to methods that can be
 * used as predicates in case definitions of matchers for int values. This class is not intended to be
 * instantiated or sub-classed.
 *
 * @author Max Bureck
 */
public final class IntMatchHelpers {

    private IntMatchHelpers() {
        throw new IllegalStateException("Class IntMatchHelpers must not be instantiated");
    }

    /**
     * Returns a predicate that checks if an int value is greater than value {@code compareWith}.
     *
     * @param compareWith value that the returned predicate uses to check if a given int is greater than this value.
     * @return predicate checking if an int value is greater than {@code compareWith}.
     */
    public static AdvIntPredicate gt(int compareWith) {
        return i -> i > compareWith;
    }

    /**
     * Returns a predicate that checks if an int value is greater than the value provided by {@code compareWith}.
     *
     * @param compareWith supplier that provides a value that the returned predicate uses to check if a given int is
     *                    greater than this provided value. This parameter must not be {@code null}.
     * @return predicate checking if an int value is greater than the value provided by {@code compareWith}.
     * @throws NullPointerException will be thrown if {@code compareWith} is {@code null}
     */
    public static AdvIntPredicate gt(IntSupplier compareWith) throws NullPointerException {
        Objects.requireNonNull(compareWith);
        return i -> i > compareWith.getAsInt();
    }

    /**
     * Returns a predicate that checks if an int value is greater than or equal to value {@code compareWith}.
     *
     * @param compareWith value that the returned predicate uses to check if a given int is greater than or equal to this value.
     * @return predicate checking if an int value is greater than or equal to {@code compareWith}.
     */
    public static AdvIntPredicate ge(int compareWith) {
        return i -> i >= compareWith;
    }

    /**
     * Returns a predicate that checks if an int value is greater than or equal to the value provided by {@code compareWith}.
     *
     * @param compareWith supplier that provides a value that the returned predicate uses to check if a given int is
     *                    greater than or equal to this provided value. This parameter must not be {@code null}.
     * @return predicate checking if an int value is greater than or equal to the value provided by {@code compareWith}.
     * @throws NullPointerException will be thrown if {@code compareWith} is {@code null}
     */
    public static AdvIntPredicate ge(IntSupplier compareWith) throws NullPointerException {
        Objects.requireNonNull(compareWith);
        return i -> i >= compareWith.getAsInt();
    }

    /**
     * Returns a predicate that checks if an int value is lower than value {@code compareWith}.
     *
     * @param compareWith value that the returned predicate uses to check if a given int is lower than this value.
     * @return predicate checking if an int value is lower than {@code compareWith}.
     */
    public static AdvIntPredicate lt(int compareWith) {
        return i -> i < compareWith;
    }

    /**
     * Returns a predicate that checks if an int value is lower than the value provided by {@code compareWith}.
     *
     * @param compareWith supplier that provides a value that the returned predicate uses to check if a given int is
     *                    lower than this provided value.
     * @return predicate checking if an int value is lower than the value provided by {@code compareWith}.
     * @throws NullPointerException will be thrown if {@code compareWith} is {@code null}
     */
    public static AdvIntPredicate lt(IntSupplier compareWith) throws NullPointerException {
        Objects.requireNonNull(compareWith);
        return i -> i < compareWith.getAsInt();
    }

    /**
     * Returns a predicate that checks if an int value is lower than or equal to value {@code compareWith}.
     *
     * @param compareWith value that the returned predicate uses to check if a given int is lower than or equal to this value.
     * @return predicate checking if an int value is lower than or equal to {@code compareWith}.
     */
    public static AdvIntPredicate le(int compareWith) {
        return i -> i <= compareWith;
    }

    /**
     * Returns a predicate that checks if an int value is lower than or equal to the value provided by {@code compareWith}.
     *
     * @param compareWith supplier that provides a value that the returned predicate uses to check if a given int is
     *                    lower than or equal to this provided value. This parameter must not be {@code null}.
     * @return predicate checking if an int value is lower than or equal to the value provided by {@code compareWith}.
     * @throws NullPointerException will be thrown if {@code compareWith} is {@code null}
     */
    public static AdvIntPredicate le(IntSupplier compareWith) throws NullPointerException {
        Objects.requireNonNull(compareWith);
        return i -> i <= compareWith.getAsInt();
    }

    /**
     * Holds a predicate checking if an int value is positive.
     */
    public static final AdvIntPredicate positive = i -> i > 0;

    /**
     * Holds a predicate checking if an int value is negative.
     */
    public static final AdvIntPredicate negative = i -> i < 0;

    /**
     * Returns predicate that returns true if the input int is one of
     * the given integers {@code el} or {@code more}.
     *
     * @param el   one element predicate will check if input is equal to it
     * @param more further elements the predicate will check if element
     *             is one of them.
     * @return predicate checking if the input int is either {@code el} or one of {@code more}.
     * @throws NullPointerException is thrown if {@code more} is {@code null}.
     */
    public static AdvIntPredicate oneOf(int el, int... more) throws NullPointerException {
        Objects.requireNonNull(more);
        // make defensive copy
        final int[] ts = Arrays.copyOf(more, more.length + 1);
        ts[more.length] = el;
        // sort copied array, so we can do lookup by binary search
        Arrays.sort(ts);
        // return predicate performing binary search on elements
        // it returns ture if element is in array, false if it does not.
        return input -> Arrays.binarySearch(ts, input) >= 0;
    }

    /**
     * Holds a predicate checking if an int value is even (dividable by 2).
     * Zero is regarded to be zero.
     */
    public static final AdvIntPredicate even = dividableBy(2);

    /**
     * Holds a predicate checking if an int value is odd (not dividable by 2).
     * Zero is not regarded as odd.
     */
    public static final AdvIntPredicate odd = notDividableBy(2);

    /**
     * Returns a predicate checking if an int value is dividable by {@code divisor} without leaving a rest.
     * Zero is considered to be dividable by any value, except for zero. If the returned predicate will be
     * tested with zero, an {@link ArithmeticException} will be thrown.
     *
     * @param divisor int value that is used in the returned predicate to check if a value is dividable by this
     *                value without leaving a rest.
     * @return predicate checking if an input int value is dividable by {@code divisor} without leaving a rest.
     */
    public static AdvIntPredicate dividableBy(int divisor) {
        return i -> (i % divisor) == 0;
    }

    /**
     * Returns a predicate checking if an int value is not dividable by {@code divisor}. Meaning an integer division
     * leaves a rest. Zero is considered to be dividable by any value, except for zero. If the returned predicate will be
     * tested with zero, an {@link ArithmeticException} will be thrown.
     *
     * @param divisor int value that is used in the returned predicate to check if a value is not dividable by this
     *                value (integer division leaves a rest).
     * @return predicate checking if an input int value is not dividable by {@code divisor} (integer division leaves a rest).
     */
    public static AdvIntPredicate notDividableBy(int divisor) {
        return i -> (i % divisor) != 0;
    }

    /**
     * Returns a predicate checking if an int value is dividable by the value provided by {@code divisor} without leaving
     * a rest.
     *
     * @param divisor is used in the returned predicate to check if a value is dividable by the
     *                value provided by {@code divisor} without leaving a rest. This parameter must not be {@code null}.
     * @return predicate checking if an input int value is not dividable by {@code divisor} (integer division leaves a rest).
     * @throws NullPointerException will be thrown if {@code divisor} is {@code null}
     */
    public static AdvIntPredicate dividableBy(IntSupplier divisor) throws NullPointerException {
        Objects.requireNonNull(divisor);
        return i -> (i % divisor.getAsInt()) == 0;
    }

    /**
     * Returns a predicate checking if an int value is not dividable by the value provided by {@code divisor}.
     * Meaning an integer division leaves a rest.
     *
     * @param divisor is used in the returned predicate to check if a value is not dividable by the value provided by
     *                {@code divisor} (integer division leaves a rest). This parameter must not be {@code null}.
     * @return predicate checking if an input int value is not dividable by the value provided by {@code divisor}
     * (integer division leaves a rest).
     * @throws NullPointerException will be thrown if {@code divisor} is {@code null}
     */
    public static AdvIntPredicate notDividableBy(IntSupplier divisor) throws NullPointerException {
        Objects.requireNonNull(divisor);
        return i -> (i % divisor.getAsInt()) != 0;
    }

    /**
     * This method returns a function takes and int and that based on predicate {@code test} returns an optional containing
     * the input int (if {@code test} returned {@code true}) or an empty optional (if {@code test} returned {@code false}).
     *
     * @param test determines if the returned function returns an optional containing the input int or an empty optional.
     *             This parameter must not be {@code null}.
     * @return A function that takes an int and returns an optional holding the input int, if predicate {@code test}
     * returned {@code true} for the input int, or an empty optional if {@code test} returned {@code false} for the
     * input int.
     * @throws NullPointerException will be thrown if {@code test} is {@code null}
     */
    public static IntFunction<OptionalInt> filterInt(IntPredicate test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> test.test(i) ? OptionalInt.of(i) : OptionalInt.empty();
    }

    /**
     * Returns a predicate that checks if an input int is in the closed range from {@code startIncluding} to
     * {@code endIncluding}.
     *
     * @param startIncluding start value. Input may be greater or equal to this.
     * @param endIncluding   end value. Input may be lower or equal to this.
     * @return predicate checking if an input value is in the closed range from {@code startIncluding} to
     * {@code endIncluding}.
     * @throws IllegalArgumentException if {@code startIncluding > endIncluding}.
     */
    public static AdvIntPredicate inClosedRange(int startIncluding, int endIncluding) throws IllegalArgumentException {
        if (startIncluding > endIncluding) {
            throw new IllegalArgumentException("startIncluding must be <= endIncluding");
        }
        return i -> (i >= startIncluding && i <= endIncluding);
    }

    /**
     * Returns a predicate that checks if a given int value is not on a black list of integer values, defined by parameters
     * {@code excluding} and {@code excludingMore}.
     *
     * @param excluding     if input value to returned predicate equals this value, the predicate will return {@code false}
     * @param excludingMore excluding if input value to returned predicate equals any of this int values, the predicate
     *                      will return {@code false}. This parameter must not be {@code null}, but may be an empty array.
     * @return Predicate that returns {@code false} if the input value equals {@code excluding} or values in {@code excludingMore},
     * otherwise it returns {@code true}.
     * @throws NullPointerException if excludingMore is {@code null}.
     */
    public static AdvIntPredicate allExcept(int excluding, int... excludingMore) throws NullPointerException {
        Objects.requireNonNull(excludingMore);
        final int excludingMoreLen = excludingMore.length;
        // if we have just one exclude, we can have a simple check
        if (excludingMoreLen == 0) {
            return i -> i != excluding;
        } else {
            // defensive copy of parameters
            final int[] exclude = Arrays.copyOf(excludingMore, excludingMoreLen + 1);
            // add first parameter to new array, so we have only one array as search source
            exclude[excludingMoreLen] = excluding;
            // sort for faster find using binary search
            Arrays.sort(exclude);
            return i -> (Arrays.binarySearch(exclude, i) < 0);
        }
    }
}
