package de.boereck.test.matcher.helpers;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.boereck.matcher.helpers.CollectionMatchHelpers.*;
import static java.util.Arrays.*;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static java.util.stream.Collectors.*;

public class CollectionMatchHelpersTest {

    @Test
    public void test$() {
        String expected = "foo";
        final Collection<String> c = asList(expected);
        Stream<String> stream = $(c);
        assertNotNull(stream);
        String[] arr = stream.toArray(String[]::new);
        assertEquals(1, arr.length);
        assertEquals(arr[0], expected);
    }

    @Test
    public void test$Null() {
        Stream<String> stream = $(null);
        assertNotNull(stream);
        long count = stream.count();
        assertEquals(0L, count);
    }

    @Test
    public void testFilter() {
        List<Object> c = asList("foo", 42, 0.0d, 32, Optional.empty());
        Stream<Integer> stream = filter(c, Integer.class);
        assertNotNull(stream);
        Integer[] arr = stream.toArray(Integer[]::new);
        assertEquals(2, arr.length);
        assertEquals((int) arr[0], 42);
        assertEquals((int)arr[1], 32);
    }

    @Test
    public void testFilterNull() {
        Stream<String> stream = filter((Collection<String>) null, String.class);
        assertNotNull(stream);
        assertEquals(0, stream.count());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterClassNull() {
        filter(asList(), null);
    }

    @Test
    public void testFilterCollection() {
        final OptionalMapper<Collection<Object>, List<Integer>> filterFuc = filterCollection(Integer.class);
        assertNotNull(filterFuc);
        List<Object> toFilter = asList("foo", 42, 0.0d, 32, Optional.empty());
        Optional<List<Integer>> opt = filterFuc.apply(toFilter);
        assertNotNull(opt);
        List<Integer> list = opt.get();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(42, (int) list.get(0));
        assertEquals(32, (int) list.get(1));
    }

    @Test
    public void testFilterCollectionNull() {
        final OptionalMapper<Collection<Object>, List<Integer>> filterFuc = filterCollection(Integer.class);
        assertNotNull(filterFuc);
        Optional<List<Integer>> opt = filterFuc.apply(null);
        assertNotNull(opt);
        assertFalse(opt.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCollectionClassNull() {
        filterCollection((Class) null);
    }

    @Test
    public void testFilterCollectionPred() {
        OptionalMapper<Collection<String>, List<String>> filterFunc = filterCollection(s -> s.startsWith("f"));
        assertNotNull(filterFunc);
        Optional<List<String>> opt = filterFunc.apply(asList("foo", "bar", "far"));
        assertNotNull(opt);
        assertTrue(opt.isPresent());
        List<String> list = opt.get();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("foo", list.get(0));
        assertEquals("far", list.get(1));
    }

    @Test
    public void testFilterCollectionPredOnNull() {
        OptionalMapper<Collection<String>, List<String>> filterFunc = filterCollection(s -> s.startsWith("f"));
        assertNotNull(filterFunc);
        Optional<List<String>> opt = filterFunc.apply(null);
        assertNotNull(opt);
        assertFalse(opt.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCollectionPredNullPredicate() {
        filterCollection((Predicate) null);
    }

    @Test
    public void testContainsInNull() {
        assertFalse(contains("foo").test(null));
    }

    @Test
    public void testContainsDoesNotContain() {
        assertFalse(contains("foo").test(asList("bar", "baz")));
    }

    @Test
    public void testContainsDoesContain() {
        assertTrue(contains("foo").test(asList("bar", "foo", "baz")));
    }

    @Test
    public void testContainsDoesNotContainNull() {
        assertFalse(contains(null).test(asList("bar", "baz")));
    }

    @Test
    public void testContainsDoesContainNull() {
        assertTrue(contains(null).test(asList("bar", null, "baz")));
    }

    @Test(expected = NullPointerException.class)
    public void testExistsNullPointer() {
        exists(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testExistsThrowing() {
        exists((t) -> {throw new NoSuchElementException();}).test(singletonList("foo"));
    }

    @Test
    public void testExistsNotExisting() {
        boolean result = exists((e) -> false).test(asList("foo", "bar"));
        assertFalse(result);
    }

    @Test
    public void testExistsExisting() {
        boolean result = exists((e) -> e.equals("foo")).test(asList("bar", "foo", "bazz"));
        assertTrue(result);
    }

    @Test
    public void testExistsNullCollection() {
        boolean result = exists((e) -> e.equals("foo")).test(null);
        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void testForAllNullPointer() {
        forAll(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testForAllThrowing() {
        forAll((t) -> {throw new NoSuchElementException();}).test(singletonList("foo"));
    }

    @Test
    public void testForAllNotTrue() {
        boolean result = forAll((e) -> e.equals("foo")).test(asList("foo", "bar"));
        assertFalse(result);
    }

    @Test
    public void testForAllTrue() {
        boolean result = exists((String e) -> e.startsWith("f")).test(asList("fuzz", "foo", "fumble"));
        assertTrue(result);
    }

    @Test
    public void testForAllNullCollection() {
        boolean result = forAll((e) -> e.equals("foo")).test(null);
        assertFalse(result);
    }
}
