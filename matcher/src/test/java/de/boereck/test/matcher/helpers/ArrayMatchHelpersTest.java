package de.boereck.test.matcher.helpers;

import de.boereck.matcher.helpers.found.*;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.*;

import static de.boereck.matcher.helpers.ArrayMatchHelpers.*;
import static org.junit.Assert.*;

public class ArrayMatchHelpersTest {

    @Test
    public void testStreamDoesReturnVal() {
        final Stream<String> str = $("foo","bar");
        assertNotNull(str);
        final List<String> lst = str.collect(Collectors.toList());
        assertEquals(2, lst.size());
        assertEquals("foo", lst.get(0));
        assertEquals("bar", lst.get(1));
    }

    @Test
    public void testStreamReturnEmptyStreamOnNull() {
        final Stream<String> str = $((String[]) null);
        assertNotNull(str);
        assertEquals(0L, str.count());
    }

    @Test
    public void testIntStreamDoesReturnVal() {
        final IntStream str = $(new int[]{42, 128});
        assertNotNull(str);
        final int[] arr = str.toArray();
        assertEquals(2, arr.length);
        assertEquals(42, arr[0]);
        assertEquals(128, arr[1]);
    }

    @Test
    public void testIntStreamReturnEmptyStreamOnNull() {
        final IntStream str = $((int[]) null);
        assertNotNull(str);
        assertEquals(0L, str.count());
    }

    @Test
    public void testLongStreamDoesReturnVal() {
        final LongStream str = $(new long[]{42L, 128L});
        assertNotNull(str);
        final long[] arr = str.toArray();
        assertEquals(2, arr.length);
        assertEquals(42L, arr[0]);
        assertEquals(128L, arr[1]);
    }

    @Test
    public void testLongStreamReturnEmptyStreamOnNull() {
        final LongStream str = $((long[]) null);
        assertNotNull(str);
        assertEquals(0L, str.count());
    }

    @Test
    public void testDoubleStreamDoesReturnVal() {
        final DoubleStream str = $(new double[]{0.0d, Double.POSITIVE_INFINITY});
        assertNotNull(str);
        final double[] arr = str.toArray();
        assertEquals(2, arr.length);
        assertTrue(0.0d == arr[0]);
        assertTrue(Double.POSITIVE_INFINITY == arr[1]);
    }

    @Test
    public void testDoubleStreamReturnEmptyStreamOnNull() {
        final DoubleStream str = $((double[]) null);
        assertNotNull(str);
        assertEquals(0L, str.count());
    }

    @Test
    public void testArrayNotEmpty() {
        assertTrue(arrayNotEmpty.test(new String[]{"foo"}));
    }

    @Test
    public void testArrayNotEmptyWithEmpty() {
        assertFalse(arrayNotEmpty.test(new String[]{}));
    }

    @Test
    public void testArrayNotEmptyWithNull() {
        assertFalse(arrayNotEmpty.test(null));
    }

    @Test
    public void testIntArrayNotEmpty() {
        assertTrue(intArrayNotEmpty.test(new int[]{42}));
    }

    @Test
    public void testIntArrayNotEmptyWithEmpty() {
        assertFalse(intArrayNotEmpty.test(new int[]{}));
    }

    @Test
    public void testIntArrayNotEmptyWithNull() {
        assertFalse(intArrayNotEmpty.test(null));
    }

    @Test
    public void testLongArrayNotEmpty() {
        assertTrue(longArrayNotEmpty.test(new long[]{42}));
    }

    @Test
    public void testLongArrayNotEmptyWithEmpty() {
        assertFalse(longArrayNotEmpty.test(new long[]{}));
    }

    @Test
    public void testLongArrayNotEmptyWithNull() {
        assertFalse(longArrayNotEmpty.test(null));
    }

    @Test
    public void testDoubleArrayNotEmpty() {
        assertTrue(doubleArrayNotEmpty.test(new double[]{42}));
    }

    @Test
    public void testDoubleArrayNotEmptyWithEmpty() {
        assertFalse(doubleArrayNotEmpty.test(new double[]{}));
    }

    @Test
    public void testDoubleArrayNotEmptyWithNull() {
        assertFalse(doubleArrayNotEmpty.test(null));
    }

    @Test
    public void testFindCountSome() {
        final Found fc = findCount(new String[]{"foo", "bar", "roo", "baz", "hey"}, s -> s.startsWith("b"));
        assertTrue(fc instanceof FoundSome);
        assertEquals(2, fc.count());
        assertEquals(FindType.some, fc.type());
    }

    @Test
    public void testFindCountAll() {
        final Found fc = findCount(new String[]{"bean", "bar", "baz"}, s -> s.startsWith("b"));
        assertTrue(fc instanceof FoundAll);
        assertEquals(3, fc.count());
        assertEquals(FindType.all, fc.type());
    }

    @Test
    public void testFindCountNone() {
        final Found fc = findCount(new String[]{"hey", "foo", "goo"}, s -> s.startsWith("b"));
        assertTrue(fc instanceof FoundNone);
        assertEquals(0, fc.count());
        assertEquals(FindType.none, fc.type());
    }

    @Test
    public void testFindCountNullInput() {
        final Found fc = findCount((String[])null, s -> s.startsWith("b"));
        assertTrue(fc instanceof FoundNone);
        assertEquals(0, fc.count());
        assertEquals(FindType.none, fc.type());
    }

    @Test
    public void testCountInArraySome() {
        final long count = countInArray((String s) -> s.startsWith("b")).applyAsLong(new String[]{"foo", "bar", "roo", "baz", "hey"});
        assertEquals(2, count);
    }

