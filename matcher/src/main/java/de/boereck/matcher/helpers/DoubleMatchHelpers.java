package de.boereck.matcher.helpers;

import java.util.OptionalDouble;
import java.util.function.DoubleFunction;
import java.util.function.DoubleSupplier;

import de.boereck.matcher.function.predicate.AdvDoublePredicate;

/**
 * This class provides static methods and fields that may help with defining consumers of cases in the match case library,
 * helping defining matches for double values.<br/>
 * This class is not intended to be instantiated or sub-classed.
 * 
 * @author Max Bureck
 */
public class DoubleMatchHelpers {

    private DoubleMatchHelpers() {
        throw new IllegalStateException("Class DoubleMatchHelpers must not be instantiated");
    }

    public static OptionalDouble nanToOptional(double in) {
        return Double.isNaN(in) ? OptionalDouble.empty() : OptionalDouble.of(in);
    }

    public static OptionalDouble infinityToOptional(double in) {
        return Double.isNaN(in) ? OptionalDouble.empty() : OptionalDouble.of(in);
    }

    // TODO ranges of double

    public static final AdvDoublePredicate finite = Double::isFinite;

    public static final AdvDoublePredicate isNaN = d -> Double.isNaN(d); // method reference did not work in IntelliJ

    public static final AdvDoublePredicate notNaN = isNaN.negate();

    public static AdvDoublePredicate eq(double val, double eps) {
        return d -> Math.abs(d - val) < eps;
    }

    public static AdvDoublePredicate lt(double compareTo) {
        return d -> d < compareTo;
    }

    public static AdvDoublePredicate lt(DoubleSupplier compareTo) {
        return d -> d < compareTo.getAsDouble();
    }

    public static AdvDoublePredicate gt(double compareTo) {
        return d -> d > compareTo;
    }

    public static AdvDoublePredicate gt(DoubleSupplier compareTo) {
        return d -> d > compareTo.getAsDouble();
    }

    /**
     * If the argument is NaN or less than zero, this method will return an empty {@link java.util.OptionalDouble}, otherwise it will
     * return the square root of the given value wrapped into an OptionalDouble.
     * 
     * @param d
     *            value to be square rooted, if not NaN or &lt; 0.
     * @return Optional either empty or s
     */
    public static OptionalDouble validSqrt(double d) {
        return nanToOptional(Math.sqrt(d));
    }

    public static DoubleFunction<OptionalDouble> validSqrt() {
        return DoubleMatchHelpers::validSqrt;
    }
}
