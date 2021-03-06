package de.boereck.matcher.helpers;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import de.boereck.matcher.function.optionalmap.OptionalDoubleMapper;
import de.boereck.matcher.function.optionalmap.OptionalIntMapper;
import de.boereck.matcher.function.optionalmap.OptionalLongMapper;
import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvDoublePredicate;
import de.boereck.matcher.function.predicate.AdvIntPredicate;
import de.boereck.matcher.function.predicate.AdvLongPredicate;
import de.boereck.matcher.function.predicate.AdvPredicate;
import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.function.testable.TestableToDoubleFunction;
import de.boereck.matcher.function.testable.TestableToIntFunction;
import de.boereck.matcher.function.testable.TestableToLongFunction;
import de.boereck.matcher.function.throwing.ThrowingFunction;

/**
 * This class provides static methods and fields that help with defining matches with CaseMatchers.
 * <p>
 * This class is not intended to be instantiated or sub-classed.
 * </p>
 *
 * @author Max Bureck
 */
public final class MatchHelpers {

    private MatchHelpers() {
        throw new IllegalStateException("Class CaseMatcher must not be instantiated");
    }

    /**
     * This is basically an alias for {@link java.util.Optional#ofNullable(Object)}, providing the additional features of
     * {@link OptionalMapper}.
     *
     * @param <I> type of input object to be wrapped into an optional
     * @return mapper that is reference to static method {@link java.util.Optional#ofNullable(Object)}
     */
    private static <I> OptionalMapper<I, I> toOptional() {
        return Optional::ofNullable;
    }

    /**
     * Exception in wrapped Predicate is mapped to "false", on regular execution, the result of the wrapped Predicate will be
     * forwarded. This method can be used to navigate object graphs without having to worry about null references. Be aware
     * that Exceptions do cost a lot of runtime!
     *
     * @param toTry predicate being executed, which may throw an exception
     * @param <T>   type of object to be checked
     * @return predicate that will return false if the wrapping predicate throws an exception during evaluation. If the
     * evaluation does not throw, the result of the wrapped predicate will be returned.
     * @throws NullPointerException will be thrown if {@code toTry} is {@code null}.
     */
    public static <T> AdvPredicate<T> _try(Predicate<T> toTry) throws NullPointerException {
        Objects.requireNonNull(toTry);
        return el -> {
            try {
                return toTry.test(el);
            } catch (Throwable t) {
                return false;
            }
        };
    }

