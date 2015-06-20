package de.boereck.test.matcher.function.predicate;

import de.boereck.matcher.function.predicate.AdvLongPredicate;
import de.boereck.matcher.function.predicate.AdvPredicate;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Testing interface {@link AdvPredicate}
 */
public class AdvLongPredicateTest {

    // TODO check exception guarantees

    @Test
    public void testThenOnTrue() {
        // then with true should invoke given function
        AdvLongPredicate alwaysTrue = o -> true;
        final String returned = "Yippiekayeah!";
        final long input = 42L;
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
        AdvLongPredicate alwaysFalse = o -> false;
        Optional<Object> empty = alwaysFalse.ifThen(o -> {
            fail();
            return null;
        }).apply(42L);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testThenWithNullParam() {
        AdvLongPredicate alwaysTrue = o -> true;
        alwaysTrue.ifThen(null).apply(42L);
    }

    @Test
    public void testThenFlatOnTrue() {
        AdvLongPredicate alwaysTrue = o -> true;
        long input = 42L;
        String output = "Arrrrr";
        Optional<String> result = alwaysTrue.ifThenFlat(o -> {
            assertEquals(input, o);
            return Optional.of(output);
        }).apply(42L);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(output, result.get());
    }

    @Test
    public void testThenFlatOnFalse() {
        AdvLongPredicate alwaysFalse = o -> false;
        long input = 42L;
        Optional<String> result = alwaysFalse.ifThenFlat(o -> {
            fail();
            return Optional.of("Arrrrr");
        }).apply(input);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testAnd() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysTrue = o -> true;
        final long o = 42L;
        // According to truth table
        assertTrue(alwaysTrue.and(alwaysTrue).test(o));
        assertFalse(alwaysFalse.and(alwaysTrue).test(o));
        assertFalse(alwaysTrue.and(alwaysFalse).test(o));
        assertFalse(alwaysFalse.and(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testAndNullPointer() {
        AdvLongPredicate alwaysTrue = o -> true;
        alwaysTrue.and(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThisFails() {
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.and(s -> {
            fail();
            return false;
        }).test(42L);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThatFails() {
        AdvLongPredicate alwaysTrue = o -> true;
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.and(alwaysThrows).test(42L);
        fail();
    }

    @Test
    public void testOr() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysTrue = o -> true;
        final long o = 42L;
        // According to truth table
        assertTrue(alwaysTrue.or(alwaysTrue).test(o));
        assertTrue(alwaysFalse.or(alwaysTrue).test(o));
        assertTrue(alwaysTrue.or(alwaysFalse).test(o));
        assertFalse(alwaysFalse.or(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testOrNullPointer() {
        AdvLongPredicate alwaysTrue = o -> true;
        alwaysTrue.or(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrThisFails() {
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.or(s -> {
            fail();
            return false;
        }).test(42L);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrThatFails() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysFalse.or(alwaysThrows).test(42L);
        fail();
    }

    @Test
    public void testXor() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysTrue = o -> true;
        final long o = 42L;
        // According to truth table
        assertFalse(alwaysTrue.xor(alwaysTrue).test(o));
        assertTrue(alwaysFalse.xor(alwaysTrue).test(o));
        assertTrue(alwaysTrue.xor(alwaysFalse).test(o));
        assertFalse(alwaysFalse.xor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testXorNullPointer() {
        AdvLongPredicate alwaysTrue = o -> true;
        alwaysTrue.xor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXorThisFails() {
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.xor(s -> {
            fail();
            return false;
        }).test(42L);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXorThatFails() {
        AdvLongPredicate alwaysTrue = o -> true;
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.xor(alwaysThrows).test(42L);
        fail();
    }

    @Test
    public void testNor() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysTrue = o -> true;
        final long o = 42L;
        // According to truth table
        assertFalse(alwaysTrue.nor(alwaysTrue).test(o));
        assertFalse(alwaysFalse.nor(alwaysTrue).test(o));
        assertFalse(alwaysTrue.nor(alwaysFalse).test(o));
        assertTrue(alwaysFalse.nor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testNorNullPointer() {
        AdvLongPredicate alwaysTrue = o -> true;
        alwaysTrue.nor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNorThisFails() {
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.nor(s -> {
            fail();
            return false;
        }).test(42L);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNorThatFails() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysFalse.nor(alwaysThrows).test(42L);
        fail();
    }

    @Test
    public void testXnor() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysTrue = o -> true;
        final long o = 42L;
        // According to truth table
        assertTrue(alwaysTrue.xnor(alwaysTrue).test(o));
        assertFalse(alwaysFalse.xnor(alwaysTrue).test(o));
        assertFalse(alwaysTrue.xnor(alwaysFalse).test(o));
        assertTrue(alwaysFalse.xnor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testXnorNullPointer() {
        AdvLongPredicate alwaysTrue = o -> true;
        alwaysTrue.xnor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXnorThisFails() {
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.xnor(s -> {
            fail();
            return false;
        }).test(42L);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXnorThatFails() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysFalse.xnor(alwaysThrows).test(42L);
        fail();
    }

    @Test
    public void testImplies() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysTrue = o -> true;
        final long o = 42L;
        // According to truth table
        assertTrue(alwaysTrue.implies(alwaysTrue).test(o));
        assertTrue(alwaysFalse.implies(alwaysTrue).test(o));
        assertFalse(alwaysTrue.implies(alwaysFalse).test(o));
        assertTrue(alwaysFalse.implies(alwaysFalse).test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testImpliesThisFails() {
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.implies(s -> {
            fail();
            return false;
        }).test(42L);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testImpliesThatFails() {
        AdvLongPredicate alwaysTrue = o -> true;
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.implies(alwaysThrows).test(42L);
        fail();
    }

    @Test
    public void testNot() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysTrue = o -> true;
        final long o = 42L;
        // According to truth table
        assertFalse(alwaysTrue.not().test(o));
        assertTrue(alwaysFalse.not().test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNotFails() {
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysThrows.not().test(42L);
        fail();
    }

    @Test
    public void testNegate() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysTrue = o -> true;
        final long o = 42L;
        // According to truth table
        assertFalse(alwaysTrue.negate().test(o));
        assertTrue(alwaysFalse.negate().test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNegateFails() {
        AdvLongPredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysThrows.not().test(42L);
        fail();
    }

    @Test
    public void testRequires() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysTrue = o -> true;
        final long o = 42L;
        // like and, since it is a commutative operator
        assertTrue(alwaysTrue.requires(alwaysTrue).test(o));
        assertFalse(alwaysFalse.requires(alwaysTrue).test(o));
        assertFalse(alwaysTrue.requires(alwaysFalse).test(o));
        assertFalse(alwaysFalse.requires(alwaysFalse).test(o));
    }

    @Test
    public void testBoxed() {
        AdvLongPredicate alwaysFalse = o -> false;
        AdvLongPredicate alwaysTrue = o -> true;
        // like and, since it is a commutative operator
        assertTrue(alwaysTrue.boxed().test(42L));
        assertFalse(alwaysFalse.boxed().test(42L));
    }
}
