package de.boereck.test.matcher.eager;

import de.boereck.matcher.eager.EagerMatchable;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.boereck.matcher.eager.EagerMatchable.matchable;
import static org.junit.Assert.*;

public class EagerMatchableTest {

    private static class EagerMatchImpl implements EagerMatchable<EagerMatchImpl> {
        int foo = 42;
    }

    @Test
    public void testMatchableMatch() {
        String expected = "foo";
        AtomicBoolean success = new AtomicBoolean(false);
        matchable(expected).match().caseIs(true, () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testMatchableResultMatch() {
        String expected = "foo";
        Optional<Boolean> res = matchable(expected)
                .<Boolean>resultMatch()
                .caseIs(true, () -> true)
                .result();
        assertNotNull(res);
        assertTrue(res.get());
    }

    @Test
    public void testMatchableImplMatch() {
        EagerMatchImpl matchable = new EagerMatchImpl();
        AtomicBoolean success = new AtomicBoolean(false);
        matchable.match().caseIs(m -> m.foo == 42, () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testMatchableImplResultMatch() {
        EagerMatchImpl matchable = new EagerMatchImpl();
        AtomicBoolean success = new AtomicBoolean(false);
        Optional<Boolean> res = matchable.
                <Boolean>resultMatch()
                .caseIs(m -> m.foo == 42, () -> true)
                .result();
        assertNotNull(res);
        assertTrue(res.get());
    }

}
