package de.boereck.test.matcher.helpers;

import de.boereck.matcher.function.predicate.AdvLongPredicate;
import org.junit.Test;

import java.util.NoSuchElementException;

import static de.boereck.matcher.helpers.LongMatchHelpers.*;
import static org.junit.Assert.*;

public class LongMatchHelpersTest {

    @Test
    public void testGt() {
        assertTrue(gt(5L).test(6L));
        assertFalse(gt(5L).test(5L));
        assertFalse(gt(6L).test(5L));
    }

    ///

    @Test
    public void testGe() {
        assertTrue(ge(5L).test(6L));
        assertTrue(ge(5L).test(5L));
        assertFalse(ge(6L).test(5L));
    }

    ///

    @Test
    public void testGtSupplied() {
        assertTrue(gt(() -> 5L).test(6L));
        assertFalse(gt(() -> 5L).test(5L));
        assertFalse(gt(() -> 6L).test(5L));
    }

    @Test(expected = NoSuchElementException.class)
    public void testGtSuppliedThrowing() {
        assertTrue(gt(() -> {
            throw new NoSuchElementException();
        }).test(6L));
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testGtSuppliedNullPointer() {
        assertTrue(gt(null).test(6L));
        fail();
    }

    ///

    @Test
    public void testGeSupplied() {
        assertTrue(ge(() -> 5L).test(6L));
        assertTrue(ge(() -> 5L).test(5L));
        assertFalse(ge(() -> 6L).test(5L));
    }

    @Test(expected = NoSuchElementException.class)
    public void testGeSuppliedThrowing() {
        assertTrue(ge(() -> {
            throw new NoSuchElementException();
        }).test(6L));
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testGeSuppliedNullPointer() {
        assertTrue(ge(null).test(6L));
        fail();
    }

    ///

    @Test
    public void testLt() {
        assertFalse(lt(5L).test(6L));
        assertFalse(lt(5L).test(5L));
        assertTrue(lt(6L).test(5L));
    }

    ///

    @Test
    public void testLe() {
        assertFalse(le(5L).test(6L));
        assertTrue(le(5L).test(5L));
        assertTrue(le(6L).test(5L));
    }

    ///

    @Test
    public void testLtSupplied() {
        assertFalse(lt(() -> 5L).test(6L));
        assertFalse(lt(() -> 5L).test(5L));
        assertTrue(lt(() -> 6L).test(5L));
    }

    @Test(expected = NoSuchElementException.class)
    public void testLtSuppliedThrowing() {
        assertTrue(lt(() -> {
            throw new NoSuchElementException();
        }).test(6L));
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testLtSuppliedNullPointer() {
        assertTrue(lt(null).test(6L));
        fail();
    }

    ///

    @Test
    public void testLeSupplied() {
        assertFalse(le(() -> 5L).test(6L));
        assertTrue(le(() -> 5L).test(5L));
        assertTrue(le(() -> 6L).test(5L));
    }

    @Test(expected = NoSuchElementException.class)
    public void testLeSuppliedThrowing() {
        assertTrue(le(() -> {
            throw new NoSuchElementException();
        }).test(6L));
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testLeSuppliedNullPointer() {
        assertTrue(le(null).test(6L));
        fail();
    }

    ///

    @Test
    public void testInClosedRange() {
        AdvLongPredicate range = inClosedRange(2L, 5L);
        assertFalse(range.test(1L));
        assertTrue(range.test(2L));
        assertTrue(range.test(4L));
        assertTrue(range.test(5L));
        assertFalse(range.test(6L));
    }

    @Test
    public void testInClosedRangeOneElementRange() {
        AdvLongPredicate range = inClosedRange(2L, 2L);
        assertFalse(range.test(1L));
        assertTrue(range.test(2L));
        assertFalse(range.test(3L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInClosedRangeWrongDiff() {
        inClosedRange(3L, 2L);
    }

    ///

    @Test
    public void testAllExcept() {
        assertFalse(allExcept(1L, 2L, 3L).test(1L));
        assertFalse(allExcept(1, 2, 3).test(3L));
        assertTrue(allExcept(1, 2, 3).test(4L));
    }

    @Test
    public void testAllExceptSingleElement() {
        assertFalse(allExcept(42L).test(42L));
        assertTrue(allExcept(13L).test(42L));
    }

    @Test(expected = NullPointerException.class)
    public void testAllExceptNullPointer() {
        allExcept(22L, (long[])null).test(42L);
    }


    ///

    @Test
    public void testOneOf() {
        assertTrue(oneOf(1L, 2L, 3L).test(1L));
        assertTrue(oneOf(1L, 2L, 3L).test(3L));
        assertFalse(oneOf(1L, 2L, 3L).test(4L));
    }

    @Test
    public void testOneOfSingleElement() {
        assertTrue(oneOf(42L).test(42L));
        assertFalse(oneOf(13L).test(42L));
    }

    @Test(expected = NullPointerException.class)
    public void testOneOfNullPointer() {
        oneOf(22L, (long[])null).test(42L);
    }

}
