package de.boereck.test.matcher.helpers;

import org.junit.Test;

import java.util.OptionalDouble;

import static de.boereck.matcher.helpers.DoubleMatchHelpers.*;
import static org.junit.Assert.*;

public class DoubleMatchHelpersTest {

    @Test
    public void testFilterNaNOnNaN() {
        OptionalDouble res = filterNaN.apply(Double.NaN);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFilterNaNOnNonNaN() {
        double expected = 42.0;
        OptionalDouble res = filterNaN.apply(expected);
        assertTrue(expected == res.getAsDouble());
    }

    ///

    public void ಠ_ಠ() {

    }

    @Test
    public void testFilterInfinityOnInfinity() {
        OptionalDouble res = filterInfinity.apply(Double.POSITIVE_INFINITY);
        assertFalse(res.isPresent());
        res = filterInfinity.apply(Double.NEGATIVE_INFINITY);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFilterInfinityOnNonInfinity() {
        double expected = 42.0;
        OptionalDouble res = filterInfinity.apply(expected);
        assertTrue(res.isPresent());
        assertTrue(expected == res.getAsDouble());
    }

    @Test
    public void testFilterInfinityOnNaN() {
        OptionalDouble res = filterInfinity.apply(Double.NaN);
        assertTrue(res.isPresent());
        assertTrue(Double.isNaN(res.getAsDouble()));
    }

    @Test
    public void testEqMinValue() {
        assertTrue(eq(0.0d, Double.MIN_VALUE).test(Double.MIN_VALUE));
    }

    @Test
    public void testNotEqNextFromMinValue() {
        final long bitMinVal = Double.doubleToLongBits(Double.MIN_VALUE);
        final double nextFromMin = bitMinVal + Math.ulp(bitMinVal);
        assertFalse(eq(0.0d, Double.MIN_VALUE).test(nextFromMin));
    }

    @Test
    public void testEqInfinity() {
        assertTrue(eq(Double.POSITIVE_INFINITY, 0.0d).test(Double.POSITIVE_INFINITY));
        assertTrue(eq(Double.NEGATIVE_INFINITY, 0.0d).test(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testNotEqPositiveAndNegativeInfinity() {
        assertFalse(eq(Double.POSITIVE_INFINITY, 0.1d).test(Double.NEGATIVE_INFINITY));
        assertFalse(eq(Double.NEGATIVE_INFINITY, 0.1d).test(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testEqNaN() {
        assertTrue(eq(Double.NaN, 0.0d).test(Double.NEGATIVE_INFINITY / Double.POSITIVE_INFINITY));
    }

    @Test
    public void testNotEqNaNOther() {
        assertFalse(eq(Double.NaN, 0.1d).test(1.0d));
        assertFalse(eq(1.0d, 0.1d).test(Double.NaN));
        assertFalse(eq(Double.POSITIVE_INFINITY, 0.1d).test(Double.NaN));
        assertFalse(eq(Double.NaN, 0.1d).test(Double.POSITIVE_INFINITY));
        assertFalse(eq(Double.NEGATIVE_INFINITY, 0.1d).test(Double.NaN));
        assertFalse(eq(Double.NaN, 0.1d).test(Double.NEGATIVE_INFINITY));
    }

    // EQ one ULP

    @Test
    public void testEqULPMinValue() {
        assertTrue(eq(0.0d).test(Double.MIN_VALUE));
    }

    @Test
    public void testEqUPLNextFromMinValue() {
        final long bitMinVal = Double.doubleToLongBits(Double.MIN_VALUE);
        final double nextFromMin = bitMinVal + Math.ulp(bitMinVal);
        assertFalse(eq(0.0d).test(nextFromMin));
    }

    @Test
    public void testEqUPLNextFromSomeValue() {
        final double someVal = 12345.0d;
        final double nextFromSome = someVal +  Math.ulp(someVal);
        assertTrue(eq(someVal).test(nextFromSome));
    }

    @Test
    public void testEqULPInfinity() {
        assertTrue(eq(Double.POSITIVE_INFINITY).test(Double.POSITIVE_INFINITY));
        assertTrue(eq(Double.NEGATIVE_INFINITY).test(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testNotEqULPPositiveAndNegativeInfinity() {
        assertFalse(eq(Double.NEGATIVE_INFINITY).test(Double.POSITIVE_INFINITY));
        assertFalse(eq(Double.POSITIVE_INFINITY).test(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testEqULPNaN() {
        assertTrue(eq(Double.NaN).test(Double.NEGATIVE_INFINITY / Double.POSITIVE_INFINITY));
    }

    @Test
    public void testNotEqULPNaNOther() {
        assertFalse(eq(Double.NaN).test(1.0d));
        assertFalse(eq(1.0d).test(Double.NaN));
        assertFalse(eq(Double.POSITIVE_INFINITY).test(Double.NaN));
        assertFalse(eq(Double.NaN).test(Double.POSITIVE_INFINITY));
        assertFalse(eq(Double.NEGATIVE_INFINITY).test(Double.NaN));
        assertFalse(eq(Double.NaN).test(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testLt() {
        assertTrue(lt(1.0d).test(0.0d));
    }

    @Test
    public void testLtPositiveInfinity() {
        assertTrue(lt(Double.POSITIVE_INFINITY).test(0.0d));
        assertFalse(lt(0.0d).test(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testLtNegativeInfinity() {
        assertFalse(lt(Double.NEGATIVE_INFINITY).test(0.0d));
        assertTrue(lt(0.0d).test(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testLtNaN() {
        assertFalse(lt(Double.NaN).test(0.0d));
        assertFalse(lt(0.0d).test(Double.NaN));
    }


    @Test
    public void testLtSupplied() {
        assertTrue(lt(() -> 1.0d).test(0.0d));
    }

    @Test
    public void testLtSuppliedPositiveInfinity() {
        assertTrue(lt(() -> Double.POSITIVE_INFINITY).test(0.0d));
        assertFalse(lt(() -> 0.0d).test(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testLSuppliedtNegativeInfinity() {
        assertFalse(lt(() -> Double.NEGATIVE_INFINITY).test(0.0d));
        assertTrue(lt(() -> 0.0d).test(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testLtSuppliedNaN() {
        assertFalse(lt(() -> Double.NaN).test(0.0d));
        assertFalse(lt(() -> 0.0d).test(Double.NaN));
    }


    @Test
    public void testGt() {
        assertTrue(gt(0.0d).test(1.0d));
    }

    @Test
    public void testGtPositiveInfinity() {
        assertTrue(gt(0.0d).test(Double.POSITIVE_INFINITY));
        assertFalse(gt(Double.POSITIVE_INFINITY).test(0.0d));
    }

    @Test
    public void testGtNegativeInfinity() {
        assertTrue(gt(Double.NEGATIVE_INFINITY).test(0.0d));
        assertFalse(gt(0.0d).test(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testGtNaN() {
        assertFalse(lt(Double.NaN).test(0.0d));
        assertFalse(lt(0.0d).test(Double.NaN));
    }


    @Test
    public void testGtSupplied() {
        assertFalse(gt(() -> 1.0d).test(0.0d));
    }

    @Test
    public void testGtSuppliedPositiveInfinity() {
        assertFalse(gt(() -> Double.POSITIVE_INFINITY).test(0.0d));
        assertTrue(gt(() -> 0.0d).test(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testGtSuppliedtNegativeInfinity() {
        assertTrue(gt(() -> Double.NEGATIVE_INFINITY).test(0.0d));
        assertFalse(gt(() -> 0.0d).test(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testGtSuppliedNaN() {
        assertFalse(gt(() -> Double.NaN).test(0.0d));
        assertFalse(gt(() -> 0.0d).test(Double.NaN));
    }

    @Test
    public void testValidSqrtOnZero() {
        OptionalDouble opt = validSqrt.apply(0.0d);
        assertTrue(opt.isPresent());
        assertTrue(opt.getAsDouble() == 0.0d);
    }

    @Test
    public void testValidSqrtOnNegative() {
        OptionalDouble opt = validSqrt.apply(-1.0d);
        assertFalse(opt.isPresent());
    }

    @Test
    public void testValidSqrtOnInfinity() {
        OptionalDouble opt = validSqrt.apply(Double.POSITIVE_INFINITY);
        assertTrue(opt.isPresent());
        assertEquals(opt.getAsDouble(), Double.POSITIVE_INFINITY, 0.0d);
    }

    @Test
    public void testValidSqrtOnNegativeInfinity() {
        OptionalDouble opt = validSqrt.apply(Double.NEGATIVE_INFINITY);
        assertFalse(opt.isPresent());
    }

    @Test
    public void testValidSqrtOnNaN() {
        OptionalDouble opt = validSqrt.apply(Double.NaN);
        assertFalse(opt.isPresent());
    }
}
