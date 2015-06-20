package de.boereck.test.matcher.eager;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;

import static de.boereck.matcher.eager.EagerMatcher.match;
import static org.junit.Assert.*;

public class EagerNoResultLongCaseMatcherTest {


    private static final LongConsumer fail = o -> fail();


    private final Runnable failR = Assert::fail;

    //////////////////////////////////////////////////////////////////
    // Testing EagerNoResultLongCaseMatcher caseOf(int, IntConsumer) //
    //////////////////////////////////////////////////////////////////


    @Test
    public void testCaseOfIntFirstNoMatch() {
        long l = 42L;
        AtomicBoolean success = new AtomicBoolean(false);
        match(l)
                .caseOf(13L, fail)
                .caseOf(42L, o -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfIntSecondAlsoMatch() {
        long l = 42L;
        AtomicBoolean success = new AtomicBoolean(false);
        match(l)
                .caseOf(42L, s -> success.set(true))
                .caseOf(42L, fail);

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfIntSecondNoMatch() {
        long l = 42L;
        AtomicBoolean success = new AtomicBoolean(false);
        match(l)
                .caseOf(42L, o -> success.set(true))
                .caseOf(32, fail);

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfIntNoMatch() {
        long l = 42L;
        AtomicBoolean success = new AtomicBoolean(false);
        match(l)
                .caseOf(18, fail)
                .caseOf(5, fail);

        assertFalse(success.get());
    }

    ////////////////////////////////////////////////////////////////////////
    // Test of EagerNoResultLongCaseMatcher#caseOf(IntPredicate, IntConsumer) //
    ////////////////////////////////////////////////////////////////////////

    @Test
    public void testCaseOfGetsSameObjectAsInput() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseOf(i -> true, s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfPredicate() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseOf(s -> true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfPredicateNoMatch() {
        match(42L).caseOf(s -> false, s -> fail());
    }

    @Test
    public void testCaseOfPredicateMatchThenDoNotEvaluate() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseOf(s -> true, s -> success.set(true))
                .caseOf(s -> {
                    fail();
                    return false;
                }, fail);
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfPredicateNoMatchThenMatch() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseOf(s -> false, fail)
                .caseOf(s -> true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfPredicateNullPointer1() {
        match(42L).caseOf((LongPredicate) null, s -> fail());
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfPredicateNullPointer2() {
        match(42L).caseOf(s -> true, null);
        fail();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Test of EagerNoResultIntCaseMatcher#caseOf(BooleanSupplier, IntConsumer) //
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void testCaseOfBooleanSupplier() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseOf(() -> true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfBooleanSupplierNoMatch() {
        match(42L).caseOf(() -> false, fail);
    }

    @Test
    public void testCaseOfBooleanSupplierMatchThenDoNotEvaluate() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseOf(() -> true, s -> success.set(true))
                .caseOf(() -> {
                    fail();
                    return false;
                }, fail);
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfBooleanSupplierNoMatchThenMatch() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseOf(() -> false, fail)
                .caseOf(() -> true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanSupplierNullPointer1() {
        match(42L).caseOf((BooleanSupplier) null, fail);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanSupplierNullPointer2() {
        match(42L).caseOf(() -> true, null);
        fail();
    }

    ///////////////////////////////////////////////////////////////////
    // Test of EagerNoResultIntCaseMatcher#caseOf(boolean, IntConsumer) //
    ///////////////////////////////////////////////////////////////////

    @Test
    public void testCaseOfBool() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseOf(true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfBooleanNoMatch() {
        match(42L).caseOf(false, fail);
    }

    @Test
    public void testCaseOfBooleanMatchThenDoNotEvaluate() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseOf(true, s -> success.set(true))
                .caseOf(true, fail);
        assertTrue(success.get());
    }

    @Test
    public void testCaseOfBooleanNoMatchThenMatch() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseOf(false, fail)
                .caseOf(true, s -> success.set(true));
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanNullPointer() {
        match(42L).caseOf(true, null);
        fail();
    }

    ////////////////////////////////////////////////////////////////
    // Test of EagerNoResultIntCaseMatcher#caseIs(boolean, Runnable) //
    ////////////////////////////////////////////////////////////////

    @Test
    public void testCaseIsBool() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseIs(true, () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testCaseIsBooleanNoMatch() {
        match(42L).caseIs(false, failR);
    }

    @Test
    public void testCaseIsBooleanMatchThenDoNotEvaluate() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseIs(true, () -> success.set(true))
                .caseIs(true, failR);
        assertTrue(success.get());
    }

    @Test
    public void testCaseIsBooleanNoMatchThenMatch() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseIs(false, failR)
                .caseIs(true, () -> success.set(true));
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsBooleanNullPointer() {
        match(42L).caseIs(true, null);
        fail();
    }

    /////////////////////////////////////////////////////////////////////
    // Test of EagerNoResultIntCaseMatcher#caseIs(IntPredicate, Runnable) //
    /////////////////////////////////////////////////////////////////////

    @Test
    public void testCaseIsPredicateBool() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseIs(s -> true, () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testCaseIsPredicateBooleanNoMatch() {
        match(42L).caseIs(s -> false, failR);
    }

    @Test
    public void testCaseIsPredicateBooleanMatchThenDoNotEvaluate() {
        String o = "";
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseIs(s -> true, () -> success.set(true))
                .caseIs(s -> true, failR);
        assertTrue(success.get());
    }

    @Test
    public void testCaseIsPredicateBooleanNoMatchThenMatch() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseIs(s -> false, failR)
                .caseIs(s -> true, () -> success.set(true));
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsPredicateBooleanNullPointer() {
        match(42L).caseIs(s -> true, null);
        fail();
    }

    ///////////////////////////////////////////////////////////////////////
    // Tests for EagerNoResultIntCaseMatcher#caseObj(IntFunction, Consumer) //
    ///////////////////////////////////////////////////////////////////////

    @Test
    public void testCaseOfInputEqualsMatchFunction() {
        AtomicBoolean success = new AtomicBoolean(false);
        int expected = 42;
        match(expected).caseObj(Optional::ofNullable, s -> success.set(s == expected));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfFunction() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseObj(Optional::ofNullable, s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfFunctionNoMatch() {
        AtomicBoolean success = new AtomicBoolean(true);
        match(42L).caseObj(s -> Optional.empty(), s -> success.set(false));

        assertTrue(success.get());
    }

    @Test
    public void testCaseOfFunctionMatchThenDoNotEvaluate() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseObj(Optional::ofNullable, s -> success.set(true))
                .caseObj(s -> {
                    fail();
                    return Optional.empty();
                }, s -> fail());

        assertTrue(success.get());
    }


    @Test
    public void testCaseOfFunctionCheckParameter() {
        AtomicBoolean success = new AtomicBoolean(false);
        int expected = 42;
        match(expected).caseObj(Optional::of, in -> success.set(in == expected));

        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer1() {
        match(42L).caseObj(null, s -> {
        });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer2() {
        match(42L).caseObj(s -> Optional.empty(), null);
        fail();
    }

    //////////////////////////////////////////////////////////////////////////
    // Tests for EagerNoResultIntCaseMatcher#caseInt(IntFunction, IntConsumer) //
    //////////////////////////////////////////////////////////////////////////

    private static final OptionalInt one = OptionalInt.of(1);

    @Test
    public void testCaseIntInputEqualsMatchFunction() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseInt(s -> {
            assertTrue(s == 42);
            return one;
        }, i -> {
            assertEquals(1, i);
            success.set(true);
        });

        assertTrue(success.get());
    }

    @Test
    public void testCaseIntFunctionNoMatch() {
        match(42L).caseInt(s -> OptionalInt.empty(), s -> fail());
    }

    @Test
    public void testCaseIntFunctionMatchThenDoNotEvaluate() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseInt(s -> one, s -> success.set(true))
                .caseInt(s -> {
                    fail();
                    return OptionalInt.empty();
                }, s -> fail());

        assertTrue(success.get());
    }

    @Test
    public void testCaseIntFunctionCheckParameter() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseInt(l -> OptionalInt.of((int)l), in -> success.set(in == 42));

        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer1() {
        match(42L).caseInt(null, s -> {
        });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer2() {
        match(42L).caseInt(s -> OptionalInt.empty(), null);
        fail();
    }

    /////////////////////////////////////////////////////////////////////////
    // Tests for EagerNoResultIntCaseMatcher#caseLong(Function, LongConsumer) //
    /////////////////////////////////////////////////////////////////////////

    private static final OptionalLong oneL = OptionalLong.of(1);

    @Test
    public void testCaseLongInputEqualsMatchFunction() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseLong(s -> {
            assertTrue(s == 42);
            return oneL;
        }, i -> {
            assertEquals(1, i);
            success.set(true);
        });

        assertTrue(success.get());
    }

    @Test
    public void testCaseLongFunctionNoMatch() {
        match(42L).caseLong(s -> OptionalLong.empty(), s -> fail());
    }

    @Test
    public void testCaseLongFunctionMatchThenDoNotEvaluate() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseLong(s -> oneL, s -> success.set(true))
                .caseLong(s -> {
                    fail();
                    return OptionalLong.empty();
                }, s -> fail());

        assertTrue(success.get());
    }

    @Test
    public void testCaseLongFunctionCheckParameter() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseLong(OptionalLong::of, in -> success.set(in == 42L));

        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer1() {
        match(42L).caseLong(null, s -> {
        });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer2() {
        match(42L).caseLong(s -> OptionalLong.empty(), null);
        fail();
    }


    /////////////////////////////////////////////////////////////////////////////
    // Tests for EagerNoResultIntCaseMatcher#caseDouble(Function, DoubleConsumer) //
    /////////////////////////////////////////////////////////////////////////////

    private static final OptionalDouble oneD = OptionalDouble.of(0.0);

    @Test
    public void testCaseDoubleInputEqualsMatchFunction() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).caseDouble(s -> {
            assertTrue(s == 42);
            return oneD;
        }, i -> {
            assertEquals(0.0, i, 0.0);
            success.set(true);
        });

        assertTrue(success.get());
    }

    @Test
    public void testCaseDoubleFunctionNoMatch() {
        match(42L).caseDouble(s -> OptionalDouble.empty(), s -> fail());
    }

    @Test
    public void testCaseDoubleFunctionMatchThenDoNotEvaluate() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L)
                .caseDouble(s -> oneD, s -> success.set(true))
                .caseDouble(s -> {
                    fail();
                    return OptionalDouble.empty();
                }, s -> fail());

        assertTrue(success.get());
    }

    @Test
    public void testCaseDoubleFunctionCheckParameter() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(0).caseDouble(OptionalDouble::of, in -> success.set(in == 0.0));

        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer1() {
        match(42L).caseDouble(null, s -> {
        });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer2() {
        match(42L).caseDouble(s -> OptionalDouble.empty(), null);
        fail();
    }

    /////////////////////////////////////////////////////////////
    // Tests for EagerNoResultIntCaseMatcher#otherwise(Object) //
    /////////////////////////////////////////////////////////////

    @Test
    public void testOtherwise() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).otherwise(s -> success.set(true));

        assertTrue(success.get());
    }

    @Test
    public void testOtherwiseNotReached() {
        match(42L).caseIs(true, () -> {
        }).otherwise(fail);
    }

    ////////////////////////////////////////////////////////////////////
    // Tests for EagerNoResultIntCaseMatcher#otherwiseThrow(Supplier) //
    ////////////////////////////////////////////////////////////////////

    @Test(expected = NoSuchElementException.class)
    public void testOtherwiseThrow() {
        AtomicBoolean success = new AtomicBoolean(false);
        match(42L).otherwiseThrow(NoSuchElementException::new);

        assertTrue(success.get());
    }

    @Test
    public void testOtherwiseThrowNotReached() {
        match(42L).caseIs(true, () -> {
        }).otherwiseThrow(NoSuchElementException::new);
    }

}
