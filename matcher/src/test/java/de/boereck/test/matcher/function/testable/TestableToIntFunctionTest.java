package de.boereck.test.matcher.function.testable;

import de.boereck.matcher.function.predicate.AdvPredicate;
import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.function.testable.TestableToIntFunction;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class TestableToIntFunctionTest {

    @Test
    public void testThenTestTrue() {
        final int expected = 42;
        final String input = "foo";
        TestableToIntFunction<String> tf = i -> {
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
        final int expected = 42;
        TestableToIntFunction<String> tf = s -> {
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
        TestableToIntFunction<String> tf = s -> 42;
        tf.thenTest(null).test("");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenTestExceptionNoEvalPredicate() {
        TestableToIntFunction<String> tf = s -> {
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
        int output = 42;
        String expected = "baz";
        AtomicBoolean result = new AtomicBoolean(false);
        TestableToIntFunction<String> id = s -> {
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
        TestableToIntFunction<String> id = s -> 42;
        id.andThen(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThenThisThrowing() {
        TestableToIntFunction<String> throwing = s -> {
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
        TestableToIntFunction<String> id = s -> 42;
        IntFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };

        id.andThen(throwing).apply("foo");
        fail();
    }

    @Test
    public void testCompose() {
        String input = "foo";
        String output = "bar";
        int expectedResult = 42;
        TestableToIntFunction<String> composed = s -> {
            assertTrue(output == s);
            return expectedResult;
        };
        TestableFunction<String, String> first = s -> {
            assertTrue(s == input);
            return output;
        };
        int result = composed.compose(first).applyAsInt(input);
        assertTrue(expectedResult == result);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullPointer() {
        TestableToIntFunction<String> id = s -> 42;
        id.compose(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testComposeThisThrowing() {
        TestableToIntFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        TestableFunction<String, String> id = s -> s;
        throwing.compose(id).applyAsInt("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testComposeThatThrowing() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        TestableToIntFunction<String> id = s -> 42;
        id.compose(throwing).applyAsInt("foo");
        fail();
    }

    @Test
    public void testThenDo() {
        String input = "foo";
        int output = 42;
        AtomicBoolean result = new AtomicBoolean(false);
        TestableToIntFunction<String> first = s -> {
            assertTrue(s == input);
            return output;
        };
        IntConsumer then = s -> {
            assertTrue(s == output);
            result.set(true);
        };
        first.thenDo(then).accept(input);
        assertTrue(result.get());
    }

    @Test(expected = NullPointerException.class)
    public void testThenDoNullPointer() {
        TestableToIntFunction<String> id = s -> 42;
        id.thenDo(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenDoThisThrowing() {
        TestableToIntFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        IntConsumer then = s -> fail();
        throwing.thenDo(then).accept("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenDoThatThrowing() {
        TestableToIntFunction<String> id = s -> 42;
        IntConsumer throwing = s -> {
            throw new NoSuchElementException();
        };
        id.thenDo(throwing).accept("foo");
        fail();
    }

    @Test
    public void testFilter() {
        int expected = 42;
        TestableToIntFunction<String> id = s -> expected;

        OptionalInt withValue = id.filter(s -> true).apply("");
        assertNotNull(withValue);
        assertTrue(withValue.isPresent());
        assertTrue(expected == withValue.getAsInt());

        OptionalInt withoutValue = id.filter(s -> false).apply("");
        assertNotNull(withoutValue);
        assertFalse(withoutValue.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullPointer() {
        TestableToIntFunction<String> id = s -> 42;
        id.filter(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterThisThrowing() {
        TestableToIntFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        IntPredicate then = s -> {
            fail();
            return false;
        };
        throwing.filter(then).apply("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterThatThrowing() {
        int output = 42;
        TestableToIntFunction<String> id = s -> output;
        IntPredicate then = s -> {
            assertTrue(s == output);
            throw new NoSuchElementException();
        };
        id.filter(then).apply("foo");
        fail();
    }

    @Test
    public void testRequires() {
        String input = "foo";
        int output = 42;

        AtomicBoolean called = new AtomicBoolean(false);
        TestableToIntFunction<String> tf = s -> {
            assertTrue(input == s);
            called.set(true);
            return output;
        };

        OptionalInt withValue = tf.requires(s -> true).apply(input);
        assertNotNull(withValue);
        assertTrue(withValue.isPresent());
        assertTrue(withValue.getAsInt() == output);

        TestableToIntFunction<String> neverCall = s -> {
            fail();
            return output;
        };
        OptionalInt withoutValue = neverCall.requires(s -> false).apply(input);
        assertNotNull(withoutValue);
        assertFalse(withoutValue.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testRequiresNullPointer() {
        TestableToIntFunction<String> id = s -> 42;
        id.requires(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRequiresThisThrowing() {
        TestableToIntFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        Predicate<String> then = s -> true;
        throwing.requires(then).apply("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRequiresThatThrowing() {
        String input = "foo";
        TestableToIntFunction<String> id = s -> {
            fail();
            return 42;
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
        int output = 42;
        TestableToIntFunction<String> toFoo = s -> output;
        OptionalInt empty = toFoo.nullAware().apply(null);
        assertNotNull(empty);
        assertFalse(empty.isPresent());

        OptionalInt notEmpty = toFoo.nullAware().apply("");
        assertNotNull(notEmpty);
        assertTrue(notEmpty.isPresent());
        assertTrue(output == notEmpty.getAsInt());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNullAwareThisThrowing() {
        TestableToIntFunction<String> id = s -> {
            throw new NoSuchElementException();
        };
        id.nullAware().apply("foo");
        fail();
    }

    @Test
    public void testWithCatch() {
        int expected = 42;
        TestableToIntFunction<String> id = s -> expected;
        OptionalInt result = id.withCatch().apply("");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsInt() == expected);
    }

    @Test
    public void testWithCatchThrowing() {
        TestableToIntFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String expected = "foo";
        OptionalInt result = throwing.withCatch().apply(expected);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testWithCatchByClass() {
        int output = 42;
        TestableToIntFunction<String> id = s -> output;
        String expected = "foo";
        OptionalInt result = id.withCatch(NoSuchElementException.class, e -> fail()).apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsInt() == output);
    }

    @Test
    public void testWithCatchByClassThrowingCaught() {
        TestableToIntFunction<String> throwing = s -> {
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
        TestableToIntFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        throwing.withCatch(UnsupportedOperationException.class, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer1() {
        TestableToIntFunction<String> id = s -> 42;
        String input = "foo";
        id.withCatch(null, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer2() {
        TestableToIntFunction<String> id = s -> 42;
        String input = "foo";
        id.withCatch(NoSuchElementException.class, null).apply(input);
        fail();
    }

    @Test
    public void testWithCatchHandler() {
        int expected = 42;
        TestableToIntFunction<String> id = s -> expected;
        OptionalInt result = id.withCatch(e -> fail()).apply("");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsInt() == expected);
    }

    @Test
    public void testWithCatchHandlerThrowingCaught() {
        TestableToIntFunction<String> throwing = s -> {
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
        TestableToIntFunction<String> throwing = s -> {
            throw new Error();
        };
        String input = "foo";
        throwing.withCatch( e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchHandlerNullPointer() {
        TestableToIntFunction<String> id = s -> 42;
        String input = "foo";
        id.withCatch(null).apply(input);
        fail();
    }

    @Test
    public void testRecoverWith() {
        int expected = 42;
        TestableToIntFunction<String> id = s -> expected;
        int result = id.recoverWith(t -> {
            fail();
            return -1;
        }).applyAsInt("");
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test
    public void testRecoverWithThrowingRecovered() {
        TestableToIntFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        int expected = 42;
        int result = throwing.recoverWith(e -> expected).applyAsInt(input);
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithNullPointer() {
        TestableToIntFunction<String> failing = s -> {
            fail();
            return -1;
        };
        failing.recoverWith(null);
        fail();
    }

    @Test
    public void testRecoverWithByClass() {
        int expected = 42;
        TestableToIntFunction<String> id = s -> expected;
        int result = id.recoverWith(Throwable.class, t -> {
            fail();
            return -1;
        }).applyAsInt("");
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test
    public void testRecoverWithByClassThrowingRecovered() {
        TestableToIntFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        int expected = 42;
        int result = throwing.recoverWith(NoSuchElementException.class, e -> expected).applyAsInt(input);
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRecoverWithByClassThrowingNotRecovered() {
        TestableToIntFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        throwing.recoverWith(UnsupportedOperationException.class, e -> {
            fail();
            return -1;
        }).applyAsInt("foo");
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithByClassNullPointer1() {
        TestableToIntFunction<String> failing = s -> {
            fail();
            return -1;
        };
        failing.recoverWith(null, e->{
            fail();
            return -1;
        }).applyAsInt("foo");
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithByClassNullPointer2() {
        TestableToIntFunction<String> failing = s -> {
            fail();
            return -1;
        };
        failing.recoverWith(Exception.class, null).applyAsInt("foo");
        fail();
    }
    
}
