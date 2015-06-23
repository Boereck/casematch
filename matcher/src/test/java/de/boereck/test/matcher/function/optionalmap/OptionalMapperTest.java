package de.boereck.test.matcher.function.optionalmap;

import de.boereck.matcher.function.optionalmap.OptionalDoubleMapper;
import de.boereck.matcher.function.optionalmap.OptionalIntMapper;
import de.boereck.matcher.function.optionalmap.OptionalLongMapper;
import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.testable.TestableFunction;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static java.util.function.Function.identity;
import static org.junit.Assert.*;

public class OptionalMapperTest {

    @Test
    public void testMap() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, String> mapped = om.map(identity());
        String in = "foo";
        Optional<String> res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testMapThisReturnsNull() {
        String in = "foo";
        OptionalMapper<String, String> om = s -> null;
        Optional<String> res = om.map(identity()).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapOfNull() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, String> mapped = om.map(identity());
        Optional<String> res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapOfNullFunction() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, String> mapped = om.map(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapThrowing() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, String> mapped = om.map(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    ///

    @Test
    public void testMapI() {
        String in = "foo";
        int out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalIntMapper<String> mapped = om.mapI(s -> out);
        OptionalInt res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsInt());
    }

    @Test
    public void testMapIThisReturnsNull() {
        String in = "foo";
        OptionalMapper<String, String> om = s -> null;
        OptionalInt res = om.mapI(s -> 42).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapIOfNull() {
        String in = "foo";
        int out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalIntMapper<String> mapped = om.mapI(s -> out);
        OptionalInt res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapIOfNullFunction() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalIntMapper<String> mapped = om.mapI(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapIThrowing() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalIntMapper<String> mapped = om.mapI(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    ///

    @Test
    public void testMapL() {
        String in = "foo";
        long out = 42L;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalLongMapper<String> mapped = om.mapL(s -> out);
        OptionalLong res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsLong());
    }

    @Test
    public void testMapLThisReturnsNull() {
        String in = "foo";
        OptionalMapper<String, String> om = s -> null;
        OptionalLong res = om.mapL(s -> 42).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapLOfNull() {
        String in = "foo";
        int out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalLongMapper<String> mapped = om.mapL(s -> out);
        OptionalLong res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapLOfNullFunction() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalLongMapper<String> mapped = om.mapL(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapLThrowing() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalLongMapper<String> mapped = om.mapL(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    ///

    @Test
    public void testMapD() {
        String in = "foo";
        double out = 0.0;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalDoubleMapper<String> mapped = om.mapD(s -> out);
        OptionalDouble res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsDouble());
    }

    @Test
    public void testMapDThisReturnsNull() {
        String in = "foo";
        OptionalMapper<String, String> om = s -> null;
        OptionalDouble res = om.mapD(s -> 42).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapDOfNull() {
        String in = "foo";
        int out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalDoubleMapper<String> mapped = om.mapD(s -> out);
        OptionalDouble res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapDOfNullFunction() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalDoubleMapper<String> mapped = om.mapD(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapDThrowing() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalDoubleMapper<String> mapped = om.mapD(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    ///

    @Test
    public void testFlatMap() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, String> mapped = om.flatMap(Optional::ofNullable);
        String in = "foo";
        Optional<String> res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testFlatMapThisReturnsNull() {
        String in = "foo";
        OptionalMapper<String, String> om = s -> null;
        Optional<String> res = om.flatMap(Optional::ofNullable).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapOfNull() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, String> mapped = om.flatMap(Optional::ofNullable);
        Optional<String> res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapOfNullFunction() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        om.flatMap(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapThrowing() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, String> mapped = om.flatMap(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    ///

    @Test
    public void testFlatMapI() {
        String in = "foo";
        int out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalIntMapper<String> mapped = om.flatMapI(s -> OptionalInt.of(out));
        OptionalInt res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsInt());
    }

    @Test
    public void testFlatMapIThisReturnsNull() {
        String in = "foo";
        int out = 42;
        OptionalIntMapper<String> om = s -> null;
        OptionalInt res = om.flatMapI(s -> OptionalInt.of(out)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapIOfNull() {
        int out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalIntMapper<String> mapped = om.flatMapI(s -> OptionalInt.of(out));
        OptionalInt res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapIOfNullFunction() {
        int out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        om.flatMapI(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapIThrowing() {
        int out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalIntMapper<String> mapped = om.flatMapI(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    ///

    @Test
    public void testFlatMapL() {
        String in = "foo";
        int out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalLongMapper<String> mapped = om.flatMapL(s -> OptionalLong.of(out));
        OptionalLong res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsLong());
    }

    @Test
    public void testFlatMapLThisReturnsNull() {
        String in = "foo";
        long out = 42;
        OptionalMapper<String, String> om = s -> null;
        OptionalLong res = om.flatMapL(s -> OptionalLong.of(out)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapLOfNull() {
        long out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalLongMapper<String> mapped = om.flatMapL(s -> OptionalLong.of(out));
        OptionalLong res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapLOfNullFunction() {
        long out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        om.flatMapL(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapLThrowing() {
        long out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalLongMapper<String> mapped = om.flatMapL(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    ///

    @Test
    public void testFlatMapD() {
        String in = "foo";
        int out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalDoubleMapper<String> mapped = om.flatMapD(s -> OptionalDouble.of(out));
        OptionalDouble res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsDouble());
    }

    @Test
    public void testFlatMapDThisReturnsNull() {
        String in = "foo";
        long out = 42;
        OptionalMapper<String, String> om = s -> null;
        OptionalDouble res = om.flatMapD(s -> OptionalDouble.of(out)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapDOfNull() {
        long out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalDoubleMapper<String> mapped = om.flatMapD(s -> OptionalDouble.of(out));
        OptionalDouble res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapDOfNullFunction() {
        long out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        om.flatMapD(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapDThrowing() {
        long out = 42;
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalDoubleMapper<String> mapped = om.flatMapD(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    ///

    @Test
    public void tsetFilterByClassLetThrough() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, String> doFilter = om.filter(String.class);
        String s = "foo";
        Optional<String> res = doFilter.apply(s);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(s == res.get());
    }

    @Test
    public void testFilterByClassNotLetThrough() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, Integer> doFilter = om.filter(Integer.class);
        String s = "foo";
        Optional<Integer> res = doFilter.apply(s);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFilterByClassLetSubclassThrough() {
        OptionalMapper<Object, Object> om = Optional::ofNullable;
        OptionalMapper<Object, List> doFilter = om.filter(List.class);
        Object s = new ArrayList();
        Optional<List> res = doFilter.apply(s);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(s == res.get());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterByClassNullPointer() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        om.filter((Class) null);
    }

    @Test
    public void testFilterByClassThisReturnsNull() {
        OptionalMapper<String, String> om = i -> null;
        OptionalMapper<String, String> doFilter = om.filter(String.class);
        String s = "foo";
        Optional<String> res = doFilter.apply(s);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testFilterByPredicateLetThrough() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, String> filtered = om.filter(s -> true);
        String in = "foo";
        Optional<String> res = filtered.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testFilterByPredicateNotLetThrough() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, String> filtered = om.filter(s -> false);
        String in = "foo";
        Optional<String> res = filtered.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterByPredicateThrowing() {
        OptionalMapper<String, String> om = Optional::ofNullable;
        OptionalMapper<String, String> filtered = om.filter(s -> {
            throw new NoSuchElementException();
        });
        filtered.apply("foo");
        fail();
    }

    @Test
    public void testFilterByPredicateThisReturnsNull() {
        OptionalMapper<String, String> om = s -> null;
        OptionalMapper<String, String> filtered = om.filter(s -> true);
        String in = "foo";
        Optional<String> res = filtered.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterByPredicateThisThrowing() {
        OptionalMapper<String, String> om = s -> {
            throw new NoSuchElementException();
        };
        OptionalMapper<String, String> filtered = om.filter(s -> true);
        filtered.apply("foo");
        fail();
    }


    ///

    @Test
    public void testHasResultHavingNoResult() {
        OptionalMapper<String, String> om = i -> Optional.empty();
        Predicate<String> pred = om.hasResult();
        assertFalse(pred.test("foo"));
    }

    @Test
    public void testHasResultHavingResult() {
        OptionalMapper<String, String> om = i -> Optional.of("bar");
        Predicate<String> pred = om.hasResult();
        assertTrue(pred.test("foo"));
    }

    @Test
    public void testHasResultThisReturnsNull() {
        OptionalMapper<String, String> om = i -> null;
        Predicate<String> pred = om.hasResult();
        assertFalse(pred.test("foo"));
    }

    ///

    @Test
    public void testHasResultAndHavingNoResultFilterTrue() {
        OptionalMapper<String, String> om = i -> Optional.empty();
        String in = "foo";
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertFalse(pred.test(in));
    }

    @Test
    public void testHasResultAndHavingNoResultFilterFalse() {
        OptionalMapper<String, String> om = i -> Optional.empty();
        String in = "foo";
        Predicate<String> pred = om.hasResultAnd(s -> false);
        assertFalse(pred.test(in));
    }

    @Test
    public void testHasResultAndHavingResultFilterTrue() {
        String in = "foo";
        OptionalMapper<String, String> om = i -> Optional.of(in);
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertTrue(pred.test(in));
    }

    @Test
    public void testHasResultAndHavingResultFilterFalse() {
        String in = "foo";
        OptionalMapper<String, String> om = i -> Optional.of(in);
        Predicate<String> pred = om.hasResultAnd(s -> false);
        assertFalse(pred.test(in));
    }

    @Test(expected = NoSuchElementException.class)
    public void testHasResultPredicateThrowing() {
        String in = "foo";
        OptionalMapper<String, String> om = i -> Optional.of(in);
        Predicate<String> pred = om.hasResultAnd(s -> {
            throw new NoSuchElementException();
        });
        pred.test(in);
        fail();
    }

    @Test
    public void testHasResultAndThisReturnsNull() {
        String in = "foo";
        OptionalMapper<String, String> om = i -> null;
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertFalse(pred.test(in));
    }

    ///

    @Test
    public void testThenDoWithOptional() {
        AtomicBoolean success = new AtomicBoolean(false);
        String in = "foo";
        OptionalMapper<String, String> om = Optional::of;
        om.thenDo(o -> {
            assertNotNull(o);
            assertTrue(o.isPresent());
            assertTrue(in == o.get());
            success.set(true);
        }).accept(in);
        assertTrue(success.get());
    }

    @Test
    public void testThenDoWithEmptyOptional() {
        AtomicBoolean success = new AtomicBoolean(false);
        OptionalMapper<String, String> om = i -> Optional.empty();
        om.thenDo(o -> {
            assertNotNull(o);
            assertFalse(o.isPresent());
            success.set(true);
        }).accept("foo");
        assertTrue(success.get());
    }

    @Test
    public void testThenDoThisReturnsNull() {
        AtomicBoolean success = new AtomicBoolean(false);
        OptionalMapper<String, String> om = i -> null;
        om.thenDo(o -> {
            assertNotNull(o);
            assertFalse(o.isPresent());
            success.set(true);
        }).accept("foo");
        assertTrue(success.get());
    }

    ///

    @Test
    public void testThenIfPresentIsPresent() {
        AtomicBoolean success = new AtomicBoolean(false);
        String out = "foo";
        OptionalMapper<String, String> om = i -> Optional.of(out);
        om.thenIfPresent(s -> {
            assertTrue(s == out);
            success.set(true);
        }).accept(out);
        assertTrue(success.get());
    }

    @Test
    public void testThenIfPresentIsNotPresent() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> Optional.empty();
        om.thenIfPresent(s -> {
            fail();
        }).accept(out);
    }

    @Test
    public void testThenIfPresentThisReturnsNull() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> null;
        om.thenIfPresent(s -> {
            fail();
        }).accept(out);
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenIfPresentConsumerThrowing() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> Optional.of(out);
        om.thenIfPresent(s -> {
            throw new NoSuchElementException();
        }).accept(out);
    }

    ///

    @Test
    public void testOrElseThisProvidesValue() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> Optional.of(out);
        TestableFunction<String, String> elsed = om.orElse("bar");
        String res = elsed.apply("");
        assertTrue(out == res);
    }

    @Test
    public void testOrElseTakeElseValue() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> Optional.empty();
        TestableFunction<String, String> elsed = om.orElse(out);
        String res = elsed.apply("");
        assertTrue(out == res);
    }

    @Test
    public void testOrElseThisReturnsNull() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> null;
        TestableFunction<String, String> elsed = om.orElse(out);
        String res = elsed.apply("");
        assertTrue(out == res);
    }

    @Test
    public void testOrElseTakeElseValueNull() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> Optional.empty();
        TestableFunction<String, String> elsed = om.orElse(null);
        String res = elsed.apply("");
        assertNull(res);
    }

    ///

    @Test
    public void testOrElseGetWithValueFromSupplier() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> Optional.empty();
        TestableFunction<String, String> elseMap = om.orElseGet(() -> out);
        String res = elseMap.apply("");
        assertTrue(res == out);
    }

    @Test
    public void testOrElseGetThisReturningNull() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> null;
        TestableFunction<String, String> elseMap = om.orElseGet(() -> out);
        String res = elseMap.apply("");
        assertTrue(res == out);
    }

    @Test
    public void testOrElseGetWithOriginalValue() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> Optional.of(out);
        TestableFunction<String, String> elseMap = om.orElseGet(() -> "bar");
        String res = elseMap.apply("");
        assertTrue(res == out);
    }

    @Test
    public void testOrElseGetNullSupplier() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> Optional.empty();
        TestableFunction<String, String> elseMap = om.orElseGet(() -> null);
        String res = elseMap.apply("");
        assertNull(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseGetSupplierThrowing() {
        String out = "foo";
        OptionalMapper<String, String> om = i -> Optional.empty();
        TestableFunction<String, String> elseMap = om.orElseGet(() -> {
            throw new NoSuchElementException();
        });
        elseMap.apply("");
        fail();
    }

    ///

    @Test
    public void testNullAwareNotNull() {
        String out = "foo";
        OptionalMapper<String, String> om = Optional::of;
        OptionalMapper<String, String> omNullAware = om.nullAware();
        Optional<String> res = omNullAware.apply(out);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.get());
    }

    @Test
    public void tesNullAwareWithNullInput() {
        String out = "foo";
        OptionalMapper<String, String> om = Optional::of;
        OptionalMapper<String, String> omNullAware = om.nullAware();
        Optional<String> res = omNullAware.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void tesNullAwareWithNullResult() {
        OptionalMapper<String, String> om = i -> null;
        OptionalMapper<String, String> omNullAware = om.nullAware();
        Optional<String> res = omNullAware.apply("foo");
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testWithCatch() {
        OptionalMapper<String, String> om = Optional::of;
        String expected = "foo";
        Optional<String> result = om.withCatch().apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get() == expected);
    }

    @Test
    public void testWithCatchThrowing() {
        OptionalMapper<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String expected = "foo";
        Optional<String> result = throwing.withCatch().apply(expected);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }


    ///

    @Test
    public void testWithCatchByClass() {
        OptionalMapper<String, String> om = Optional::of;
        String expected = "foo";
        Optional<String> result = om.withCatch(NoSuchElementException.class, e -> fail()).apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get() == expected);
    }

    @Test
    public void testWithCatchByClassThrowingCaught() {
        OptionalMapper<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        AtomicBoolean result = new AtomicBoolean(false);
        Optional<String> empty = throwing.withCatch(NoSuchElementException.class, e -> result.set(true)).apply(input);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
        assertTrue(result.get());
    }

    @Test(expected = NoSuchElementException.class)
    public void testWithCatchByClassThrowingUncaught() {
        OptionalMapper<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        throwing.withCatch(UnsupportedOperationException.class, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer1() {
        OptionalMapper<String, String> id = Optional::of;
        String input = "foo";
        id.withCatch(null, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer2() {
        OptionalMapper<String, String> id = Optional::of;
        String input = "foo";
        id.withCatch(NoSuchElementException.class, null).apply(input);
        fail();
    }


    ///

    @Test
    public void testWithCatchHandler() {
        OptionalMapper<String, String> id = Optional::of;
        String expected = "foo";
        Optional<String> result = id.withCatch(e -> fail()).apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get() == expected);
    }

    @Test
    public void testWithCatchHandlerThrowingCaught() {
        OptionalMapper<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        AtomicBoolean result = new AtomicBoolean(false);
        Optional<String> empty = throwing.withCatch(e -> result.set(true)).apply(input);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
        assertTrue(result.get());
    }

    @Test(expected = Error.class)
    public void testWithCatchHandlerThrowingUncaught() {
        OptionalMapper<String, String> throwing = s -> {
            throw new Error();
        };
        String input = "foo";
        throwing.withCatch(e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchHandlerNullPointer() {
        OptionalMapper<String, String> id = Optional::of;
        String input = "foo";
        id.withCatch(null).apply(input);
        fail();
    }

    ///

    @Test
    public void testRecoverWith() {
        OptionalMapper<String, String> id = Optional::of;
        String expected = "foo";
        Optional<String> result = id.recoverWith(t -> {
            fail();
            return null;
        }).apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get() == expected);
    }

    @Test
    public void testRecoverWithThrowingRecovered() {
        OptionalMapper<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        String expected = "bar";
        Optional<String> result = throwing.recoverWith(e -> Optional.of(expected)).apply(input);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get() == expected);
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithNullPointer() {
        OptionalMapper<String, String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(null);
        fail();
    }

    ///

    @Test
    public void testRecoverWithByClass() {
        OptionalMapper<String, String> id = Optional::of;
        String expected = "foo";
        Optional<String> result = id.recoverWith(Throwable.class, t -> {
            fail();
            return null;
        }).apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get() == expected);
    }

    @Test
    public void testRecoverWithByClassThrowingRecovered() {
        OptionalMapper<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        String expected = "bar";
        Optional<String> result = throwing.recoverWith(NoSuchElementException.class, e -> Optional.of(expected)).apply(input);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get() == expected);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRecoverWithByClassThrowingNotRecovered() {
        OptionalMapper<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        throwing.recoverWith(UnsupportedOperationException.class, e -> {
            fail();
            return null;
        }).apply("foo");
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithByClassNullPointer1() {
        OptionalMapper<String, String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(null, e -> {
            fail();
            return null;
        }).apply("foo");
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithByClassNullPointer2() {
        OptionalMapper<String, String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(Exception.class, null).apply("foo");
        fail();
    }

    ///

    @Test
    public void testPartialDefinedForInput() {
        OptionalMapper<String, String> om = Optional::of;
        String in = "foo";
        String out = om.partial().apply(in);
        assertTrue(in == out);
    }

    @Test(expected = NoSuchElementException.class)
    public void testPartialUndefined() {
        OptionalMapper<String, String> om = s -> Optional.empty();
        String in = "foo";
        om.partial().apply(in);
        fail();
    }
}
