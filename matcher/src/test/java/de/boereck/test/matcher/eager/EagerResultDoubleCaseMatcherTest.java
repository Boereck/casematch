package de.boereck.test.matcher.eager;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;

import static de.boereck.matcher.eager.EagerMatcher.resultMatch;
import static org.junit.Assert.*;


public class EagerResultDoubleCaseMatcherTest {

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
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(s -> s == o, s -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfPredicate() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(s -> true, s -> true)
                .result();
        isTrue(res);
    }

    @Test
    public void testCaseOfPredicateNoMatch() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(s -> false, s -> {
                    fail();
                    return false;
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfPredicateMatchThenDoNotEvaluate() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(s -> true, s -> true)
                .caseOf(s -> {
                    fail();
                    return false;
                }, s -> {
                    fail();
                    return false;
                }).result();
        isTrue(result);
    }

    @Test
    public void testCaseOfPredicateNoMatchThenMatch() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(s -> false, s -> {
                    fail();
                    return false;
                })
                .caseOf(s -> true, s -> true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfPredicateNullPointer1() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseOf((DoublePredicate) null, s -> {
                    fail();
                    return false;
                });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfPredicateNullPointer2() {
        double o = 0.0;
        resultMatch(Boolean.class, o).caseOf(s -> true, null);
        fail();
    }


    @Test
    public void testCaseOfBooleanSupplier() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> true, s -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanSupplierNoMatch() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> false, s -> {
                    fail();
                    return false;
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfBooleanSupplierMatchThenDoNotEvaluate() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> true, s -> true)
                .caseOf(() -> {
                    fail();
                    return false;
                }, s -> {
                    fail();
                    return false;
                }).result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanSupplierNoMatchThenMatch() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> false, s -> {
                    fail();
                    return false;
                })
                .caseOf(() -> true, s -> true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanSupplierNullPointer1() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseOf((BooleanSupplier) null, s -> {
                    throw new AssertionError();
                }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanSupplierNullPointer2() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseOf(() -> true, null)
                .result();
        fail();
    }


    @Test
    public void testCaseOfBool() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(true, s -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanNoMatch() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(false, s -> {
                    throw new AssertionError();
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfBooleanMatchThenDoNotEvaluate() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(true, s -> true)
                .caseOf(true, s -> {
                    throw new AssertionError();
                })
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsBooleanNoMatchThenMatch() {
        double o = 0.0;
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
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseIs(true, null)
                .result();
        fail();
    }

    @Test
    public void testCaseIsBool() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(true, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsBooleanNoMatch() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(false, () -> {
                    throw new AssertionError();
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseIsBooleanMatchThenDoNotEvaluate() {
        double o = 0.0;
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
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(false, s -> {
                    throw new AssertionError();
                })
                .caseOf(true, s -> true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanNullPointer() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseOf(true, null)
                .result();
        fail();
    }


    @Test
    public void testCaseIsInputEqualsMatchPredicateBool() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(s -> s == o, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsPredicateBool() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(s -> true, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsPredicateBooleanNoMatch() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(s -> false, () -> {
                    throw new AssertionError();
                })
                .result();
        isEmpty(result);
    }

    @Test
    public void testCaseIsPredicateBooleanMatchThenDoNotEvaluate() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(s -> true, () -> true)
                .caseIs(s -> true, () -> {
                    throw new AssertionError();
                })
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsPredicateBooleanNoMatchThenMatch() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(s -> false, () -> {
                    throw new AssertionError();
                })
                .caseIs(s -> true, () -> true)
                .result();
        isTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsPredicateBooleanNullPointer() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseIs(s -> true, null)
                .result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsPredicateBooleanNullPointer2() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseIs(null, () -> {
                    throw new AssertionError();
                })
                .result();
        fail();
    }


    @Test
    public void testCaseOfInputEqualsMatchFunction() {
        double o = 0.0;
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseObj(Optional::ofNullable, s -> s == o)
                .result();

        isTrue(result);
    }

    @Test
    public void testCaseOfFunction() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(s -> true, s -> true)
                .result();
        isTrue(res);
    }

    @Test
    public void testCaseOfFunctionNoMatch() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseObj(s -> Optional.empty(), s -> {
                    throw new AssertionError();
                })
                .result();

        isEmpty(res);
    }

    @Test
    public void testCaseOfFunctionMatchThenDoNotEvaluate() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseObj(Optional::ofNullable, s -> true)
                .caseObj(s -> {
                    throw new AssertionError();
                }, s -> {
                    throw new AssertionError();
                })
                .result();

        isTrue(res);
    }

    @Test
    public void testCaseOfFunctionCheckParameter() {
        double o = 0.0;
        double output = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseObj(s -> Optional.of(output), in -> in == output)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer1() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseObj(null, s -> {
                    throw new AssertionError();
                })
                .result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer2() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseObj(s -> Optional.empty(), null)
                .result();
        fail();
    }

    private static final OptionalInt one = OptionalInt.of(1);

    @Test
    public void testCaseIntInputEqualsMatchFunction() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseInt(s -> {
                    assertTrue(s == o);
                    return one;
                }, i -> {
                    assertEquals(1, i);
                    return true;
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseIntFunctionNoMatch() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseInt(s -> OptionalInt.empty(), s -> {
                    throw new AssertionError();
                });
    }

    @Test
    public void testCaseIntFunctionMatchThenDoNotEvaluate() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseInt(s -> one, s -> true)
                .caseInt(s -> {
                    fail();
                    return OptionalInt.empty();
                }, s -> {
                    throw new AssertionError();
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseIntFunctionCheckParameter() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseInt(s -> OptionalInt.of(3), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer1() {
        double o = 0.0;
        resultMatch(Boolean.class, o).caseInt(null, s -> {
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer2() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseInt(s -> OptionalInt.empty(), null)
                .result();
        fail();
    }

    private static final OptionalLong oneL = OptionalLong.of(1);

    @Test
    public void testCaseLongInputEqualsMatchFunction() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(s -> {
                    assertTrue(s == o);
                    return oneL;
                }, i -> {
                    assertEquals(1, i);
                    return true;
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseLongFunctionNoMatch() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(s -> OptionalLong.empty(), s -> {
                    throw new AssertionError();
                }).result();
        isEmpty(res);
    }

    @Test
    public void testCaseLongFunctionMatchThenDoNotEvaluate() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(s -> oneL, s -> true)
                .caseLong(s -> {
                    fail();
                    return OptionalLong.empty();
                }, s -> {
                    throw new AssertionError();
                })
                .result();

        isTrue(res);
    }

    @Test
    public void testCaseLongFunctionCheckParameter() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(s -> OptionalLong.of(3), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer1() {
        double o = 0.0;
        resultMatch(Boolean.class, o).caseLong(null, s -> {
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer2() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseLong(s -> OptionalLong.empty(), null)
                .result();
        fail();
    }


    private static final OptionalDouble oneD = OptionalDouble.of(0.0);

    @Test
    public void testCaseDoubleInputEqualsMatchFunction() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(s -> {
                    assertTrue(s == o);
                    return oneD;
                }, i -> {
                    assertEquals(0.0, i, 0.0);
                    return true;
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseDoubleFunctionNoMatch() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(s -> OptionalDouble.empty(), s -> {
                    throw new AssertionError();
                }).result();
        isEmpty(res);
    }

    @Test
    public void testCaseDoubleFunctionMatchThenDoNotEvaluate() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(s -> oneD, s -> true)
                .caseDouble(s -> {
                    fail();
                    return OptionalDouble.empty();
                }, s -> {
                    throw new AssertionError();
                }).result();

        isTrue(res);
    }

    @Test
    public void testCaseDoubleFunctionCheckParameter() {
        double o = 0.0;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(s -> OptionalDouble.of(3), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer1() {
        double o = 0.0;
        resultMatch(Boolean.class, o).caseDouble(null, s -> {
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer2() {
        double o = 0.0;
        resultMatch(Boolean.class, o)
                .caseDouble(s -> OptionalDouble.empty(), null)
                .result();
        fail();
    }

    @Test
    public void testOtherwise() {
        Boolean result = resultMatch(Boolean.class, 0.0)
                .otherwise(s -> true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testOtherwiseNull() {
        Boolean result = resultMatch(Boolean.class, 0.0)
                .otherwise(s -> null);

        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOtherwiseNullPointer() {
        Boolean result = resultMatch(Boolean.class, 0.0)
                .otherwise((DoubleFunction<Boolean>)null);

        assertNull(result);
    }

    @Test
    public void testOtherwiseNotReached() {
        Boolean res = resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> true)
                .otherwise(s -> {
                    throw new AssertionError();
                });
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOtherwiseNullNotReached() {
        // even if result is null, otherwise should not be evaluated
        Boolean res = resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> null)
                .otherwise(s -> {
                    throw new AssertionError();
                });
        assertNull(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOtherwiseThrow() {
        resultMatch(Boolean.class, 0.0)
                .otherwiseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testOtherwiseThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> true)
                .otherwiseThrow(NoSuchElementException::new);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOtherwiseNullThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> null)
                .otherwiseThrow(NoSuchElementException::new);
        assertNull(res);
    }

    @Test
    public void testIfResultWithoutResult() {
        resultMatch(Boolean.class, 0.0).ifResult(b -> fail());
    }

    @Test
    public void testIfResultWithResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> true)
                .ifResult(b -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testOtherwiseObject() {
        Boolean result = resultMatch(Boolean.class, 0.0)
                .otherwise(true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testOtherwiseObjectNull() {
        // null is allowed as otherwise value
        Boolean result = resultMatch(Boolean.class, 0.0)
                .otherwise((Boolean)null);

        assertNull(result);
    }

    @Test
    public void testOtherwiseObjectNotReached() {
        Boolean res = resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> true)
                .otherwise(false);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOtherwiseObjectNullNotReached() {
        Boolean res = resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> null)
                .otherwise(false);
        assertNull(res);
    }


    @Test
    public void testOrElse() {
        Boolean result = resultMatch(Boolean.class, 0.0)
                .orElse(() -> true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseNull() {
        resultMatch(Boolean.class, 0.0)
                .orElse(() -> null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseNullPointer() {
        resultMatch(Boolean.class, 0.0)
                .orElse((Supplier<Boolean>)null);
        fail();
    }

    @Test
    public void testOrElseNotReached() {
        Boolean res = resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> true)
                .orElse(() -> {
                    throw new AssertionError();
                });
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseReachedOnNullResult() {
        Boolean res = resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> null)
                .orElse(() -> true);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseObject() {
        Boolean result = resultMatch(Boolean.class, 0.0)
                .orElse(true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseObjectNull() {
        // null is allowed as otherwise value
        resultMatch(Boolean.class, 0.0)
                .orElse((Boolean) null);
    }

    @Test
    public void testOrElseObjectNotReached() {
        Boolean res = resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> true)
                .orElse(false);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseObjectReachedOnNull() {
        Boolean res = resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> null)
                .orElse(true);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseThrow() {
        resultMatch(Boolean.class, 0.0)
                .orElseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testOrElseThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> true)
                .orElseThrow(NoSuchElementException::new);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOtherwiseNullThrow() {
        resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> null)
                .orElseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testThenNoResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, 0.0)
                .then(r -> fail(), () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testThenNullResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, 0.0)
                .caseIs(true, () -> null)
                .then(r -> fail(), () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testThenOnResult() {
        double input = 0.0;
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> true)
                .then(success::set, Assert::fail);
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer() {
        double input = 0.0;
        resultMatch(Boolean.class, input)
                .then(null, Assert::fail);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer2() {
        double input = 0.0;
        resultMatch(Boolean.class, input)
                .then(r -> fail(), null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer3() {
        double input = 0.0;
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> false)
                .then(null, Assert::fail);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer4() {
        double input = 0.0;
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> false)
                .then(r -> fail(), null);
        fail();
    }

    // TODO test
    // Finished#caseIs(boolean, Supplier<? extends O>)
}
