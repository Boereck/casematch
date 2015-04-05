package de.boereck.matcher.de.boereck.test.matcher;

import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.MatchHelpers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import org.junit.Test;

import de.boereck.matcher.function.optionalmap.OptionalMapper;

public class NoResultCaseMatcherTest {

    @Test
    public void testCaseOfClass() {
        
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseOf(String.class, s -> success.set(true));

        assertTrue(success.get());
    }

    private static final Function<Object, Optional<Integer>> strLen = cast(String.class).map(String::length);

    @Test
    public void testCaseOfTransform() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseObj(strLen, in -> success.set(in == 3));

        assertTrue(success.get());
    }

    private static final OptionalMapper<Object, String> greater2chars = cast(String.class).filter(s -> s.length() > 2);

    @Test
    public void testCaseOfTransformWithCheck() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseObj(greater2chars, in -> success.set(in == o));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassFirstNoMatch() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseOf(Class.class, s -> fail()).caseOf(String.class, s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassSecondAlsoMatch() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseOf(Object.class, s -> success.set(true)).caseOf(String.class, s -> fail());

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassSecondNoMatch() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
        .caseOf(Object.class, s -> success.set(true))
        .caseOf(Class.class, s -> fail());
        
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassNoMatch() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseOf(Boolean.class, b -> fail()).caseOf(Class.class, c -> fail());

        assertFalse(success.get());
    }

    @Test
    public void testCaseOfPredicate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseOf(o::isEmpty, s -> success.set(true));
        assertTrue(success.get());
    }

    
    @Test
    public void testCaseOfFunction() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseObj(s -> Optional.ofNullable(s).map(String::toUpperCase), s -> success.set(true));
        
        assertTrue(success.get());
    }
}
