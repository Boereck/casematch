package de.boereck.test.matcher.helpers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;

import static de.boereck.matcher.helpers.StringMatchHelpers.*;
import static org.junit.Assert.*;

public class StringMatchHelpersTest {

    @Test
    public void testIsStringMatching() {
        assertTrue(isString.test("foo"));
    }

    @Test
    public void testIsStringNotMatchingOnSuperType() {
        assertFalse(isString.test(new Object()));
    }

    @Test
    public void testIsStringNotMatching() {
        assertFalse(isString.test(new ArrayList<>()));
    }

    @Test
    public void testIsStringNotMatchingOnNull() {
        assertFalse(isString.test(null));
    }

    ///

    @Test
    public void testIsEmpty() {
        assertTrue(strIsEmpty.test(""));
    }

    @Test
    public void testNullIsEmpty() {
        assertTrue(strIsEmpty.test(null));
    }

    @Test
    public void testWhitespaceStringNotIsEmpty() {
        assertFalse(strIsEmpty.test(" "));
    }

    @Test
    public void testStringNotIsEmpty() {
        assertFalse(strIsEmpty.test("foo"));
    }

    ///

    @Test
    public void testNotEmpty() {
        assertFalse(strNotEmpty.test(""));
    }

    @Test
    public void testNullNotEmpty() {
        assertFalse(strNotEmpty.test(null));
    }

    @Test
    public void testWhitespaceStringNotEmpty() {
        assertTrue(strNotEmpty.test(" "));
    }

    @Test
    public void testStringNotEmpty() {
        assertTrue(strNotEmpty.test("foo"));
    }

    ///

    @Test(expected = NullPointerException.class)
    public void testStartsWithNull() {
        startsWith(null);
    }

    @Test
    public void testNotStartsWithString() {
        assertFalse(startsWith("foo").test("fo"));
    }

    @Test
    public void testStartsWithStringEqualStrings() {
        assertTrue(startsWith("foo").test("foo"));
    }

    @Test
    public void testStartsWithString() {
        assertTrue(startsWith("foo").test("foobar"));
    }

    @Test
    public void testStartsWithStringNullTest() {
        assertFalse(startsWith("foo").test(null));
    }

    ///

