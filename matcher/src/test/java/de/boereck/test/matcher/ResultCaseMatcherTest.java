package de.boereck.test.matcher;

import static de.boereck.matcher.eager.EagerMatcher.*;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.function.Predicate;

import org.junit.Test;


public class ResultCaseMatcherTest {

    @Test
    public void testCaseOfClass() {
        Object o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(String.class, s -> true)
                .result();
        check(res);
    }

    void check(Optional<Boolean> result) {
        assertNotNull(result);
        Boolean resultVal = result.get();
        assertNotNull(resultVal);
        assertTrue(resultVal);
    }

    @Test
    public void testCaseOfClassFirstNoMatch() {
        Object o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(Class.class, s -> false)
                .caseOf(String.class, s -> true)
                .result();

        check(res);
    }

    @Test
    public void testCaseOfClassSecondAlsoMatch() {
        Object o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(Object.class, s -> true)
                .caseOf(String.class, s -> false)
                .result();

        check(res);
    }

    @Test
    public void testCaseOfClassSecondNoMatch() {
        Object o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(Object.class, s -> true)
                .caseOf(Class.class, s -> false)
                .result();

        check(res);
    }

    @Test
    public void testCaseOfClassNoMatch() {
        Object o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(Boolean.class, s -> false)
                .caseOf(Class.class, s -> false)
                .result();

        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test
    public void testCaseOfPredicate() {
        String o = "";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(o::isEmpty, s -> true)
                .result();
        check(res);
    }

    @Test
    public void testCaseOfFunction() {
        String o = "";
        Predicate<String> isEmpty = String::isEmpty;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(isEmpty, s -> true)
                .result();
        check(res);
    }

    @Test
    public void testCaseOfTypeAndPredicateFunction() {
        Object o = "";
        Predicate<String> isEmpty = String::isEmpty;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(String.class, isEmpty, s -> true)
                .result();
        check(res);
    }
}
