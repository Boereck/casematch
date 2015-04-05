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

/**
 * This class provides static methods and final fields holding references to methods that can be
 * used as predicates in case definitions of matchers for int values.
 */
public final class IntMatchHelpers {

    private IntMatchHelpers() {
        throw new IllegalStateException("Class IntMatchHelpers must not be instantiated");
    }

    /**
     * Returns a predicate that checks if an int value is greater than value {@code compareWith}.
     * @param compareWith value that the returned predicate uses to check if a given int is greater than this value.
     * @return predicate checking if an int value is greater than {@code compareWith}.
     */
    public static AdvIntPredicate gt(int compareWith) {
        return i -> i > compareWith;
    }

    /**
     * Returns a predicate that checks if an int value is greater than the value provided by {@code compareWith}.
     * @param compareWith supplier that provides a value that the returned predicate uses to check if a given int is
     *                    greater than this provided value. This parameter must not be {@code null}.
     * @return predicate checking if an int value is greater than the value provided by {@code compareWith}.
     */
    public static AdvIntPredicate gt(IntSupplier compareWith) {
        Objects.requireNonNull(compareWith);
        return i -> i > compareWith.getAsInt();
    }

    /**
     * Returns a predicate that checks if an int value is greater than or equal to value {@code compareWith}.
     * @param compareWith value that the returned predicate uses to check if a given int is greater than or equal to this value.
     * @return predicate checking if an int value is greater than or equal to {@code compareWith}.
     */
    public static AdvIntPredicate ge(int compareWith) {
        return i -> i >= compareWith;
    }

    /**
     * Returns a predicate that checks if an int value is greater than or equal to the value provided by {@code compareWith}.
     * @param compareWith supplier that provides a value that the returned predicate uses to check if a given int is
     *                    greater than or equal to this provided value. This parameter must not be {@code null}.
     * @return predicate checking if an int value is greater than or equal to the value provided by {@code compareWith}.
     */
    public static AdvIntPredicate ge(IntSupplier compareWith) {
        Objects.requireNonNull(compareWith);
        return i -> i >= compareWith.getAsInt();
    }

    /**
     * Returns a predicate that checks if an int value is lower than value {@code compareWith}.
     * @param compareWith value that the returned predicate uses to check if a given int is lower than this value.
     * @return predicate checking if an int value is lower than {@code compareWith}.
     */
    public static AdvIntPredicate lt(int compareWith) {
        return i -> i < compareWith;
    }

    /**
     * Returns a predicate that checks if an int value is lower than the value provided by {@code compareWith}.
     * @param compareWith supplier that provides a value that the returned predicate uses to check if a given int is
     *                    lower than this provided value.
     * @return predicate checking if an int value is lower than the value provided by {@code compareWith}.
     */
    public static AdvIntPredicate lt(IntSupplier compareWith) {
        Objects.requireNonNull(compareWith);
        return i -> i < compareWith.getAsInt();
    }

    /**
     * Returns a predicate that checks if an int value is lower than or equal to value {@code compareWith}.
     * @param compareWith value that the returned predicate uses to check if a given int is lower than or equal to this value.
     * @return predicate checking if an int value is lower than or equal to {@code compareWith}.
     */
    public static AdvIntPredicate le(int compareWith) {
        return i -> i <= compareWith;
    }

