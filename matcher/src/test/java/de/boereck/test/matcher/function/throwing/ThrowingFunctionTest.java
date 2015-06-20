package de.boereck.test.matcher.function.throwing;

import de.boereck.matcher.function.throwing.ThrowingFunction;
import org.junit.Test;

import static de.boereck.matcher.function.throwing.ThrowingFunction.cloak;
import static de.boereck.matcher.function.throwing.ThrowingFunction.throwing;
import static org.junit.Assert.fail;

/**
 *
 */
public class ThrowingFunctionTest {

    static class MyExecption extends Exception {
    }

    @Test(expected = MyExecption.class)
    public void testThrowing() {
        ThrowingFunction<String, String, MyExecption> f = this::foo;
        f.apply("foo");
        fail();
    }

    String foo(String s) throws MyExecption {
        throw new MyExecption();
    }

    @Test(expected = NullPointerException.class)
    public void testThrowingRuntimeException() {
        ThrowingFunction<String, String, MyExecption> f = s -> {
            throw new NullPointerException();
        };
        f.apply("bar");
        fail();
    }

    @Test(expected = MyExecption.class)
    public void testCloak() {
        // change to cloak(this::foo).apply("foo");
        // when IntelliJ Issue is fixed and available in release:
        // https://youtrack.jetbrains.com/issue/IDEA-140586
        ThrowingFunction<String, String, MyExecption> f = this::foo;
        cloak(f).apply("foo");
        fail();
    }

//// Uncomment when bug from above fiexed
//    @Test(expected = Exception.class)
//    public void testThrowingBuilder() {
//        throwing(this::alwaysThrow).apply("");
//    }

    public String alwaysThrow(String input) throws Exception {
        throw new Exception();
    }
}
