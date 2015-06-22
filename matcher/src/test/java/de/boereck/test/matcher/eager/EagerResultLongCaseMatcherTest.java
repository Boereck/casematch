package de.boereck.test.matcher.eager;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;

import static de.boereck.matcher.eager.EagerMatcher.resultMatch;
import static org.junit.Assert.*;


public class EagerResultLongCaseMatcherTest {

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
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(l -> l == o, l -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfPredicate() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(l -> true, l -> true)
                .result();
        isTrue(res);
    }

    @Test
    public void testCaseOfPredicateNoMatch() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(l -> false, l -> {
                    fail();
                    return false;
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfPredicateMatchThenDoNotEvaluate() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(l -> true, l -> true)
                .caseOf(l -> {
                    fail();
                    return false;
                }, l -> {
                    fail();
                    return false;
                }).result();
        isTrue(result);
    }

    @Test
    public void testCaseOfPredicateNoMatchThenMatch() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(l -> false, l -> {
                    fail();
                    return false;
                })
                .caseOf(l -> true, l -> true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfPredicateNullPointer1() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseOf((LongPredicate) null, l -> {
                    fail();
                    return false;
                });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfPredicateNullPointer2() {
        long o = 42L;
        resultMatch(Boolean.class, o).caseOf(l -> true, null);
        fail();
    }

    @Test
    public void testCaseOfElement() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(o, l -> true)
                .result();
        isTrue(res);
    }

    @Test
    public void testCaseOfElementNoMatch() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(43L, l -> {
                    fail();
                    return false;
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfElementMatchThenDoNotEvaluate() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(o, l -> true)
                .caseOf(o, l -> {
                    fail();
                    return false;
                }).result();
        isTrue(result);
    }

    @Test
    public void testCaseOfElementNoMatchThenMatch() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(43L, l -> {
                    fail();
                    return false;
                })
                .caseOf(o, l -> true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfElementNullPointer2() {
        long o = 42L;
        resultMatch(Boolean.class, o).caseOf(o, null);
        fail();
    }


    @Test
    public void testCaseOfBooleanSupplier() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> true, l -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanSupplierNoMatch() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> false, l -> {
                    fail();
                    return false;
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfBooleanSupplierMatchThenDoNotEvaluate() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> true, l -> true)
                .caseOf(() -> {
                    fail();
                    return false;
                }, l -> {
                    fail();
                    return false;
                }).result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanSupplierNoMatchThenMatch() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> false, l -> {
                    fail();
                    return false;
                })
                .caseOf(() -> true, l -> true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanSupplierNullPointer1() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseOf((BooleanSupplier) null, l -> {
                    throw new AssertionError();
                }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanSupplierNullPointer2() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseOf(() -> true, null)
                .result();
        fail();
    }


    @Test
    public void testCaseOfBool() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(true, l -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanNoMatch() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(false, l -> {
                    throw new AssertionError();
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfBooleanMatchThenDoNotEvaluate() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(true, l -> true)
                .caseOf(true, l -> {
                    throw new AssertionError();
                })
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsBooleanNoMatchThenMatch() {
        long o = 42L;
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
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseIs(true, null)
                .result();
        fail();
    }

    @Test
    public void testCaseIsBool() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(true, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsBooleanNoMatch() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(false, () -> {
                    throw new AssertionError();
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseIsBooleanMatchThenDoNotEvaluate() {
        long o = 42L;
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
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(false, l -> {
                    throw new AssertionError();
                })
                .caseOf(true, l -> true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanNullPointer() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseOf(true, null)
                .result();
        fail();
    }


    @Test
    public void testCaseIsInputEqualsMatchPredicateBool() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(l -> l == o, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsPredicateBool() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(l -> true, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsPredicateBooleanNoMatch() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(l -> false, () -> {
                    throw new AssertionError();
                })
                .result();
        isEmpty(result);
    }

    @Test
    public void testCaseIsPredicateBooleanMatchThenDoNotEvaluate() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(l -> true, () -> true)
                .caseIs(l -> true, () -> {
                    throw new AssertionError();
                })
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsPredicateBooleanNoMatchThenMatch() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(l -> false, () -> {
                    throw new AssertionError();
                })
                .caseIs(l -> true, () -> true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsPredicateBooleanNullPointer() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseIs(l -> true, null)
                .result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsPredicateBooleanNullPointer2() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseIs(null, () -> {
                    throw new AssertionError();
                })
                .result();
        fail();
    }


    @Test
    public void testCaseOfInputEqualsMatchFunction() {
        long o = 42L;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseObj(Optional::ofNullable, l -> l == o)
                .result();

        isTrue(result);
    }

    @Test
    public void testCaseOfFunction() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(l -> true, l -> true)
                .result();
        isTrue(res);
    }

    @Test
    public void testCaseOfFunctionNoMatch() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseObj(l -> Optional.empty(), l -> {
                    throw new AssertionError();
                })
                .result();

        isEmpty(res);
    }

    @Test
    public void testCaseOfFunctionMatchThenDoNotEvaluate() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseObj(Optional::ofNullable, l -> true)
                .caseObj(l -> {
                    throw new AssertionError();
                }, l -> {
                    throw new AssertionError();
                })
                .result();

        isTrue(res);
    }

    @Test
    public void testCaseOfFunctionCheckParameter() {
        long o = 42L;
        long output = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseObj(l -> Optional.of(output), in -> in == output)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer1() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseObj(null, l -> {
                    throw new AssertionError();
                })
                .result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer2() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseObj(l -> Optional.empty(), null)
                .result();
        fail();
    }

    private static final OptionalInt one = OptionalInt.of(1);

    @Test
    public void testCaseIntInputEqualsMatchFunction() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseInt(l -> {
                    assertTrue(l == o);
                    return one;
                }, l -> {
                    assertEquals(1, l);
                    return true;
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseIntFunctionNoMatch() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseInt(l -> OptionalInt.empty(), l -> {
                    throw new AssertionError();
                });
    }

    @Test
    public void testCaseIntFunctionMatchThenDoNotEvaluate() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseInt(l -> one, l -> true)
                .caseInt(l -> {
                    fail();
                    return OptionalInt.empty();
                }, l -> {
                    throw new AssertionError();
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseIntFunctionCheckParameter() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseInt(l -> OptionalInt.of(3), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer1() {
        long o = 42L;
        resultMatch(Boolean.class, o).caseInt(null, l -> {
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer2() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseInt(l -> OptionalInt.empty(), null)
                .result();
        fail();
    }

    private static final OptionalLong oneL = OptionalLong.of(1);

    @Test
    public void testCaseLongInputEqualsMatchFunction() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(l -> {
                    assertTrue(l == o);
                    return oneL;
                }, l -> {
                    assertEquals(1, l);
                    return true;
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseLongFunctionNoMatch() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(l -> OptionalLong.empty(), l -> {
                    throw new AssertionError();
                }).result();
        isEmpty(res);
    }

    @Test
    public void testCaseLongFunctionMatchThenDoNotEvaluate() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(l -> oneL, l -> true)
                .caseLong(l -> {
                    fail();
                    return OptionalLong.empty();
                }, l -> {
                    throw new AssertionError();
                })
                .result();

        isTrue(res);
    }

    @Test
    public void testCaseLongFunctionCheckParameter() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(l -> OptionalLong.of(3), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer1() {
        long o = 42L;
        resultMatch(Boolean.class, o).caseLong(null, l -> {
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer2() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseLong(l -> OptionalLong.empty(), null)
                .result();
        fail();
    }


    private static final OptionalDouble oneD = OptionalDouble.of(0.0);

    @Test
    public void testCaseDoubleInputEqualsMatchFunction() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(l -> {
                    assertTrue(l == o);
                    return oneD;
                }, d -> {
                    assertEquals(0.0, d, 0.0);
                    return true;
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseDoubleFunctionNoMatch() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(l -> OptionalDouble.empty(), l -> {
                    throw new AssertionError();
                }).result();
        isEmpty(res);
    }

    @Test
    public void testCaseDoubleFunctionMatchThenDoNotEvaluate() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(l -> oneD, l -> true)
                .caseDouble(l -> {
                    fail();
                    return OptionalDouble.empty();
                }, l -> {
                    throw new AssertionError();
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseDoubleFunctionCheckParameter() {
        long o = 42L;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(l -> OptionalDouble.of(3), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer1() {
        long o = 42L;
        resultMatch(Boolean.class, o).caseDouble(null, l -> {
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer2() {
        long o = 42L;
        resultMatch(Boolean.class, o)
                .caseDouble(l -> OptionalDouble.empty(), null)
                .result();
        fail();
    }

    @Test
    public void testOtherwise() {
        Boolean result = resultMatch(Boolean.class, 42L)
                .otherwise(l -> true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testOtherwiseNull() {
        Boolean result = resultMatch(Boolean.class, 42L)
                .otherwise(l -> null);

        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOtherwiseNullPointer() {
        Boolean result = resultMatch(Boolean.class, 42L)
                .otherwise((LongFunction<Boolean>)null);

        assertNull(result);
    }

    @Test
    public void testOtherwiseNotReached() {
        Boolean res = resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> true)
                .otherwise(l -> {
                    throw new AssertionError();
                });
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOtherwiseNullNotReached() {
        // even if result is null, otherwise should not be evaluated
        Boolean res = resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> null)
                .otherwise(l -> {
                    throw new AssertionError();
                });
        assertNull(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOtherwiseThrow() {
        resultMatch(Boolean.class, 42L)
                .otherwiseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testOtherwiseThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> true)
                .otherwiseThrow(NoSuchElementException::new);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOtherwiseNullThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> null)
                .otherwiseThrow(NoSuchElementException::new);
        assertNull(res);
    }

    @Test
    public void testIfResultWithoutResult() {
        resultMatch(Boolean.class, 42L).ifResult(b -> fail());
    }

    @Test
    public void testIfResultWithResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> true)
                .ifResult(b -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testOtherwiseObject() {
        Boolean result = resultMatch(Boolean.class, 42L)
                .otherwise(true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testOtherwiseObjectNull() {
        // null is allowed as otherwise value
        Boolean result = resultMatch(Boolean.class, 42L)
                .otherwise((Boolean)null);

        assertNull(result);
    }

    @Test
    public void testOtherwiseObjectNotReached() {
        Boolean res = resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> true)
                .otherwise(false);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOtherwiseObjectNullNotReached() {
        Boolean res = resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> null)
                .otherwise(false);
        assertNull(res);
    }


    @Test
    public void testOrElse() {
        Boolean result = resultMatch(Boolean.class, 42L)
                .orElse(() -> true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseNull() {
        resultMatch(Boolean.class, 42L)
                .orElse(() -> null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseNullPointer() {
        resultMatch(Boolean.class, 42L)
                .orElse((Supplier<Boolean>)null);
        fail();
    }

    @Test
    public void testOrElseNotReached() {
        Boolean res = resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> true)
                .orElse(() -> {
                    throw new AssertionError();
                });
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseReachedOnNullResult() {
        Boolean res = resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> null)
                .orElse(() -> true);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseObject() {
        Boolean result = resultMatch(Boolean.class, 42L)
                .orElse(true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseObjectNull() {
        // null is allowed as otherwise value
        resultMatch(Boolean.class, 42L)
                .orElse((Boolean) null);
    }

    @Test
    public void testOrElseObjectNotReached() {
        Boolean res = resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> true)
                .orElse(false);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseObjectReachedOnNull() {
        Boolean res = resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> null)
                .orElse(true);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseThrow() {
        resultMatch(Boolean.class, 42L)
                .orElseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testOrElseThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> true)
                .orElseThrow(NoSuchElementException::new);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOtherwiseNullThrow() {
        resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> null)
                .orElseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testThenNoResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, 42L)
                .then(r -> fail(), () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testThenNullResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, 42L)
                .caseIs(true, () -> null)
                .then(r -> fail(), () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testThenOnResult() {
        long input = 42L;
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> true)
                .then(success::set, Assert::fail);
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer() {
        long input = 42L;
        resultMatch(Boolean.class, input)
                .then(null, Assert::fail);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer2() {
        long input = 42L;
        resultMatch(Boolean.class, input)
                .then(r -> fail(), null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer3() {
        long input = 42L;
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> false)
                .then(null, Assert::fail);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer4() {
        long input = 42L;
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> false)
                .then(r -> fail(), null);
        fail();
    }

    // TODO test
    // Finished#caseIs(boolean, Supplier<? extends O>)
}
