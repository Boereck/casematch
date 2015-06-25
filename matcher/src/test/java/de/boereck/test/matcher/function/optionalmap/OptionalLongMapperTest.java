package de.boereck.test.matcher.function.optionalmap;

import de.boereck.matcher.function.optionalmap.OptionalDoubleMapper;
import de.boereck.matcher.function.optionalmap.OptionalIntMapper;
import de.boereck.matcher.function.optionalmap.OptionalLongMapper;
import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.testable.TestableToLongFunction;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class OptionalLongMapperTest {

    @Test
    public void testMap() {
        long out = 42;
        String in = "foo";
        OptionalLongMapper<String> om = s -> OptionalLong.of(out);
        OptionalMapper<String,String> mapped = om.map(i -> in);
        Optional<String> res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testMapThisReturnsNull() {
        String in = "foo";
        OptionalLongMapper<String> om = s -> null;
        Optional<String> res = om.map(i -> in).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapOfEmpty() {
        OptionalLongMapper<String> om = s -> OptionalLong.empty();
        OptionalMapper<String,String> mapped = om.map((long i) -> "foo");
        Optional<String> res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapOfNullFunction() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        om.map(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapThrowing() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        OptionalMapper<String,String> mapped = om.map(s -> {
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
        OptionalLongMapper<String> om = s -> OptionalLong.of(out);
        OptionalIntMapper<String> mapped = om.mapI(i -> out);
        OptionalInt res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsInt());
    }

    @Test
    public void testMapIThisReturnsNull() {
        String in = "foo";
        OptionalLongMapper<String> om = s -> null;
        OptionalInt res = om.mapI(s -> 42).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapIOfNullFunction() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        om.mapI(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapIThrowing() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
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
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        OptionalLongMapper<String> mapped = om.mapL(s -> out);
        OptionalLong res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsLong());
    }

    @Test
    public void testMapLThisReturnsNull() {
        String in = "foo";
        OptionalLongMapper<String> om = s -> null;
        OptionalLong res = om.mapL(s -> 42).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapLOfEmpty() {
        long out = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.empty();
        OptionalLongMapper<String> mapped = om.mapL(s -> out);
        OptionalLong res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapLOfNullFunction() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        om.mapL(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapLThrowing() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
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
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        OptionalDoubleMapper<String> mapped = om.mapD(s -> out);
        OptionalDouble res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsDouble());
    }

    @Test
    public void testMapDThisReturnsNull() {
        String in = "foo";
        OptionalLongMapper<String> om = s -> null;
        OptionalDouble res = om.mapD(s -> 42).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapDOfEmpty() {
        long out = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.empty();
        OptionalDoubleMapper<String> mapped = om.mapD(s -> out);
        OptionalDouble res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapDOfNullFunction() {
        OptionalLongMapper<String> om = i -> OptionalLong.of(42);
         om.mapD(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapDThrowing() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        OptionalDoubleMapper<String> mapped = om.mapD(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    ///

    @Test
    public void testFlatMap() {
        String in = "foo";
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        OptionalMapper<String, String> mapped = om.flatMap(s -> Optional.of(in));
        Optional<String> res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testFlatMapThisReturnsNull() {
        String in = "foo";
        OptionalLongMapper<String> om = s -> null;
        Optional<String> res = om.flatMap(s -> Optional.of(in)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapOfEmpty() {
        OptionalLongMapper<String> om = s -> OptionalLong.empty();
        OptionalMapper<String,String> mapped = om.flatMap(i -> Optional.of("bar"));
        Optional<String> res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapOfNullFunction() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        om.flatMap(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapThrowing() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        OptionalMapper<String,String> mapped = om.flatMap(s -> {
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
        OptionalLongMapper<String> om = s -> OptionalLong.of(out);
        OptionalIntMapper<String> mapped = om.flatMapI(i -> OptionalInt.of(out));
        OptionalInt res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsInt());
    }

    @Test
    public void testFlatMapIThisReturnsNull() {
        String in = "foo";
        int out = 42;
        OptionalLongMapper<String> om = s -> null;
        OptionalInt res = om.flatMapI(s -> OptionalInt.of(out)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapIOfEmpty() {
        int out = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.empty();
        OptionalIntMapper<String> mapped = om.flatMapI(s -> OptionalInt.of(out));
        OptionalInt res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapIOfNullFunction() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        om.flatMapI(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapIThrowing() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
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
        long out = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
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
        OptionalLongMapper<String> om = s -> null;
        OptionalLong res = om.flatMapL(s -> OptionalLong.of(out)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapLOfEmpty() {
        long out = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.empty();
        OptionalLongMapper<String> mapped = om.flatMapL(s -> OptionalLong.of(out));
        OptionalLong res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapLOfNullFunction() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        om.flatMapL(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapLThrowing() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
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
        long out = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
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
        OptionalLongMapper<String> om = s -> null;
        OptionalDouble res = om.flatMapD(s -> OptionalDouble.of(out)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapDOfEmpty() {
        long out = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.empty();
        OptionalDoubleMapper<String> mapped = om.flatMapD(s -> OptionalDouble.of(out));
        OptionalDouble res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapDOfNullFunction() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        om.flatMapD(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapDThrowing() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        OptionalDoubleMapper<String> mapped = om.flatMapD(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    @Test
    public void testFilterByPredicateLetThrough() {
        String in = "foo";
        long out = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.of(out);
        OptionalLongMapper<String> filtered = om.filter(s -> true);
        OptionalLong res = filtered.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsLong());
    }

    @Test
    public void testFilterByPredicateNotLetThrough() {
        long out = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.of(out);
        OptionalLongMapper<String> filtered = om.filter(s -> false);
        String in = "foo";
        OptionalLong res = filtered.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterByPredicateThrowing() {
        long out = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.of(out);
        OptionalLongMapper<String> filtered = om.filter(s -> {
            throw new NoSuchElementException();
        });
        filtered.apply("foo");
        fail();
    }

    @Test
    public void testFilterByPredicateThisReturnsNull() {
        OptionalLongMapper<String> om = s -> null;
        OptionalLongMapper<String> filtered = om.filter(s -> true);
        String in = "foo";
        OptionalLong res = filtered.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterByPredicateThisThrowing() {
        OptionalLongMapper<String> om = s -> {
            throw new NoSuchElementException();
        };
        OptionalLongMapper<String> filtered = om.filter(s -> true);
        filtered.apply("foo");
        fail();
    }


    ///

    @Test
    public void testHasResultHavingNoResult() {
        OptionalLongMapper<String> om = i -> OptionalLong.empty();
        Predicate<String> pred = om.hasResult();
        assertFalse(pred.test("foo"));
    }

    @Test
    public void testHasResultHavingResult() {
        OptionalLongMapper<String> om = i -> OptionalLong.of(42);
        Predicate<String> pred = om.hasResult();
        assertTrue(pred.test("foo"));
    }

    @Test
    public void testHasResultThisReturnsNull() {
        OptionalLongMapper<String> om = i -> null;
        Predicate<String> pred = om.hasResult();
        assertFalse(pred.test("foo"));
    }

    ///

    @Test
    public void testHasResultAndHavingNoResultFilterTrue() {
        OptionalLongMapper<String> om = i -> OptionalLong.empty();
        String in = "foo";
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertFalse(pred.test(in));
    }

    @Test
    public void testHasResultAndHavingNoResultFilterFalse() {
        OptionalLongMapper<String> om = i -> OptionalLong.empty();
        String in = "foo";
        Predicate<String> pred = om.hasResultAnd(s -> false);
        assertFalse(pred.test(in));
    }

    @Test
    public void testHasResultAndHavingResultFilterTrue() {
        long in = 42;
        OptionalLongMapper<String> om = i -> OptionalLong.of(in);
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertTrue(pred.test("foo"));
    }

    @Test
    public void testHasResultAndHavingResultFilterFalse() {
        OptionalLongMapper<String> om = i -> OptionalLong.of(42);
        Predicate<String> pred = om.hasResultAnd(s -> false);
        assertFalse(pred.test("foo"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testHasResultPredicateThrowing() {
        String in = "foo";
        OptionalLongMapper<String> om = i -> OptionalLong.of(42);
        Predicate<String> pred = om.hasResultAnd(s -> {
            throw new NoSuchElementException();
        });
        pred.test(in);
        fail();
    }

    @Test
    public void testHasResultAndThisReturnsNull() {
        String in = "foo";
        OptionalLongMapper<String> om = i -> null;
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertFalse(pred.test(in));
    }

    ///

    @Test
    public void testThenDoWithOptional() {
        AtomicBoolean success = new AtomicBoolean(false);
        String in = "foo";
        long out = 42;
        OptionalLongMapper<String> om = i -> OptionalLong.of(out);
        om.thenDo(o -> {
            assertNotNull(o);
            assertTrue(o.isPresent());
            assertTrue(out == o.getAsLong());
            success.set(true);
        }).accept(in);
        assertTrue(success.get());
    }

    @Test
    public void testThenDoWithEmptyOptional() {
        AtomicBoolean success = new AtomicBoolean(false);
        OptionalLongMapper<String> om = i -> OptionalLong.empty();
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
        OptionalLongMapper<String> om = i -> null;
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
        long out = 42;
        OptionalLongMapper<String> om = i -> OptionalLong.of(out);
        om.thenIfPresent(s -> {
            assertTrue(s == out);
            success.set(true);
        }).accept("foo");
        assertTrue(success.get());
    }

    @Test
    public void testThenIfPresentIsNotPresent() {
        String out = "foo";
        OptionalLongMapper<String> om = i -> OptionalLong.empty();
        om.thenIfPresent(s -> fail()).accept(out);
    }

    @Test
    public void testThenIfPresentThisReturnsNull() {
        String out = "foo";
        OptionalLongMapper<String> om = i -> null;
        om.thenIfPresent(s -> fail()).accept(out);
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenIfPresentConsumerThrowing() {
        long out = 42;
        OptionalLongMapper<String> om = i -> OptionalLong.of(out);
        om.thenIfPresent(s -> {
            throw new NoSuchElementException();
        }).accept("foo");
    }

    ///

    @Test
    public void testOrElseThisProvidesValue() {
        long out = 42;
        OptionalLongMapper<String> om = i -> OptionalLong.of(out);
        TestableToLongFunction<String> elsed = om.orElse(43);
        long res = elsed.applyAsLong("");
        assertTrue(out == res);
    }

    @Test
    public void testOrElseTakeElseValue() {
        long out = 43;
        OptionalLongMapper<String> om = i -> OptionalLong.empty();
        TestableToLongFunction<String> elsed = om.orElse(out);
        long res = elsed.applyAsLong("");
        assertTrue(out == res);
    }

    @Test
    public void testOrElseThisReturnsNull() {
        long out = 43;
        OptionalLongMapper<String> om = i -> null;
        TestableToLongFunction<String> elsed = om.orElse(out);
        long res = elsed.applyAsLong("");
        assertTrue(out == res);
    }

    ///

    @Test
    public void testOrElseGetWithValueFromSupplier() {
        long out = 42;
        OptionalLongMapper<String> om = i -> OptionalLong.empty();
        TestableToLongFunction<String> elseMap = om.orElseGet(() -> out);
        long res = elseMap.applyAsLong("");
        assertTrue(res == out);
    }

    @Test
    public void testOrElseGetThisReturningNull() {
        long out = 42;
        OptionalLongMapper<String> om = i -> null;
        TestableToLongFunction<String> elseMap = om.orElseGet(() -> out);
        long res = elseMap.applyAsLong("");
        assertTrue(res == out);
    }

    @Test
    public void testOrElseGetWithOriginalValue() {
        long out = 42;
        OptionalLongMapper<String> om = i -> OptionalLong.of(out);
        TestableToLongFunction<String> elseMap = om.orElseGet(() -> 43);
        long res = elseMap.applyAsLong("");
        assertTrue(res == out);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseGetSupplierThrowing() {
        OptionalLongMapper<String> om = i -> OptionalLong.empty();
        TestableToLongFunction<String> elseMap = om.orElseGet(() -> {
            throw new NoSuchElementException();
        });
        elseMap.applyAsLong("");
        fail();
    }

    ///

    @Test
    public void testNullAwareNotNull() {
        long out = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.of(out);
        OptionalLongMapper<String> omNullAware = om.nullAware();
        OptionalLong res = omNullAware.apply("foo");
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsLong());
    }

    @Test
    public void tesNullAwareWithNullInput() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        OptionalLongMapper<String> omNullAware = om.nullAware();
        OptionalLong res = omNullAware.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void tesNullAwareWithNullResult() {
        OptionalLongMapper<String> om = i -> null;
        OptionalLongMapper<String> omNullAware = om.nullAware();
        OptionalLong res = omNullAware.apply("foo");
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testWithCatch() {
        long expected = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.of(expected);
        OptionalLong result = om.withCatch().apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsLong() == expected);
    }

    @Test
    public void testWithCatchThrowing() {
        OptionalLongMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String expected = "foo";
        OptionalLong result = throwing.withCatch().apply(expected);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }


    ///

    @Test
    public void testWithCatchByClass() {
        long expected = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.of(expected);
        OptionalLong result = om.withCatch(NoSuchElementException.class, e -> fail()).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsLong() == expected);
    }

    @Test
    public void testWithCatchByClassThrowingCaught() {
        OptionalLongMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        AtomicBoolean result = new AtomicBoolean(false);
        OptionalLong empty = throwing.withCatch(NoSuchElementException.class, e -> result.set(true)).apply(input);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
        assertTrue(result.get());
    }

    @Test(expected = NoSuchElementException.class)
    public void testWithCatchByClassThrowingUncaught() {
        OptionalLongMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        throwing.withCatch(UnsupportedOperationException.class, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer1() {
        OptionalLongMapper<String> id = s -> OptionalLong.of(42);
        String input = "foo";
        id.withCatch(null, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer2() {
        OptionalLongMapper<String> id = s -> OptionalLong.of(42);
        String input = "foo";
        id.withCatch(NoSuchElementException.class, null).apply(input);
        fail();
    }


    ///

    @Test
    public void testWithCatchHandler() {
        long expected = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.of(expected);
        OptionalLong result = om.withCatch(e -> fail()).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsLong() == expected);
    }

    @Test
    public void testWithCatchHandlerThrowingCaught() {
        OptionalLongMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        AtomicBoolean result = new AtomicBoolean(false);
        OptionalLong empty = throwing.withCatch(e -> result.set(true)).apply(input);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
        assertTrue(result.get());
    }

    @Test(expected = Error.class)
    public void testWithCatchHandlerThrowingUncaught() {
        OptionalLongMapper<String> throwing = s -> {
            throw new Error();
        };
        String input = "foo";
        throwing.withCatch(e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchHandlerNullPointer() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        String input = "foo";
        om.withCatch(null).apply(input);
        fail();
    }

    ///

    @Test
    public void testRecoverWith() {
        long expected = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.of(expected);
        OptionalLong result = om.recoverWith(t -> {
            fail();
            return null;
        }).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsLong() == expected);
    }

    @Test
    public void testRecoverWithThrowingRecovered() {
        OptionalLongMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        long expected = 42;
        OptionalLong result = throwing.recoverWith(e -> OptionalLong.of(expected)).apply(input);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsLong() == expected);
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithNullPointer() {
        OptionalLongMapper<String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(null);
        fail();
    }

    ///

    @Test
    public void testRecoverWithByClass() {
        OptionalLongMapper<String> om = s -> OptionalLong.of(42);
        long expected = 42;
        OptionalLong result = om.recoverWith(Throwable.class, t -> {
            fail();
            return null;
        }).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsLong() == expected);
    }

    @Test
    public void testRecoverWithByClassThrowingRecovered() {
        OptionalLongMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        long expected = 42;
        OptionalLong result = throwing.recoverWith(NoSuchElementException.class, e -> OptionalLong.of(expected)).apply(input);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsLong() == expected);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRecoverWithByClassThrowingNotRecovered() {
        OptionalLongMapper<String> throwing = s -> {
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
        OptionalLongMapper<String> failing = s -> {
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
        OptionalLongMapper<String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(Exception.class, null).apply("foo");
        fail();
    }

    ///

    @Test
    public void testPartialDefinedForInput() {
        long expected = 42;
        OptionalLongMapper<String> om = s -> OptionalLong.of(expected);
        String in = "foo";
        long out = om.partial().applyAsLong(in);
        assertTrue(expected == out);
    }

    @Test(expected = NoSuchElementException.class)
    public void testPartialUndefined() {
        OptionalLongMapper<String> om = s -> OptionalLong.empty();
        String in = "foo";
        om.partial().applyAsLong(in);
        fail();
    }
}
