package de.boereck.test.matcher.helpers;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.function.testable.TestableToLongFunction;
import de.boereck.matcher.helpers.found.Found;
import de.boereck.matcher.helpers.found.FoundAll;
import de.boereck.matcher.helpers.found.FoundNone;
import de.boereck.matcher.helpers.found.FoundSome;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static de.boereck.matcher.helpers.CollectionMatchHelpers.*;
import static java.util.Arrays.*;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

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
        forAll((t) -> {
            throw new NoSuchElementException();
        }).test(singletonList("foo"));
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

    @Test(expected = NullPointerException.class)
    public void testCountNull() {
        count(null);
    }

    @Test
    public void testCountNullCollection() {
        TestableToLongFunction<Collection<Object>> f = count(o -> true);
        long res = f.applyAsLong(null);
        assertEquals(0, res);
    }

    @Test
    public void testCountAll() {
        TestableToLongFunction<Collection<Object>> f = count(o -> true);
        long res = f.applyAsLong(asList("foo", "bar", "baz"));
        assertEquals(3, res);
    }

    @Test
    public void testCountSome() {
        TestableToLongFunction<Collection<Integer>> f = count(i -> i>0);
        long res = f.applyAsLong(asList(-1, 2, 0, -10, 4));
        assertEquals(2, res);
    }

    @Test(expected = NullPointerException.class)
    public void testFindCountNullPredicate() {
        findCount(Collections.emptyList(), null);
    }

    @Test
    public void testFindCountNullCollection() {
        Found f = findCount(null, p -> true);
        assertTrue(f instanceof FoundNone);
    }

    @Test
    public void testFindCountNoMatch() {
        Found f = findCount(asList("foo", "bar", "baz"), p -> false);
        assertTrue(f instanceof FoundNone);
    }

    @Test
    public void testFindCountSomeMatch() {
        Found f = findCount(asList("foo", "bar", "baz", "tar"), s -> s.startsWith("b"));
        assertTrue(f instanceof FoundSome && f.count() == 2);
    }

    @Test
    public void testFindCountAllMatch() {
        Found f = findCount(asList("foo", "bar", "baz", "tar"), s -> true);
        assertTrue(f instanceof FoundAll && f.count() == 4);
    }


    @Test(expected = NullPointerException.class)
    public void testFindCountFuncNullPredicate() {
        findCount(null);
    }

    @Test
    public void testFindCountFuncNullCollection() {
        TestableFunction<Collection<String>, Found> func = findCount(p -> true);
        Found f = func.apply(null);
        assertTrue(f instanceof FoundNone);
    }

    @Test
    public void testFindCountFuncNoMatch() {
        TestableFunction<Collection<String>, Found> func = findCount(p -> false);
        Found f = func.apply(asList("foo", "bar", "baz"));
        assertTrue(f instanceof FoundNone);
    }

    @Test
    public void testFindCountFuncSomeMatch() {
        TestableFunction<Collection<String>, Found> func = findCount(s -> s.startsWith("b"));
        Found f = func.apply(asList("foo", "bar", "baz", "tar"));
        assertTrue(f instanceof FoundSome && f.count() == 2);
    }

    @Test
    public void testFindCountFuncAllMatch() {
        TestableFunction<Collection<String>, Found> func = findCount(s -> true);
        Found f = func.apply( asList("foo", "bar", "baz", "tar") );
        assertTrue(f instanceof FoundAll && f.count() == 4);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterExistsNull() {
        filterExists(null);
    }

    @Test()
    public void testFilterExistsNullInput() {
        OptionalMapper<Collection<Object>, List<Object>> fe = filterExists(s -> true);
        Optional<List<Object>> res = fe.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test()
    public void testFilterExistsNoneExisting() {
        OptionalMapper<Collection<Object>, List<Object>> fe = filterExists(s -> false);
        Optional<List<Object>> res = fe.apply(asList("foo", "bar", "baz"));
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test()
    public void testFilterExistsExisting() {
        OptionalMapper<Collection<String>, List<String>> fe = filterExists(s -> s.startsWith("b"));
        Optional<List<String>> res = fe.apply(asList("foo", "bar", "baz"));
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertEquals(res.get(), Arrays.asList("bar", "baz"));
    }

    @Test(expected = NullPointerException.class)
    public void testElementsOfTypeNull() {
        elementsOfType(null);
    }

    @Test()
    public void testElementsOfTypeNullInput() {
        OptionalMapper<Collection<Object>, List<String>> f = elementsOfType(String.class);
        Optional<List<String>> res = f.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test()
    public void testElementsOfTypeNoMatch() {
        OptionalMapper<Collection<Object>, List<String>> f = elementsOfType(String.class);
        Optional<List<String>> res = f.apply(Arrays.asList(1, 2.0, Optional.empty()));
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test()
    public void testElementsOfTypeMatch() {
        OptionalMapper<Collection<Object>, List<String>> f = elementsOfType(String.class);
        Optional<List<String>> res = f.apply(Arrays.asList(1, 2.0, "foo", Optional.empty()));
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertEquals(singletonList("foo"), res.get());
    }

    @Test
    public void testCastToCollectionNull() {
        Optional<Collection<?>> res = castToCollection().apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testCastToCollectionNotCollection() {
        Optional<Collection<?>> res = castToCollection().apply("foo");
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testCastListToCollectionOfString() {
        Optional<Collection<?>> res = castToCollection().apply(asList("foo", "bar"));
        assertNotNull(res);
        assertTrue(res.isPresent());
        Collection<?> col = res.get();
        assertEquals(2, col.size());
        col.containsAll(asList("foo", "bar"));
    }

    @Test
    public void testCastSetToCollectionOfString() {
        Optional<Collection<?>> res = castToCollection().apply(new HashSet<>(asList("foo", "bar")));
        assertNotNull(res);
        assertTrue(res.isPresent());
        Collection<?> col = res.get();
        assertEquals(2, col.size());
        col.containsAll(asList("foo", "bar"));
    }

    ///

    @Test
    public void testCastToListNull() {
        Optional<List<?>> res = castToList().apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testCastToListNotCollection() {
        Optional<List<?>> res = castToList().apply("foo");
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testCastListToListOfString() {
        Optional<List<?>> res = castToList().apply(asList("foo", "bar"));
        assertNotNull(res);
        assertTrue(res.isPresent());
        Collection<?> col = res.get();
        assertEquals(2, col.size());
        col.containsAll(asList("foo", "bar"));
    }

    @Test
    public void testCastSetToListOfString() {
        Optional<List<?>> res = castToList().apply(new HashSet<>(asList("foo", "bar")));
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testCastToMapNull() {
        Optional<Map<?,?>> res = castToMap().apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testCastToMapNotCollection() {
        Optional<Map<?,?>> res = castToMap().apply("foo");
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testCastMapToMapOfString() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("foo", "bar");
        Optional<Map<?,?>> res = castToMap().apply(map);
        assertNotNull(res);
        assertTrue(res.isPresent());
        Map<?,?> m = res.get();
        assertEquals(1, m.size());
        assertEquals("bar", m.get("foo"));
    }

    @Test
    public void testCastListToMap() {
        Optional<Map<?,?>> res = castToMap().apply(asList("foo", "bar"));
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testHasValueForNullMap() {
        OptionalMapper<Map<String, Object>, Object> func = hasValueFor("foo");
        assertNotNull(func);
        Optional<Object> res = func.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testHasValueForNotAvailable() {
        OptionalMapper<Map<String, String>, String> func = hasValueFor("foo");
        assertNotNull(func);
        HashMap<String, String> map = new HashMap<>();
        map.put("bar", "baz");
        Optional<String> res = func.apply(map);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testHasValueForAvailable() {
        OptionalMapper<Map<String, String>, String> func = hasValueFor("foo");
        assertNotNull(func);
        HashMap<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        Optional<String> res = func.apply(map);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertEquals("bar", res.get());
    }

    @Test
    public void testIsCollectionOfNull() {
        boolean res = isCollection().test(null);
        assertFalse(res);
    }

    @Test
    public void testIsCollectionNotCollection() {
        boolean res = isCollection().test("foo");
        assertFalse(res);
    }

    @Test
    public void testIsCollectionOnList() {
        boolean res = isCollection().test(singletonList("foo"));
        assertTrue(res);
    }

    @Test
    public void testIsCollectionOnSet() {
        boolean res = isCollection().test(new HashSet<>(singletonList("foo")));
        assertTrue(res);
    }
}
