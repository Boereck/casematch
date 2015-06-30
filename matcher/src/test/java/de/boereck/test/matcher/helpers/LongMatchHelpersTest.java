package de.boereck.test.matcher.helpers;

import org.junit.Test;

import java.util.NoSuchElementException;

import static de.boereck.matcher.helpers.LongMatchHelpers.gt;
import static de.boereck.matcher.helpers.LongMatchHelpers.ge;
import static de.boereck.matcher.helpers.LongMatchHelpers.lt;
import static de.boereck.matcher.helpers.LongMatchHelpers.le;
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
}