    /**
     * Returns a predicate that checks if an int value is lower than or equal to the value provided by {@code compareWith}.
     * @param compareWith supplier that provides a value that the returned predicate uses to check if a given int is
     *                    lower than or equal to this provided value. This parameter must not be {@code null}.
     * @return predicate checking if an int value is lower than or equal to the value provided by {@code compareWith}.
     */
    public static AdvIntPredicate le(IntSupplier compareWith) {
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
     * Holds a predicate checking if an int value is even (dividable by 2).
     */
    public static final AdvIntPredicate even = dividableBy(2);

    /**
     * Holds a predicate checking if an int value is odd (not dividable by 2).
     */
    public static final AdvIntPredicate odd = notDividableBy(2);

    /**
     * Returns a predicate checking if an int value is dividable by {@code divisor} without leaving a rest.
     * @param divisor int value that is used in the returned predicate to check if a value is dividable by this
     *                value without leaving a rest.
     * @return predicate checking if an input int value is dividable by {@code divisor} without leaving a rest.
     */
    public static AdvIntPredicate dividableBy(int divisor) {
        return i -> (i % divisor) == 0;
    }

    /**
     * Returns a predicate checking if an int value is not dividable by {@code divisor}. Meaning an integer division
     * leaves a rest.
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
     * @param divisor is used in the returned predicate to check if a value is dividable by the
     *                value provided by {@code divisor} without leaving a rest. This parameter must not be {@code null}.
     * @return predicate checking if an input int value is not dividable by {@code divisor} (integer division leaves a rest).
     */
    public static AdvIntPredicate dividableBy(IntSupplier divisor) {
        Objects.requireNonNull(divisor);
        return i -> (i % divisor.getAsInt()) == 0;
    }

    /**
     * Returns a predicate checking if an int value is not dividable by the value provided by {@code divisor}.
     * Meaning an integer division leaves a rest.
     * @param divisor is used in the returned predicate to check if a value is not dividable by the value provided by
     *                {@code divisor} (integer division leaves a rest). This parameter must not be {@code null}.
     * @return predicate checking if an input int value is not dividable by the value provided by {@code divisor}
     *         (integer division leaves a rest).
     */
    public static AdvIntPredicate notDividableBy(IntSupplier divisor) {
        Objects.requireNonNull(divisor);
        return i -> (i % divisor.getAsInt()) != 0;
    }

    /**
     * This method returns a function takes and int and that based on predicate {@code test} returns an optional containing
     * the input int (if {@code test} returned {@code true}) or an empty optional (if {@code test} returned {@code false}).
     * @param test determines if the returned function returns an optional containing the input int or an empty optional
     * @return A function that takes an int and returns an optional holding the input int, if predicate {@code test}
     *  returned {@code true} for the input int, or an empty optional if {@code test} returned {@code false} for the
     *  input int.
     */
    public static IntFunction<OptionalInt> selectInt(IntPredicate test) {
        Objects.requireNonNull(test);
        return i -> test.test(i) ? OptionalInt.of(i) : OptionalInt.empty();
    }

    /**
     * This method returns a function that filters the output of a given function mapping an int to an OptionalInt.
     * This means that based on the predicate {@code p} will be used to test the output of {@code orig}. If the
     * output OptionalInt does not contain a value, the result will be this optional. If the result does contain an
     * int value, it will be tested with {@code p}, if the predicate returns {@code false}, an empty optional will be
     * returned, otherwise the optional holding the value will be returned.
     * @param orig function mapping an int to an OptionalInt. The result of this function will be filtered.
     *             Must not be {@code null}.
     * @param p used to filter the output of {@code orig}. Must not be {@code null}.
     * @return A function taking an int, calls {@code orig} with the int, filters the returned output using {@code p}
     *         and returns the result of this filtering.
     */
    public static IntFunction<OptionalInt> filterInt(IntFunction<OptionalInt> orig, IntPredicate p) {
        Objects.requireNonNull(orig);
        Objects.requireNonNull(p);
        return (int i) -> {
            OptionalInt aResult = orig.apply(i);
            if(aResult.isPresent()) {
                final int val = aResult.getAsInt();
                if(p.test(val)){
                    return aResult;
                } else {
                    return OptionalInt.empty();
                }
            } else {
                return aResult;
            }
        };
    }

    /**
     * Returns a function mapping from int to OptionalInt. This method will take the input int and use
     * function {@code a} on this. If the resulting optional is empty, it will be returned. If the resulting
     * optional is not empty, operation {@code toApply} will be called in the int in the returned optional and the
     * result will be wrapped into an OptionalInt and returned.
     * @param a
     * @param toApply
     * @return
     */
    public static IntFunction<OptionalInt> compose(IntFunction<OptionalInt> a, IntUnaryOperator toApply) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(toApply);
        return (int i) -> {
            OptionalInt aResult = a.apply(i);
            if(aResult.isPresent()) {
                final int val = aResult.getAsInt();
                return OptionalInt.of(toApply.applyAsInt(val));
            } else {
                return aResult;
            }
        };
    }
    
    /**
     * Returns a predicate that checks if an input int is in the closed range from {@code startIncluding} to
     * {@code endIncluding}.
     * @param startIncluding start value. Input may be greater or equal to this.
     * @param endIncluding end value. Input may be lower or equal to this.
     * @return predicate checking if an input value is in the closed range from {@code startIncluding} to
     *         {@code endIncluding}.
     */
    public static AdvIntPredicate inClosedRange(int startIncluding, int endIncluding) {
        if (startIncluding > endIncluding) {
            throw new InvalidParameterException("startIncluding must be <= endIncluding");
        }
        return i -> (i >= startIncluding && i <= endIncluding);
    }

    /**
     * Returns a predicate that checks if a given int value is not on a black list of integer values, defined by parameters
     * {@code excluding} and {@code excludingMore}.
     * @param excluding if input value to returned predicate equals this value, the predicate will return {@code false}
     * @param excludingMore excluding if input value to returned predicate equals any of this int values, the predicate
     *                      will return {@code false}. This parameter must not be {@code null}.
     * @return Predicate that returns {@code false} if the input value equals {@code excluding} or values in {@code excludingMore},
     *         otherwise it returns {@code true}.
     */
    public static AdvIntPredicate allExcept(int excluding, int... excludingMore) {
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
