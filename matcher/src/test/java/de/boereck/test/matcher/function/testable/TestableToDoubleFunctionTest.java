package de.boereck.test.matcher.function.testable;

import de.boereck.matcher.function.predicate.AdvPredicate;
import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.function.testable.TestableToDoubleFunction;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class TestableToDoubleFunctionTest {

    @Test
    public void testThenTestTrue() {
        final double expected = 0.0;
        final String input = "foo";
        TestableToDoubleFunction<String> tf = i -> {
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
        final double expected = 0.0;
        TestableToDoubleFunction<String> tf = s -> {
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
        TestableToDoubleFunction<String> tf = s -> 0.0;
        tf.thenTest(null).test("");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenTestExceptionNoEvalPredicate() {
        TestableToDoubleFunction<String> tf = s -> {
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
        double output = 0.0;
        String expected = "baz";
        AtomicBoolean result = new AtomicBoolean(false);
        TestableToDoubleFunction<String> id = s -> {
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
        TestableToDoubleFunction<String> id = s -> 0.0;
        id.andThen(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThenThisThrowing() {
        TestableToDoubleFunction<String> throwing = s -> {
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
        TestableToDoubleFunction<String> id = s -> 0.0;
        DoubleFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };

        id.andThen(throwing).apply("foo");
        fail();
    }

    @Test
    public void testCompose() {
        String input = "foo";
        String output = "bar";
        double expectedResult = 0.0;
        TestableToDoubleFunction<String> composed = s -> {
            assertTrue(output == s);
            return expectedResult;
        };
        TestableFunction<String, String> first = s -> {
            assertTrue(s == input);
            return output;
        };
        double result = composed.compose(first).applyAsDouble(input);
        assertTrue(expectedResult == result);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullPointer() {
        TestableToDoubleFunction<String> id = s -> 0.0;
        id.compose(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testComposeThisThrowing() {
        TestableToDoubleFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        TestableFunction<String, String> id = s -> s;
        throwing.compose(id).applyAsDouble("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testComposeThatThrowing() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        TestableToDoubleFunction<String> id = s -> 0.0;
        id.compose(throwing).applyAsDouble("foo");
        fail();
    }

    @Test
    public void testThenDo() {
        String input = "foo";
        double output = 0.0;
        AtomicBoolean result = new AtomicBoolean(false);
        TestableToDoubleFunction<String> first = s -> {
            assertTrue(s == input);
            return output;
        };
        DoubleConsumer then = s -> {
            assertTrue(s == output);
            result.set(true);
        };
        first.thenDo(then).accept(input);
        assertTrue(result.get());
    }

    @Test(expected = NullPointerException.class)
    public void testThenDoNullPointer() {
        TestableToDoubleFunction<String> id = s -> 0.0;
        id.thenDo(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenDoThisThrowing() {
        TestableToDoubleFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        DoubleConsumer then = s -> fail();
        throwing.thenDo(then).accept("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenDoThatThrowing() {
        TestableToDoubleFunction<String> id = s -> 0.0;
        DoubleConsumer throwing = s -> {
            throw new NoSuchElementException();
        };
        id.thenDo(throwing).accept("foo");
        fail();
    }

    @Test
    public void testFilter() {
        double expected = 0.0;
        TestableToDoubleFunction<String> id = s -> expected;

        OptionalDouble withValue = id.filter(s -> true).apply("");
        assertNotNull(withValue);
        assertTrue(withValue.isPresent());
        assertTrue(expected == withValue.getAsDouble());

        OptionalDouble withoutValue = id.filter(s -> false).apply("");
        assertNotNull(withoutValue);
        assertFalse(withoutValue.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullPointer() {
        TestableToDoubleFunction<String> id = s -> 0.0;
        id.filter(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterThisThrowing() {
        TestableToDoubleFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        DoublePredicate then = s -> {
            fail();
            return false;
        };
        throwing.filter(then).apply("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterThatThrowing() {
        double output = 0.0;
        TestableToDoubleFunction<String> id = s -> output;
        DoublePredicate then = s -> {
            assertTrue(s == output);
            throw new NoSuchElementException();
        };
        id.filter(then).apply("foo");
        fail();
    }

    @Test
    public void testRequires() {
        String input = "foo";
        double output = 0.0;

        AtomicBoolean called = new AtomicBoolean(false);
        TestableToDoubleFunction<String> tf = s -> {
            assertTrue(input == s);
            called.set(true);
            return output;
        };

        OptionalDouble withValue = tf.requires(s -> true).apply(input);
        assertNotNull(withValue);
        assertTrue(withValue.isPresent());
        assertTrue(withValue.getAsDouble() == output);

        TestableToDoubleFunction<String> neverCall = s -> {
            fail();
            return output;
        };
        OptionalDouble withoutValue = neverCall.requires(s -> false).apply(input);
        assertNotNull(withoutValue);
        assertFalse(withoutValue.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testRequiresNullPointer() {
        TestableToDoubleFunction<String> id = s -> 0.0;
        id.requires(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRequiresThisThrowing() {
        TestableToDoubleFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        Predicate<String> then = s -> true;
        throwing.requires(then).apply("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRequiresThatThrowing() {
        String input = "foo";
        TestableToDoubleFunction<String> id = s -> {
            fail();
            return 0.0;
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
        double output = 0.0;
        TestableToDoubleFunction<String> toFoo = s -> output;
        OptionalDouble empty = toFoo.nullAware().apply(null);
        assertNotNull(empty);
        assertFalse(empty.isPresent());

        OptionalDouble notEmpty = toFoo.nullAware().apply("");
        assertNotNull(notEmpty);
        assertTrue(notEmpty.isPresent());
        assertTrue(output == notEmpty.getAsDouble());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNullAwareThisThrowing() {
        TestableToDoubleFunction<String> id = s -> {
            throw new NoSuchElementException();
        };
        id.nullAware().apply("foo");
        fail();
    }

    @Test
    public void testWithCatch() {
        double expected = 0.0;
        TestableToDoubleFunction<String> id = s -> expected;
        OptionalDouble result = id.withCatch().apply("");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsDouble() == expected);
    }

    @Test
    public void testWithCatchThrowing() {
        TestableToDoubleFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String expected = "foo";
        OptionalDouble result = throwing.withCatch().apply(expected);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testWithCatchByClass() {
        double output = 0.0;
        TestableToDoubleFunction<String> id = s -> output;
        String expected = "foo";
        OptionalDouble result = id.withCatch(NoSuchElementException.class, e -> fail()).apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsDouble() == output);
    }

    @Test
    public void testWithCatchByClassThrowingCaught() {
        TestableToDoubleFunction<String> throwing = s -> {
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
        TestableToDoubleFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        throwing.withCatch(UnsupportedOperationException.class, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer1() {
        TestableToDoubleFunction<String> id = s -> 0.0;
        String input = "foo";
        id.withCatch(null, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer2() {
        TestableToDoubleFunction<String> id = s -> 0.0;
        String input = "foo";
        id.withCatch(NoSuchElementException.class, null).apply(input);
        fail();
    }

    @Test
    public void testWithCatchHandler() {
        double expected = 0.0;
        TestableToDoubleFunction<String> id = s -> expected;
        OptionalDouble result = id.withCatch(e -> fail()).apply("");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.getAsDouble() == expected);
    }

    @Test
    public void testWithCatchHandlerThrowingCaught() {
        TestableToDoubleFunction<String> throwing = s -> {
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
        TestableToDoubleFunction<String> throwing = s -> {
            throw new Error();
        };
        String input = "foo";
        throwing.withCatch( e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchHandlerNullPointer() {
        TestableToDoubleFunction<String> id = s -> 0.0;
        String input = "foo";
        id.withCatch(null).apply(input);
        fail();
    }

    @Test
    public void testRecoverWith() {
        double expected = 0.0;
        TestableToDoubleFunction<String> id = s -> expected;
        double result = id.recoverWith(t -> {
            fail();
            return -1;
        }).applyAsDouble("");
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test
    public void testRecoverWithThrowingRecovered() {
        TestableToDoubleFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        double expected = 0.0;
        double result = throwing.recoverWith(e -> expected).applyAsDouble(input);
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithNullPointer() {
        TestableToDoubleFunction<String> failing = s -> {
            fail();
            return -1;
        };
        failing.recoverWith(null);
        fail();
    }

    @Test
    public void testRecoverWithByClass() {
        double expected = 0.0;
        TestableToDoubleFunction<String> id = s -> expected;
        double result = id.recoverWith(Throwable.class, t -> {
            fail();
            return -1;
        }).applyAsDouble("");
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test
    public void testRecoverWithByClassThrowingRecovered() {
        TestableToDoubleFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        double expected = 0.0;
        double result = throwing.recoverWith(NoSuchElementException.class, e -> expected).applyAsDouble(input);
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRecoverWithByClassThrowingNotRecovered() {
        TestableToDoubleFunction<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        throwing.recoverWith(UnsupportedOperationException.class, e -> {
            fail();
            return -1;
        }).applyAsDouble("foo");
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithByClassNullPointer1() {
        TestableToDoubleFunction<String> failing = s -> {
            fail();
            return -1;
        };
        failing.recoverWith(null, e->{
            fail();
            return -1;
        }).applyAsDouble("foo");
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithByClassNullPointer2() {
        TestableToDoubleFunction<String> failing = s -> {
            fail();
            return -1;
        };
        failing.recoverWith(Exception.class, null).applyAsDouble("foo");
        fail();
    }
}
