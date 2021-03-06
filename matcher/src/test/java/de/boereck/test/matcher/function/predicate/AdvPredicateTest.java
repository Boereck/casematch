package de.boereck.test.matcher.function.predicate;

import de.boereck.matcher.function.predicate.AdvPredicate;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Testing interface {@link de.boereck.matcher.function.predicate.AdvPredicate}
 */
public class AdvPredicateTest {

    // TODO check exception guarantees

    @Test
    public void testThenOnTrue() {
        // then with true should invoke given function
        AdvPredicate<Object> alwaysTrue = o -> true;
        final String returned = "Yippiekayeah!";
        final String input = "foobar";
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
        AdvPredicate<Object> alwaysFalse = o -> false;
        Optional<Object> empty = alwaysFalse.ifThen(o -> {
            fail();
            return null;
        }).apply(null);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testThenWithNullParam() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        alwaysTrue.ifThen(null).apply(null);
    }

    @Test
    public void testThenFlatOnTrue() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        String input = "HuiBuh";
        String output = "Arrrrr";
        Optional<String> result = alwaysTrue.ifThenFlat(o -> {
            assertEquals(input, o);
            return Optional.of(output);
        }).apply(input);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(output, result.get());
    }

    @Test
    public void testThenFlatOnFalse() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        String input = "HuiBuh";
        Optional<String> result = alwaysFalse.ifThenFlat(o -> {
            fail();
            return Optional.of("Arrrrr");
        }).apply(input);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testAnd() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysTrue = o -> true;
        Object o = new Object();
        // According to truth table
        assertTrue(alwaysTrue.and(alwaysTrue).test(o));
        assertFalse(alwaysFalse.and(alwaysTrue).test(o));
        assertFalse(alwaysTrue.and(alwaysFalse).test(o));
        assertFalse(alwaysFalse.and(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testAndNullPointer() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        alwaysTrue.and(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThisFails() {
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.and(s -> {
            fail();
            return false;
        }).test(o);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThatFails() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.and(alwaysThrows).test(o);
        fail();
    }

    @Test
    public void testOr() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysTrue = o -> true;
        Object o = new Object();
        // According to truth table
        assertTrue(alwaysTrue.or(alwaysTrue).test(o));
        assertTrue(alwaysFalse.or(alwaysTrue).test(o));
        assertTrue(alwaysTrue.or(alwaysFalse).test(o));
        assertFalse(alwaysFalse.or(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testOrNullPointer() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        alwaysTrue.or(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrThisFails() {
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.or(s -> {
            fail();
            return false;
        }).test(o);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrThatFails() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysFalse.or(alwaysThrows).test(o);
        fail();
    }

    @Test
    public void testXor() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysTrue = o -> true;
        Object o = new Object();
        // According to truth table
        assertFalse(alwaysTrue.xor(alwaysTrue).test(o));
        assertTrue(alwaysFalse.xor(alwaysTrue).test(o));
        assertTrue(alwaysTrue.xor(alwaysFalse).test(o));
        assertFalse(alwaysFalse.xor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testXorNullPointer() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        alwaysTrue.xor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXorThisFails() {
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.xor(s -> {
            fail();
            return false;
        }).test(o);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXorThatFails() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.xor(alwaysThrows).test(o);
        fail();
    }

    @Test
    public void testNor() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysTrue = o -> true;
        Object o = new Object();
        // According to truth table
        assertFalse(alwaysTrue.nor(alwaysTrue).test(o));
        assertFalse(alwaysFalse.nor(alwaysTrue).test(o));
        assertFalse(alwaysTrue.nor(alwaysFalse).test(o));
        assertTrue(alwaysFalse.nor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testNorNullPointer() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        alwaysTrue.nor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNorThisFails() {
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.nor(s -> {
            fail();
            return false;
        }).test(o);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNorThatFails() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysFalse.nor(alwaysThrows).test(o);
        fail();
    }

    @Test
    public void testXnor() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysTrue = o -> true;
        Object o = new Object();
        // According to truth table
        assertTrue(alwaysTrue.xnor(alwaysTrue).test(o));
        assertFalse(alwaysFalse.xnor(alwaysTrue).test(o));
        assertFalse(alwaysTrue.xnor(alwaysFalse).test(o));
        assertTrue(alwaysFalse.xnor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testXnorNullPointer() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        alwaysTrue.xnor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXnorThisFails() {
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.xnor(s -> {
            fail();
            return false;
        }).test(o);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXnorThatFails() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysFalse.xnor(alwaysThrows).test(o);
        fail();
    }

    @Test
    public void testImplies() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysTrue = o -> true;
        Object o = new Object();
        // According to truth table
        assertTrue(alwaysTrue.implies(alwaysTrue).test(o));
        assertTrue(alwaysFalse.implies(alwaysTrue).test(o));
        assertFalse(alwaysTrue.implies(alwaysFalse).test(o));
        assertTrue(alwaysFalse.implies(alwaysFalse).test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testImpliesThisFails() {
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.implies(s -> {
            fail();
            return false;
        }).test(o);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testImpliesThatFails() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.implies(alwaysThrows).test(o);
        fail();
    }

    @Test
    public void testNot() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysTrue = o -> true;
        Object o = new Object();
        // According to truth table
        assertFalse(alwaysTrue.not().test(o));
        assertTrue(alwaysFalse.not().test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNotFails() {
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysThrows.not().test(new Object());
        fail();
    }

    @Test
    public void testNegate() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysTrue = o -> true;
        Object o = new Object();
        // According to truth table
        assertFalse(alwaysTrue.negate().test(o));
        assertTrue(alwaysFalse.negate().test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNegateFails() {
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysThrows.not().test(new Object());
        fail();
    }

    @Test
    public void testRequires() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        AdvPredicate<Object> alwaysTrue = o -> true;
        Object o = new Object();
        // like and, since it is a commutative operator
        assertTrue(alwaysTrue.requires(alwaysTrue).test(o));
        assertFalse(alwaysFalse.requires(alwaysTrue).test(o));
        assertFalse(alwaysTrue.requires(alwaysFalse).test(o));
        assertFalse(alwaysFalse.requires(alwaysFalse).test(o));
    }

    @Test
    public void testNullToFalse() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        Object o = new Object();
        // like and, since it is a commutative operator
        assertFalse(alwaysTrue.nullToFalse().test(null));
        assertTrue(alwaysTrue.nullToFalse().test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNullToFalseFails() {
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysThrows.nullToFalse().test(new Object());
        fail();
    }

    @Test
    public void testNullToTrue() {
        AdvPredicate<Object> alwaysFalse = o -> false;
        Object o = new Object();
        // like and, since it is a commutative operator
        assertTrue(alwaysFalse.nullToTrue().test(null));
        assertFalse(alwaysFalse.nullToTrue().test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNullToTrueFails() {
        AdvPredicate<Object> alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysThrows.nullToTrue().test(new Object());
        fail();
    }
}
