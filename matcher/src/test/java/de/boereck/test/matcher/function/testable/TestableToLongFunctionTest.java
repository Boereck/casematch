package de.boereck.test.matcher.function.testable;

import de.boereck.matcher.function.predicate.AdvPredicate;
import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.function.testable.TestableToLongFunction;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.OptionalLong;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class TestableToLongFunctionTest {

    @Test
    public void testThenTestTrue() {
        final long expected = 42L;
        final String input = "foo";
        TestableToLongFunction<String> tf = i -> {
            assertTrue(i == input);
            return expected;
        };
        AdvPredicate<String> sut = tf.thenTest(i -> {
            assertTrue(i == expected);
            return true;
        });

        assertTrue(sut.test(input));
    }

    @Test
    public void testThenTestFalse() {
        final String input = "";
        final long expected = 42L;
        TestableToLongFunction<String> tf = s -> {
            assertTrue(s == input);
            return expected;
        };
        AdvPredicate<String> sut = tf.thenTest(s -> {
            assertTrue(s == expected);
            return false;
        });

        assertFalse(sut.test(input));
    }

    @Test(expected = NullPointerException.class)
    public void testThenTestNullPointer() {
        TestableToLongFunction<String> tf = s -> 42L;
        tf.thenTest(null).test("");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenTestExceptionNoEvalPredicate() {
        TestableToLongFunction<String> tf = s -> {
            throw new NoSuchElementException();
        };
        tf.thenTest(o -> {
            fail();
            return false;
        }).test("foo");
        fail();
    }

    @Test
    public void testAndThen() {
        String input = "foo";
        long output = 42L;
        String expected = "baz";
        AtomicBoolean result = new AtomicBoolean(false);
        TestableToLongFunction<String> id = s -> {
            assertTrue(input == s);
            return output;
        };
        String resultVal = id.andThen(s -> {
            assertTrue(s == output);
            result.set(true);
            return expected;
        }).apply(input);
        assertTrue(result.get());
        assertTrue(expected == resultVal);
    }

    @Test(expected = NullPointerException.class)
    public void testAndThenNullPointer() {
        TestableToLongFunction<String> id = s -> 42L;
        id.andThen(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThenThisThrowing() {
        TestableToLongFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };

        throwing.andThen(s -> {
            fail();
            return s;
        }).apply("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThenThatThrowing() {
        TestableToLongFunction<String> id = s -> 42L;
        LongFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };

        id.andThen(throwing).apply("foo");
        fail();
    }

    @Test
    public void testCompose() {
        String input = "foo";
        String output = "bar";
        long expectedResult = 42L;
        TestableToLongFunction<String> composed = s -> {
            assertTrue(output == s);
            return expectedResult;
        };
        TestableFunction<String, String> first = s -> {
            assertTrue(s == input);
            return output;
        };
        long result = composed.compose(first).applyAsLong(input);
        assertTrue(expectedResult == result);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullPointer() {
        TestableToLongFunction<String> id = s -> 42L;
        id.compose(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testComposeThisThrowing() {
        TestableToLongFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        TestableFunction<String, String> id = s -> s;
        throwing.compose(id).applyAsLong("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testComposeThatThrowing() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        TestableToLongFunction<String> id = s -> 42L;
        id.compose(throwing).applyAsLong("foo");
        fail();
    }

    @Test
    public void testThenDo() {
        String input = "foo";
        long output = 42L;
        AtomicBoolean result = new AtomicBoolean(false);
        TestableToLongFunction<String> first = s -> {
            assertTrue(s == input);
            return output;
        };
        LongConsumer then = s -> {
            assertTrue(s == output);
            result.set(true);
        };
        first.thenDo(then).accept(input);
        assertTrue(result.get());
    }

    @Test(expected = NullPointerException.class)
    public void testThenDoNullPointer() {
        TestableToLongFunction<String> id = s -> 42L;
        id.thenDo(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenDoThisThrowing() {
        TestableToLongFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        LongConsumer then = s -> fail();
        throwing.thenDo(then).accept("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenDoThatThrowing() {
        TestableToLongFunction<String> id = s -> 42L;
        LongConsumer throwing = s -> {
            throw new NoSuchElementException();
        };
        id.thenDo(throwing).accept("foo");
        fail();
    }

    @Test
    public void testFilter() {
        long expected = 42L;
        TestableToLongFunction<String> id = s -> expected;

        OptionalLong withValue = id.filter(s -> true).apply("");
        assertNotNull(withValue);
        assertTrue(withValue.isPresent());
        assertTrue(expected == withValue.getAsLong());

        OptionalLong withoutValue = id.filter(s -> false).apply("");
        assertNotNull(withoutValue);
        assertFalse(withoutValue.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullPointer() {
        TestableToLongFunction<String> id = s -> 42L;
        id.filter(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterThisThrowing() {
        TestableToLongFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        LongPredicate then = s -> {
            fail();
            return false;
        };
        throwing.filter(then).apply("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterThatThrowing() {
        long output = 42L;
        TestableToLongFunction<String> id = s -> output;
        LongPredicate then = s -> {
            assertTrue(s == output);
            throw new NoSuchElementException();
        };
        id.filter(then).apply("foo");
        fail();
    }

    @Test
    public void testRequires() {
        String input = "foo";
        long output = 42L;

        AtomicBoolean called = new AtomicBoolean(false);
        TestableToLongFunction<String> tf = s -> {
            assertTrue(input == s);
            called.set(true);
            return output;
        };

        OptionalLong withValue = tf.requires(s -> true).apply(input);
        assertNotNull(withValue);
        assertTrue(withValue.isPresent());
        assertTrue(withValue.getAsLong() == output);

        TestableToLongFunction<String> neverCall = s -> {
            fail();
            return output;
        };
        OptionalLong withoutValue = neverCall.requires(s -> false).apply(input);
        assertNotNull(withoutValue);
        assertFalse(withoutValue.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testRequiresNullPointer() {
        TestableToLongFunction<String> id = s -> 42L;
        id.requires(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRequiresThisThrowing() {
        TestableToLongFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        Predicate<String> then = s -> true;
        throwing.requires(then).apply("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRequiresThatThrowing() {
        String input = "foo";
        TestableToLongFunction<String> id = s -> {
            fail();
            return 42L;
        };
        Predicate<String> then = s -> {
            assertTrue(s == input);
            throw new NoSuchElementException();
        };
        id.requires(then).apply("foo");
        fail();
    }

    @Test
    public void testNullAware() {
        long output = 42L;
        TestableToLongFunction<String> toFoo = s -> output;
        OptionalLong empty = toFoo.nullAware().apply(null);
        assertNotNull(empty);
        assertFalse(empty.isPresent());

        OptionalLong notEmpty = toFoo.nullAware().apply("");
        assertNotNull(notEmpty);
        assertTrue(notEmpty.isPresent());
        assertTrue(output == notEmpty.getAsLong());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNullAwareThisThrowing() {
        TestableToLongFunction<String> id = s -> {
            throw new NoSuchElementException();
        };
        id.nullAware().apply("foo");
        fail();
    }

    @Test
    public void testWithCatch() {
        long expected = 42L;
        TestableToLongFunction<String> id = s -> expected;
        OptionalLong result = id.withCatch().apply("");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsLong() == expected);
    }

    @Test
    public void testWithCatchThrowing() {
        TestableToLongFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String expected = "foo";
        OptionalLong result = throwing.withCatch().apply(expected);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testWithCatchByClass() {
        long output = 42L;
        TestableToLongFunction<String> id = s -> output;
        String expected = "foo";
        OptionalLong result = id.withCatch(NoSuchElementException.class, e -> fail()).apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsLong() == output);
    }

    @Test
    public void testWithCatchByClassThrowingCaught() {
        TestableToLongFunction<String> throwing = s -> {
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
        TestableToLongFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        throwing.withCatch(UnsupportedOperationException.class, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer1() {
        TestableToLongFunction<String> id = s -> 42L;
        String input = "foo";
        id.withCatch(null, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer2() {
        TestableToLongFunction<String> id = s -> 42L;
        String input = "foo";
        id.withCatch(NoSuchElementException.class, null).apply(input);
        fail();
    }

    @Test
    public void testWithCatchHandler() {
        long expected = 42L;
        TestableToLongFunction<String> id = s -> expected;
        OptionalLong result = id.withCatch(e -> fail()).apply("");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsLong() == expected);
    }

    @Test
    public void testWithCatchHandlerThrowingCaught() {
        TestableToLongFunction<String> throwing = s -> {
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
        TestableToLongFunction<String> throwing = s -> {
            throw new Error();
        };
        String input = "foo";
        throwing.withCatch( e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchHandlerNullPointer() {
        TestableToLongFunction<String> id = s -> 42L;
        String input = "foo";
        id.withCatch(null).apply(input);
        fail();
    }

    @Test
    public void testRecoverWith() {
        long expected = 42L;
        TestableToLongFunction<String> id = s -> expected;
        long result = id.recoverWith(t -> {
            fail();
            return -1;
        }).applyAsLong("");
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test
    public void testRecoverWithThrowingRecovered() {
        TestableToLongFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        long expected = 42L;
        long result = throwing.recoverWith(e -> expected).applyAsLong(input);
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithNullPointer() {
        TestableToLongFunction<String> failing = s -> {
            fail();
            return -1;
        };
        failing.recoverWith(null);
        fail();
    }

    @Test
    public void testRecoverWithByClass() {
        long expected = 42L;
        TestableToLongFunction<String> id = s -> expected;
        long result = id.recoverWith(Throwable.class, t -> {
            fail();
            return -1;
        }).applyAsLong("");
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test
    public void testRecoverWithByClassThrowingRecovered() {
        TestableToLongFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        long expected = 42L;
        long result = throwing.recoverWith(NoSuchElementException.class, e -> expected).applyAsLong(input);
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRecoverWithByClassThrowingNotRecovered() {
        TestableToLongFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        throwing.recoverWith(UnsupportedOperationException.class, e -> {
            fail();
            return -1;
        }).applyAsLong("foo");
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithByClassNullPointer1() {
        TestableToLongFunction<String> failing = s -> {
            fail();
            return -1;
        };
        failing.recoverWith(null, e->{
            fail();
            return -1;
        }).applyAsLong("foo");
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithByClassNullPointer2() {
        TestableToLongFunction<String> failing = s -> {
            fail();
            return -1;
        };
        failing.recoverWith(Exception.class, null).applyAsLong("foo");
        fail();
    }
    
}
