package de.boereck.test.matcher.eager;

import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.MatchHelpers.*;
import static org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;

public class EagerNoResultCaseMatcherTest {


    //////////////////////////////////////////////////////////////
    // Test of EagerNoResultCaseMatcher#caseOf(Class, Consumer) //
    //////////////////////////////////////////////////////////////

    @Test
    public void testCaseOfClass() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseOf(String.class, s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassFirstNoMatch() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(Class.class, c -> fail())
                .caseOf(String.class, s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassSecondAlsoMatch() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(Object.class, ob -> success.set(true))
                .caseOf(String.class, s -> fail());

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassSecondNoMatch() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(Object.class, ob -> success.set(true))
                .caseOf(Class.class, c -> fail());

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassNoMatch() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(Boolean.class, b -> fail())
                .caseOf(Class.class, c -> fail());

        assertFalse(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfClassNullPointer1() {
        Object o = "Boo";
        match(o).caseOf((Class<String>) null, s -> fail());
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfClassNullPointer2() {
        Object o = "Boo";
        match(o).caseOf(String.class, null);
        fail();
    }

    /////////////////////////////////////////////////////////////////////////
    // Test of EagerNoResultCaseMatcher#caseOf(Class, Predicate, Consumer) //
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void testCaseOfClassNotMatchingWithPredicate() {
        Object o = "Boo";
        match(o).caseOf(Integer.class, s -> {
            fail();
            return false;
        }, s -> fail());
    }

    @Test
    public void testCaseOfClassWithPredicateMatching() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseOf(String.class, s -> true, s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassWithPredicateNotMatching() {
        Object o = "Boo";
        match(o).caseOf(String.class, s -> false, s -> fail());
    }

    @Test
    public void testCaseOfClassWithPredicateNotMatchingThenMatching() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(String.class, s -> false, s -> fail())
                .caseOf(String.class, s -> true, s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassWithPredicateMatchingThenNotMatching() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(String.class, s -> true, s -> success.set(true))
                .caseOf(String.class, s -> false, s -> fail());

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassNotMatchingWithPredicateThenMatching() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(Integer.class, s -> true, s -> fail())
                .caseOf(String.class, s -> true, s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfClassWithPredicateTwoMatchingOnlyFirst() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(String.class, s -> true, s -> success.set(true))
                .caseOf(String.class, s -> true, s -> fail());

        assertTrue(success.get());
    }

    //////////////////////////////////////////////////////////////////
    // Test of EagerNoResultCaseMatcher#caseOf(Predicate, Consumer) //
    //////////////////////////////////////////////////////////////////

    @Test
    public void testCaseOfGetsSameObjectAsInput() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseOf(s -> s == o, s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfPredicate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseOf(s -> true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfPredicateNoMatch() {
        String o = "";
        match(o).caseOf(s -> false, s -> fail());
    }

    @Test
    public void testCaseOfPredicateMatchThenDoNotEvaluate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(s -> true, s -> success.set(true))
                .caseOf(s -> {
                    fail();
                    return false;
                }, s -> fail());
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfPredicateNoMatchThenMatch() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(s -> false, s -> fail())
                .caseOf(s -> true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfPredicateNullPointer1() {
        String o = "";
        match(o).caseOf((Predicate<String>) null, s -> fail());
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfPredicateNullPointer2() {
        String o = "";
        match(o).caseOf(s -> true, null);
        fail();
    }

    ////////////////////////////////////////////////////////////////////////
    // Test of EagerNoResultCaseMatcher#caseOf(BooleanSupplier, Consumer) //
    ////////////////////////////////////////////////////////////////////////

    @Test
    public void testCaseOfBooleanSupplier() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseOf(() -> true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfBooleanSupplierNoMatch() {
        String o = "";
        match(o).caseOf(() -> false, s -> fail());
    }

    @Test
    public void testCaseOfBooleanSupplierMatchThenDoNotEvaluate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(() -> true, s -> success.set(true))
                .caseOf(() -> {
                    fail();
                    return false;
                }, s -> fail());
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfBooleanSupplierNoMatchThenMatch() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(() -> false, s -> fail())
                .caseOf(() -> true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanSupplierNullPointer1() {
        String o = "";
        match(o).caseOf((BooleanSupplier) null, s -> fail());
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanSupplierNullPointer2() {
        String o = "";
        match(o).caseOf(() -> true, null);
        fail();
    }

    ////////////////////////////////////////////////////////////////
    // Test of EagerNoResultCaseMatcher#caseOf(boolean, Consumer) //
    ////////////////////////////////////////////////////////////////

    @Test
    public void testCaseOfBool() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseOf(true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfBooleanNoMatch() {
        String o = "";
        match(o).caseOf(false, s -> fail());
    }

    @Test
    public void testCaseOfBooleanMatchThenDoNotEvaluate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(true, s -> success.set(true))
                .caseOf(true, s -> fail());
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfBooleanNoMatchThenMatch() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseOf(false, s -> fail())
                .caseOf(true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanNullPointer() {
        String o = "";
        match(o).caseOf(true, null);
        fail();
    }

    ////////////////////////////////////////////////////////////////
    // Test of EagerNoResultCaseMatcher#caseIs(boolean, Runnable) //
    ////////////////////////////////////////////////////////////////

    @Test
    public void testCaseIsBool() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseIs(true, () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testCaseIsBooleanNoMatch() {
        String o = "";
        match(o).caseIs(false, Assert::fail);
    }

    @Test
    public void testCaseIsBooleanMatchThenDoNotEvaluate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseIs(true, () -> success.set(true))
                .caseIs(true, Assert::fail);
        assertTrue(success.get());
    }

    @Test
    public void testCaseIsBooleanNoMatchThenMatch() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseIs(false, Assert::fail)
                .caseIs(true, () -> success.set(true));
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsBooleanNullPointer() {
        String o = "";
        match(o).caseIs(true, null);
        fail();
    }

    //////////////////////////////////////////////////////////////////
    // Test of EagerNoResultCaseMatcher#caseIs(Predicate, Runnable) //
    //////////////////////////////////////////////////////////////////

    @Test
    public void testCaseIsInputEqualsMatchPredicateBool() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseIs(s -> s == o, () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testCaseIsPredicateBool() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseIs(s -> true, () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testCaseIsPredicateBooleanNoMatch() {
        String o = "";
        match(o).caseIs(s -> false, Assert::fail);
    }

    @Test
    public void testCaseIsPredicateBooleanMatchThenDoNotEvaluate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseIs(s -> true, () -> success.set(true))
                .caseIs(s -> true, Assert::fail);
        assertTrue(success.get());
    }

    @Test
    public void testCaseIsPredicateBooleanNoMatchThenMatch() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseIs(s -> false, Assert::fail)
                .caseIs(s -> true, () -> success.set(true));
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsPredicateBooleanNullPointer() {
        String o = "";
        match(o).caseIs(s -> true, null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsPredicateBooleanNullPointer2() {
        String o = "";
        match(o).caseIs(null, Assert::fail);
        fail();
    }

    ////////////////////////////////////////////////////////////////////
    // Tests for EagerNoResultCaseMatcher#caseObj(Function, Consumer) //
    ////////////////////////////////////////////////////////////////////

    @Test
    public void testCaseOfInputEqualsMatchFunction() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseObj(Optional::ofNullable, s -> success.set(s == o));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfFunction() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseObj(Optional::ofNullable, s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfFunctionNoMatch() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(true);
        match(o).caseObj(s -> Optional.empty(), s -> success.set(false));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfFunctionMatchThenDoNotEvaluate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseObj(Optional::ofNullable, s -> success.set(true))
                .caseObj(s -> {
                    fail();
                    return Optional.empty();
                }, s -> fail());

        assertTrue(success.get());
    }


    private static final Function<Object, Optional<Integer>> strLen = cast(String.class).map(String::length);

    @Test
    public void testCaseOfFunctionCheckParameter() {
        Object o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseObj(strLen, in -> success.set(in == 3));

        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer1() {
        String o = "";
        match(o).caseObj(null, s -> {
            fail();
        });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer2() {
        String o = "";
        match(o).caseObj(s -> Optional.empty(), null);
        fail();
    }

    ///////////////////////////////////////////////////////////////////////
    // Tests for EagerNoResultCaseMatcher#caseInt(Function, IntConsumer) //
    ///////////////////////////////////////////////////////////////////////

    private static final OptionalInt one = OptionalInt.of(1);

    @Test
    public void testCaseIntInputEqualsMatchFunction() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseInt(s -> {
            assertTrue(s == o);
            return one;
        }, i -> {
            assertEquals(1, i);
            success.set(true);
        });

        assertTrue(success.get());
    }

    @Test
    public void testCaseIntFunctionNoMatch() {
        String o = "";
        match(o).caseInt(s -> OptionalInt.empty(), s -> fail());
    }

    @Test
    public void testCaseIntFunctionMatchThenDoNotEvaluate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseInt(s -> one, s -> success.set(true))
                .caseInt(s -> {
                    fail();
                    return OptionalInt.empty();
                }, s -> fail());

        assertTrue(success.get());
    }

    @Test
    public void testCaseIntFunctionCheckParameter() {
        String o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseInt(s -> OptionalInt.of(s.length()), in -> success.set(in == 3));

        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer1() {
        String o = "";
        match(o).caseInt(null, s -> {
            fail();
        });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer2() {
        String o = "";
        match(o).caseInt(s -> OptionalInt.empty(), null);
        fail();
    }

    /////////////////////////////////////////////////////////////////////////
    // Tests for EagerNoResultCaseMatcher#caseLong(Function, LongConsumer) //
    /////////////////////////////////////////////////////////////////////////

    private static final OptionalLong oneL = OptionalLong.of(1);

    @Test
    public void testCaseLongInputEqualsMatchFunction() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseLong(s -> {
            assertTrue(s == o);
            return oneL;
        }, i -> {
            assertEquals(1, i);
            success.set(true);
        });

        assertTrue(success.get());
    }

    @Test
    public void testCaseLongFunctionNoMatch() {
        String o = "";
        match(o).caseLong(s -> OptionalLong.empty(), s -> fail());
    }

    @Test
    public void testCaseLongFunctionMatchThenDoNotEvaluate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseLong(s -> oneL, s -> success.set(true))
                .caseLong(s -> {
                    fail();
                    return OptionalLong.empty();
                }, s -> fail());

        assertTrue(success.get());
    }

    @Test
    public void testCaseLongFunctionCheckParameter() {
        String o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseLong(s -> OptionalLong.of(s.length()), in -> success.set(in == 3));

        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer1() {
        String o = "";
        match(o).caseLong(null, s -> {
            fail();
        });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer2() {
        String o = "";
        match(o).caseLong(s -> OptionalLong.empty(), null);
        fail();
    }

    /////////////////////////////////////////////////////////////////////////////
    // Tests for EagerNoResultCaseMatcher#caseDouble(Function, DoubleConsumer) //
    /////////////////////////////////////////////////////////////////////////////

    private static final OptionalDouble oneD = OptionalDouble.of(0.0);

    @Test
    public void testCaseDoubleInputEqualsMatchFunction() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseDouble(s -> {
            assertTrue(s == o);
            return oneD;
        }, i -> {
            assertEquals(0.0, i, 0.0);
            success.set(true);
        });

        assertTrue(success.get());
    }

    @Test
    public void testCaseDoubleFunctionNoMatch() {
        String o = "";
        match(o).caseDouble(s -> OptionalDouble.empty(), s -> fail());
    }

    @Test
    public void testCaseDoubleFunctionMatchThenDoNotEvaluate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o)
                .caseDouble(s -> oneD, s -> success.set(true))
                .caseDouble(s -> {
                    fail();
                    return OptionalDouble.empty();
                }, s -> fail());

        assertTrue(success.get());
    }

    @Test
    public void testCaseDoubleFunctionCheckParameter() {
        String o = "Boo";
        AtomicBoolean success = new AtomicBoolean(false);
        match(o).caseDouble(s -> OptionalDouble.of(s.length()), in -> success.set(in == 3));

        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer1() {
        String o = "";
        match(o).caseDouble(null, s -> {
            fail();
        });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer2() {
        String o = "";
        match(o).caseDouble(s -> OptionalDouble.empty(), null);
        fail();
    }

    //////////////////////////////////////////////////////////
    // Tests for EagerNoResultCaseMatcher#otherwise(Object) //
    //////////////////////////////////////////////////////////

    @Test
    public void testOtherwise() {
        AtomicBoolean success = new AtomicBoolean(false);
        match("foo").otherwise(s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testOtherwiseNotReached() {
        match("foo").caseIs(true, () -> {
        }).otherwise(s -> fail());
    }

    /////////////////////////////////////////////////////////////////
    // Tests for EagerNoResultCaseMatcher#otherwiseThrow(Supplier) //
    /////////////////////////////////////////////////////////////////

    @Test(expected = NoSuchElementException.class)
    public void testOtherwiseThrow() {
        match("foo").otherwiseThrow(NoSuchElementException::new);
    fail();
    }

    @Test
    public void testOtherwiseThrowNotReached() {
        AtomicBoolean success = new AtomicBoolean(false);
        match("foo").caseIs(true, () -> {
            success.set(true);
        }).otherwiseThrow(NoSuchElementException::new);
        assertTrue(success.get());
    }

}
