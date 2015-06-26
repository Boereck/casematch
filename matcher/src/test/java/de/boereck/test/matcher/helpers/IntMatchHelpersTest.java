package de.boereck.test.matcher.helpers;

import static de.boereck.matcher.helpers.IntMatchHelpers.*;
import static org.junit.Assert.*;

import de.boereck.matcher.function.predicate.AdvIntPredicate;
import org.junit.Test;

import java.util.NoSuchElementException;

public class IntMatchHelpersTest {

    @Test
    public void testGt() {
        assertTrue(gt(5).test(6));
        assertFalse(gt(5).test(5));
        assertFalse(gt(6).test(5));
    }

    ///

    @Test
    public void testGe() {
        assertTrue(ge(5).test(6));
        assertTrue(ge(5).test(5));
        assertFalse(ge(6).test(5));
    }

    ///

    @Test
    public void testGtSupplied() {
        assertTrue(gt(() -> 5).test(6));
        assertFalse(gt(() -> 5).test(5));
        assertFalse(gt(() -> 6).test(5));
    }

    @Test(expected = NoSuchElementException.class)
    public void testGtSuppliedThrowing() {
        assertTrue(gt(() -> {
            throw new NoSuchElementException();
        }).test(6));
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testGtSuppliedNullPointer() {
        assertTrue(gt(null).test(6));
        fail();
    }

    ///

    @Test
    public void testGeSupplied() {
        assertTrue(ge(() -> 5).test(6));
        assertTrue(ge(() -> 5).test(5));
        assertFalse(ge(() -> 6).test(5));
    }

    @Test(expected = NoSuchElementException.class)
    public void testGeSuppliedThrowing() {
        assertTrue(ge(() -> {
            throw new NoSuchElementException();
        }).test(6));
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testGeSuppliedNullPointer() {
        assertTrue(ge(null).test(6));
        fail();
    }

    ///

    @Test
    public void testLt() {
        assertFalse(lt(5).test(6));
        assertFalse(lt(5).test(5));
        assertTrue(lt(6).test(5));
    }

    ///

    @Test
    public void testLe() {
        assertFalse(le(5).test(6));
        assertTrue(le(5).test(5));
        assertTrue(le(6).test(5));
    }

    ///

    @Test
    public void testLtSupplied() {
        assertFalse(lt(() -> 5).test(6));
        assertFalse(lt(() -> 5).test(5));
        assertTrue(lt(() -> 6).test(5));
    }

    @Test(expected = NoSuchElementException.class)
    public void testLtSuppliedThrowing() {
        assertTrue(lt(() -> {
            throw new NoSuchElementException();
        }).test(6));
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testLtSuppliedNullPointer() {
        assertTrue(lt(null).test(6));
        fail();
    }

    ///

    @Test
    public void testLeSupplied() {
        assertFalse(le(() -> 5).test(6));
        assertTrue(le(() -> 5).test(5));
        assertTrue(le(() -> 6).test(5));
    }

    @Test(expected = NoSuchElementException.class)
    public void testLeSuppliedThrowing() {
        assertTrue(le(() -> {
            throw new NoSuchElementException();
        }).test(6));
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testLeSuppliedNullPointer() {
        assertTrue(le(null).test(6));
        fail();
    }

    ///

    @Test
    public void testPositive() {
        assertTrue(positive.test(1));
        assertFalse(positive.test(0));
        assertFalse(positive.test(-1));
    }

    ///

    @Test
    public void testNegative() {
        assertFalse(negative.test(1));
        assertFalse(negative.test(0));
        assertTrue(negative.test(-1));
    }

    ///

    @Test
    public void testOneOf() {
        assertTrue(oneOf(1,2,3).test(1));
        assertTrue(oneOf(1,2,3).test(3));
        assertFalse(oneOf(1, 2, 3).test(4));
    }

    @Test
    public void testOneOfSingleElement() {
        assertTrue(oneOf(42).test(42));
        assertFalse(oneOf(13).test(42));
    }

    @Test(expected = NullPointerException.class)
    public void testOneOfNullPointer() {
        oneOf(22, null).test(42);
    }

    ///

    @Test
    public void testEven() {
        assertFalse(even.test(1));
        assertTrue(even.test(0));
        assertTrue(even.test(-2));
        assertFalse(even.test(-3));
    }

    ///

    @Test
    public void testOdd() {
        assertTrue(odd.test(1));
        assertFalse(odd.test(0));
        assertFalse(odd.test(-2));
        assertTrue(odd.test(-3));
    }

    ///

    @Test
    public void testDividableBy() {
        AdvIntPredicate by3 = dividableBy(3);
        assertFalse(by3.test(2));
        assertTrue(by3.test(6));
        assertTrue(by3.test(-9));
        assertFalse(by3.test(2));
        assertTrue(by3.test(0));
        assertFalse(by3.test(5));
    }

    @Test(expected = ArithmeticException.class)
    public void testDividableByDivideByZero() {
        dividableBy(0).test(5);
        fail();
    }

    ///

    @Test
    public void testNotDividableBy() {
        AdvIntPredicate by3 = notDividableBy(3);
        assertTrue(by3.test(2));
        assertFalse(by3.test(6));
        assertFalse(by3.test(-9));
        assertTrue(by3.test(2));
        assertFalse(by3.test(0));
        assertTrue(by3.test(5));
    }

    @Test(expected = ArithmeticException.class)
    public void testNotDividableByDivideByZero() {
        notDividableBy(0).test(5);
        fail();
    }



}
