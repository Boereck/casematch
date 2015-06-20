package de.boereck.test.matcher.function.predicate;

import de.boereck.matcher.function.predicate.AdvDoublePredicate;
import de.boereck.matcher.function.predicate.AdvIntPredicate;
import de.boereck.matcher.function.predicate.AdvPredicate;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Testing interface {@link AdvPredicate}
 */
public class AdvDoublePredicateTest {

    // TODO check exception guarantees

    @Test
    public void testThenOnTrue() {
        // then with true should invoke given function
        AdvDoublePredicate alwaysTrue = o -> true;
        final String returned = "Yippiekayeah!";
        final double input = 0.0;
        Optional<String> nonEmpty = alwaysTrue.ifThen(o -> {
            assertEquals(input, o, 0.0);
            return returned;
        }).apply(input);
        assertNotNull(nonEmpty);
        assertTrue(nonEmpty.isPresent());
        assertEquals(returned, nonEmpty.get());

        // then with true with then returning null should return emtpy optional
        Optional<Object> emptyOnNull = alwaysTrue.ifThen(o -> {
            assertEquals(input, o, 0.0);
            return null;
        }).apply(input);
        assertNotNull(emptyOnNull);
        assertFalse(emptyOnNull.isPresent());
    }

    @Test
    public void testThenOnFalse() {
        // then with false should invoke given function
        AdvDoublePredicate alwaysFalse = o -> false;
        Optional<Object> empty = alwaysFalse.ifThen(o -> {
            fail();
            return null;
        }).apply(0.0);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testThenWithNullParam() {
        AdvDoublePredicate alwaysTrue = o -> true;
        alwaysTrue.ifThen(null).apply(0.0);
    }

    @Test
    public void testThenFlatOnTrue() {
        AdvDoublePredicate alwaysTrue = o -> true;
        double input = 0.0;
        String output = "Arrrrr";
        Optional<String> result = alwaysTrue.ifThenFlat(o -> {
            assertEquals(input, o, 0.0);
            return Optional.of(output);
        }).apply(0.0);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(output, result.get());
    }

    @Test
    public void testThenFlatOnFalse() {
        AdvDoublePredicate alwaysFalse = o -> false;
        double input = 0.0;
        Optional<String> result = alwaysFalse.ifThenFlat(o -> {
            fail();
            return Optional.of("Arrrrr");
        }).apply(input);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testAnd() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysTrue = o -> true;
        final double o = 0.0;
        // According to truth table
        assertTrue(alwaysTrue.and(alwaysTrue).test(o));
        assertFalse(alwaysFalse.and(alwaysTrue).test(o));
        assertFalse(alwaysTrue.and(alwaysFalse).test(o));
        assertFalse(alwaysFalse.and(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testAndNullPointer() {
        AdvDoublePredicate alwaysTrue = o -> true;
        alwaysTrue.and(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThisFails() {
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.and(s -> {
            fail();
            return false;
        }).test(0.0);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAndThatFails() {
        AdvDoublePredicate alwaysTrue = o -> true;
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.and(alwaysThrows).test(0.0);
        fail();
    }

    @Test
    public void testOr() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysTrue = o -> true;
        final double o = 0.0;
        // According to truth table
        assertTrue(alwaysTrue.or(alwaysTrue).test(o));
        assertTrue(alwaysFalse.or(alwaysTrue).test(o));
        assertTrue(alwaysTrue.or(alwaysFalse).test(o));
        assertFalse(alwaysFalse.or(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testOrNullPointer() {
        AdvDoublePredicate alwaysTrue = o -> true;
        alwaysTrue.or(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrThisFails() {
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.or(s -> {
            fail();
            return false;
        }).test(0.0);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrThatFails() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysFalse.or(alwaysThrows).test(0.0);
        fail();
    }

    @Test
    public void testXor() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysTrue = o -> true;
        final double o = 0.0;
        // According to truth table
        assertFalse(alwaysTrue.xor(alwaysTrue).test(o));
        assertTrue(alwaysFalse.xor(alwaysTrue).test(o));
        assertTrue(alwaysTrue.xor(alwaysFalse).test(o));
        assertFalse(alwaysFalse.xor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testXorNullPointer() {
        AdvDoublePredicate alwaysTrue = o -> true;
        alwaysTrue.xor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXorThisFails() {
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.xor(s -> {
            fail();
            return false;
        }).test(0.0);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXorThatFails() {
        AdvDoublePredicate alwaysTrue = o -> true;
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.xor(alwaysThrows).test(0.0);
        fail();
    }

    @Test
    public void testNor() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysTrue = o -> true;
        final double o = 0.0;
        // According to truth table
        assertFalse(alwaysTrue.nor(alwaysTrue).test(o));
        assertFalse(alwaysFalse.nor(alwaysTrue).test(o));
        assertFalse(alwaysTrue.nor(alwaysFalse).test(o));
        assertTrue(alwaysFalse.nor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testNorNullPointer() {
        AdvDoublePredicate alwaysTrue = o -> true;
        alwaysTrue.nor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNorThisFails() {
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.nor(s -> {
            fail();
            return false;
        }).test(0.0);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNorThatFails() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysFalse.nor(alwaysThrows).test(0.0);
        fail();
    }

    @Test
    public void testXnor() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysTrue = o -> true;
        final double o = 0.0;
        // According to truth table
        assertTrue(alwaysTrue.xnor(alwaysTrue).test(o));
        assertFalse(alwaysFalse.xnor(alwaysTrue).test(o));
        assertFalse(alwaysTrue.xnor(alwaysFalse).test(o));
        assertTrue(alwaysFalse.xnor(alwaysFalse).test(o));
    }

    @Test(expected = NullPointerException.class)
    public void testXnorNullPointer() {
        AdvDoublePredicate alwaysTrue = o -> true;
        alwaysTrue.xnor(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXnorThisFails() {
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.xnor(s -> {
            fail();
            return false;
        }).test(0.0);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testXnorThatFails() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysFalse.xnor(alwaysThrows).test(0.0);
        fail();
    }

    @Test
    public void testImplies() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysTrue = o -> true;
        final double o = 0.0;
        // According to truth table
        assertTrue(alwaysTrue.implies(alwaysTrue).test(o));
        assertTrue(alwaysFalse.implies(alwaysTrue).test(o));
        assertFalse(alwaysTrue.implies(alwaysFalse).test(o));
        assertTrue(alwaysFalse.implies(alwaysFalse).test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testImpliesThisFails() {
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysThrows.implies(s -> {
            fail();
            return false;
        }).test(0.0);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testImpliesThatFails() {
        AdvDoublePredicate alwaysTrue = o -> true;
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        Object o = new Object();
        alwaysTrue.implies(alwaysThrows).test(0.0);
        fail();
    }

    @Test
    public void testNot() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysTrue = o -> true;
        final double o = 0.0;
        // According to truth table
        assertFalse(alwaysTrue.not().test(o));
        assertTrue(alwaysFalse.not().test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNotFails() {
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysThrows.not().test(0.0);
        fail();
    }

    @Test
    public void testNegate() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysTrue = o -> true;
        final double o = 0.0;
        // According to truth table
        assertFalse(alwaysTrue.negate().test(o));
        assertTrue(alwaysFalse.negate().test(o));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNegateFails() {
        AdvDoublePredicate alwaysThrows = s -> {
            throw new NoSuchElementException();
        };
        alwaysThrows.not().test(0.0);
        fail();
    }

    @Test
    public void testRequires() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysTrue = o -> true;
        final double o = 0.0;
        // like and, since it is a commutative operator
        assertTrue(alwaysTrue.requires(alwaysTrue).test(o));
        assertFalse(alwaysFalse.requires(alwaysTrue).test(o));
        assertFalse(alwaysTrue.requires(alwaysFalse).test(o));
        assertFalse(alwaysFalse.requires(alwaysFalse).test(o));
    }

    @Test
    public void testBoxed() {
        AdvDoublePredicate alwaysFalse = o -> false;
        AdvDoublePredicate alwaysTrue = o -> true;
        // like and, since it is a commutative operator
        assertTrue(alwaysTrue.boxed().test(0.0));
        assertFalse(alwaysFalse.boxed().test(0.0));
    }
}
