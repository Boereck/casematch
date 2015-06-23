package de.boereck.test.matcher.eager;

import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.MatchHelpers.cast;
import static org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;

import javax.print.attribute.standard.NumberOfDocuments;


public class EagerResultCaseMatcherTest {

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
    public void testCaseOfClass() {
        Object o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(String.class, s -> true)
                .result();
        isTrue(res);
    }

    @Test
    public void testCaseOfClassFirstNoMatch() {
        Object o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(Class.class, s -> false)
                .caseOf(String.class, s -> true)
                .result();

        isTrue(res);
    }

    @Test
    public void testCaseOfClassSecondAlsoMatch() {
        Object o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(Object.class, s -> true)
                .caseOf(String.class, s -> false)
                .result();

        isTrue(res);
    }

    @Test
    public void testCaseOfClassSecondNoMatch() {
        Object o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(Object.class, s -> true)
                .caseOf(Class.class, s -> false)
                .result();

        isTrue(res);
    }

    @Test
    public void testCaseOfClassNoMatch() {
        Object o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(Boolean.class, s -> false)
                .caseOf(Class.class, s -> false)
                .result();

        isEmpty(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfClassNullPointer1() {
        Object o = "Boo";
        resultMatch(o).caseOf((Class<String>) null, s -> {
            fail();
            return null;
        });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfClassNullPointer2() {
        Object o = "Boo";
        resultMatch(o).caseOf(String.class, null);
        fail();
    }


    @Test
    public void testCaseOfClassNotMatchingWithPredicate() {
        Object o = "Boo";
        Optional<Object> result = resultMatch(o).caseOf(Integer.class, s -> {
            fail();
            return false;
        }, s -> {
            fail();
            return null;
        }).result();

        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testCaseOfClassWithPredicateMatching() {
        Object o = "Boo";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(String.class, s -> true, s -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfClassWithPredicateNotMatching() {
        Object o = "Boo";
        Optional<Object> result = resultMatch(o)
                .caseOf(String.class, s -> false, s -> {
                    fail();
                    return null;
                }).result();
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testCaseOfClassWithPredicateNotMatchingThenMatching() {
        Object o = "Boo";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(String.class, s -> false, s -> {
                    fail();
                    return false;
                })
                .caseOf(String.class, s -> true, s -> true)
                .result();

        isTrue(result);
    }

    @Test
    public void testCaseOfClassWithPredicateMatchingThenNotMatching() {
        Object o = "Boo";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(String.class, s -> true, s -> true)
                .caseOf(String.class, s -> false, s -> {
                    fail();
                    return false;
                })
                .result();

        isTrue(result);
    }

    @Test
    public void testCaseOfClassNotMatchingWithPredicateThenMatching() {
        Object o = "Boo";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(Integer.class, s -> true, s -> {
                    fail();
                    return false;
                })
                .caseOf(String.class, s -> true, s -> true)
                .result();

        isTrue(result);
    }

    @Test
    public void testCaseOfClassWithPredicateTwoMatchingOnlyFirst() {
        Object o = "Boo";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(String.class, s -> true, s -> true)
                .caseOf(String.class, s -> true, s -> {
                    fail();
                    return false;
                })
                .result();

        isTrue(result);
    }


    @Test
    public void testCaseOfGetsSameObjectAsInput() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(s -> s == o, s -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfPredicate() {
        String o = "";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(s -> true, s -> true)
                .result();
        isTrue(res);
    }

    @Test
    public void testCaseOfPredicateNoMatch() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(s -> false, s -> {
                    fail();
                    return false;
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfPredicateMatchThenDoNotEvaluate() {
        String o = "";
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
        String o = "";
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
        String o = "";
        resultMatch(Boolean.class, o)
                .caseOf((Predicate<String>) null, s -> {
                    fail();
                    return false;
                });
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfPredicateNullPointer2() {
        String o = "";
        resultMatch(Boolean.class, o).caseOf(s -> true, null);
        fail();
    }


    @Test
    public void testCaseOfBooleanSupplier() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> true, s -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanSupplierNoMatch() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(() -> false, s -> {
                    fail();
                    return false;
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfBooleanSupplierMatchThenDoNotEvaluate() {
        String o = "";
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
        String o = "";
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
        String o = "";
        resultMatch(Boolean.class, o)
                .caseOf((BooleanSupplier) null, s -> {
                    throw new AssertionError();
                }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfBooleanSupplierNullPointer2() {
        String o = "";
        resultMatch(Boolean.class, o)
                .caseOf(() -> true, null)
                .result();
        fail();
    }


    @Test
    public void testCaseOfBool() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(true, s -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseOfBooleanNoMatch() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseOf(false, s -> {
                    throw new AssertionError();
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseOfBooleanMatchThenDoNotEvaluate() {
        String o = "";
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
        String o = "";
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
        String o = "";
        resultMatch(Boolean.class, o)
                .caseIs(true, null)
                .result();
        fail();
    }

    @Test
    public void testCaseIsBool() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(true, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsBooleanNoMatch() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(false, () -> {
                    throw new AssertionError();
                }).result();
        isEmpty(result);
    }

    @Test
    public void testCaseIsBooleanMatchThenDoNotEvaluate() {
        String o = "";
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
        String o = "";
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
        String o = "";
        resultMatch(Boolean.class, o)
                .caseOf(true, null)
                .result();
        fail();
    }


    @Test
    public void testCaseIsInputEqualsMatchPredicateBool() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(s -> s == o, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsPredicateBool() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(s -> true, () -> true)
                .result();
        isTrue(result);
    }

    @Test
    public void testCaseIsPredicateBooleanNoMatch() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseIs(s -> false, () -> {
                    throw new AssertionError();
                })
                .result();
        isEmpty(result);
    }

    @Test
    public void testCaseIsPredicateBooleanMatchThenDoNotEvaluate() {
        String o = "";
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
        String o = "";
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
        String o = "";
        resultMatch(Boolean.class, o)
                .caseIs(s -> true, null)
                .result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIsPredicateBooleanNullPointer2() {
        String o = "";
        resultMatch(Boolean.class, o)
                .caseIs(null, () -> {
                    throw new AssertionError();
                })
                .result();
        fail();
    }


    @Test
    public void testCaseOfInputEqualsMatchFunction() {
        String o = "";
        Optional<Boolean> result = resultMatch(Boolean.class, o)
                .caseObj(Optional::ofNullable, s -> s == o)
                .result();

        isTrue(result);
    }

    @Test
    public void testCaseOfFunction() {
        String o = "";
        Predicate<String> isEmpty = String::isEmpty;
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseOf(isEmpty, s -> true)
                .result();
        isTrue(res);
    }

    @Test
    public void testCaseOfFunctionNoMatch() {
        String o = "";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseObj(s -> Optional.empty(), s -> {
                    throw new AssertionError();
                })
                .result();

        isEmpty(res);
    }

    @Test
    public void testCaseOfFunctionMatchThenDoNotEvaluate() {
        String o = "";
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
        Object o = "Boo";
        String output = "foo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseObj(s -> Optional.of(output), in -> in == output)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer1() {
        String o = "";
        resultMatch(Boolean.class, o)
                .caseObj(null, s -> {
                    throw new AssertionError();
                })
                .result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseOfFunctionNullPointer2() {
        String o = "";
        resultMatch(Boolean.class, o)
                .caseObj(s -> Optional.empty(), null)
                .result();
        fail();
    }

    private static final OptionalInt one = OptionalInt.of(1);

    @Test
    public void testCaseIntInputEqualsMatchFunction() {
        String o = "";
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
        String o = "";
        resultMatch(Boolean.class, o)
                .caseInt(s -> OptionalInt.empty(), s -> {
                    throw new AssertionError();
                });
    }

    @Test
    public void testCaseIntFunctionMatchThenDoNotEvaluate() {
        String o = "";
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
        String o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseInt(s -> OptionalInt.of(s.length()), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer1() {
        String o = "";
        resultMatch(Boolean.class, o).caseInt(null, s -> {
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseIntFunctionNullPointer2() {
        String o = "";
        resultMatch(Boolean.class, o)
                .caseInt(s -> OptionalInt.empty(), null)
                .result();
        fail();
    }

    private static final OptionalLong oneL = OptionalLong.of(1);

    @Test
    public void testCaseLongInputEqualsMatchFunction() {
        String o = "";
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
        String o = "";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(s -> OptionalLong.empty(), s -> {
                    throw new AssertionError();
                }).result();
        isEmpty(res);
    }

    @Test
    public void testCaseLongFunctionMatchThenDoNotEvaluate() {
        String o = "";
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
        String o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseLong(s -> OptionalLong.of(s.length()), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer1() {
        String o = "";
        resultMatch(Boolean.class, o).caseLong(null, s -> {
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseLongFunctionNullPointer2() {
        String o = "";
        resultMatch(Boolean.class, o)
                .caseLong(s -> OptionalLong.empty(), null)
                .result();
        fail();
    }


    private static final OptionalDouble oneD = OptionalDouble.of(0.0);

    @Test
    public void testCaseDoubleInputEqualsMatchFunction() {
        String o = "";
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
        String o = "";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(s -> OptionalDouble.empty(), s -> {
                    throw new AssertionError();
                }).result();
        isEmpty(res);
    }

    @Test
    public void testCaseDoubleFunctionMatchThenDoNotEvaluate() {
        String o = "";
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
        String o = "Boo";
        Optional<Boolean> res = resultMatch(Boolean.class, o)
                .caseDouble(s -> OptionalDouble.of(s.length()), in -> in == 3)
                .result();

        isTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer1() {
        String o = "";
        resultMatch(Boolean.class, o).caseDouble(null, s -> {
            throw new AssertionError();
        }).result();
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testCaseDoubleFunctionNullPointer2() {
        String o = "";
        resultMatch(Boolean.class, o)
                .caseDouble(s -> OptionalDouble.empty(), null)
                .result();
        fail();
    }

    @Test
    public void testOtherwise() {
        Boolean result = resultMatch(Boolean.class, "foo")
                .otherwise(s -> true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testOtherwiseNull() {
        Boolean result = resultMatch(Boolean.class, "foo")
                .otherwise(s -> null);

        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOtherwiseNullPointer() {
        Boolean result = resultMatch(Boolean.class, "foo")
                .otherwise((Function<String,Boolean>)null);

        assertNull(result);
    }

    @Test
    public void testOtherwiseNotReached() {
        Boolean res = resultMatch(Boolean.class, "foo")
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
        Boolean res = resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> null)
                .otherwise(s -> {
                    throw new AssertionError();
                });
        assertNull(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOtherwiseThrow() {
        resultMatch(Boolean.class, "foo")
                .otherwiseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testOtherwiseThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> true)
                .otherwiseThrow(NoSuchElementException::new);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOtherwiseNullThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> null)
                .otherwiseThrow(NoSuchElementException::new);
        assertNull(res);
    }

    @Test
    public void testIfResultWithoutResult() {
        resultMatch(Boolean.class, "foo").ifResult(b -> fail());
    }

    @Test
    public void testIfResultWithResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> true)
                .ifResult(b -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testOtherwiseObject() {
        Boolean result = resultMatch(Boolean.class, "foo")
                .otherwise(true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testOtherwiseObjectNull() {
        // null is allowed as otherwise value
        Boolean result = resultMatch(Boolean.class, "foo")
                .otherwise((Boolean)null);

        assertNull(result);
    }

    @Test
    public void testOtherwiseObjectNotReached() {
        Boolean res = resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> true)
                .otherwise(false);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOtherwiseObjectNullNotReached() {
        Boolean res = resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> null)
                .otherwise(false);
        assertNull(res);
    }


    @Test
    public void testOrElse() {
        Boolean result = resultMatch(Boolean.class, "foo")
                .orElse(() -> true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseNull() {
        resultMatch(Boolean.class, "foo")
                .orElse(() -> null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseNullPointer() {
        resultMatch(Boolean.class, "foo")
                .orElse((Supplier<Boolean>)null);
        fail();
    }

    @Test
    public void testOrElseNotReached() {
        Boolean res = resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> true)
                .orElse(() -> {
                    throw new AssertionError();
                });
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseReachedOnNullResult() {
        Boolean res = resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> null)
                .orElse(() -> true);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseObject() {
        Boolean result = resultMatch(Boolean.class, "foo")
                .orElse(true);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testOrElseObjectNull() {
        // null is allowed as otherwise value
        resultMatch(Boolean.class, "foo")
                .orElse((Boolean) null);
    }

    @Test
    public void testOrElseObjectNotReached() {
        Boolean res = resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> true)
                .orElse(false);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test
    public void testOrElseObjectReachedOnNull() {
        Boolean res = resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> null)
                .orElse(true);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseThrow() {
        resultMatch(Boolean.class, "foo")
                .orElseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testOrElseThrowNotReached() {
        Boolean res = resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> true)
                .orElseThrow(NoSuchElementException::new);
        assertNotNull(res);
        assertTrue(res);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOtherwiseNullThrow() {
        resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> null)
                .orElseThrow(NoSuchElementException::new);
        fail();
    }

    @Test
    public void testThenNoResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, "foo")
                .then(r -> fail(), () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testThenNullResult() {
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, "foo")
                .caseIs(true, () -> null)
                .then(r -> fail(), () -> success.set(true));
        assertTrue(success.get());
    }

    @Test
    public void testThenOnResult() {
        String input = "foo";
        AtomicBoolean success = new AtomicBoolean(false);
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> true)
                .then(success::set, Assert::fail);
        assertTrue(success.get());
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer() {
        String input = "foo";
        resultMatch(Boolean.class, input)
                .then(null, Assert::fail);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer2() {
        String input = "foo";
        resultMatch(Boolean.class, input)
                .then(r -> fail(), null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer3() {
        String input = "foo";
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> false)
                .then(null, Assert::fail);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testThenNullPointer4() {
        String input = "foo";
        resultMatch(Boolean.class, input)
                .caseIs(true, () -> false)
                .then(r -> fail(), null);
        fail();
    }
}
