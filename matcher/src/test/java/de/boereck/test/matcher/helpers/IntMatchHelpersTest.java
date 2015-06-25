package de.boereck.test.matcher.helpers;

import static de.boereck.matcher.helpers.IntMatchHelpers.*;
import static org.junit.Assert.*;

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
    public void testLt() {
        assertFalse(lt(5).test(6));
        assertFalse(lt(5).test(5));
        assertTrue(lt(6).test(5));
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
}
