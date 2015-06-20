package de.boereck.test.matcher.function.predicate;

import de.boereck.matcher.function.predicate.AdvIntPredicate;
import de.boereck.matcher.function.predicate.AdvLongPredicate;
import de.boereck.matcher.function.predicate.AdvPredicate;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Testing interface {@link AdvPredicate}
 */
public class AdvIntPredicateTest {

    // TODO check exception guarantees

    @Test
    public void testThenOnTrue() {
        // then with true should invoke given function
        AdvIntPredicate alwaysTrue = o -> true;
        final String returned = "Yippiekayeah!";
        final int input = 42;
        Optional<String> nonEmpty = alwaysTrue.ifThen(o -> {
            assertEquals(input, o);
            return returned;
        }).apply(input);
        assertNotNull(nonEmpty);
        assertTrue(nonEmpty.isPresent());
        assertEquals(returned, nonEmpty.get());

        // then with true with then returning null should return emtpy optional
        Optional<Object> emptyOnNull = alwaysTrue.ifThen(o -> {
            assertEquals(input, o);
            return null;
        }).apply(input);
        assertNotNull(emptyOnNull);
        assertFalse(emptyOnNull.isPresent());
    }

    @Test
    public void testThenOnFalse() {
        // then with false should invoke given function
        AdvIntPredicate alwaysFalse = o -> false;
        Optional<Object> empty = alwaysFalse.ifThen(o -> {
            fail();
            return null;
        }).apply(42);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testThenWithNullParam() {
        AdvIntPredicate alwaysTrue = o -> true;
        alwaysTrue.ifThen(null).apply(42);
    }

    @Test
    public void testThenFlatOnTrue() {
        AdvIntPredicate alwaysTrue = o -> true;
        int input = 42;
        String output = "Arrrrr";
        Optional<String> result = alwaysTrue.ifThenFlat(o -> {
            assertEquals(input, o);
            return Optional.of(output);
        }).apply(42);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(output, result.get());
    }

    @Test
    public void testThenFlatOnFalse() {
        AdvIntPredicate alwaysFalse = o -> false;
        int input = 42;
        Optional<String> result = alwaysFalse.ifThenFlat(o -> {
            fail();
            return Optional.of("Arrrrr");
        }).apply(input);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testAnd() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysTrue = o -> true;
        final int o = 42;
        // According to truth table
        assertTrue(alwaysTrue.and(alwaysTrue).test(o));
        assertFalse(alwaysFalse.and(alwaysTrue).test(o));
        assertFalse(alwaysTrue.and(alwaysFalse).test(o));
        assertFalse(alwaysFalse.and(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testAndNullPointer() {
        AdvIntPredicate alwaysTrue = o -> true;
        alwaysTrue.and(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThisFails() {
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.and(s -> {
            fail();
            return false;
        }).test(42);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThatFails() {
        AdvIntPredicate alwaysTrue = o -> true;
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.and(alwaysThrows).test(42);
        fail();
    }

    @Test
    public void testOr() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysTrue = o -> true;
        final int o = 42;
        // According to truth table
        assertTrue(alwaysTrue.or(alwaysTrue).test(o));
        assertTrue(alwaysFalse.or(alwaysTrue).test(o));
        assertTrue(alwaysTrue.or(alwaysFalse).test(o));
        assertFalse(alwaysFalse.or(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testOrNullPointer() {
        AdvIntPredicate alwaysTrue = o -> true;
        alwaysTrue.or(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrThisFails() {
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.or(s -> {
            fail();
            return false;
        }).test(42);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrThatFails() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysFalse.or(alwaysThrows).test(42);
        fail();
    }

    @Test
    public void testXor() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysTrue = o -> true;
        final int o = 42;
        // According to truth table
        assertFalse(alwaysTrue.xor(alwaysTrue).test(o));
        assertTrue(alwaysFalse.xor(alwaysTrue).test(o));
        assertTrue(alwaysTrue.xor(alwaysFalse).test(o));
        assertFalse(alwaysFalse.xor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testXorNullPointer() {
        AdvIntPredicate alwaysTrue = o -> true;
        alwaysTrue.xor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXorThisFails() {
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.xor(s -> {
            fail();
            return false;
        }).test(42);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXorThatFails() {
        AdvIntPredicate alwaysTrue = o -> true;
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.xor(alwaysThrows).test(42);
        fail();
    }

    @Test
    public void testNor() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysTrue = o -> true;
        final int o = 42;
        // According to truth table
        assertFalse(alwaysTrue.nor(alwaysTrue).test(o));
        assertFalse(alwaysFalse.nor(alwaysTrue).test(o));
        assertFalse(alwaysTrue.nor(alwaysFalse).test(o));
        assertTrue(alwaysFalse.nor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testNorNullPointer() {
        AdvIntPredicate alwaysTrue = o -> true;
        alwaysTrue.nor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNorThisFails() {
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.nor(s -> {
            fail();
            return false;
        }).test(42);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNorThatFails() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysFalse.nor(alwaysThrows).test(42);
        fail();
    }

    @Test
    public void testXnor() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysTrue = o -> true;
        final int o = 42;
        // According to truth table
        assertTrue(alwaysTrue.xnor(alwaysTrue).test(o));
        assertFalse(alwaysFalse.xnor(alwaysTrue).test(o));
        assertFalse(alwaysTrue.xnor(alwaysFalse).test(o));
        assertTrue(alwaysFalse.xnor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testXnorNullPointer() {
        AdvIntPredicate alwaysTrue = o -> true;
        alwaysTrue.xnor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXnorThisFails() {
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.xnor(s -> {
            fail();
            return false;
        }).test(42);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXnorThatFails() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysFalse.xnor(alwaysThrows).test(42);
        fail();
    }

    @Test
    public void testImplies() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysTrue = o -> true;
        final int o = 42;
        // According to truth table
        assertTrue(alwaysTrue.implies(alwaysTrue).test(o));
        assertTrue(alwaysFalse.implies(alwaysTrue).test(o));
        assertFalse(alwaysTrue.implies(alwaysFalse).test(o));
        assertTrue(alwaysFalse.implies(alwaysFalse).test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testImpliesThisFails() {
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.implies(s -> {
            fail();
            return false;
        }).test(42);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testImpliesThatFails() {
        AdvIntPredicate alwaysTrue = o -> true;
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.implies(alwaysThrows).test(42);
        fail();
    }

    @Test
    public void testNot() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysTrue = o -> true;
        final int o = 42;
        // According to truth table
        assertFalse(alwaysTrue.not().test(o));
        assertTrue(alwaysFalse.not().test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNotFails() {
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysThrows.not().test(42);
        fail();
    }

    @Test
    public void testNegate() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysTrue = o -> true;
        final int o = 42;
        // According to truth table
        assertFalse(alwaysTrue.negate().test(o));
        assertTrue(alwaysFalse.negate().test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNegateFails() {
        AdvIntPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysThrows.not().test(42);
        fail();
    }

    @Test
    public void testRequires() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysTrue = o -> true;
        final int o = 42;
        // like and, since it is a commutative operator
        assertTrue(alwaysTrue.requires(alwaysTrue).test(o));
        assertFalse(alwaysFalse.requires(alwaysTrue).test(o));
        assertFalse(alwaysTrue.requires(alwaysFalse).test(o));
        assertFalse(alwaysFalse.requires(alwaysFalse).test(o));
    }

    @Test
    public void testBoxed() {
        AdvIntPredicate alwaysFalse = o -> false;
        AdvIntPredicate alwaysTrue = o -> true;
        // like and, since it is a commutative operator
        assertTrue(alwaysTrue.boxed().test(42));
        assertFalse(alwaysFalse.boxed().test(42));
    }
}
