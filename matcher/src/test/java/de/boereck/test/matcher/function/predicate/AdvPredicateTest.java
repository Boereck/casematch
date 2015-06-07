package de.boereck.test.matcher.function.predicate;

import de.boereck.matcher.function.predicate.AdvPredicate;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * Testing interface {@link de.boereck.matcher.function.predicate.AdvPredicate}
 */
public class AdvPredicateTest {

    @Test
    public void testThenOnTrue() {
        // then with true should invoke given function
        AdvPredicate<Object> alwaysTrue = o -> true;
        final String returned = "Yippiekayeah!";
        final String input = "foobar";
        Optional<String> nonEmpty = alwaysTrue.then(o -> {
            assertEquals(input, o);
            return returned;
        }).apply(input);
        assertNotNull(nonEmpty);
        assertTrue(nonEmpty.isPresent());
        assertEquals(returned, nonEmpty.get());

        // then with true with then returning null should return emtpy optional
        Optional<Object> emptyOnNull = alwaysTrue.then(o -> {
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
        Optional<Object> empty = alwaysFalse.then(o -> {
            fail();
            return null;
        }).apply(null);
        assertNotNull(empty);
        assertFalse(empty.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testThenWithNullParam() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        alwaysTrue.then(null).apply(null);
    }

    @Test
    public void testThenFlatOnTrue() {
        AdvPredicate<Object> alwaysTrue = o -> true;
        String input = "HuiBuh";
        String output = "Arrrrr";
        Optional<String> result = alwaysTrue.thenFlat(o -> {
            assertEquals(input, o);
            return Optional.of(output);
        }).apply(input);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(output, result.get());
    }
}
