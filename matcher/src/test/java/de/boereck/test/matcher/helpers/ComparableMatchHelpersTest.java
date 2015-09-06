package de.boereck.test.matcher.helpers;

import de.boereck.matcher.function.predicate.AdvPredicate;
import org.junit.Test;
import static de.boereck.matcher.helpers.ComparableMatchHelpers.*;
import static de.boereck.matcher.helpers.ComparableMatchHelpers.lessThan;
import static org.junit.Assert.*;

public class ComparableMatchHelpersTest {

    @Test(expected = NullPointerException.class)
    public void testLessThanNull() {
        lessThan(null);
    }

    @Test
    public void testLessThan() {
        AdvPredicate<Comparable<? super Integer>> func = lessThan(3);
        boolean notLess = func.test(4);
        assertFalse(notLess);
    }

    @Test
    public void testNotLessThan() {
        AdvPredicate<Comparable<? super Integer>> func = lessThan(3);
        boolean isLess = func.test(-2);
        assertTrue(isLess);
    }

    @Test
    public void testEqualNotLessThan() {
        AdvPredicate<Comparable<? super Integer>> func = lessThan(3);
        boolean isLess = func.test(3);
        assertFalse(isLess);
    }

    @Test(expected = NullPointerException.class)
    public void testGreaterThanNull() {
        greaterThan(null);
    }

    @Test
    public void testGreaterThan() {
        AdvPredicate<Comparable<? super Integer>> func = greaterThan(3);
        boolean greaterThan = func.test(4);
        assertTrue(greaterThan);
    }

    @Test
    public void testNotGreaterThan() {
        AdvPredicate<Comparable<? super Integer>> func = greaterThan(3);
        boolean greaterThan = func.test(-2);
        assertFalse(greaterThan);
    }

    @Test
    public void testEqualNotGreaterThan() {
        AdvPredicate<Comparable<? super Integer>> func = greaterThan(3);
        boolean greaterThan = func.test(3);
        assertFalse(greaterThan);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsNull() {
        equalTo(null);
    }

    @Test()
    public void testEqualTo() {
        AdvPredicate<Comparable<? super Integer>> func = equalTo(3);
        boolean isEqual = func.test(3);
        assertTrue(isEqual);
    }

    @Test()
    public void testNotEqualtToGreater() {
        AdvPredicate<Comparable<? super Integer>> func = equalTo(3);
        boolean isEqual = func.test(2);
        assertFalse(isEqual);
    }

    @Test()
    public void testNotEqualtToLess() {
        AdvPredicate<Comparable<? super Integer>> func = equalTo(3);
        boolean isEqual = func.test(4);
        assertFalse(isEqual);
    }
}
