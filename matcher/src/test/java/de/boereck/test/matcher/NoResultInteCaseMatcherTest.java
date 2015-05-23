package de.boereck.test.matcher;

import static de.boereck.matcher.eager.EagerMatcher.match;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntConsumer;

import org.junit.Test;

public class NoResultInteCaseMatcherTest {

    private static final IntConsumer fail = o -> fail();

    @Test
    public void testCaseOfIntFirstNoMatch() {
        int i = 42;
        AtomicBoolean success = new AtomicBoolean(false);
        match(i)
                .caseOf(13, fail)
                .caseOf(42, o -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfIntSecondAlsoMatch() {
        int i = 42;
        AtomicBoolean success = new AtomicBoolean(false);
        match(i)
                .caseOf(42, s -> success.set(true))
                .caseOf(42, fail);

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfIntSecondNoMatch() {
        int i = 42;
        AtomicBoolean success = new AtomicBoolean(false);
        match(i)
                .caseOf(42, o -> success.set(true))
                .caseOf(32, fail);

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfIntNoMatch() {
        int i = 42;
        AtomicBoolean success = new AtomicBoolean(false);
        match(i)
                .caseOf(18, fail)
                .caseOf(5, fail);

        assertFalse(success.get());
    }

    @Test
    public void testCaseOfPredicate() {
        int i = 42;
        AtomicBoolean success = new AtomicBoolean(false);
        match(i).caseOf(() -> i > 3, o -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfBoolExpr() {
        int i = 42;
        AtomicBoolean success = new AtomicBoolean(false);
        match(i).caseOf(i > 3, o -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfFunction() {
        int i = 42;
        AtomicBoolean success = new AtomicBoolean(false);
        match(i).caseObj(v -> v > 0 ? Optional.of(Math.sqrt(v)) : Optional.empty(), s -> success.set(true));

        assertTrue(success.get());
    }

}