    @Test
    public void testCountInArrayAll() {
        final long count = countInArray((String s) -> s.startsWith("b")).applyAsLong(new String[]{"bean", "bar", "baz"});
        assertEquals(3, count);
    }

    @Test
    public void testCountInArrayNone() {
        final long count = countInArray((String s) -> s.startsWith("b")).applyAsLong(new String[]{"hey", "foo", "goo"});
        assertEquals(0, count);
    }

    @Test
    public void testCountInArrayNullInput() {
        final long count = countInArray((String s) -> s.startsWith("b")).applyAsLong(null);
        assertEquals(0, count);
    }

    @Test
    public void testFindCountInArraySome() {
        final Found fc = findCountInArray((String s) -> s.startsWith("b")).apply(new String[]{"foo", "bar", "roo", "baz", "hey"});
        assertTrue(fc instanceof FoundSome);
        assertEquals(2, fc.count());
        assertEquals(FindType.some, fc.type());
    }

    @Test
    public void testFindCountInArrayAll() {
        final Found fc = findCountInArray((String s) -> s.startsWith("b")).apply(new String[]{"bean", "bar", "baz"});
        assertTrue(fc instanceof FoundAll);
        assertEquals(3, fc.count());
        assertEquals(FindType.all, fc.type());
    }

    @Test
    public void testFindCountInArrayNone() {
        final Found fc = findCountInArray((String s) -> s.startsWith("b")).apply(new String[]{"hey", "foo", "goo"});
        assertTrue(fc instanceof FoundNone);
        assertEquals(0, fc.count());
        assertEquals(FindType.none, fc.type());
    }

    @Test
    public void testFindCountInArrayNullInput() {
        final Found fc = findCountInArray((String s) -> s.startsWith("b")).apply(null);
                assertTrue(fc instanceof FoundNone);
        assertEquals(0, fc.count());
        assertEquals(FindType.none, fc.type());
    }

    @Test
    public void testExistsInArrayNotExisting() {
        assertFalse(existsInArray((String s) -> s.startsWith("b")).test(new String[]{"hey", "foo", "goo"}));
    }

    @Test
    public void testExistsInArrayExisting() {
        assertTrue(existsInArray((String s) -> s.startsWith("b")).test(new String[]{"hey", "foo", "boo"}));
    }

    @Test
    public void testExistsInArrayInputNull() {
        assertFalse(existsInArray((String s) -> s.startsWith("b")).test(null));
    }

    @Test
    public void testForAllInArrayNotExisting() {
        assertFalse(forAllInArray((String s) -> s.startsWith("b")).test(new String[]{"hey", "foo", "goo"}));
    }

    @Test
    public void testForAllInArrayExisting() {
        assertFalse(forAllInArray((String s) -> s.startsWith("b")).test(new String[]{"hey", "foo", "boo"}));
    }

    @Test
    public void testForAllInArrayAllMatch() {
        assertTrue(forAllInArray((String s) -> s.startsWith("b")).test(new String[]{"baz", "boom", "boo"}));
    }

    @Test
    public void testForAllInArrayInputNull() {
        assertFalse(forAllInArray((String s) -> s.startsWith("b")).test(null));
    }

    public static <T> T[] array(T ... val) {
        return val;
    }

    @Test
    public void testContainsOrderedComparator() {
        final Predicate<String[]> checkFoo = containsOrdered("foo", String::compareTo);
        assertTrue(checkFoo.test(array("abort", "cool", "foo", "idea")));
    }

    @Test
    public void testContainsOrderedNotContainingComparator() {
        final Predicate<String[]> checkFoo = containsOrdered("foo", String::compareTo);
        assertFalse(checkFoo.test(array("abort", "cool", "idea")));
    }

    @Test
    public void testContainsOrderedNullInputComparator() {
        final Predicate<String[]> checkFoo = containsOrdered("foo", String::compareTo);
        assertFalse(checkFoo.test(null));
    }

    @Test
    public void testContainsOrdered() {
        final Predicate<String[]> checkFoo = containsOrdered("foo");
        assertTrue(checkFoo.test(array("abort", "cool", "foo", "idea")));
    }

    @Test
    public void testContainsOrderedNotContaining() {
        final Predicate<String[]> checkFoo = containsOrdered("foo");
        assertFalse(checkFoo.test(array("abort", "cool", "idea")));
    }

    @Test
    public void testContainsOrderedNullInput() {
        final Predicate<String[]> checkFoo = containsOrdered("foo");
        assertFalse(checkFoo.test(null));
    }

    @Test
    public void testToArrayOfNotInstanceOf() {
        final Object o = new Integer[] {1,2,3};
        final Optional<String[]> result = toArrayOf(String.class).apply(o);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testToArrayOfNullInput() {
        final Object o = null;
        final Optional<String[]> result = toArrayOf(String.class).apply(o);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testToArrayOfMatching() {
        final Object o = new String[] {"foo", "bar"};
        final Optional<String[]> result = toArrayOf(String.class).apply(o);
        assertNotNull(result);
        assertTrue(result.isPresent());
        final String[] str = result.get();
        assertEquals(o, str);
    }

    @Test
    public void testIsArrayOfWrongType() {
        final Object o = new Integer[] {1,2,3};
        assertFalse(isArrayOf(String.class).test(o));
    }

    @Test
    public void testIsArrayOfNullInput() {
        final Object o = null;
        assertFalse(isArrayOf(String.class).test(o));
    }

    @Test
    public void testIsArrayOfMatching() {
        final Object o = new String[] {"foo", "bar"};
        assertTrue(isArrayOf(String.class).test(o));
    }
}