    /**
     * Returns a mapper that will try to perform the given function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty Optional instead. The caller of
     * this function cannot distinguish between a null result returned by the wrapped {@code function} or
     * if an exception was caught. In both cases an empty optional is returned.
     *
     * @param function mapping function that is tried to executed.
     * @param <I>      Type of input of {@code function}
     * @param <O>      Type of output of {@code function}
     * @return function that will try to perform the given function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty Optional instead.
     * @throws NullPointerException if {@code function} is {@code null}
     */
    public static <I, O> OptionalMapper<I, O> tryMap(Function<I, O> function) throws NullPointerException {
        Objects.requireNonNull(function);
        return i -> {
            try {
                return Optional.ofNullable(function.apply(i));
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }

    /**
     * Returns a mapper that will try to perform the given to-int-function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty OptionalInt instead.
     *
     * @param function mapping function that is tried to executed.
     * @param <I>      Type of input of {@code function}
     * @return function that will try to perform the given to-int-function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty OptionalInt instead.
     * @throws NullPointerException if {@code function} is {@code null}
     */
    public static <I> OptionalIntMapper<I> tryMapI(ToIntFunction<I> function) throws NullPointerException {
        Objects.requireNonNull(function);
        return i -> {
            try {
                return OptionalInt.of(function.applyAsInt(i));
            } catch (Exception e) {
                return OptionalInt.empty();
            }
        };
    }

    /**
     * Returns a mapper that will try to perform the given to-double-function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty OptionalDouble instead.
     *
     * @param function mapping function that is tried to executed.
     * @param <I>      Type of input of {@code function}
     * @return function that will try to perform the given to-double-function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty OptionalDouble instead.
     * @throws NullPointerException if {@code function} is {@code null}
     */
    public static <I> OptionalDoubleMapper<I> tryMapD(ToDoubleFunction<I> function) throws NullPointerException {
        Objects.requireNonNull(function);
        return i -> {
            try {
                return OptionalDouble.of(function.applyAsDouble(i));
            } catch (Exception e) {
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * Returns a mapper that will try to perform the given to-long-function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty OptionalLong instead.
     *
     * @param function mapping function that is tried to executed.
     * @param <I>      Type of input of {@code function}
     * @return function that will try to perform the given to-long-function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty OptionalLong instead.
     * @throws NullPointerException if {@code function} is {@code null}
     */
    public static <I> OptionalLongMapper<I> tryMapL(ToLongFunction<I> function) throws NullPointerException {
        Objects.requireNonNull(function);
        return i -> {
            try {
                return OptionalLong.of(function.applyAsLong(i));
            } catch (Exception e) {
                return OptionalLong.empty();
            }
        };
    }

    /**
     * Returns a mapper that will try to perform the given function, if an exception is thrown during the
     * execution, the exception is passed to {@code handler} and empty Optional will be returned instead.
     * If the handler throws an exception it will be thrown to the caller of the returned mapper.
     *
     * @param function mapping function that is tried to executed. Must not be {@code null}.
     * @param handler  consumer that is handling the exception that was thrown. Must not be {@code null}.
     * @param <I>      Type of input of {@code function}
     * @param <O>      Type of output of {@code function}
     * @return function that will try to perform the given function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty Optional instead.
     * @throws NullPointerException if {@code function} or {@code handler} is {@code null}
     */
    public static <I, O> OptionalMapper<I, O> tryMap(Function<I, O> function, Consumer<Exception> handler) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return Optional.ofNullable(function.apply(i));
            } catch (Exception e) {
                handler.accept(e);
                return Optional.empty();
            }
        };
    }

    /**
     * Returns a mapper that will try to perform the given to-int-function, if an exception is thrown during the
     * execution, the exception is passed to {@code handler} and empty OptionalInt will be returned instead.
     * If the handler throws an exception it will be thrown to the caller of the returned mapper.
     *
     * @param function mapping function that is tried to executed.
     * @param handler  consumer that is handling the exception that was thrown.
     * @param <I>      Type of input of {@code function}
     * @return function that will try to perform the given to-int-function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty OptionalInt instead.
     * @throws NullPointerException if {@code function} or {@code handler} is {@code null}
     */
    public static <I> OptionalIntMapper<I> tryMapI(ToIntFunction<I> function, Consumer<Exception> handler) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return OptionalInt.of(function.applyAsInt(i));
            } catch (Exception e) {
                handler.accept(e);
                return OptionalInt.empty();
            }
        };
    }

    /**
     * Returns a mapper that will try to perform the given to-double-function, if an exception is thrown during the
     * execution, the exception is passed to {@code handler} and empty OptionalDouble will be returned instead.
     * If the handler throws an exception it will be thrown to the caller of the returned mapper.
     *
     * @param function mapping function that is tried to executed.
     * @param handler  consumer that is handling the exception that was thrown.
     * @param <I>      Type of input of {@code function}
     * @return function that will try to perform the given to-double-function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty OptionalDouble instead.
     * @throws NullPointerException if {@code function} or {@code handler} is {@code null}
     */
    public static <I> OptionalDoubleMapper<I> tryMapD(ToDoubleFunction<I> function, Consumer<Exception> handler) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return OptionalDouble.of(function.applyAsDouble(i));
            } catch (Exception e) {
                handler.accept(e);
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * Returns a mapper that will try to perform the given to-long-function, if an exception is thrown during the
     * execution, the exception is passed to {@code handler} and empty OptionalLong will be returned instead.
     * If the handler throws an exception it will be thrown to the caller of the returned mapper.
     *
     * @param function mapping function that is tried to executed.
     * @param handler  consumer that is handling the exception that was thrown.
     * @param <I>      Type of input of {@code function}
     * @return function that will try to perform the given to-long-function, if an exception is thrown during the
     * execution, the mapper will swallow the exception and return an empty OptionalLong instead.
     * @throws NullPointerException if {@code function} or {@code handler} is {@code null}
     */
    public static <I> OptionalLongMapper<I> tryMapL(ToLongFunction<I> function, Consumer<Exception> handler) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(handler);
        return i -> {
            try {
                return OptionalLong.of(function.applyAsLong(i));
            } catch (Exception e) {
                handler.accept(e);
                return OptionalLong.empty();
            }
        };
    }

    /**
     * Shortcut for Objects::notNull as {@link AdvPredicate}.
     */
    public static final AdvPredicate<Object> notNull = Objects::nonNull;

    /**
     * Check for referential equality to {@code null} as {@link AdvPredicate}.
     */
    public static final AdvPredicate<Object> isNull = o -> o == null;

    /**
     * Returns {@link java.util.Objects#nonNull(Object)} as predicate. This can be useful for using concatenation functions
     * {@link java.util.function.Predicate#and(java.util.function.Predicate)} or {@link java.util.function.Predicate#or(java.util.function.Predicate)}.
     *
     * @param <T> type of input object checked to be not {@code null}.
     * @return {@link java.util.Objects#nonNull(Object)} as Predicate
     */
    public static <T> AdvPredicate<T> notNull() {
        return Objects::nonNull;
    }

    /**
     * Calls {@link de.boereck.matcher.helpers.MatchHelpers#notNull() notNull()}, but takes a Class object as parameter that determines the generic type
     * T.
     *
     * @param clazz class that helps determine generic type T
     * @param <T>   type of input object checked to be not {@code null}
     * @return reference to {@link java.util.Objects#nonNull(Object)} as AdvPredicate.
     */
    public static <T> AdvPredicate<T> notNull(Class<T> clazz) {
        return notNull();
    }

    /**
     * The returned function will return an empty {@link java.util.Optional} if the input object is null. Otherwise the function
     * {@code f} will be called and the returned object will be wrapped in an Optional. So there will never be null passed to
     * function {@code f}.
     *
     * @param f   function that's output will be wrapped into an optional. If the input value is null, the function will not
     *            be called.
     * @param <I> type of input object to be mapped to type {@code O}
     * @param <O> type of output of the mapping
     * @return Function, either returning an Optional containing the output of function {@code f}, or an empty Optional if
     * the input is null.
     * @throws NullPointerException will be thrown if {@code f} is {@code null}.
     */
    public static <I, O> OptionalMapper<I, O> nullAware(Function<I, O> f) {
        Objects.requireNonNull(f);
        // equivalent with
        // (I i) -> i == null ? Optional.empty() : Optional.ofNullable(f.apply(i));
        return MatchHelpers.<I>toOptional().map(f);
    }

    /**
     * The returned function will return an empty {@link java.util.OptionalInt} if the input object is null. Otherwise the function
     * {@code f} will be called and the returned int value will be wrapped in an OptionalInt. So there will never be null
     * passed to function {@code f}.
     *
     * @param f   function that's output will be wrapped into an optional. If the input value is null, the function will not
     *            be called.
     * @param <I> type of input object mapped to int
     * @return Function, either returning an OptionalInt containing the output of function {@code f}, or an empty OptionalInt
     * if the input is null.
     * @throws NullPointerException will be thrown if {@code f} is {@code null}.
     */
    public static <I> OptionalIntMapper<I> nullAwareI(ToIntFunction<I> f) throws NullPointerException {
        Objects.requireNonNull(f);
        // equivalent with
        // (I i) -> i == null ? OptionalInt.empty() : OptionalInt.of(f.applyAsInt(i));
        return MatchHelpers.<I>toOptional().mapI(f);
    }

    /**
     * The returned function will return an empty {@link java.util.OptionalLong} if the input object is null. Otherwise the function
     * {@code f} will be called and the returned long value will be wrapped in an OptionalInt. So there will never be null
     * passed to function {@code f}.
     *
     * @param f   function that's output will be wrapped into an optional. If the input value is null, the function will not
     *            be called.
     * @param <I> type of input object mapped to long value
     * @return Function, either returning an OptionalLong containing the output of function {@code f}, or an empty
     * OptionalLong if the input is null.
     * @throws NullPointerException will be thrown if {@code f} is {@code null}.
     */
    public static <I> OptionalLongMapper<I> nullAwareL(ToLongFunction<I> f) throws NullPointerException {
        Objects.requireNonNull(f);
        // equivalent with
        // (I i) -> i == null ? OptionalLong.empty() : OptionalLong.of(f.applyAsLong(i));
        return MatchHelpers.<I>toOptional().mapL(f);
    }

    /**
     * The returned function will return an empty {@link java.util.OptionalDouble} if the input object is null. Otherwise the function
     * {@code f} will be called and the returned double value will be wrapped in an OptionalDouble. So there will never be
     * null passed to function {@code f}.
     *
     * @param f   function that's output will be wrapped into an optional. If the input value is null, the function will not
     *            be called.
     * @param <I> type of input object to be mapped to double
     * @return Function, either returning an OptionalDouble containing the output of function {@code f}, or an empty
     * OptionalDouble if the input is null.
     * @throws NullPointerException will be thrown if {@code f} is {@code null}.
     */
    public static <I> Function<I, OptionalDouble> nullAwareD(ToDoubleFunction<I> f) throws NullPointerException {
        Objects.requireNonNull(f);
        // equivalent with
        // (I i) -> i == null ? OptionalDouble.empty() : OptionalDouble.of(f.applyAsDouble(i));
        return MatchHelpers.<I>toOptional().mapD(f);
    }

    /**
     * Takes a Predicate and returns an AdvPredicate performing the same test. This can be used to make function references
     * (to static or member methods) explicitly available as predicates, which can be handy if a lambda expression would be
     * ambiguous (e.g. other functional interfaces can be expected in a certain context). It also allows easy composition of
     * functions using the methods on Predicate, like {@link java.util.function.Predicate#and(java.util.function.Predicate)} or {@link java.util.function.Predicate#or(java.util.function.Predicate)}.
     * Example: <pre> <code>
     *   test(String::isEmpty).not()
     * </code>
     * </pre>
     *
     * @param p   predicate that should be made available as an AdvPredicate
     * @param <T> type of object to be checked by predicate {@code p}
     * @return AdvPredicate representation of predicate {@code p}
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static <T> AdvPredicate<T> test(Predicate<T> p) {
        Objects.requireNonNull(p);
        return p::test;
    }

    /**
     * Takes an IntPredicate and returns an AdvIntPredicate performing the same test. This can be used to make function
     * references (to static or member methods) explicitly available as predicates, which can be handy if a lambda expression
     * would be ambiguous (e.g. other functional interfaces can be expected in a certain context). It also allows easy
     * composition of functions using the methods on IntPredicate, like {@link java.util.function.IntPredicate#and(java.util.function.IntPredicate)} or
     * {@link java.util.function.IntPredicate#or(java.util.function.IntPredicate)}. Example: <pre> <code>
     *   testI(i -&gt; i &lt; 0).implies(i -&gt; i % 2 == 0) // negative values must be even
     * </code>
     * </pre>
     *
     * @param p predicate that should be made available as an AdvIntPredicate
     * @return AdvIntPredicate representation of predicate {@code p}
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static AdvIntPredicate testI(IntPredicate p) {
        Objects.requireNonNull(p);
        return p::test;
    }

    /**
     * Takes an LongPredicate and returns an AdvLongPredicate performing the same test. This can be used to make function
     * references (to static or member methods) explicitly available as predicates, which can be handy if a lambda expression
     * would be ambiguous (e.g. other functional interfaces can be expected in a certain context). It also allows easy
     * composition of functions using the methods on LongPredicate, like {@link java.util.function.LongPredicate#and(java.util.function.LongPredicate)} or
     * {@link java.util.function.LongPredicate#or(java.util.function.LongPredicate)}. Example: <pre><code>
     *   testL(l -&gt; l &lt; 0).implies(l -&gt; l % 2 == 0) // negative values must be even
     * </code>
     * </pre>
     *
     * @param p predicate that should be made available as an AdvLongPredicate
     * @return AdvLongPredicate representation of predicate {@code p}
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static AdvLongPredicate testL(LongPredicate p) {
        Objects.requireNonNull(p);
        return p::test;
    }

    /**
     * Takes an DouoblePredicate and returns an AdvDoublePredicate performing the same test. This can be used to make
     * function references (to static or member methods) explicitly available as predicates, which can be handy if a lambda
     * expression would be ambiguous (e.g. other functional interfaces can be expected in a certain context). It also allows
     * easy composition of functions using the methods on DoublePredicate, like {@link java.util.function.DoublePredicate#and(java.util.function.DoublePredicate)}
     * or {@link java.util.function.DoublePredicate#or(java.util.function.DoublePredicate)}.
     *
     * @param p predicate that should be made available as an AdvDoublePredicate
     * @return AdvDoublePredicate representation of predicate {@code p}
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static AdvDoublePredicate testD(DoublePredicate p) {
        Objects.requireNonNull(p);
        return p::test;
    }

    /**
     * Simple shortcut for {@code p.negate()}. This can e.g. used to negate
     * a method reference. Exmaple: {@code not(String::isEmpty)}.
     *
     * @param p   predicate to be negated
     * @param <T> type of object to be checked by predicate
     * @return negated predicate
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static <T> AdvPredicate<T> not(Predicate<T> p) {
        Objects.requireNonNull(p);
        return p.negate()::test;
    }

    /**
     * Simple shortcut for {@code p.negate()}. This can e.g. used to negate
     * a method reference. Exmaple: <br>{@code notI(IntMatchHelpers.positive) // <= 0} <br>
     *
     * @param p predicate to be negated
     * @return negated predicate
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static AdvIntPredicate notI(IntPredicate p) {
        Objects.requireNonNull(p);
        return p.negate()::test;
    }

    /**
     * Simple shortcut for {@code p.negate()}. This can e.g. used to negate
     * a method reference. Exmaple: <br>{@code notL(LongMatchHelpers.inClosedRange(0,10)) // <= 0} <br>
     *
     * @param p predicate to be negated
     * @return negated predicate
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static AdvLongPredicate notL(LongPredicate p) {
        Objects.requireNonNull(p);
        return p.negate()::test;
    }

    /**
     * Simple shortcut for {@code p.negate()}. This can e.g. used to negate
     * a method reference. Exmaple: <br>{@code notD(DoubleMatchHelpers.finite)} <br>
     *
     * @param p predicate to be negated
     * @return negated predicate
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static AdvDoublePredicate notD(DoublePredicate p) {
        Objects.requireNonNull(p);
        return p.negate()::test;
    }

    /**
     * This function takes a {@link java.util.function.Function} and returns it as a {@link TestableFunction}. This can be used to make function
     * references (to static or member methods) explicitly available as functions, which can be handy if a lambda expression
     * would be ambiguous (e.g. other functional interfaces can be expected in a certain context). It also allows easy
     * composition of functions using the methods on TestableFunction, like {@link TestableFunction#andThen(java.util.function.Function)} or
     * {@link TestableFunction#filter(java.util.function.Predicate)}.
     *
     * @param f   function that should be made available as a TestableFunction.
     * @param <I> type of input to mapping function
     * @param <O> type of output of mapping function
     * @return TestableFunction representation of parameter {@code f}.
     * @throws NullPointerException will be thrown if {@code f} is {@code null}.
     */
    public static <I, O> TestableFunction<I, O> map(Function<I, O> f) {
        Objects.requireNonNull(f);
        return f::apply;
    }

    /**
     * This function takes an {@link java.util.function.ToIntFunction} and returns it as a {@link de.boereck.matcher.function.testable.TestableToIntFunction}. This can be used to make
     * function references (to static or member methods) explicitly available as functions, which can be handy if a lambda
     * expression would be ambiguous (e.g. other functional interfaces can be expected in a certain context). It also allows
     * easy composition of functions using the methods on TestableToIntFunction, like
     * {@link TestableToIntFunction#filter(java.util.function.IntPredicate)}.
     *
     * @param f   function that should be made available as a TestableFunction.
     * @param <I> type of input object to be casted to input object
     * @return TestableFunction representation of parameter {@code f}.
     * @throws NullPointerException will be thrown if {@code f} is {@code null}.
     */
    public static <I> TestableToIntFunction<I> mapI(ToIntFunction<I> f) {
        Objects.requireNonNull(f);
        return f::applyAsInt;
    }

    /**
     * This function takes an {@link java.util.function.ToLongFunction} and returns it as a {@link TestableToLongFunction}. This can be used to
     * make function references (to static or member methods) explicitly available as functions, which can be handy if a
     * lambda expression would be ambiguous (e.g. other functional interfaces can be expected in a certain context). It also
     * allows easy composition of functions using the methods on TestableToLongFunction, like
     * {@link TestableToIntFunction#filter(java.util.function.IntPredicate)}.
     *
     * @param f   function that should be made available as a TestableFunction.
     * @param <I> type of input object to be mapped to long by function {@code f}
     * @return TestableFunction representation of parameter {@code f}.
     * @throws NullPointerException will be thrown if {@code f} is {@code null}.
     */
    public static <I> TestableToLongFunction<I> mapL(ToLongFunction<I> f) {
        Objects.requireNonNull(f);
        return f::applyAsLong;
    }

    /**
     * This function takes an {@link java.util.function.ToDoubleFunction} and returns it as a {@link TestableToDoubleFunction}. This can be used
     * to make function references (to static or member methods) explicitly available as functions, which can be handy if a
     * lambda expression would be ambiguous (e.g. other functional interfaces can be expected in a certain context). It also
     * allows easy composition of functions using the methods on TestableToDoubleFunction, like
     * {@link TestableToDoubleFunction#filter(java.util.function.DoublePredicate)}.
     *
     * @param f   function that should be made available as a TestableFunction.
     * @param <I> type of input object to be mapped to double value by function {@code f}
     * @return TestableFunction representation of parameter {@code f}.
     * @throws NullPointerException will be thrown if {@code f} is {@code null}.
     */
    public static <I> TestableToDoubleFunction<I> mapD(ToDoubleFunction<I> f) {
        Objects.requireNonNull(f);
        return f::applyAsDouble;
    }

    /**
     * This filed references a method checking if an Object is not {@code null} and instance of
     * {@link java.lang.Integer}.
     */
    public static final OptionalIntMapper<Object> isInteger = MatchHelpers::isInteger;

    private static OptionalInt isInteger(Object o) {
        return (o != null && o instanceof Integer) ? OptionalInt.of((Integer) o) : OptionalInt.empty();
    }

    /**
     * This filed references a method checking if an Object is not {@code null} and instance of
     * {@link java.lang.Long}.
     */
    public static final OptionalLongMapper<Object> isLong = MatchHelpers::isLong;

    private static OptionalLong isLong(Object o) {
        return (o != null && o instanceof Long) ? OptionalLong.of((Long) o) : OptionalLong.empty();
    }

    /**
     * This filed references a method checking if an Object is not {@code null} and instance of
     * {@link java.lang.Double}.
     */
    public static final OptionalDoubleMapper<Object> isDouble = MatchHelpers::isDouble;

    private static OptionalDouble isDouble(Object o) {
        return (o != null && o instanceof Double) ? OptionalDouble.of((Double) o) : OptionalDouble.empty();
    }

    /**
     * Reference to {@link java.util.Objects#toString(Object)} represented as a {@link TestableFunction}.
     */
    public static final TestableFunction<Object, String> toString = Objects::toString;

    /**
     * Function tries to cast the given object to the specified class. If the object is instance of the given class the
     * method will return an Optional containing the casted object. If the object is not instance of the specified class, the
     * result will be an empty Optional.
     *
     * @param o     object to be casted
     * @param clazz class to cast parameter {@code o} to.
     * @param <T>   type the input object is casted to, if it is instance of this type
     * @return if {@code o} is instance of {@code clazz}, an Optional containing o casted to clazz. Otherwise an empty
     * Optional.
     * @throws NullPointerException will be thrown if {@code clazz} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    // class check before unsafe cast
    public static <T> Optional<T> cast(Object o, Class<T> clazz) throws NullPointerException {
        if (clazz.isInstance(o)) {
            return Optional.of((T) o);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Creates a function that checks if the given predicate {@code test} returns true for input objects passed to that
     * function. Depending on the outcome, the function will either return an empty {@link java.util.Optional} (if the predicate
     * returns false) or an Optional holding the input object (if the predicate returns true).
     *
     * @param test predicate to check if input objects should be wrapped in an optional. Must not be {@code null}.
     * @param <I>  type of element to be tested by {@code test}
     * @return function mapping from I to Optional&lt;I&gt;, based on predicate {@code test}.
     * @throws NullPointerException will be thrown if {@code test} is {@code null}.
     */
    public static <I> OptionalMapper<I, I> filter(Predicate<I> test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> test.test(i) ? Optional.ofNullable(i) : Optional.empty();
    }

    /**
     * Creates a function that will return the input wrapped in an optional if it is not {@code null} and predicate {@code test}
     * returns {@code true} for this input. Otherwise it will return an empty optional. If the given predicate throws an
     * exception the exception will be thrown to the caller of the returned filter function.
     *
     * @param test predicate checking if the input will be returned in an optional, or if an empty optional will be returned.
     *             Must not be {@code null}.
     * @param <I> type of input object to returned function
     * @return function filtering the input out if it is {@code null} or does not match predicate {@code test}.
     * @throws NullPointerException if {@code test} is {@code null}.
     */
    public static <I> OptionalMapper<I, I> filterNullAware(Predicate<I> test) throws NullPointerException {
        Objects.requireNonNull(test);
        return i -> Optional.ofNullable(i).filter(test);
    }

    /**
     * Creates a function that can receive a value and returns an Optional, either containing an object, casted to the given
     * class or being empty if the input to the function was null or the input object is not instance of the given class.
     *
     * @param clazz Class to cast to
     * @param <T>   type the input object to the returned function should be casted to.
     * @return function that does cast or returns empty Optional
     * @throws NullPointerException will be thrown if {@code clazz} is {@code null}.
     */
    public static <T> OptionalMapper<Object, T> cast(Class<T> clazz) throws NullPointerException {
        Objects.requireNonNull(clazz);
        return t -> cast(t, clazz);
    }

    /**
     * This predicate checks if an object is exactly of the given type. This is <em>not</em> an <code>instanceof</code>
     * check, since this would also return true if the object was of a subtype of the class in question.
     *
     * @param clazz Class the predicate checks input objects are type of.
     * @param <I>   type of input element checked to be of type {@code T}
     * @param <O>   type the input element is checked to be of
     * @return Predicate testing if an object is exactly of the given type.
     * @throws NullPointerException will be thrown if {@code clazz} is {@code null}.
     */
    public static <I, O> TypeCheck<I, O> typed(Class<O> clazz) throws NullPointerException {
        Objects.requireNonNull(clazz);
        return t -> t != null && t.getClass() == clazz;
    }

    /**
     * The returned predicate checks if an input object is instance of the given class. This predicate allows follow up
     * checks on the input object, that will automatically be cased to the checked type.
     *
     * @param clazz type input objects are checked to be instance of
     * @param <I>   type of input element to be checked to be instance of {@code T}
     * @param <O>   type the input is checked to be instance of.
     * @return predicate checking input objects if they are instance of class {@code clazz}.
     * @throws NullPointerException will be thrown if {@code clazz} is {@code null}.
     */
    public static <I, O> TypeCheck<I, O> instanceOf(Class<O> clazz) throws NullPointerException {
        Objects.requireNonNull(clazz);
        return t -> t != null && clazz.isInstance(t);
    }

    /**
     * Returns predicate checking if input objects are equal to the given object {@code t}. Equality check is performed
     * using {@link Objects#equals(Object, Object)}.
     *
     * @param t   object to check for equality
     * @param <T> Type of element to be checked for equality
     * @return predicate, checking input objects for equality to {@code t}.
     */
    public static <T> AdvPredicate<T> eq(T t) {
        return o -> Objects.equals(o, t);
    }

    /**
     * Returns predicate that returns true if the input object is one of
     * the given objects {@code t} or {@code more}. The function will use
     * <em>referential</em> equality to check if the input is one of the
     * give objects. Be aware that changes to the given elements {@code more}
     * will have no effect on the returned predicate.
     *
     * @param t    one element predicate will check if input is equal to it
     * @param more further elements the predicate will check if element
     *             is one of them.
     * @param <T>  type of elements to be checked
     * @return predicate checking if the input object is either {@code t} or one of {@code more}.
     * @throws NullPointerException will be thrown if {@code more} is {@code null}.
     */
    public static <T> AdvPredicate<T> oneOf(T t, T... more) throws NullPointerException {
        Objects.requireNonNull(more);
        // make defensive copy
        final T[] ts = Arrays.copyOf(more, more.length + 1);
        ts[more.length] = t;
        return o -> contains(ts, o);
    }

    private static <T> boolean contains(T[] ts, T input) {
        for (int i = 0; i < ts.length; i++) {
            if (ts[i] == input) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates predicate checking for <em>referential</em> equality. The predicate will use the == operator and does
     * <em>not</em> call any equals method.
     *
     * @param o   to be checked for referential equality.
     * @param <T> type of element checked for referential equality with parameter {@code o}.
     * @return predicate checking an input for referential equality with parameter o.
     */
    public static <T> AdvPredicate<T> refEq(Object o) {
        return t -> t == o;
    }

    /**
     * Simple alias for boolean true
     */
    public static final boolean any = true;

    /**
     * Predicate for objects always returning true
     */
    public static final Predicate<Object> __ = o -> true;

    /**
     * Predicate for integer values always returning true
     */
    public static final IntPredicate _I = i -> true;

    /**
     * Predicate for long values always returning true
     */
    public static final LongPredicate _L = l -> true;

    /**
     * Predicate for double values always returning true
     */
    public static final DoublePredicate _D = d -> true;

}
