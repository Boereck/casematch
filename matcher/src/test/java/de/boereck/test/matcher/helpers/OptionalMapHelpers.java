package de.boereck.test.matcher.helpers;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.helpers.OptionalMatchHelpers;
import org.junit.Test;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import static de.boereck.matcher.helpers.OptionalMatchHelpers.*;
import static org.junit.Assert.*;

public class OptionalMapHelpers {

    @Test
    public void testSome() {
        String expected = "foo";
        Optional<String> o = Optional.of(expected);
        Optional<String> res = OptionalMatchHelpers.<String>some().apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.get() == expected);
    }

    @Test
    public void testSomeEmptyOptional() {
        Optional<String> o = Optional.empty();
        Optional<String> res = OptionalMatchHelpers.<String>some().apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeOnNull() {
        Optional<String> o = null;
        Optional<String> res = OptionalMatchHelpers.<String>some().apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testSomeI() {
        int expected = 42;
        OptionalInt o = OptionalInt.of(expected);
        OptionalInt res = someI.apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsInt() == expected);
    }

    @Test
    public void testSomeIEmptyOptional() {
        int expected = 42;
        OptionalInt o = OptionalInt.empty();
        OptionalInt res = someI.apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeINull() {
        OptionalInt o = null;
        OptionalInt res = someI.apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testSomeL() {
        long expected = 42;
        OptionalLong o = OptionalLong.of(expected);
        OptionalLong res = someL.apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsLong() == expected);
    }

    @Test
    public void testSomeLEmptyOptional() {
        OptionalLong o = OptionalLong.empty();
        OptionalLong res = someL.apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeLNull() {
        OptionalLong o = null;
        OptionalLong res = someL.apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testSomeD() {
        double expected = 0.0;
        OptionalDouble o = OptionalDouble.of(expected);
        OptionalDouble res = someD.apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsDouble() == expected);
    }

    @Test
    public void testSomeDEmptyOptional() {
        OptionalDouble o = OptionalDouble.empty();
        OptionalDouble res = someD.apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeDNull() {
        OptionalDouble o = null;
        OptionalDouble res = someD.apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testSomeByClass() {
        String expected = "foo";
        Optional<String> o = Optional.of(expected);
        Optional<String> res = some(String.class).apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.get() == expected);
    }

    @Test
    public void testSomeByClassEmptyOptional() {
        Optional<String> o = Optional.empty();
        Optional<String> res = some(String.class).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeByClassOnNull() {
        Optional<String> o = null;
        Optional<String> res = some(String.class).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testSomePredicateValue() {
        String expected = "foo";
        Optional<String> o = Optional.of(expected);
        Optional<String> res = some((String s) -> true).apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.get() == expected);
    }

    @Test
    public void testSomePredicateNoValue() {
        Optional<String> o = Optional.empty();
        Optional<String> res = some((String s) -> true).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomePredicateFilterOut() {
        Optional<String> o = Optional.of("foo");
        Optional<String> res = some((String s) -> false).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomePredicateOnNull() {
        Optional<String> o = null;
        Optional<String> res = some((String s) -> true).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testSomeIPredicateValue() {
        int expected = 42;
        OptionalInt o = OptionalInt.of(expected);
        OptionalInt res = someI((int s) -> true).apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsInt() == expected);
    }

    @Test
    public void testSomeIPredicateNoValue() {
        OptionalInt o = OptionalInt.empty();
        OptionalInt res = someI((int s) -> true).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeIPredicateFilterOut() {
        OptionalInt o = OptionalInt.of(42);
        OptionalInt res = someI((int s) -> false).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeIPredicateOnNull() {
        OptionalInt o = null;
        OptionalInt res = someI((int s) -> true).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testSomeMap() {

        Optional<String> o = Optional.of("foo");
        String expected = "bar";
        Optional<String> res = someMap((String s) -> expected).apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.get() == expected);
    }

    ///

    @Test
    public void testSomeLPredicateValue() {
        long expected = 42;
        OptionalLong o = OptionalLong.of(expected);
        OptionalLong res = someL(s -> true).apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsLong() == expected);
    }

    @Test
    public void testSomeLPredicateNoValue() {
        OptionalLong o = OptionalLong.empty();
        OptionalLong res = someL(s -> true).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeLPredicateFilterOut() {
        OptionalLong o = OptionalLong.of(42);
        OptionalLong res = someL(s -> false).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeLPredicateOnNull() {
        OptionalLong o = null;
        OptionalLong res = someL(s -> true).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testSomeDPredicateValue() {
        double expected = 0.0;
        OptionalDouble o = OptionalDouble.of(expected);
        OptionalDouble res = someD(s -> true).apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.getAsDouble() == expected);
    }

    @Test
    public void testSomeDPredicateNoValue() {
        OptionalDouble o = OptionalDouble.empty();
        OptionalDouble res = someD(s -> true).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeDPredicateFilterOut() {
        OptionalDouble o = OptionalDouble.of(0.0);
        OptionalDouble res = someD(s -> false).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeDPredicateOnNull() {
        OptionalDouble o = null;
        OptionalDouble res = someD(s -> true).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testSomeField() {
        String expected = "foo";
        Optional<String> o = Optional.of(expected);
        Optional<Object> res = some.apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.get() == expected);
    }

    @Test
    public void testSomeFieldEmptyOptional() {
        Optional<String> o = Optional.empty();
        Optional<String> res = some(String.class).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeFieldOnNull() {
        Optional<String> o = null;
        Optional<String> res = some(String.class).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testSomeFlat() {
        String expected = "foo";
        Optional<String> o = Optional.of(expected);
        Optional<String> res = OptionalMatchHelpers.<String, String>someFlat(Optional::of).apply(o);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertTrue(res.get() == expected);
    }

    @Test
    public void testSomeFlatOnEmpty() {
        Optional<String> o = Optional.empty();
        Optional<String> res = OptionalMatchHelpers.<String, String>someFlat(s -> {
            throw new AssertionError();
        }).apply(o);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSomeFlatOnNull() {
        Optional<String> res = OptionalMatchHelpers.<String, String>someFlat(s -> {
            throw new AssertionError();
        }).apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    ///

    @Test
    public void testNoneIsSome() {
        Optional<String> o = Optional.of("foo");
        boolean res = none.test(o);
        assertFalse(res);
    }

    @Test
    public void testNoneIsNone() {
        Optional<String> o = Optional.empty();
        boolean res = none.test(o);
        assertTrue(res);
    }

    @Test
    public void testNoneIsNull() {
        Optional<String> o = null;
        boolean res = none.test(o);
        assertTrue(res);
    }

    ///

    @Test
    public void testNoneIIsSome() {
        OptionalInt o = OptionalInt.of(42);
        boolean res = noneI.test(o);
        assertFalse(res);
    }

    @Test
    public void testNoneIIsNone() {
        OptionalInt o = OptionalInt.empty();
        boolean res = noneI.test(o);
        assertTrue(res);
    }

    @Test
    public void testNoneIIsNull() {
        OptionalInt o = null;
        boolean res = noneI.test(o);
        assertTrue(res);
    }

    ///

    @Test
    public void testNoneLIsSome() {
        OptionalLong o = OptionalLong.of(42);
        boolean res = noneL.test(o);
        assertFalse(res);
    }

    @Test
    public void testNoneLIsNone() {
        OptionalLong o = OptionalLong.empty();
        boolean res = noneL.test(o);
        assertTrue(res);
    }

    @Test
    public void testNoneLIsNull() {
        OptionalLong o = null;
        boolean res = noneL.test(o);
        assertTrue(res);
    }

    ///

    @Test
    public void testNoneDIsSome() {
        OptionalDouble o = OptionalDouble.of(42);
        boolean res = noneD.test(o);
        assertFalse(res);
    }

    @Test
    public void testNoneDIsNone() {
        OptionalDouble o = OptionalDouble.empty();
        boolean res = noneD.test(o);
        assertTrue(res);
    }

    @Test
    public void testNoneDIsNull() {
        OptionalDouble o = null;
        boolean res = noneD.test(o);
        assertTrue(res);
    }
}
