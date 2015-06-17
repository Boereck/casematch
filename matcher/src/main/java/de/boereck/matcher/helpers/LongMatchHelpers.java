package de.boereck.matcher.helpers;

import de.boereck.matcher.function.predicate.AdvIntPredicate;
import de.boereck.matcher.function.predicate.AdvLongPredicate;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;

/**
 * This class provides static methods and final fields holding references to methods that can be
 * used as predicates in case definitions of matchers for long values. This class is not intended to be
 * instantiated or sub-classed.
 *
 * @author Max Bureck
 */
public final class LongMatchHelpers {

    private LongMatchHelpers() {
        throw new IllegalStateException("Class IntMatchHelpers must not be instantiated");
    }

    /**
     * Returns a predicate that checks if a long value is greater than value {@code compareWith}.
     *
     * @param compareWith value that the returned predicate uses to check if a given long is greater than this value.
     * @return predicate checking if a long value is greater than {@code compareWith}.
     */
    public static AdvLongPredicate gt(long compareWith) {
        return l -> l > compareWith;
    }

    /**
     * Returns a predicate that checks if a long value is greater than the value provided by {@code compareWith}.
     *
     * @param compareWith supplier that provides a value that the returned predicate uses to check if a given long is
     *                    greater than this provided value. This parameter must not be {@code null}.
     * @return predicate checking if an int value is greater than the value provided by {@code compareWith}.
     * @throws NullPointerException will be thrown if {@code compareWith} is {@code null}.
     */
    public static AdvLongPredicate gt(LongSupplier compareWith) throws NullPointerException {
        Objects.nonNull(compareWith);
        return l -> l > compareWith.getAsLong();
    }

    /**
     * Returns a predicate that checks if a long value is lower than value {@code compareWith}.
     *
     * @param compareWith value that the returned predicate uses to check if a given long is lower than this value.
     * @return predicate checking if a long value is lower than {@code compareWith}.
     */
    public static AdvLongPredicate lt(long compareWith) {
        return l -> l < compareWith;
    }

    /**
     * Returns a predicate that checks if a long value is lower than the value provided by {@code compareWith}.
     *
     * @param compareWith supplier that provides a value that the returned predicate uses to check if a given long is
     *                    lower than this provided value.
     * @return predicate checking if a long value is lower than the value provided by {@code compareWith}.
     * @throws NullPointerException will be thrown if {@code compareWith} is {@code null}
     */
    public static AdvLongPredicate lt(LongSupplier compareWith) throws NullPointerException {
        Objects.requireNonNull(compareWith);
        return i -> i < compareWith.getAsLong();
    }

    /**
     * Returns predicate that returns true if the input long is one of
     * the given longs {@code el} or {@code more}.
     * @param el one element predicate will check if input is equal to it
     * @param more further elements the predicate will check if element
     *             is one of them.
     * @return predicate checking if the input long is either {@code el} or one of {@code more}.
     * @throws NullPointerException will be thrown if {@code more} is {@code null}.
     */
    public static AdvLongPredicate oneOfL(long el, long... more) throws NullPointerException {
        Objects.requireNonNull(more);
        // make defensive copy
        final long[] ts = Arrays.copyOf(more, more.length + 1);
        ts[more.length] = el;
        // sort copied array, so we can do lookup by binary search
        Arrays.sort(ts);
        // return predicate performing binary search on elements
        // it returns ture if element is in array, false if it does not.
        return input -> Arrays.binarySearch(ts,input) >= 0;
    }

    /**
     * Holds a predicate checking if a long value is positive.
     */
    public static final AdvLongPredicate positive = l -> l > 0;

    /**
     * Holds a predicate checking if a long value is negative.
     */
    public static final AdvLongPredicate negative = l -> l < 0;

    /**
     * Returns a predicate checking if a long value is dividable by {@code divisor} without leaving a rest.
     *
     * @param divisor long value that is used in the returned predicate to check if a value is dividable by this
     *                value without leaving a rest.
     * @return predicate checking if an input long value is dividable by {@code divisor} without leaving a rest.
     */
    public static AdvLongPredicate dividableBy(long divisor) {
        return l -> (l % divisor) == 0;
    }

    /**
     * Returns a predicate checking if a long value is not dividable by {@code divisor}. Meaning an long division
     * leaves a rest.
     *
     * @param divisor int value that is used in the returned predicate to check if a value is not dividable by this
     *                value (long division leaves a rest).
     * @return predicate checking if an input long value is not dividable by {@code divisor} (long division leaves a rest).
     */
    public static AdvLongPredicate notDividableBy(long divisor) {
        return i -> (i % divisor) != 0;
    }

    /**
     * Holds a predicate checking if a long value is even (dividable by 2).
     */
    public static final AdvLongPredicate evenLong = dividableBy(2L);

    /**
     * Holds a predicate checking if a long value is odd (not dividable by 2).
     */
    public static final AdvLongPredicate oddLong = notDividableBy(2L);

    /**
     * Returns a predicate that checks if an input long is in the closed range from {@code startIncluding} to
     * {@code endIncluding}.
     *
     * @param startIncluding start value. Input may be greater or equal to this.
     * @param endIncluding   end value. Input may be lower or equal to this.
     * @return predicate checking if an input value is in the closed range from {@code startIncluding} to
     * {@code endIncluding}.
     * @throws IllegalArgumentException if {@code startIncluding > endIncluding}.
     */
    public static AdvLongPredicate inClosedRange(long startIncluding, long endIncluding) throws IllegalArgumentException {
        if (startIncluding > endIncluding) {
            throw new IllegalArgumentException("startIncluding must be <= endIncluding");
        }
        return l -> (l >= startIncluding && l <= endIncluding);
    }

    /**
     * Returns a predicate that checks if a given long value is not on a black list of long values, defined by parameters
     * {@code excluding} and {@code excludingMore}.
     *
     * @param excluding     if input value to returned predicate equals this value, the predicate will return {@code false}
     * @param excludingMore excluding if input value to returned predicate equals any of this int values, the predicate
     *                      will return {@code false}. This parameter must not be {@code null}, but may be an empty array.
     * @return Predicate that returns {@code false} if the input value equals {@code excluding} or values in {@code excludingMore},
     * otherwise it returns {@code true}.
     * @throws NullPointerException if excludingMore is {@code null}.
     */
    public static AdvLongPredicate allExcept(long excluding, long... excludingMore) throws NullPointerException  {
        Objects.requireNonNull(excluding);
        final int excludingMoreLen = excludingMore.length;
        // if we have just one exclude, we can have a simple check
        if (excludingMoreLen == 0) {
            return l -> l != excluding;
        } else {
            // defensive copy of parameters
            final long[] exclude = Arrays.copyOf(excludingMore, excludingMoreLen + 1);
            // add first parameter to new array, so we have only one array as search source
            exclude[excludingMoreLen] = excluding;
            // sort for faster find using binary search
            Arrays.sort(exclude);
            return i -> (Arrays.binarySearch(exclude, i) < 0);
        }
    }
}
