package de.boereck.test.matcher.function.optionalmap;

import de.boereck.matcher.function.optionalmap.OptionalDoubleMapper;
import de.boereck.matcher.function.optionalmap.OptionalIntMapper;
import de.boereck.matcher.function.optionalmap.OptionalLongMapper;
import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.testable.TestableToDoubleFunction;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class OptionalDoubleMapperTest {

    @Test
    public void testMap() {
        double out = 0.0;
        String in = "foo";
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(out);
        OptionalMapper<String,String> mapped = om.map(i -> in);
        Optional<String> res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testMapThisReturnsNull() {
        String in = "foo";
        OptionalDoubleMapper<String> om = s -> null;
        Optional<String> res = om.map(i -> in).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapOfEmpty() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.empty();
        OptionalMapper<String,String> mapped = om.map((double i) -> "foo");
        Optional<String> res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapOfNullFunction() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        om.map(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapThrowing() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
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
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(out);
        OptionalIntMapper<String> mapped = om.mapI(i -> out);
        OptionalInt res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsInt());
    }

    @Test
    public void testMapIThisReturnsNull() {
        String in = "foo";
        OptionalDoubleMapper<String> om = s -> null;
        OptionalInt res = om.mapI(s -> 42).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapIOfNullFunction() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        om.mapI(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapIThrowing() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
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
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        OptionalLongMapper<String> mapped = om.mapL(s -> out);
        OptionalLong res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsLong());
    }

    @Test
    public void testMapLThisReturnsNull() {
        String in = "foo";
        OptionalDoubleMapper<String> om = s -> null;
        OptionalLong res = om.mapL(s -> 42).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapLOfEmpty() {
        long out = 42;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.empty();
        OptionalLongMapper<String> mapped = om.mapL(s -> out);
        OptionalLong res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapLOfNullFunction() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        om.mapL(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapLThrowing() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
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
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        OptionalDoubleMapper<String> mapped = om.mapD(s -> out);
        OptionalDouble res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsDouble());
    }

    @Test
    public void testMapDThisReturnsNull() {
        String in = "foo";
        OptionalDoubleMapper<String> om = s -> null;
        OptionalDouble res = om.mapD(s -> 0.0).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMapDOfEmpty() {
        double out = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.empty();
        OptionalDoubleMapper<String> mapped = om.mapD(s -> out);
        OptionalDouble res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testMapDOfNullFunction() {
        OptionalDoubleMapper<String> om = i -> OptionalDouble.of(0.0);
         om.mapD(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapDThrowing() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
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
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        OptionalMapper<String, String> mapped = om.flatMap(s -> Optional.of(in));
        Optional<String> res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(in == res.get());
    }

    @Test
    public void testFlatMapThisReturnsNull() {
        String in = "foo";
        OptionalDoubleMapper<String> om = s -> null;
        Optional<String> res = om.flatMap(s -> Optional.of(in)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapOfEmpty() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.empty();
        OptionalMapper<String,String> mapped = om.flatMap(i -> Optional.of("bar"));
        Optional<String> res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapOfNullFunction() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        om.flatMap(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapThrowing() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
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
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(out);
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
        OptionalDoubleMapper<String> om = s -> null;
        OptionalInt res = om.flatMapI(s -> OptionalInt.of(out)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapIOfEmpty() {
        int out = 42;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.empty();
        OptionalIntMapper<String> mapped = om.flatMapI(s -> OptionalInt.of(out));
        OptionalInt res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapIOfNullFunction() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        om.flatMapI(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapIThrowing() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
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
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        OptionalLongMapper<String> mapped = om.flatMapL(s -> OptionalLong.of(out));
        OptionalLong res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsLong());
    }

    @Test
    public void testFlatMapLThisReturnsNull() {
        String in = "foo";
        long out = 42L;
        OptionalDoubleMapper<String> om = s -> null;
        OptionalLong res = om.flatMapL(s -> OptionalLong.of(out)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapLOfEmpty() {
        long out = 42;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.empty();
        OptionalLongMapper<String> mapped = om.flatMapL(s -> OptionalLong.of(out));
        OptionalLong res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapLOfNullFunction() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        om.flatMapL(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapLThrowing() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
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
        double out = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        OptionalDoubleMapper<String> mapped = om.flatMapD(s -> OptionalDouble.of(out));
        OptionalDouble res = mapped.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsDouble());
    }

    @Test
    public void testFlatMapDThisReturnsNull() {
        String in = "foo";
        double out = 0.0;
        OptionalDoubleMapper<String> om = s -> null;
        OptionalDouble res = om.flatMapD(s -> OptionalDouble.of(out)).apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testFlatMapDOfEmpty() {
        double out = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.empty();
        OptionalDoubleMapper<String> mapped = om.flatMapD(s -> OptionalDouble.of(out));
        OptionalDouble res = mapped.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapDOfNullFunction() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        om.flatMapD(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapDThrowing() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        OptionalDoubleMapper<String> mapped = om.flatMapD(s -> {
            throw new NoSuchElementException();
        });
        mapped.apply("foo");
        fail();
    }

    @Test
    public void testFilterByPredicateLetThrough() {
        String in = "foo";
        double out = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(out);
        OptionalDoubleMapper<String> filtered = om.filter(s -> true);
        OptionalDouble res = filtered.apply(in);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsDouble());
    }

    @Test
    public void testFilterByPredicateNotLetThrough() {
        double out = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(out);
        OptionalDoubleMapper<String> filtered = om.filter(s -> false);
        String in = "foo";
        OptionalDouble res = filtered.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterByPredicateThrowing() {
        double out = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(out);
        OptionalDoubleMapper<String> filtered = om.filter(s -> {
            throw new NoSuchElementException();
        });
        filtered.apply("foo");
        fail();
    }

    @Test
    public void testFilterByPredicateThisReturnsNull() {
        OptionalDoubleMapper<String> om = s -> null;
        OptionalDoubleMapper<String> filtered = om.filter(s -> true);
        String in = "foo";
        OptionalDouble res = filtered.apply(in);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterByPredicateThisThrowing() {
        OptionalDoubleMapper<String> om = s -> {
            throw new NoSuchElementException();
        };
        OptionalDoubleMapper<String> filtered = om.filter(s -> true);
        filtered.apply("foo");
        fail();
    }


    ///

    @Test
    public void testHasResultHavingNoResult() {
        OptionalDoubleMapper<String> om = i -> OptionalDouble.empty();
        Predicate<String> pred = om.hasResult();
        assertFalse(pred.test("foo"));
    }

    @Test
    public void testHasResultHavingResult() {
        OptionalDoubleMapper<String> om = i -> OptionalDouble.of(0.0);
        Predicate<String> pred = om.hasResult();
        assertTrue(pred.test("foo"));
    }

    @Test
    public void testHasResultThisReturnsNull() {
        OptionalDoubleMapper<String> om = i -> null;
        Predicate<String> pred = om.hasResult();
        assertFalse(pred.test("foo"));
    }

    ///

    @Test
    public void testHasResultAndHavingNoResultFilterTrue() {
        OptionalDoubleMapper<String> om = i -> OptionalDouble.empty();
        String in = "foo";
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertFalse(pred.test(in));
    }

    @Test
    public void testHasResultAndHavingNoResultFilterFalse() {
        OptionalDoubleMapper<String> om = i -> OptionalDouble.empty();
        String in = "foo";
        Predicate<String> pred = om.hasResultAnd(s -> false);
        assertFalse(pred.test(in));
    }

    @Test
    public void testHasResultAndHavingResultFilterTrue() {
        double in = 0.0;
        OptionalDoubleMapper<String> om = i -> OptionalDouble.of(in);
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertTrue(pred.test("foo"));
    }

    @Test
    public void testHasResultAndHavingResultFilterFalse() {
        OptionalDoubleMapper<String> om = i -> OptionalDouble.of(0.0);
        Predicate<String> pred = om.hasResultAnd(s -> false);
        assertFalse(pred.test("foo"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testHasResultPredicateThrowing() {
        String in = "foo";
        OptionalDoubleMapper<String> om = i -> OptionalDouble.of(0.0);
        Predicate<String> pred = om.hasResultAnd(s -> {
            throw new NoSuchElementException();
        });
        pred.test(in);
        fail();
    }

    @Test
    public void testHasResultAndThisReturnsNull() {
        String in = "foo";
        OptionalDoubleMapper<String> om = i -> null;
        Predicate<String> pred = om.hasResultAnd(s -> true);
        assertFalse(pred.test(in));
    }

    ///

    @Test
    public void testThenDoWithOptional() {
        AtomicBoolean success = new AtomicBoolean(false);
        String in = "foo";
        double out = 0.0;
        OptionalDoubleMapper<String> om = i -> OptionalDouble.of(out);
        om.thenDo(o -> {
            assertNotNull(o);
            assertTrue(o.isPresent());
            assertTrue(out == o.getAsDouble());
            success.set(true);
        }).accept(in);
        assertTrue(success.get());
    }

    @Test
    public void testThenDoWithEmptyOptional() {
        AtomicBoolean success = new AtomicBoolean(false);
        OptionalDoubleMapper<String> om = i -> OptionalDouble.empty();
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
        OptionalDoubleMapper<String> om = i -> null;
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
        double out = 0.0;
        OptionalDoubleMapper<String> om = i -> OptionalDouble.of(out);
        om.thenIfPresent(s -> {
            assertTrue(s == out);
            success.set(true);
        }).accept("foo");
        assertTrue(success.get());
    }

    @Test
    public void testThenIfPresentIsNotPresent() {
        String out = "foo";
        OptionalDoubleMapper<String> om = i -> OptionalDouble.empty();
        om.thenIfPresent(s -> fail()).accept(out);
    }

    @Test
    public void testThenIfPresentThisReturnsNull() {
        String out = "foo";
        OptionalDoubleMapper<String> om = i -> null;
        om.thenIfPresent(s -> fail()).accept(out);
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenIfPresentConsumerThrowing() {
        double out = 0.0;
        OptionalDoubleMapper<String> om = i -> OptionalDouble.of(out);
        om.thenIfPresent(s -> {
            throw new NoSuchElementException();
        }).accept("foo");
    }

    ///

    @Test
    public void testOrElseThisProvidesValue() {
        double out = 0.0;
        OptionalDoubleMapper<String> om = i -> OptionalDouble.of(out);
        TestableToDoubleFunction<String> elsed = om.orElse(43);
        double res = elsed.applyAsDouble("");
        assertTrue(out == res);
    }

    @Test
    public void testOrElseTakeElseValue() {
        double out = 43;
        OptionalDoubleMapper<String> om = i -> OptionalDouble.empty();
        TestableToDoubleFunction<String> elsed = om.orElse(out);
        double res = elsed.applyAsDouble("");
        assertTrue(out == res);
    }

    @Test
    public void testOrElseThisReturnsNull() {
        double out = 43;
        OptionalDoubleMapper<String> om = i -> null;
        TestableToDoubleFunction<String> elsed = om.orElse(out);
        double res = elsed.applyAsDouble("");
        assertTrue(out == res);
    }

    ///

    @Test
    public void testOrElseGetWithValueFromSupplier() {
        double out = 0.0;
        OptionalDoubleMapper<String> om = i -> OptionalDouble.empty();
        TestableToDoubleFunction<String> elseMap = om.orElseGet(() -> out);
        double res = elseMap.applyAsDouble("");
        assertTrue(res == out);
    }

    @Test
    public void testOrElseGetThisReturningNull() {
        double out = 0.0;
        OptionalDoubleMapper<String> om = i -> null;
        TestableToDoubleFunction<String> elseMap = om.orElseGet(() -> out);
        double res = elseMap.applyAsDouble("");
        assertTrue(res == out);
    }

    @Test
    public void testOrElseGetWithOriginalValue() {
        double out = 0.0;
        OptionalDoubleMapper<String> om = i -> OptionalDouble.of(out);
        TestableToDoubleFunction<String> elseMap = om.orElseGet(() -> 43);
        double res = elseMap.applyAsDouble("");
        assertTrue(res == out);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseGetSupplierThrowing() {
        OptionalDoubleMapper<String> om = i -> OptionalDouble.empty();
        TestableToDoubleFunction<String> elseMap = om.orElseGet(() -> {
            throw new NoSuchElementException();
        });
        elseMap.applyAsDouble("");
        fail();
    }

    ///

    @Test
    public void testNullAwareNotNull() {
        double out = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(out);
        OptionalDoubleMapper<String> omNullAware = om.nullAware();
        OptionalDouble res = omNullAware.apply("foo");
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(out == res.getAsDouble());
    }

    @Test
    public void tesNullAwareWithNullInput() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        OptionalDoubleMapper<String> omNullAware = om.nullAware();
        OptionalDouble res = omNullAware.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void tesNullAwareWithNullResult() {
        OptionalDoubleMapper<String> om = i -> null;
        OptionalDoubleMapper<String> omNullAware = om.nullAware();
        OptionalDouble res = omNullAware.apply("foo");
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testWithCatch() {
        double expected = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(expected);
        OptionalDouble result = om.withCatch().apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsDouble() == expected);
    }

    @Test
    public void testWithCatchThrowing() {
        OptionalDoubleMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String expected = "foo";
        OptionalDouble result = throwing.withCatch().apply(expected);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }


    ///

    @Test
    public void testWithCatchByClass() {
        double expected = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(expected);
        OptionalDouble result = om.withCatch(NoSuchElementException.class, e -> fail()).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsDouble() == expected);
    }

    @Test
    public void testWithCatchByClassThrowingCaught() {
        OptionalDoubleMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        AtomicBoolean result = new AtomicBoolean(false);
        OptionalDouble empty = throwing.withCatch(NoSuchElementException.class, e -> result.set(true)).apply(input);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
        assertTrue(result.get());
    }

    @Test(expected = NoSuchElementException.class)
    public void testWithCatchByClassThrowingUncaught() {
        OptionalDoubleMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        throwing.withCatch(UnsupportedOperationException.class, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer1() {
        OptionalDoubleMapper<String> id = s -> OptionalDouble.of(0.0);
        String input = "foo";
        id.withCatch(null, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer2() {
        OptionalDoubleMapper<String> id = s -> OptionalDouble.of(0.0);
        String input = "foo";
        id.withCatch(NoSuchElementException.class, null).apply(input);
        fail();
    }


    ///

    @Test
    public void testWithCatchHandler() {
        double expected = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(expected);
        OptionalDouble result = om.withCatch(e -> fail()).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsDouble() == expected);
    }

    @Test
    public void testWithCatchHandlerThrowingCaught() {
        OptionalDoubleMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        AtomicBoolean result = new AtomicBoolean(false);
        OptionalDouble empty = throwing.withCatch(e -> result.set(true)).apply(input);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
        assertTrue(result.get());
    }

    @Test(expected = Error.class)
    public void testWithCatchHandlerThrowingUncaught() {
        OptionalDoubleMapper<String> throwing = s -> {
            throw new Error();
        };
        String input = "foo";
        throwing.withCatch(e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchHandlerNullPointer() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        String input = "foo";
        om.withCatch(null).apply(input);
        fail();
    }

    ///

    @Test
    public void testRecoverWith() {
        double expected = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(expected);
        OptionalDouble result = om.recoverWith(t -> {
            fail();
            return null;
        }).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsDouble() == expected);
    }

    @Test
    public void testRecoverWithThrowingRecovered() {
        OptionalDoubleMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        double expected = 0.0;
        OptionalDouble result = throwing.recoverWith(e -> OptionalDouble.of(expected)).apply(input);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsDouble() == expected);
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithNullPointer() {
        OptionalDoubleMapper<String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(null);
        fail();
    }

    ///

    @Test
    public void testRecoverWithByClass() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(0.0);
        double expected = 0.0;
        OptionalDouble result = om.recoverWith(Throwable.class, t -> {
            fail();
            return null;
        }).apply("foo");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsDouble() == expected);
    }

    @Test
    public void testRecoverWithByClassThrowingRecovered() {
        OptionalDoubleMapper<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        double expected = 0.0;
        OptionalDouble result = throwing.recoverWith(NoSuchElementException.class, e -> OptionalDouble.of(expected)).apply(input);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsDouble() == expected);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRecoverWithByClassThrowingNotRecovered() {
        OptionalDoubleMapper<String> throwing = s -> {
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
        OptionalDoubleMapper<String> failing = s -> {
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
        OptionalDoubleMapper<String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(Exception.class, null).apply("foo");
        fail();
    }

    ///

    @Test
    public void testPartialDefinedForInput() {
        double expected = 0.0;
        OptionalDoubleMapper<String> om = s -> OptionalDouble.of(expected);
        String in = "foo";
        double out = om.partial().applyAsDouble(in);
        assertTrue(expected == out);
    }

    @Test(expected = NoSuchElementException.class)
    public void testPartialUndefined() {
        OptionalDoubleMapper<String> om = s -> OptionalDouble.empty();
        String in = "foo";
        om.partial().applyAsDouble(in);
        fail();
    }
}
