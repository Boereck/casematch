package de.boereck.test.matcher.eager;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;

import static de.boereck.matcher.eager.EagerMatcher.resultMatch;
import static org.junit.Assert.*;


public class EagerResultIntCaseMatcherTest {

    void isTrue(Optional<Boolean> result) {
        assertNotNull(result);
        assertTrue(result.isPresent());
        Boolean resultVal = result.get();
        assertTrue(resultVal);
    }

    private static void isEmpty(Optional<Boolean> result) {
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testCaseOfGetsSameObjectAsInput() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(i -> i == o, i ->true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfPredicate() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(i ->true, i ->true)
                .result();
        isTrue(res);
    }

    @Test
    public void testCaseOfPredicateNoMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(i ->false, i ->{
                    fail();
                    return false;
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfPredicateMatchThenDoNotEvaluate() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(i ->true, i ->true)
                .caseOf(i ->{
                    fail();
                    return false;
                }, i ->{
                    fail();
                    return false;
                }).result();
        isTrue(result);
    }

    @Test
    public void testCaseOfPredicateNoMatchThenMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(i ->false, i ->{
                    fail();
                    return false;
                })
                .caseOf(i ->true, i ->true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfPredicateNullPointer1() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseOf((IntPredicate) null, i ->{
                    fail();
                    return false;
                });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfPredicateNullPointer2() {
        int o = 42;
        resultMatch(Boolean.class, o).caseOf(i ->true, null);
        fail();
    }

    @Test
    public void testCaseOfElement() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(o, i ->true)
                .result();
        isTrue(res);
    }

    @Test
    public void testCaseOfElementNoMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(43, i ->{
                    fail();
                    return false;
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfElementMatchThenDoNotEvaluate() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(o, i ->true)
                .caseOf(o, i ->{
                    fail();
                    return false;
                }).result();
        isTrue(result);
    }

    @Test
    public void testCaseOfElementNoMatchThenMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(43, i ->{
                    fail();
                    return false;
                })
                .caseOf(o, i ->true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfElementNullPointer2() {
        int o = 42;
        resultMatch(Boolean.class, o).caseOf(o, null);
        fail();
    }


    @Test
    public void testCaseOfBooleanSupplier() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> true, i ->true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanSupplierNoMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> false, i ->{
                    fail();
                    return false;
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfBooleanSupplierMatchThenDoNotEvaluate() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> true, i ->true)
                .caseOf(() -> {
                    fail();
                    return false;
                }, i ->{
                    fail();
                    return false;
                }).result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanSupplierNoMatchThenMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> false, i ->{
                    fail();
                    return false;
                })
                .caseOf(() -> true, i ->true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanSupplierNullPointer1() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseOf((BooleanSupplier) null, i ->{
                    throw new AssertionError();
                }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanSupplierNullPointer2() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseOf(() -> true, null)
                .result();
        fail();
    }


    @Test
    public void testCaseOfBool() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(true, i ->true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanNoMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(false, i ->{
                    throw new AssertionError();
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfBooleanMatchThenDoNotEvaluate() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(true, i ->true)
                .caseOf(true, i ->{
                    throw new AssertionError();
                })
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsBooleanNoMatchThenMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(false, () -> {
                    throw new AssertionError();
                })
                .caseIs(true, () -> true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsBooleanNullPointer() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseIs(true, null)
                .result();
        fail();
    }

    @Test
    public void testCaseIsBool() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(true, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsBooleanNoMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(false, () -> {
                    throw new AssertionError();
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseIsBooleanMatchThenDoNotEvaluate() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(true, () -> true)
                .caseIs(true, () -> {
                    throw new AssertionError();
                })
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanNoMatchThenMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(false, i ->{
                    throw new AssertionError();
                })
                .caseOf(true, i ->true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanNullPointer() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseOf(true, null)
                .result();
        fail();
    }


    @Test
    public void testCaseIsInputEqualsMatchPredicateBool() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(i -> i == o, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsPredicateBool() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(i ->true, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsPredicateBooleanNoMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(i ->false, () -> {
                    throw new AssertionError();
                })
                .result();
        isEmpty(result);
    }

    @Test
    public void testCaseIsPredicateBooleanMatchThenDoNotEvaluate() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(i ->true, () -> true)
                .caseIs(i ->true, () -> {
                    throw new AssertionError();
                })
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsPredicateBooleanNoMatchThenMatch() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(i ->false, () -> {
                    throw new AssertionError();
                })
                .caseIs(i ->true, () -> true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsPredicateBooleanNullPointer() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseIs(i ->true, null)
                .result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsPredicateBooleanNullPointer2() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseIs(null, () -> {
                    throw new AssertionError();
                })
                .result();
        fail();
    }


    @Test
    public void testCaseOfInputEqualsMatchFunction() {
        int o = 42;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseObj(Optional::ofNullable, i -> i == o)
                .result();

        isTrue(result);
    }

    @Test
    public void testCaseOfFunction() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(i ->true, i ->true)
                .result();
        isTrue(res);
    }

    @Test
    public void testCaseOfFunctionNoMatch() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseObj(i ->Optional.empty(), i ->{
                    throw new AssertionError();
                })
                .result();

        isEmpty(res);
    }

    @Test
    public void testCaseOfFunctionMatchThenDoNotEvaluate() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseObj(Optional::ofNullable, i ->true)
                .caseObj(i ->{
                    throw new AssertionError();
                }, i ->{
                    throw new AssertionError();
                })
                .result();

        isTrue(res);
    }

    @Test
    public void testCaseOfFunctionCheckParameter() {
        int o = 42;
        long output = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseObj(i ->Optional.of(output), in -> in == output)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer1() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseObj(null, i ->{
                    throw new AssertionError();
                })
                .result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer2() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseObj(i ->Optional.empty(), null)
                .result();
        fail();
    }

    private static final OptionalInt one = OptionalInt.of(1);

    @Test
    public void testCaseIntInputEqualsMatchFunction() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseInt(i ->{
                    assertTrue(i == o);
                    return one;
                }, i -> {
                    assertEquals(1, i);
                    return true;
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseIntFunctionNoMatch() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseInt(i ->OptionalInt.empty(), i ->{
                    throw new AssertionError();
                });
    }

    @Test
    public void testCaseIntFunctionMatchThenDoNotEvaluate() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseInt(i ->one, i ->true)
                .caseInt(i ->{
                    fail();
                    return OptionalInt.empty();
                }, i ->{
                    throw new AssertionError();
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseIntFunctionCheckParameter() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseInt(i ->OptionalInt.of(3), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer1() {
        int o = 42;
        resultMatch(Boolean.class, o).caseInt(null, i ->{
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer2() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseInt(i ->OptionalInt.empty(), null)
                .result();
        fail();
    }

    private static final OptionalLong oneL = OptionalLong.of(1);

    @Test
    public void testCaseLongInputEqualsMatchFunction() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(i ->{
                    assertTrue(i == o);
                    return oneL;
                }, i -> {
                    assertEquals(1, i);
                    return true;
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseLongFunctionNoMatch() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(i ->OptionalLong.empty(), i ->{
                    throw new AssertionError();
                }).result();
        isEmpty(res);
    }

    @Test
    public void testCaseLongFunctionMatchThenDoNotEvaluate() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(i ->oneL, i ->true)
                .caseLong(i ->{
                    fail();
                    return OptionalLong.empty();
                }, i ->{
                    throw new AssertionError();
                })
                .result();

        isTrue(res);
    }

    @Test
    public void testCaseLongFunctionCheckParameter() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(i ->OptionalLong.of(3), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer1() {
        int o = 42;
        resultMatch(Boolean.class, o).caseLong(null, i ->{
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer2() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseLong(i ->OptionalLong.empty(), null)
                .result();
        fail();
    }


    private static final OptionalDouble oneD = OptionalDouble.of(0.0);

    @Test
    public void testCaseDoubleInputEqualsMatchFunction() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(i ->{
                    assertTrue(i == o);
                    return oneD;
                }, i -> {
                    assertEquals(0.0, i, 0.0);
                    return true;
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseDoubleFunctionNoMatch() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(i ->OptionalDouble.empty(), i ->{
                    throw new AssertionError();
                }).result();
        isEmpty(res);
    }

    @Test
    public void testCaseDoubleFunctionMatchThenDoNotEvaluate() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(i ->oneD, i ->true)
                .caseDouble(i ->{
                    fail();
                    return OptionalDouble.empty();
                }, i ->{
                    throw new AssertionError();
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseDoubleFunctionCheckParameter() {
        int o = 42;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(i ->OptionalDouble.of(3), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer1() {
        int o = 42;
        resultMatch(Boolean.class, o).caseDouble(null, i ->{
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer2() {
        int o = 42;
        resultMatch(Boolean.class, o)
                .caseDouble(i ->OptionalDouble.empty(), null)
                .result();
        fail();
    }

    @Test
    public void testOtherwise() {
        Boolean result = resultMatch(Boolean.class, 42)
                .otherwise(i ->true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testOtherwiseNull() {
        Boolean result = resultMatch(Boolean.class, 42)
                .otherwise(i ->null);

        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOtherwiseNullPointer() {
        Boolean result = resultMatch(Boolean.class, 42)
                .otherwise((IntFunction<Boolean>)null);

        assertNull(result);
    }

    @Test
    public void testOtherwiseNotReached() {
        Boolean res = resultMatch(Boolean.class, 42)
                .caseIs(true, () -> true)
                .otherwise(i ->{
                    throw new AssertionError();
                });
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOtherwiseNullNotReached() {
        // even if result is null, otherwise should not be evaluated
        Boolean res = resultMatch(Boolean.class, 42)
                .caseIs(true, () -> null)
                .otherwise(i ->{
                    throw new AssertionError();
                });
        assertNull(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOtherwiseThrow() {
        resultMatch(Boolean.class, 42)
                .otherwiseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testOtherwiseThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, 42)
                .caseIs(true, () -> true)
                .otherwiseThrow(NoSuchElementException::new);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOtherwiseNullThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, 42)
                .caseIs(true, () -> null)
                .otherwiseThrow(NoSuchElementException::new);
        assertNull(res);
    }

    @Test
    public void testIfResultWithoutResult() {
        resultMatch(Boolean.class, 42).ifResult(b -> fail());
    }

    @Test
    public void testIfResultWithResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, 42)
                .caseIs(true, () -> true)
                .ifResult(b -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testOtherwiseObject() {
        Boolean result = resultMatch(Boolean.class, 42)
                .otherwise(true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testOtherwiseObjectNull() {
        // null is allowed as otherwise value
        Boolean result = resultMatch(Boolean.class, 42)
                .otherwise((Boolean)null);

        assertNull(result);
    }

    @Test
    public void testOtherwiseObjectNotReached() {
        Boolean res = resultMatch(Boolean.class, 42)
                .caseIs(true, () -> true)
                .otherwise(false);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOtherwiseObjectNullNotReached() {
        Boolean res = resultMatch(Boolean.class, 42)
                .caseIs(true, () -> null)
                .otherwise(false);
        assertNull(res);
    }


    @Test
    public void testOrElse() {
        Boolean result = resultMatch(Boolean.class, 42)
                .orElse(() -> true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseNull() {
        resultMatch(Boolean.class, 42)
                .orElse(() -> null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseNullPointer() {
        resultMatch(Boolean.class, 42)
                .orElse((Supplier<Boolean>)null);
        fail();
    }

    @Test
    public void testOrElseNotReached() {
        Boolean res = resultMatch(Boolean.class, 42)
                .caseIs(true, () -> true)
                .orElse(() -> {
                    throw new AssertionError();
                });
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseReachedOnNullResult() {
        Boolean res = resultMatch(Boolean.class, 42)
                .caseIs(true, () -> null)
                .orElse(() -> true);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseObject() {
        Boolean result = resultMatch(Boolean.class, 42)
                .orElse(true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseObjectNull() {
        // null is allowed as otherwise value
        resultMatch(Boolean.class, 42)
                .orElse((Boolean) null);
    }

    @Test
    public void testOrElseObjectNotReached() {
        Boolean res = resultMatch(Boolean.class, 42)
                .caseIs(true, () -> true)
                .orElse(false);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseObjectReachedOnNull() {
        Boolean res = resultMatch(Boolean.class, 42)
                .caseIs(true, () -> null)
                .orElse(true);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseThrow() {
        resultMatch(Boolean.class, 42)
                .orElseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testOrElseThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, 42)
                .caseIs(true, () -> true)
                .orElseThrow(NoSuchElementException::new);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOtherwiseNullThrow() {
        resultMatch(Boolean.class, 42)
                .caseIs(true, () -> null)
                .orElseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testThenNoResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, 42)
                .then(r -> fail(), () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testThenNullResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, 42)
                .caseIs(true, () -> null)
                .then(r -> fail(), () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testThenOnResult() {
        int input = 42;
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> true)
                .then(success::set, Assert::fail);
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer() {
        int input = 42;
        resultMatch(Boolean.class, input)
                .then(null, Assert::fail);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer2() {
        int input = 42;
        resultMatch(Boolean.class, input)
                .then(r -> fail(), null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer3() {
        int input = 42;
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> false)
                .then(null, Assert::fail);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer4() {
        int input = 42;
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> false)
                .then(r -> fail(), null);
        fail();
    }
}