    @Test(expected = NullPointerException.class)
    public void testStartsWithIndexNull() {
        startsWith(null, 0);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStartsWithIndexWrongIndex() {
        startsWith("", -1);
    }

    @Test
    public void testNotStartsWithIndexedString() {
        assertFalse(startsWith("foo", 0).test("fo"));
    }

    @Test
    public void testStartsWithIndexedStringEqualString() {
        assertTrue(startsWith("foo", 0).test("foo"));
    }

    @Test
    public void testStartsWithIndexedString() {
        assertTrue(startsWith("foo", 0).test("foobar"));
    }

    @Test
    public void testStartsWithIndexedStringFurther() {
        assertTrue(startsWith("bar", 3).test("foobar"));
    }

    @Test
    public void testStartsWithStringIndexedNullTest() {
        assertFalse(startsWith("foo").test(null));
    }

    ///

    @Test(expected = NullPointerException.class)
    public void testMatchesNullPointer() {
        matches(null);
    }

    @Test
    public void testMatchesWithNull() {
        assertFalse(matches("\\w+").test(null));
    }

    @Test(expected = PatternSyntaxException.class)
    public void testMatchesInvalid() {
        matches("[\\w+");
    }

    @Test
    public void testMatchesWithNotMatchingEmpty() {
        assertFalse(matches("\\w+").test(""));
    }

    @Test
    public void testMatchesWithNotMatchingOnNull() {
        assertFalse(matches("\\w+").test(null));
    }

    @Test
    public void testMatchesWithNotMatching() {
        assertFalse(matches("\\w+").test("()"));
    }

    @Test
    public void testMatchesWithMatching() {
        assertTrue(matches("\\w+").test("foo"));
    }

    ///

    @Test(expected = NullPointerException.class)
    public void testMatchingNullPointer() {
        matching(null);
    }

    @Test(expected = PatternSyntaxException.class)
    public void testMatchingPatternException() {
        matching("[\\w+");
    }

    @Test
    public void testMatchingWithNotMatchingEmpty() {
        String toTest = "";
        Optional<String> res = matching("\\w+").apply(toTest);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMatchingWithNotMatching() {
        Optional<String> res = matching("\\w+").apply("()");
        assertFalse(res.isPresent());
    }

    @Test
    public void testMatchingWithNotMatchingOnNull() {
        Optional<String> res = matching("\\w+").apply(null);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMatchingWithMatching() {
        String expected = "foo";
        Optional<String> res = matching("\\w+").apply(expected);
        assertTrue(res.isPresent());
        assertTrue(res.get() == expected);
    }

    ///


    @Test(expected = NullPointerException.class)
    public void testMatcherNullPointer() {
        matcher(null);
    }

    @Test(expected = PatternSyntaxException.class)
    public void testMatcherPatternException() {
        matcher("[\\w+");
    }

    @Test
    public void testMatcherWithNotMatchingEmpty() {
        String toTest = "";
        Optional<Matcher> res = matcher("\\w+").apply(toTest);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMatcherWithNotMatching() {
        Optional<Matcher> res = matcher("\\w+").apply("()");
        assertFalse(res.isPresent());
    }

    @Test
    public void testMatcherWithNotMatchingOnNull() {
        Optional<Matcher> res = matcher("\\w+").apply(null);
        assertFalse(res.isPresent());
    }

    @Test
    public void testMatcherWithMatching() {
        String expected = "foo";
        Optional<Matcher> res = matcher("(\\w+)").apply(expected);
        assertTrue(res.isPresent());
        Matcher matcher = res.get();
        assertTrue(matcher.matches());
        assertEquals(matcher.groupCount(), 1);
        String group = matcher.group(1);
        assertEquals(group, expected);
    }

    ///

    @Test(expected = NullPointerException.class)
    public void testSplitNullPointer() {
        split(null);
    }

    @Test(expected = PatternSyntaxException.class)
    public void testSplitIllegalPattern() {
        split("[\\w+");
    }

    @Test
    public void testSplitOnNull() {
        Optional<String[]> res = split(",").apply(null);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSplitLongStrOnNull() {
        Optional<String[]> res = split(":::").apply(null);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSplit() {
        Optional<String[]> res = split(",").apply("foo,bar");
        assertTrue(res.isPresent());
        String[] arr = res.get();
        assertNotNull(arr);
        assertEquals(arr.length, 2);
        assertEquals(arr[0], "foo");
        assertEquals(arr[1], "bar");
    }

    @Test
    public void testSplitLongSeparator() {
        Optional<String[]> res = split("rrrr").apply("foorrrrbar");
        assertTrue(res.isPresent());
        String[] arr = res.get();
        assertNotNull(arr);
        assertEquals(arr.length, 2);
        assertEquals(arr[0], "foo");
        assertEquals(arr[1], "bar");
    }

    @Test
    public void testSplitNoMatch() {
        String expected = "foobar";
        Optional<String[]> res = split(",").apply(expected);
        assertTrue(res.isPresent());
        String[] arr = res.get();
        assertNotNull(arr);
        assertEquals(arr.length, 1);
        assertEquals(arr[0], expected);
    }

    @Test
    public void testSplitLongStringNoMatch() {
        String expected = "foobar";
        Optional<String[]> res = split("rrrr").apply(expected);
        assertTrue(res.isPresent());
        String[] arr = res.get();
        assertNotNull(arr);
        assertEquals(arr.length, 1);
        assertEquals(arr[0], expected);
    }

    ///

    @Test
    public void testEqIgnoreCaseNull() {
        assertTrue(eqIgnoreCase(null).test(null));
    }

    @Test
    public void testEqIgnoreCaseNullNotNull() {
        assertFalse(eqIgnoreCase(null).test("bar"));
    }

    @Test
    public void testEqIgnoreCaseEmptyString() {
        assertTrue(eqIgnoreCase("").test(""));
    }

    @Test
    public void testEqIgnoreCase() {
        assertTrue(eqIgnoreCase("foobar").test("FoObAr"));
    }

    @Test
    public void testEqIgnoreCaseNotEqual() {
        assertFalse(eqIgnoreCase("foo").test("bar"));
    }

    ///

    @Test
    public void testMinLengthEmpty() {
        assertTrue(minLength(0).test(""));
    }

    @Test
    public void testMinLengthOneTooShort() {
        assertFalse(minLength(4).test("foo"));
    }

    @Test
    public void testMinLengthLongEnough() {
        assertTrue(minLength(4).test("fooo"));
    }

    @Test
    public void testMinLengthNull() {
        assertFalse(minLength(4).test(null));
    }

    @Test
    public void testMinLengthNullNotEmptyString() {
        assertFalse(minLength(0).test(null));
    }

    ///

    @Test(expected = NullPointerException.class)
    public void testMinLengthSupplierNullPointer() {
        minLength(null);
    }

    @Test
    public void testMinLengthSupplierEmpty() {
        assertTrue(minLength(() -> 0).test(""));
    }

    @Test
    public void testMinLengthSupplierOneTooShort() {
        assertFalse(minLength(() -> 4).test("foo"));
    }

    @Test
    public void testMinLengthSupplierLongEnough() {
        assertTrue(minLength(() -> 4).test("fooo"));
    }

    @Test
    public void testMinLengthSupplierNull() {
        assertFalse(minLength(()->4).test(null));
    }

    @Test
    public void testMinLengthSupplierNullNotEmptyString() {
        assertFalse(minLength(() -> 0).test(null));
    }

    ///

    @Test
    public void testMaxLengthEmpty() {
        assertTrue(maxLength(0).test(""));
    }

    @Test
    public void testMinLengthOneTooLong() {
        assertFalse(maxLength(4).test("foooo"));
    }

    @Test
    public void testMaxLengthExactMax() {
        assertTrue(maxLength(4).test("fooo"));
    }

    @Test
    public void testMaxLengthNull() {
        assertFalse(maxLength(4).test(null));
    }

    @Test
    public void testMaxLengthNullNotEmptyString() {
        assertFalse(maxLength(0).test(null));
    }

    ///

    @Test(expected = NullPointerException.class)
    public void testMaxLengthSupplierNullPointer() {
        maxLength(null);
    }

    @Test
    public void testMaxLengthSupplierEmpty() {
        assertTrue(maxLength(()->0).test(""));
    }

    @Test
    public void testMinLengthSupplierOneTooLong() {
        assertFalse(maxLength(()->4).test("foooo"));
    }

    @Test
    public void testMaxLengthSupplierExactMax() {
        assertTrue(maxLength(()->4).test("fooo"));
    }

    @Test
    public void testMaxLengthSupplierNull() {
        assertFalse(maxLength(()->4).test(null));
    }

    @Test
    public void testMaxLengthSupplierNullNotEmptyString() {
        assertFalse(maxLength(()->0).test(null));
    }

    ///

    @Test(expected = NullPointerException.class)
    public void testSplitLimitedNullPointer() {
        split(null,2);
    }

    @Test(expected = PatternSyntaxException.class)
    public void testSplitLimitedIllegalPattern() {
        split("[\\w+",2);
    }

    @Test
    public void testSplitLimitedOnNull() {
        Optional<String[]> res = split(",",2).apply(null);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSplitLimitedLongStrOnNull() {
        Optional<String[]> res = split(":::",2).apply(null);
        assertFalse(res.isPresent());
    }

    @Test
    public void testSplitLimited() {
        Optional<String[]> res = split(",", 2).apply("foo,bar,baz");
        assertTrue(res.isPresent());
        String[] arr = res.get();
        assertNotNull(arr);
        assertEquals(2, arr.length);
        assertEquals(arr[0], "foo");
        assertEquals(arr[1], "bar,baz");
    }

    @Test
    public void testSplitLimitedLongSeparator() {
        Optional<String[]> res = split("rrrr").apply("foorrrrbar");
        assertTrue(res.isPresent());
        String[] arr = res.get();
        assertNotNull(arr);
        assertEquals(arr.length, 2);
        assertEquals(arr[0], "foo");
        assertEquals(arr[1], "bar");
    }

    @Test
    public void testSplitLimitedNoMatch() {
        String expected = "foobar";
        Optional<String[]> res = split(",",3).apply(expected);
        assertTrue(res.isPresent());
        String[] arr = res.get();
        assertNotNull(arr);
        assertEquals(arr.length, 1);
        assertEquals(arr[0], expected);
    }

    @Test
    public void testSplitLimitedLongStringNoMatch() {
        String expected = "foobar";
        Optional<String[]> res = split("rrrr",3).apply(expected);
        assertTrue(res.isPresent());
        String[] arr = res.get();
        assertNotNull(arr);
        assertEquals(arr.length, 1);
        assertEquals(arr[0], expected);
    }
}

