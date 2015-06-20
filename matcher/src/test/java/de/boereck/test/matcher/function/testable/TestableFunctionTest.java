package de.boereck.test.matcher.function.testable;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;
import de.boereck.matcher.function.testable.TestableFunction;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class TestableFunctionTest {

    @Test
    public void testThenTestTrue() {
        TestableFunction<String, String> tf = s -> s;
        final String expected = "";
        AdvPredicate<String> sut = tf.thenTest(s -> {
            assertTrue(s == expected);
            return true;
        });

        assertTrue(sut.test(expected));
    }

    @Test
    public void testThenTestFalse() {
        TestableFunction<String, String> tf = s -> s;
        final String expected = "";
        AdvPredicate<String> sut = tf.thenTest(s -> {
            assertTrue(s == expected);
            return false;
        });

        assertFalse(sut.test(expected));
    }

    @Test(expected = NullPointerException.class)
    public void testThenTestNullPointer() {
        TestableFunction<String, String> tf = s -> s;
        tf.thenTest(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenTestExceptionNoEvalPredicate() {
        TestableFunction<String, String> tf = s -> {
            throw new NoSuchElementException();
        };
        tf.thenTest(o -> {
            fail();
            return false;
        }).test("foo");
        fail();
    }

    @Test
    public void testOptionalNullOutput() {
        TestableFunction<String, String> tf = s -> null;
        OptionalMapper<String, String> sut = tf.optional();
        Optional<String> result = sut.apply("");
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testOptionalNonNullOutput() {
        TestableFunction<String, String> id = s -> s;
        OptionalMapper<String, String> sut = id.optional();
        String expected = "foo";
        Optional<String> result = sut.apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test(expected = NoSuchElementException.class)
    public void testOptionalThrowing() {
        TestableFunction<String, String> id = s -> {
            throw new NoSuchElementException();
        };
        OptionalMapper<String, String> sut = id.optional();
        String input = "foo";
        sut.apply(input);
        fail();
    }

    @Test
    public void testAndThen() {
        String input = "foo";
        String output = "bar";
        String expected = "baz";
        AtomicBoolean result = new AtomicBoolean(false);
        TestableFunction<String, String> id = s -> {
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
        TestableFunction<String, String> id = s -> s;
        id.andThen(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThenThisThrowing() {
        TestableFunction<String, String> throwing = s -> {
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
        TestableFunction<String, String> id = s -> s;
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };

        id.andThen(throwing).apply("foo");
        fail();
    }

    @Test
    public void testCompose() {
        String input = "foo";
        String output = "bar";
        String expectedResult = "baz";
        TestableFunction<String, String> composed = s -> {
            assertTrue(output == s);
            return expectedResult;
        };
        TestableFunction<String, String> first = s -> {
            assertTrue(s == input);
            return output;
        };
        String result = composed.compose(first).apply(input);
        assertTrue(expectedResult == result);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullPointer() {
        TestableFunction<String, String> id = s -> s;
        id.compose(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testComposeThisThrowing() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        TestableFunction<String, String> id = s -> s;
        throwing.compose(id).apply("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testComposeThatThrowing() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        TestableFunction<String, String> id = s -> s;
        id.compose(throwing).apply("foo");
        fail();
    }

    @Test
    public void testThenDo() {
        String input = "foo";
        String output = "bar";
        AtomicBoolean result = new AtomicBoolean(false);
        TestableFunction<String, String> first = s -> {
            assertTrue(s == input);
            return output;
        };
        Consumer<String> then = s -> {
            assertTrue(s == output);
            result.set(true);
        };
        first.thenDo(then).accept(input);
        assertTrue(result.get());
    }

    @Test(expected = NullPointerException.class)
    public void testThenDoNullPointer() {
        TestableFunction<String, String> id = s -> s;
        id.thenDo(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenDoThisThrowing() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        Consumer<String> then = s -> fail();
        throwing.thenDo(then).accept("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThenDoThatThrowing() {
        TestableFunction<String, String> id = s -> s;
        Consumer<String> throwing = s -> {
            throw new NoSuchElementException();
        };
        id.thenDo(throwing).accept("foo");
        fail();
    }

    @Test
    public void testFilter() {
        TestableFunction<String, String> id = s -> s;

        String expected = "foo";
        Optional<String> withValue = id.filter(s -> true).apply(expected);
        assertNotNull(withValue);
        assertTrue(withValue.isPresent());
        assertTrue(expected == withValue.get());

        Optional<String> withoutValue = id.filter(s -> false).apply(expected);
        assertNotNull(withoutValue);
        assertFalse(withoutValue.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullPointer() {
        TestableFunction<String, String> id = s -> s;
        id.filter((Predicate<String>) null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterThisThrowing() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        Predicate<String> then = s -> {
            fail();
            return false;
        };
        throwing.filter(then).apply("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterThatThrowing() {
        String input = "foo";
        TestableFunction<String, String> id = s -> s;
        Predicate<String> then = s -> {
            assertTrue(s == input);
            throw new NoSuchElementException();
        };
        id.filter(then).apply("foo");
        fail();
    }

    @Test
    public void testFilterClass() {
        TestableFunction<String, String> id = s -> s;

        String expected = "foo";
        Optional<String> withValue = id.filter(String.class).apply(expected);
        assertNotNull(withValue);
        assertTrue(withValue.isPresent());
        assertTrue(expected == withValue.get());

        Optional<Integer> withoutValue = id.filter(Integer.class).apply(expected);
        assertNotNull(withoutValue);
        assertFalse(withoutValue.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterClassNullPointer() {
        TestableFunction<String, String> id = s -> s;
        id.filter((Class) null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterClassThisThrowing() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        throwing.filter(String.class).apply("foo");
        fail();
    }

    @Test
    public void testRequires() {
        String input = "foo";
        String output = "bar";

        AtomicBoolean called = new AtomicBoolean(false);
        TestableFunction<String, String> tf = s -> {
            assertTrue(input == s);
            called.set(true);
            return output;
        };

        Optional<String> withValue = tf.requires(s -> true).apply(input);
        assertNotNull(withValue);
        assertTrue(withValue.isPresent());
        assertTrue(withValue.get() == output);

        TestableFunction<String, String> neverCall = s -> {
            fail();
            return output;
        };
        Optional<String> withoutValue = neverCall.requires(s -> false).apply(input);
        assertNotNull(withoutValue);
        assertFalse(withoutValue.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testRequiresNullPointer() {
        TestableFunction<String, String> id = s -> s;
        id.requires(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRequiresThisThrowing() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        Predicate<String> then = s -> true;
        throwing.requires(then).apply("foo");
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRequiresThatThrowing() {
        String input = "foo";
        TestableFunction<String, String> id = s -> {
            fail();
            return null;
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
        String foo = "foo";
        TestableFunction<String, String> toFoo = s -> foo;
        Optional<String> empty = toFoo.nullAware().apply(null);
        assertNotNull(empty);
        assertFalse(empty.isPresent());

        Optional<String> notEmpty = toFoo.nullAware().apply("");
        assertNotNull(notEmpty);
        assertTrue(notEmpty.isPresent());
        assertTrue(foo == notEmpty.get());

        TestableFunction<String, String> toNull = s -> null;
        Optional<String> emptyAgain = toNull.nullAware().apply("");
        assertNotNull(emptyAgain);
        assertFalse(emptyAgain.isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNullAwareThisThrowing() {
        TestableFunction<String, String> id = s -> {
            throw new NoSuchElementException();
        };
        id.nullAware().apply("foo");
        fail();
    }

    @Test
    public void testWithCatch() {
        TestableFunction<String, String> id = s -> s;
        String expected = "foo";
        Optional<String> result = id.withCatch().apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get() == expected);
    }

    @Test
    public void testWithCatchThrowing() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String expected = "foo";
        Optional<String> result = throwing.withCatch().apply(expected);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testWithCatchByClass() {
        TestableFunction<String, String> id = s -> s;
        String expected = "foo";
        Optional<String> result = id.withCatch(NoSuchElementException.class, e -> fail()).apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get() == expected);
    }

    @Test
    public void testWithCatchByClassThrowingCaught() {
        TestableFunction<String, String> throwing = s -> {
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
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        throwing.withCatch(UnsupportedOperationException.class, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer1() {
        TestableFunction<String, String> id = s -> s;
        String input = "foo";
        id.withCatch(null, e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchByClassNullPointer2() {
        TestableFunction<String, String> id = s -> s;
        String input = "foo";
        id.withCatch(NoSuchElementException.class, null).apply(input);
        fail();
    }

    @Test
    public void testWithCatchHandler() {
        TestableFunction<String, String> id = s -> s;
        String expected = "foo";
        Optional<String> result = id.withCatch(e -> fail()).apply(expected);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get() == expected);
    }

    @Test
    public void testWithCatchHandlerThrowingCaught() {
        TestableFunction<String, String> throwing = s -> {
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
        TestableFunction<String, String> throwing = s -> {
            throw new Error();
        };
        String input = "foo";
        throwing.withCatch( e -> fail()).apply(input);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testWithCatchHandlerNullPointer() {
        TestableFunction<String, String> id = s -> s;
        String input = "foo";
        id.withCatch(null).apply(input);
        fail();
    }

    @Test
    public void testRecoverWith() {
        TestableFunction<String,String> id = s -> s;
        String expected = "foo";
        String result = id.recoverWith(t -> {
            fail();
            return null;
        }).apply(expected);
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test
    public void testRecoverWithThrowingRecovered() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        String expected = "bar";
        String result = throwing.recoverWith(e -> expected).apply(input);
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithNullPointer() {
        TestableFunction<String, String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(null);
        fail();
    }

    @Test
    public void testRecoverWithByClass() {
        TestableFunction<String,String> id = s -> s;
        String expected = "foo";
        String result = id.recoverWith(Throwable.class, t -> {
            fail();
            return null;
        }).apply(expected);
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test
    public void testRecoverWithByClassThrowingRecovered() {
        TestableFunction<String, String> throwing = s -> {
            throw new NoSuchElementException();
        };
        String input = "foo";
        String expected = "bar";
        String result = throwing.recoverWith(NoSuchElementException.class, e -> expected).apply(input);
        assertNotNull(result);
        assertTrue(result == expected);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRecoverWithByClassThrowingNotRecovered() {
        TestableFunction<String, String> throwing = s -> {
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
        TestableFunction<String, String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(null, e->{
            fail();
            return null;
        }).apply("foo");
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testRecoverWithByClassNullPointer2() {
        TestableFunction<String, String> failing = s -> {
            fail();
            return null;
        };
        failing.recoverWith(Exception.class, null).apply("foo");
        fail();
    }
}
