package de.boereck.test.matcher.helpers;

import static de.boereck.matcher.helpers.MatchHelpers.*;
import static java.util.function.Function.identity;
import static org.junit.Assert.*;

import de.boereck.matcher.function.optionalmap.OptionalDoubleMapper;
import de.boereck.matcher.function.optionalmap.OptionalIntMapper;
import de.boereck.matcher.function.optionalmap.OptionalLongMapper;
import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;

public class MatchHelperTest {

    @Test(expected = NullPointerException.class)
    public void testTryPredicateNull() {
        _try(null);
        fail();
    }

    @Test()
    public void testTryPredicateWrapFalse() {
        Predicate<String> p = _try(i -> false);
        assertFalse(p.test("foo"));
    }

    @Test()
    public void testTryPredicateWrapTrue() {
        Predicate<String> p = _try(i -> true);
        assertTrue(p.test("foo"));
    }

    @Test()
    public void testTryPredicateExceptionally() {
        Predicate<String> p = _try(i -> {
            throw new NoSuchElementException();
        });
        assertFalse(p.test("foo"));
    }

    ///

    @Test
    public void testTryMap() {
        OptionalMapper<String, Integer> mapper = tryMap(String::length);
        String in = "foo";
        Optional<Integer> res = mapper.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.get() == in.length());
    }

    @Test
    public void testTryMapReturnsNull() {
        OptionalMapper<String, Integer> mapper = tryMap(s -> null);
        String in = "foo";
        Optional<Integer> res = mapper.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testTryMapThrowing() {
        OptionalMapper<String, Integer> mapper = tryMap(s -> {
            throw new NoSuchElementException();
        });
        String in = "foo";
        Optional<Integer> res = mapper.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapNullPointer() {
        tryMap(null);
        fail();
    }

    ///

    @Test
    public void testTryMapI() {
        OptionalIntMapper<String> mapper = tryMapI(String::length);
        String in = "foo";
        OptionalInt res = mapper.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsInt() == in.length());
    }

    @Test
    public void testTryMapIThrowing() {
        OptionalIntMapper<String> mapper = tryMapI(s -> {
            throw new NoSuchElementException();
        });
        String in = "foo";
        OptionalInt res = mapper.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapINullPointer() {
        tryMapI(null);
        fail();
    }

    ///

    @Test
    public void testTryMapL() {
        OptionalLongMapper<String> mapper = tryMapL(String::length);
        String in = "foo";
        OptionalLong res = mapper.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsLong() == in.length());
    }

    @Test
    public void testTryMapLThrowing() {
        OptionalLongMapper<String> mapper = tryMapL(s -> {
            throw new NoSuchElementException();
        });
        String in = "foo";
        OptionalLong res = mapper.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapLNullPointer() {
        tryMapL(null);
        fail();
    }

    ///

    @Test
    public void testTryMapD() {
        String in = "foo";
        double out = 0.0;
        OptionalDoubleMapper<String> mapper = tryMapD(s -> out);
        OptionalDouble res = mapper.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsDouble() == out);
    }

    @Test
    public void testTryMapDThrowing() {
        OptionalDoubleMapper<String> mapper = tryMapD(s -> {
            throw new NoSuchElementException();
        });
        String in = "foo";
        OptionalDouble res = mapper.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapDNullPointer() {
        tryMapD(null);
        fail();
    }

    ///

    @Test
    public void testTryMapExConsumer() {
        OptionalMapper<String, Integer> mapper = tryMap(String::length, ex -> fail());
        String in = "foo";
        Optional<Integer> res = mapper.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.get() == in.length());
    }

    @Test
    public void testTryMapExConsumerReturnsNull() {
        OptionalMapper<String, Integer> mapper = tryMap(s -> null, ex -> fail());
        String in = "foo";
        Optional<Integer> res = mapper.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testTryMapExConsumerThrowing() {
        AtomicBoolean success = new AtomicBoolean(false);
        OptionalMapper<String, Integer> mapper = tryMap(s -> {
            throw new NoSuchElementException();
        }, ex -> success.set(true));
        String in = "foo";
        Optional<Integer> res = mapper.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapExConsumerNullPointer1() {
        tryMap(null, ex -> fail());
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapExConsumerNullPointer2() {
        tryMap(String::length, null);
        fail();
    }

    ///

    @Test
    public void testTryMapIExConsumer() {
        OptionalIntMapper<String> mapper = tryMapI(String::length, ex -> fail());
        String in = "foo";
        OptionalInt res = mapper.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsInt() == in.length());
    }

    @Test
    public void testTryMapIExConsumerThrowing() {
        AtomicBoolean success = new AtomicBoolean(false);
        OptionalIntMapper<String> mapper = tryMapI(s -> {
            throw new NoSuchElementException();
        }, ex -> success.set(true));
        String in = "foo";
        OptionalInt res = mapper.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapIExConsumerNullPointer() {
        tryMapI(null, ex -> fail());
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapIExConsumerNullPointer2() {
        tryMapI(String::length, null);
        fail();
    }

    ///

    @Test
    public void testTryMapLExConsumer() {
        OptionalLongMapper<String> mapper = tryMapL(String::length, ex -> fail());
        String in = "foo";
        OptionalLong res = mapper.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsLong() == in.length());
    }

    @Test
    public void testTryMapLExConsumerThrowing() {
        AtomicBoolean success = new AtomicBoolean(false);
        OptionalLongMapper<String> mapper = tryMapL(s -> {
            throw new NoSuchElementException();
        }, ex -> success.set(true));
        String in = "foo";
        OptionalLong res = mapper.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapLExConsumerNullPointer() {
        tryMapL(null, ex -> fail());
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapLExConsumerNullPointer2() {
        tryMapL(String::length, null);
        fail();
    }

    ///

    @Test
    public void testTryMapDExConsumer() {
        double expected = 0.0;
        OptionalDoubleMapper<String> mapper = tryMapD(s -> expected, ex -> fail());
        String in = "foo";
        OptionalDouble res = mapper.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsDouble() == expected);
    }

    @Test
    public void testTryMapDExConsumerThrowing() {
        AtomicBoolean success = new AtomicBoolean(false);
        OptionalDoubleMapper<String> mapper = tryMapD(s -> {
            throw new NoSuchElementException();
        }, ex -> success.set(true));
        String in = "foo";
        OptionalDouble res = mapper.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapDExConsumerNullPointer() {
        tryMapD(null, ex -> fail());
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testTryMapDExConsumerNullPointer2() {
        tryMapD(String::length, null);
        fail();
    }

    ///

    @Test
    public void testNotNullOnNonNullVal() {
        assertTrue(notNull.test(""));
    }

    @Test
    public void testNotNullOnNullVal() {
        assertFalse(notNull.test(null));
    }

    ///

    @Test
    public void testNotNullMethodOnNonNullVal() {
        assertTrue(notNull().test(""));
    }

    @Test
    public void testNotNullMethodOnNullVal() {
        assertFalse(notNull().test(null));
    }

    ///

    @Test
    public void testNotNullClassMethodOnNonNullVal() {
        assertTrue(notNull(String.class).test(""));
    }

    @Test
    public void testNotNullClassMethodOnNullVal() {
        assertFalse(notNull(String.class).test(null));
    }

    ///

    @Test
    public void testIsNullOnNonNullVal() {
        assertFalse(isNull.test(""));
    }

    @Test
    public void testIsNullOnNullVal() {
        assertTrue(isNull.test(null));
    }

    ///

    @Test
    public void testNullAware() {
        Function<String, String> f = identity();
        String in = "foo";
        Optional<String> out = nullAware(f).apply(in);
        assertNotNull(out);
        assertTrue(out.isPresent());
        assertTrue(in == out.get());
    }

    @Test
    public void testNullAwareNullInput() {
        Function<String, String> f = identity();
        Optional<String> out = nullAware(f).apply(null);
        assertNotNull(out);
        assertFalse(out.isPresent());
    }

    @Test
    public void testNullAwareToNullFunction() {
        Function<String, String> f = s -> null;
        Optional<String> out = nullAware(f).apply("foo");
        assertNotNull(out);
        assertFalse(out.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAwareNullPointer() {
        nullAware(null).apply("foo");
    }

    ///

    @Test
    public void testNullAwareI() {
        String in = "foo";
        int out = 42;
        ToIntFunction<String> f = i -> out;
        OptionalInt res = nullAwareI(f).apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsInt());
    }

    @Test
    public void testNullAwareINullInput() {
        int out = 42;
        ToIntFunction<String> f = i -> out;
        OptionalInt res = nullAwareI(f).apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAwareINullPointer() {
        nullAwareI(null).apply("foo");
    }

    ///

    @Test
    public void testNullAwareL() {
        String in = "foo";
        long out = 42;
        ToLongFunction<String> f = i -> out;
        OptionalLong res = nullAwareL(f).apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsLong());
    }

    @Test
    public void testNullAwareLNullInput() {
        long out = 42;
        ToLongFunction<String> f = i -> out;
        OptionalLong res = nullAwareL(f).apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAwareLNullPointer() {
        nullAwareL(null).apply("foo");
    }

    ///

    @Test
    public void testNullAwareD() {
        String in = "foo";
        double out = 0.0;
        ToDoubleFunction<String> f = i -> out;
        OptionalDouble res = nullAwareD(f).apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsDouble());
    }

    @Test
    public void testNullAwareDNullInput() {
        double out = 0.0;
        ToDoubleFunction<String> f = i -> out;
        OptionalDouble res = nullAwareD(f).apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAwareDNullPointer() {
        nullAwareD(null).apply("foo");
    }

    ///

    @Test
    public void testTestToTrue() {
        String in = "foo";
        boolean res = test(s -> s == in).test(in);
        assertTrue(res);
    }

    @Test
    public void testTestToFalse() {
        String in = "foo";
        boolean res = test(s -> s != in).test(in);
        assertFalse(res);
    }

    @Test(expected = NullPointerException.class)
    public void testTestNullPointer() {
        String in = "foo";
        test(null).test(in);
    }

    ///

    @Test
    public void testTestIToTrue() {
        int in = 42;
        boolean res = testI(s -> s == in).test(in);
        assertTrue(res);
    }

    @Test
    public void testTestIToFalse() {
        int in = 42;
        boolean res = testI(s -> s != in).test(in);
        assertFalse(res);
    }

    @Test(expected = NullPointerException.class)
    public void testTestINullPointer() {
        int in = 42;
        testI(null).test(in);
    }

    ///

    @Test
    public void testTestLToTrue() {
        long in = 42;
        boolean res = testL(s -> s == in).test(in);
        assertTrue(res);
    }

    @Test
    public void testTestLToFalse() {
        long in = 42;
        boolean res = testL(s -> s != in).test(in);
        assertFalse(res);
    }

    @Test(expected = NullPointerException.class)
    public void testTestLNullPointer() {
        long in = 42;
        testL(null).test(in);
    }

    ///

    @Test
    public void testTestDToTrue() {
        double in = 0.0;
        boolean res = testD(s -> s == in).test(in);
        assertTrue(res);
    }

    @Test
    public void testTestDToFalse() {
        double in = 0.0;
        boolean res = testD(s -> s != in).test(in);
        assertFalse(res);
    }

    @Test(expected = NullPointerException.class)
    public void testTestDNullPointer() {
        double in = 0.0;
        testD(null).test(in);
    }

    ///


    @Test
    public void testNotToFalse() {
        String in = "foo";
        boolean res = not(s -> s == in).test(in);
        assertFalse(res);
    }

    @Test
    public void testNotToTrue() {
        String in = "foo";
        boolean res = not(s -> s != in).test(in);
        assertTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullPointer() {
        String in = "foo";
        not(null).test(in);
    }

    ///

    @Test
    public void testNotIToFalse() {
        int in = 42;
        boolean res = notI(s -> s == in).test(in);
        assertFalse(res);
    }

    @Test
    public void testNotIToTrue() {
        int in = 42;
        boolean res = notI(s -> s != in).test(in);
        assertTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testNotINullPointer() {
        int in = 42;
        notI(null).test(in);
    }

    ///

    @Test
    public void testNotLToFalse() {
        long in = 42L;
        boolean res = notL(s -> s == in).test(in);
        assertFalse(res);
    }

    @Test
    public void testNotLToTrue() {
        long in = 42L;
        boolean res = notL(s -> s != in).test(in);
        assertTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testNotLNullPointer() {
        long in = 42L;
        notL(null).test(in);
    }

    ///

    @Test
    public void testNotDToFalse() {
        double in = 0.0d;
        boolean res = notD(s -> s == in).test(in);
        assertFalse(res);
    }

    @Test
    public void testNotDToTrue() {
        double in = 0.0d;
        boolean res = notD(s -> s != in).test(in);
        assertTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testNotDNullPointer() {
        double in = 0.0d;
        notD(null).test(in);
    }

    ///

    @Test
    public void testMap() {
        String in = "foo";
        String out = map((String s) -> s).apply(in);
        assertTrue(in == out);
    }

    @Test(expected = NullPointerException.class)
    public void testMapNullPointer() {
        map(null).apply("");
    }

    ///

    @Test
    public void testMapI() {
        int in = 42;
        int out = mapI((Integer i) -> i).applyAsInt(in);
        assertTrue(in == out);
    }

    @Test(expected = NullPointerException.class)
    public void testMapINullPointer() {
        mapI(null).applyAsInt("");
    }

    ///

    @Test
    public void testMapL() {
        long in = 42;
        long out = mapL((Long i) -> i).applyAsLong(in);
        assertTrue(in == out);
    }

    @Test(expected = NullPointerException.class)
    public void testMapLNullPointer() {
        mapL(null).applyAsLong("");
    }

    ///

    @Test
    public void testMapD() {
        double in = 0.0;
        double out = mapD((Double i) -> i).applyAsDouble(in);
        assertTrue(in == out);
    }

    @Test(expected = NullPointerException.class)
    public void testMapDNullPointer() {
        mapD(null).applyAsDouble("");
    }

    ///

    @Test
    public void testIsInteger() {
        int in = 42;
        OptionalInt res = isInteger.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.getAsInt());
    }

    @Test
    public void testIsIntegerForNoInt() {
        OptionalInt res = isInteger.apply("");
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testIsIntegerForNull() {
        OptionalInt res = isInteger.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testIsLong() {
        long in = 42;
        OptionalLong res = isLong.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.getAsLong());
    }

    @Test
    public void testIsLongForNoInt() {
        OptionalLong res = isLong.apply("");
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testIsLongForNull() {
        OptionalLong res = isLong.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testIsDouble() {
        double in = 0.0;
        OptionalDouble res = isDouble.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.getAsDouble());
    }

    @Test
    public void testIsDoubleForNoInt() {
        OptionalDouble res = isDouble.apply("");
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testIsDoubleForNull() {
        OptionalDouble res = isDouble.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testToString() {
        String in = "foo";
        String out = toString.apply(in);
        assertTrue(in == out);
    }

    @Test
    public void testToStringOfNull() {
        String expected = "null";
        String out = toString.apply(null);
        assertEquals(expected, out);
    }

    ///

    @Test
    public void testFilterLetThrough() {
        String in = "foo";
        Optional<String> res = filter((String s) -> true).apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testFilterOut() {
        String in = "foo";
        Optional<String> res = filter((String s) -> false).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFilterOutNull() {
        String in = "foo";
        Optional<String> res = filter((String s) -> true).apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullPointer() {
        filter(null).apply("");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterThrowing() {
        filter(s -> {
            throw new NoSuchElementException();
        }).apply("");
        fail();
    }

    ///

    @Test
    public void testCast() {
        Object in = "foo";
        Optional<String> res = cast(String.class).apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testCastSubclass() {
        Object o = new ArrayList<Object>();
        Optional<List> res = cast(List.class).apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(o == res.get());
    }

    @Test
    public void testCastNotMatchingClass() {
        Object in = "foo";
        Optional<Integer> res = cast(Integer.class).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testCastOnNull() {
        Optional<Integer> res = cast(Integer.class).apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testCastNullPointer() {
        cast(null).apply("");
        fail();
    }

    ///

    @Test
    public void testFilterNullAwareLetThrough() {
        String in = "foo";
        Optional<String> res = filterNullAware((String s) -> true).apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testFilterNullAwareNotLetThrough() {
        String in = "foo";
        Optional<String> res = filterNullAware((String s) -> false).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFilterNullWithNullInput() {
        Optional<String> res = filterNullAware((String s) -> true).apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullAwareNullPointer() {
        filterNullAware(null).apply("");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterNullAwareThrowing() {
        filterNullAware(s -> {
            throw new NoSuchElementException();
        }).apply("");
        fail();
    }

    ///

    @Test
    public void testTyped() {
        Object in = "foo";
        boolean res = typed(String.class).test(in);
        assertTrue(res);
    }

    @Test
    public void testTypedSubclassNotSameType() {
        Object o = new ArrayList<Object>();
        boolean res = typed(List.class).test(o);
        assertFalse(res);
    }

    @Test
    public void testTypedNotMatchingClass() {
        Object in = "foo";
        boolean res = typed(Integer.class).test(in);
        assertFalse(res);
    }

    @Test
    public void testTypedOnNull() {
        boolean res = typed(Integer.class).test(null);
        assertFalse(res);
    }

    @Test(expected = NullPointerException.class)
    public void testTypedNullPointer() {
        typed(null).test("");
        fail();
    }

    ///

    @Test
    public void testInstanceOf() {
        Object in = "foo";
        boolean res = instanceOf(String.class).test(in);
        assertTrue(res);
    }

    @Test
    public void testInstanceOfSubclassNotSameType() {
        Object o = new ArrayList<Object>();
        boolean res = instanceOf(List.class).test(o);
        assertTrue(res);
    }

    @Test
    public void testInstanceOfNotMatchingClass() {
        Object in = "foo";
        boolean res = instanceOf(Integer.class).test(in);
        assertFalse(res);
    }

    @Test
    public void testInstanceOfOnNull() {
        boolean res = instanceOf(Integer.class).test(null);
        assertFalse(res);
    }

    @Test(expected = NullPointerException.class)
    public void testInstanceOfNullPointer() {
        instanceOf(null).test("");
        fail();
    }

    ///

    @Test
    public void testEqTrue() {
        String in = "foo";
        boolean res = eq(in).test(in);
        assertTrue(res);
    }

    @Test
    public void testEqNulls() {
        boolean res = eq(null).test(null);
        assertTrue(res);
    }

    @Test
    public void testEqStrNull() {
        boolean res = eq("").test(null);
        assertFalse(res);
    }

    @Test
    public void testEqNullStr() {
        boolean res = eq(null).test("");
        assertFalse(res);
    }

    @Test
    public void testEqFalse() {
        String in = "foo";
        String tst = "bar";
        boolean res = eq(in).test(tst);
        assertFalse(res);
    }

    ///

    @Test
    public void testOneOfMatching() {
        String baz = "baz";
        boolean res = oneOf("foo", "bar", baz).test(baz);
        assertTrue(res);
    }

    @Test
    public void testOneOfMatchingArrayModification() {
        String baz = "baz";
        String[] arr = {"bar", baz};
        AdvPredicate<String> test = oneOf("foo", arr);
        arr[1] = "woo";
        boolean res = test.test(baz);
        assertTrue(res);
    }

    @Test
    public void testOneOfNoMatch() {
        boolean res = oneOf("foo", "bar", "baz").test("woo");
        assertFalse(res);
    }

    @Test(expected = NullPointerException.class)
    public void testOneOfNullPointer() {
        String[] args = null;
        oneOf("foo", args).test("");
        fail();
    }

    @Test
    public void testOneOfTestNull() {
        boolean tst = oneOf("foo", "bar", "baz").test(null);
        assertFalse(tst);
    }

    ///

    @Test
    public void testRefEqTrue() {
        String in = "foo";
        boolean res = refEq(in).test(in);
        assertTrue(res);
    }

    @Test
    public void testRefEqFalse() {
        String in = "foo";
        String test = "bar";
        boolean res = refEq(in).test(test);
        assertFalse(res);
    }

    ///

    @Test
    public void testAnyWithObject() {
       assertTrue(__.test(""));
    }

    @Test
    public void testAnyWithNull() {
        assertTrue( __.test(null) );
    }

    ///

    @Test
    public void testAnyInteger() {
        assertTrue(_I.test(42));
    }

    @Test
    public void testAnyIntegerMinVal() {
        assertTrue(_I.test(Integer.MIN_VALUE));
    }

    ///

    @Test
    public void testAnyLong() {
        assertTrue(_L.test(42L));
    }

    @Test
    public void testAnyLongMinVal() {
        assertTrue(_L.test(Long.MIN_VALUE));
    }

    ///

    @Test
    public void testAnyDouble() {
        assertTrue(_D.test(0.0d));
    }

    @Test
    public void testAnyDoubleMinVal() {
        assertTrue(_D.test(Long.MIN_VALUE));
    }
}
