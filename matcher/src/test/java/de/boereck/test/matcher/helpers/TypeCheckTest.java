package de.boereck.test.matcher.helpers;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;
import de.boereck.matcher.helpers.TypeCheck;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class TypeCheckTest {

    @Test(expected = NullPointerException.class)
    public void testAndTest() {
        TypeCheck<Object, String> tf = i -> false;
        AdvPredicate<Object> sut = tf.andTest(null);
    }

    @Test
    public void testAndTestFalseTrue() {
        TypeCheck<Object, String> tf = i -> false;
        AdvPredicate<Object> sut = tf.andTest(i -> true);
        assertFalse(sut.test(null));
    }

    @Test
    public void testAndTestTrueFalse() {
        TypeCheck<Object, String> tf = i -> true;
        AdvPredicate<Object> sut = tf.andTest(i -> false);
        assertFalse(sut.test(null));
    }

    @Test
    public void testAndTestTrueTrue() {
        TypeCheck<Object, String> tf = i -> true;
        AdvPredicate<Object> sut = tf.andTest(i -> true);
        assertTrue(sut.test(null));
    }

    @Test
    public void testAndTestFalseFalse() {
        TypeCheck<Object, String> tf = i -> false;
        AdvPredicate<Object> sut = tf.andTest(i -> false);
        assertFalse(sut.test(null));
    }

    @Test(expected = NullPointerException.class)
    public void testThenMapNull() {
        TypeCheck<Object, String> tf = i -> true;
        tf.thenMap(null);
    }

    @Test
    public void testThenMap() {
        TypeCheck<Object, String> tf = i -> true;
        String expected = "foo";
        OptionalMapper<Object, String> map = tf.thenMap(i -> expected);
        Optional<String> res = map.apply(null);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertNotNull(res);
        assertEquals(expected, res.get());
    }

    @Test
    public void testThenMapOnFalse() {
        TypeCheck<Object, String> tf = i -> false;
        String expected = "foo";
        OptionalMapper<Object, String> map = tf.thenMap(i -> expected);
        Optional<String> res = map.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testThenFlatMapNull() {
        TypeCheck<Object, String> tf = i -> true;
        tf.thenFlatMap(null);
    }

    @Test
    public void testThenFlatMap() {
        TypeCheck<Object, String> tf = i -> true;
        String expected = "foo";
        OptionalMapper<Object, String> map = tf.thenFlatMap(i -> Optional.of(expected));
        Optional<String> res = map.apply(null);
        assertNotNull(res);
        assertTrue(res.isPresent());
        assertNotNull(res);
        assertEquals(expected, res.get());
    }

    @Test
    public void testThenFlatMapOnFalse() {
        TypeCheck<Object, String> tf = i -> false;
        String expected = "foo";
        OptionalMapper<Object, String> map = tf.thenFlatMap(i -> Optional.of(expected));
        Optional<String> res = map.apply(null);
        assertNotNull(res);
        assertFalse(res.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testImpliesTestNull() {
        TypeCheck<Object, String> tf = i -> false;
        tf.impliesTest(null);
    }

    @Test
    public void testImpliesTestFalseTrue() {
        TypeCheck<Object, String> tf = i -> false;
        AdvPredicate<Object> func = tf.impliesTest(s -> true);
        boolean res = func.test(null);
        assertTrue(res);
    }

    @Test
    public void testImpliesTestTrueFalse() {
        TypeCheck<Object, String> tf = i -> true;
        AdvPredicate<Object> func = tf.impliesTest(s -> false);
        boolean res = func.test(null);
        assertFalse(res);
    }

    @Test
    public void testImpliesTestTrueTrue() {
        TypeCheck<Object, String> tf = i -> true;
        AdvPredicate<Object> func = tf.impliesTest(s -> true);
        boolean res = func.test(null);
        assertTrue(res);
    }

    @Test
    public void testImpliesTestFalseFalse() {
        TypeCheck<Object, String> tf = i -> false;
        AdvPredicate<Object> func = tf.impliesTest(s -> false);
        boolean res = func.test(null);
        assertTrue(res);
    }
}
