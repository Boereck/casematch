package de.boereck.matcher.helpers;

import java.util.OptionalDouble;
import java.util.function.DoubleFunction;
import java.util.function.DoubleSupplier;

import de.boereck.matcher.function.predicate.AdvDoublePredicate;

/**
 * This class provides static methods and fields that may help with defining consumers of cases in the found case library,
 * helping defining matches for double values.<br>
 * This class is not intended to be instantiated or sub-classed.
 *
 * @author Max Bureck
 */
public class DoubleMatchHelpers {

    private DoubleMatchHelpers() {
        throw new IllegalStateException("Class DoubleMatchHelpers must not be instantiated");
    }

    /**
     * Reference to a function taking a double value and mapping it to an OptionalDouble. If the input to the function
     * is NaN, then the function will return an empty optional, otherwise it will return an optional holding the input value.
     */
    public static final DoubleFunction<OptionalDouble> filterNaN = in -> Double.isNaN(in) ? OptionalDouble.empty() : OptionalDouble.of(in);

    /**
     * Reference to a function taking a double value and mapping it to an OptionalDouble. If the input to the function
     * is positive or negative infinity, then the function will return an empty optional, otherwise it will return an
     * optional holding the input value.
     */
    public static final DoubleFunction<OptionalDouble> filterInfinity = in -> Double.isNaN(in) ? OptionalDouble.empty() : OptionalDouble.of(in);

    // TODO ranges of double

    /**
     * Shortcut for {@code Double::isFinite}.
     */
    public static final AdvDoublePredicate finite = Double::isFinite;

    /**
     * Predicate checking if a double value is NaN.
     */
    public static final AdvDoublePredicate isNaN = d -> Double.isNaN(d); // method reference did not work in IntelliJ

    /**
     * Predicate checking if a double value is not NaN.
     */
    public static final AdvDoublePredicate notNaN = isNaN.negate();

    /**
     * Returns a predicate checking if an input double value is equal to the given value {@code val}.
     * Since calculations with double values have a limited precision, the check for equality
     * should be done by checking if the two values are smaller than a given difference. In this
     * case it is checked if the difference between {@code val} and an input value is smaller than
     * {@code eps}.
     * @param val value that input values will be compared with.
     * @param eps epsilon value by which the two compared values may differ and still be
     *            considered equal.
     * @return function checking if an input double value is equal to value {@code val} (by checking
     *         if the difference is smaller than {@code eps}).
     */
    public static AdvDoublePredicate eq(double val, double eps) {
        return d -> Math.abs(d - val) < eps;
    }

    /**
     * Returns a predicate checking if an input double value is lower than the given value {@code compareTo}.
     * @param compareTo value that input values will be compared with.
     * @return function checking if an input double value is lower than the given value {@code compareTo}.
     */
    public static AdvDoublePredicate lt(double compareTo) {
        return d -> d < compareTo;
    }

    /**
     * Returns a predicate checking if an input double value is lower than the value provided by {@code compareTo}.
     * @param compareTo supplier, that's provided value that will be compared with the input value.
     * @return function checking if an input double value is lower than the value provided by supplier {@code compareTo}.
     */
    public static AdvDoublePredicate lt(DoubleSupplier compareTo) {
        return d -> d < compareTo.getAsDouble();
    }

    /**
     * Returns a predicate checking if an input double value is greater than the given value {@code compareTo}.
     * @param compareTo value that input values will be compared with.
     * @return function checking if an input double value is greater than the given value {@code compareTo}.
     */
    public static AdvDoublePredicate gt(double compareTo) {
        return d -> d > compareTo;
    }

    /**
     * Returns a predicate checking if an input double value is greater than the value provided by {@code compareTo}.
     * @param compareTo supplier, that's provided value that will be compared with the input value.
     * @return function checking if an input double value is greater than the value provided by supplier {@code compareTo}.
     */
    public static AdvDoublePredicate gt(DoubleSupplier compareTo) {
        return d -> d > compareTo.getAsDouble();
    }

    /**
     * If the argument is NaN or less than zero, this method will return an empty {@link java.util.OptionalDouble}, otherwise it will
     * return the square root of the given value wrapped into an OptionalDouble.
     *
     * @param d value to be square rooted, if not NaN or &lt; 0.
     * @return Optional either empty or s
     */
    public static OptionalDouble validSqrt(double d) {
        return filterNaN.apply(Math.sqrt(d));
    }

    /**
     * Reference to {@link DoubleMatchHelpers#validSqrt(double)}.
     */
    public static final DoubleFunction<OptionalDouble> validSqrt = DoubleMatchHelpers::validSqrt;
}
