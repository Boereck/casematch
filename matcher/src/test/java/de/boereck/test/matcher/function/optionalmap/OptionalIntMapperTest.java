package de.boereck.test.matcher.function.optionalmap;

import de.boereck.matcher.function.optionalmap.OptionalDoubleMapper;
import de.boereck.matcher.function.optionalmap.OptionalIntMapper;
import de.boereck.matcher.function.optionalmap.OptionalLongMapper;
import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.function.testable.TestableToIntFunction;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static java.util.function.Function.identity;
import static org.junit.Assert.*;

public class OptionalIntMapperTest {

    @Test
    public void testMap() {
        int out = 42;
        String in = "foo";
        OptionalIntMapper<String> om = s -> OptionalInt.of(out);
        OptionalMapper<String,String> mapped = om.map(i -> in);
        Optional<String> res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testMapThisReturnsNull() {
        String in = "foo";
        OptionalIntMapper<String> om = s -> null;
        Optional<String> res = om.map(i -> in).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapOfEmpty() {
        OptionalIntMapper<String> om = s -> OptionalInt.empty();
        OptionalMapper<String,String> mapped = om.map((int i) -> "foo");
        Optional<String> res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapOfNullFunction() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        om.map(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapThrowing() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
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
        OptionalIntMapper<String> om = s -> OptionalInt.of(out);
        OptionalIntMapper<String> mapped = om.mapI(i -> i);
        OptionalInt res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsInt());
    }

    @Test
    public void testMapIThisReturnsNull() {
        String in = "foo";
        OptionalIntMapper<String> om = s -> null;
        OptionalInt res = om.mapI(s -> 42).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapIOfNullFunction() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        om.mapI(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapIThrowing() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
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
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        OptionalLongMapper<String> mapped = om.mapL(s -> out);
        OptionalLong res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsLong());
    }

    @Test
    public void testMapLThisReturnsNull() {
        String in = "foo";
        OptionalIntMapper<String> om = s -> null;
        OptionalLong res = om.mapL(s -> 42).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapLOfEmpty() {
        long out = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.empty();
        OptionalLongMapper<String> mapped = om.mapL(s -> out);
        OptionalLong res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapLOfNullFunction() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        om.mapL(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapLThrowing() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
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
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        OptionalDoubleMapper<String> mapped = om.mapD(s -> out);
        OptionalDouble res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsDouble());
    }

    @Test
    public void testMapDThisReturnsNull() {
        String in = "foo";
        OptionalIntMapper<String> om = s -> null;
        OptionalDouble res = om.mapD(s -> 42).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapDOfEmpty() {
        int out = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.empty();
        OptionalDoubleMapper<String> mapped = om.mapD(s -> out);
        OptionalDouble res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapDOfNullFunction() {
        OptionalIntMapper<String> om = i -> OptionalInt.of(42);
         om.mapD(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapDThrowing() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
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
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        OptionalMapper<String, String> mapped = om.flatMap(s -> Optional.of(in));
        Optional<String> res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testFlatMapThisReturnsNull() {
        String in = "foo";
        OptionalIntMapper<String> om = s -> null;
        Optional<String> res = om.flatMap(s -> Optional.of(in)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapOfEmpty() {
        OptionalIntMapper<String> om = s -> OptionalInt.empty();
        OptionalMapper<String,String> mapped = om.flatMap(i -> Optional.of("bar"));
        Optional<String> res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapOfNullFunction() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        om.flatMap(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapThrowing() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
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
        OptionalIntMapper<String> om = s -> OptionalInt.of(out);
        OptionalIntMapper<String> mapped = om.flatMapI(OptionalInt::of);
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
    public void testFlatMapIOfEmpty() {
        int out = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.empty();
        OptionalIntMapper<String> mapped = om.flatMapI(s -> OptionalInt.of(out));
        OptionalInt res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapIOfNullFunction() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        om.flatMapI(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapIThrowing() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
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
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
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
        OptionalIntMapper<String> om = s -> null;
        OptionalLong res = om.flatMapL(s -> OptionalLong.of(out)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapLOfEmpty() {
        long out = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.empty();
        OptionalLongMapper<String> mapped = om.flatMapL(s -> OptionalLong.of(out));
        OptionalLong res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapLOfNullFunction() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        om.flatMapL(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapLThrowing() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
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
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
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
        OptionalIntMapper<String> om = s -> null;
        OptionalDouble res = om.flatMapD(s -> OptionalDouble.of(out)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapDOfEmpty() {
        long out = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.empty();
        OptionalDoubleMapper<String> mapped = om.flatMapD(s -> OptionalDouble.of(out));
        OptionalDouble res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapDOfNullFunction() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        om.flatMapD(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapDThrowing() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        OptionalDoubleMapper<String> mapped = om.flatMapD(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    @Test
    public void testFilterByPredicateLetThrough() {
        String in = "foo";
        int out = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.of(out);
        OptionalIntMapper<String> filtered = om.filter(s -> true);
        OptionalInt res = filtered.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsInt());
    }

    @Test
    public void testFilterByPredicateNotLetThrough() {
        int out = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.of(out);
        OptionalIntMapper<String> filtered = om.filter(s -> false);
        String in = "foo";
        OptionalInt res = filtered.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterByPredicateThrowing() {
        int out = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.of(out);
        OptionalIntMapper<String> filtered = om.filter(s -> {
            throw new NoSuchElementException();
        });
        filtered.apply("foo");
        fail();
    }

    @Test
    public void testFilterByPredicateThisReturnsNull() {
        OptionalIntMapper<String> om = s -> null;
        OptionalIntMapper<String> filtered = om.filter(s -> true);
        String in = "foo";
        OptionalInt res = filtered.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterByPredicateThisThrowing() {
        OptionalIntMapper<String> om = s -> {
            throw new NoSuchElementException();
        };
        OptionalIntMapper<String> filtered = om.filter(s -> true);
        filtered.apply("foo");
        fail();
    }


    ///

    @Test
    public void testHasResultHavingNoResult() {
        OptionalIntMapper<String> om = i -> OptionalInt.empty();
        Predicate<String> pred = om.hasResult();
        assertFalse(pred.test("foo"));
    }

    @Test
    public void testHasResultHavingResult() {
        OptionalIntMapper<String> om = i -> OptionalInt.of(42);
        Predicate<String> pred = om.hasResult();
        assertTrue(pred.test("foo"));
    }

    @Test
    public void testHasResultThisReturnsNull() {
        OptionalIntMapper<String> om = i -> null;
        Predicate<String> pred = om.hasResult();
        assertFalse(pred.test("foo"));
    }

    ///

    @Test
    public void testHasResultAndHavingNoResultFilterTrue() {
        OptionalIntMapper<String> om = i -> OptionalInt.empty();
        String in = "foo";
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertFalse(pred.test(in));
    }

    @Test
    public void testHasResultAndHavingNoResultFilterFalse() {
        OptionalIntMapper<String> om = i -> OptionalInt.empty();
        String in = "foo";
        Predicate<String> pred = om.hasResultAnd(s -> false);
        assertFalse(pred.test(in));
    }

    @Test
    public void testHasResultAndHavingResultFilterTrue() {
        int in = 42;
        OptionalIntMapper<String> om = i -> OptionalInt.of(in);
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertTrue(pred.test("foo"));
    }

    @Test
    public void testHasResultAndHavingResultFilterFalse() {
        OptionalIntMapper<String> om = i -> OptionalInt.of(42);
        Predicate<String> pred = om.hasResultAnd(s -> false);
        assertFalse(pred.test("foo"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testHasResultPredicateThrowing() {
        String in = "foo";
        OptionalIntMapper<String> om = i -> OptionalInt.of(42);
        Predicate<String> pred = om.hasResultAnd(s -> {
            throw new NoSuchElementException();
        });
        pred.test(in);
        fail();
    }

    @Test
    public void testHasResultAndThisReturnsNull() {
        String in = "foo";
        OptionalIntMapper<String> om = i -> null;
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertFalse(pred.test(in));
    }

    ///

    @Test
    public void testThenDoWithOptional() {
        AtomicBoolean success = new AtomicBoolean(false);
        String in = "foo";
        int out = 42;
        OptionalIntMapper<String> om = i -> OptionalInt.of(out);
        om.thenDo(o -> {
            assertNotNull(o);
            assertTrue(o.isPresent());
            assertTrue(out == o.getAsInt());
            success.set(true);
        }).accept(in);
        assertTrue(success.get());
    }

    @Test
    public void testThenDoWithEmptyOptional() {
        AtomicBoolean success = new AtomicBoolean(false);
        OptionalIntMapper<String> om = i -> OptionalInt.empty();
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
        OptionalIntMapper<String> om = i -> null;
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
        int out = 42;
        OptionalIntMapper<String> om = i -> OptionalInt.of(out);
        om.thenIfPresent(s -> {
            assertTrue(s == out);
            success.set(true);
        }).accept("foo");
        assertTrue(success.get());
    }

    @Test
    public void testThenIfPresentIsNotPresent() {
        String out = "foo";
        OptionalIntMapper<String> om = i -> OptionalInt.empty();
        om.thenIfPresent(s -> fail()).accept(out);
    }

    @Test
    public void testThenIfPresentThisReturnsNull() {
        String out = "foo";
        OptionalIntMapper<String> om = i -> null;
        om.thenIfPresent(s -> fail()).accept(out);
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenIfPresentConsumerThrowing() {
        int out = 42;
        OptionalIntMapper<String> om = i -> OptionalInt.of(out);
        om.thenIfPresent(s -> {
            throw new NoSuchElementException();
        }).accept("foo");
    }

    ///

    @Test
    public void testOrElseThisProvidesValue() {
        int out = 42;
        OptionalIntMapper<String> om = i -> OptionalInt.of(out);
        TestableToIntFunction<String> elsed = om.orElse(43);
        int res = elsed.applyAsInt("");
        assertTrue(out == res);
    }

    @Test
    public void testOrElseTakeElseValue() {
        int out = 43;
        OptionalIntMapper<String> om = i -> OptionalInt.empty();
        TestableToIntFunction<String> elsed = om.orElse(out);
        int res = elsed.applyAsInt("");
        assertTrue(out == res);
    }

    @Test
    public void testOrElseThisReturnsNull() {
        int out = 43;
        OptionalIntMapper<String> om = i -> null;
        TestableToIntFunction<String> elsed = om.orElse(out);
        int res = elsed.applyAsInt("");
        assertTrue(out == res);
    }

    ///

    @Test
    public void testOrElseGetWithValueFromSupplier() {
        int out = 42;
        OptionalIntMapper<String> om = i -> OptionalInt.empty();
        TestableToIntFunction<String> elseMap = om.orElseGet(() -> out);
        int res = elseMap.applyAsInt("");
        assertTrue(res == out);
    }

    @Test
    public void testOrElseGetThisReturningNull() {
        int out = 42;
        OptionalIntMapper<String> om = i -> null;
        TestableToIntFunction<String> elseMap = om.orElseGet(() -> out);
        int res = elseMap.applyAsInt("");
        assertTrue(res == out);
    }

    @Test
    public void testOrElseGetWithOriginalValue() {
        int out = 42;
        OptionalIntMapper<String> om = i -> OptionalInt.of(out);
        TestableToIntFunction<String> elseMap = om.orElseGet(() -> 43);
        int res = elseMap.applyAsInt("");
        assertTrue(res == out);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseGetSupplierThrowing() {
        OptionalIntMapper<String> om = i -> OptionalInt.empty();
        TestableToIntFunction<String> elseMap = om.orElseGet(() -> {
            throw new NoSuchElementException();
        });
        elseMap.applyAsInt("");
        fail();
    }

    ///

    @Test
    public void testNullAwareNotNull() {
        int out = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.of(out);
        OptionalIntMapper<String> omNullAware = om.nullAware();
        OptionalInt res = omNullAware.apply("foo");
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsInt());
    }

    @Test
    public void tesNullAwareWithNullInput() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        OptionalIntMapper<String> omNullAware = om.nullAware();
        OptionalInt res = omNullAware.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void tesNullAwareWithNullResult() {
        OptionalIntMapper<String> om = i -> null;
        OptionalIntMapper<String> omNullAware = om.nullAware();
        OptionalInt res = omNullAware.apply("foo");
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testWithCatch() {
        int expected = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.of(expected);
        OptionalInt result = om.withCatch().apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsInt() == expected);
    }

    @Test
    public void testWithCatchThrowing() {
        OptionalIntMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String expected = "foo";
        OptionalInt result = throwing.withCatch().apply(expected);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }


    ///

    @Test
    public void testWithCatchByClass() {
        int expected = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.of(expected);
        OptionalInt result = om.withCatch(NoSuchElementException.class, e -> fail()).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsInt() == expected);
    }

    @Test
    public void testWithCatchByClassThrowingCaught() {
        OptionalIntMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        AtomicBoolean result = new AtomicBoolean(false);
        OptionalInt empty = throwing.withCatch(NoSuchElementException.class, e -> result.set(true)).apply(input);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
        assertTrue(result.get());
    }

    @Test(expected = NoSuchElementException.class)
    public void testWithCatchByClassThrowingUncaught() {
        OptionalIntMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        throwing.withCatch(UnsupportedOperationException.class, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer1() {
        OptionalIntMapper<String> id = s -> OptionalInt.of(42);
        String input = "foo";
        id.withCatch(null, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer2() {
        OptionalIntMapper<String> id = s -> OptionalInt.of(42);
        String input = "foo";
        id.withCatch(NoSuchElementException.class, null).apply(input);
        fail();
    }


    ///

    @Test
    public void testWithCatchHandler() {
        int expected = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.of(expected);
        OptionalInt result = om.withCatch(e -> fail()).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsInt() == expected);
    }

    @Test
    public void testWithCatchHandlerThrowingCaught() {
        OptionalIntMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        AtomicBoolean result = new AtomicBoolean(false);
        OptionalInt empty = throwing.withCatch(e -> result.set(true)).apply(input);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
        assertTrue(result.get());
    }

    @Test(expected = Error.class)
    public void testWithCatchHandlerThrowingUncaught() {
        OptionalIntMapper<String> throwing = s -> {
            throw new Error();
        };
        String input = "foo";
        throwing.withCatch(e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchHandlerNullPointer() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        String input = "foo";
        om.withCatch(null).apply(input);
        fail();
    }

    ///

    @Test
    public void testRecoverWith() {
        int expected = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.of(expected);
        OptionalInt result = om.recoverWith(t -> {
            fail();
            return null;
        }).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsInt() == expected);
    }

    @Test
    public void testRecoverWithThrowingRecovered() {
        OptionalIntMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        int expected = 42;
        OptionalInt result = throwing.recoverWith(e -> OptionalInt.of(expected)).apply(input);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsInt() == expected);
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithNullPointer() {
        OptionalIntMapper<String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(null);
        fail();
    }

    ///

    @Test
    public void testRecoverWithByClass() {
        OptionalIntMapper<String> om = s -> OptionalInt.of(42);
        int expected = 42;
        OptionalInt result = om.recoverWith(Throwable.class, t -> {
            fail();
            return null;
        }).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsInt() == expected);
    }

    @Test
    public void testRecoverWithByClassThrowingRecovered() {
        OptionalIntMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        int expected = 42;
        OptionalInt result = throwing.recoverWith(NoSuchElementException.class, e -> OptionalInt.of(expected)).apply(input);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsInt() == expected);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRecoverWithByClassThrowingNotRecovered() {
        OptionalIntMapper<String> throwing = s -> {
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
        OptionalIntMapper<String> failing = s -> {
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
        OptionalIntMapper<String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(Exception.class, null).apply("foo");
        fail();
    }

    ///

    @Test
    public void testPartialDefinedForInput() {
        int expected = 42;
        OptionalIntMapper<String> om = s -> OptionalInt.of(expected);
        String in = "foo";
        int out = om.partial().applyAsInt(in);
        assertTrue(expected == out);
    }

    @Test(expected = NoSuchElementException.class)
    public void testPartialUndefined() {
        OptionalIntMapper<String> om = s -> OptionalInt.empty();
        String in = "foo";
        om.partial().applyAsInt(in);
        fail();
    }
}
