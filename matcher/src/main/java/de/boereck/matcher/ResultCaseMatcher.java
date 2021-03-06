package de.boereck.matcher;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p>
 * This interface is used to define cases that can be used to found a given object and define associated functions that are
 * executed when a case matches for the given object and return a result value. The result value will be returned by closing
 * methods. See below which methods of this class are closing methods.<br>
 * The object to be checked has to be provided during creation of the instance. Only the action of one of the given cases
 * that matches will be executed.
 * </p>
 * <p>
 * The case de.boereck.matcher interface does <em>not</em> give a guarantee if the cases are checked in the order they are specified. It
 * is also not guaranteed that remaining cases are evaluated when a matching case was found. Implementations may even execute
 * the found checks and actions asynchronously. This has to be clarified by the factory method providing the instance or by
 * sub-types providing a stricter API.
 * </p>
 * <p>
 * It is also not defined if the evaluation of case predicates or functions is done eager when a case method is called or
 * lazy when a closing method is called (starting the evaluation of cases). This interface does not declare any closing methods,
 * sub-types may define their own closing methods.
 * </p>
 *
 * @param <I> type of the input object to be matched
 * @param <O> type of the result object returned from the case found
 * @author Max Bureck
 */
public interface ResultCaseMatcher<I, O> {

    /**
     * Defines a case that checks if the input object is instance of the given class. If the case is determined to be the
     * matching case, the provided function {@code f} will be called with the input object casted to the type the case is
     * checking for. The result of the function will be the result of the case found.
     *
     * @param clazz Type the input object is checked to be instance of
     * @param f     function that will be executed (with input object as parameter) when the case is determined to be the
     *              matching one. The result of this function will be the result of the case found.
     * @return instance of ResultCaseMatcher (maybe same as same object as this) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code clazz} or {@code f} is {@code null}.
     * @param <T> type the input object is checked to be instance of
     */
     <T> ResultCaseMatcher<I, O> caseOf(Class<T> clazz, Function<? super T, ? extends O> f) throws NullPointerException;

    /**
     * Defines a case that checks if the input object is instance of the given class. If the case is determined to be the
     * matching case, the provided function {@code f} will be called with the input object casted to the type the case is
     * checking for. The result of the function will be the result of the case found.
     *
     * @param clazz     Type the input object is checked to be instance of
     * @param condition predicate that has to evaluate to {@code true} for the input object to determine the case as
     *                  the matching one.
     * @param f         function that will be executed (with input object as parameter) when the case is determined to be the
     *                  matching one. The result of this function will be the result of the case found.
     * @return instance of ResultCaseMatcher (maybe same as same object as this) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code clazz} or {@code f} is {@code null}.
     * @param <T> type the input object is checked to be instance of
     */
     <T> ResultCaseMatcher<I, O> caseOf(Class<T> clazz, Predicate<? super T> condition, Function<? super T, ? extends O> f) throws NullPointerException;

    /**
     * Defines a case that checks if the given predicate returns true when it is provided with the input value. If the case
     * is determined to be the matching case, the provided consumer method will be called with the input value.
     *
     * @param p Checking predicate that defines if the case is a found, when provided with the input value.
     * @param f Function that will be called with the input value if the case is determined to be the matching one. The
     *          result of this function will be the result of the case found.
     * @return instance of ResultCaseMatcher (maybe same as same object as this) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code f} is {@code null}.
     */
     ResultCaseMatcher<I, O> caseOf(Predicate<? super I> p, Function<? super I, ? extends O> f) throws NullPointerException;

    /**
     * Defines a case that checks if the given supplier returns true. If the case is determined to be the matching case, the
     * provided consumer method will be called with the input value.
     *
     * @param s supplier that if returns true signals this case is a matching case.
     * @param f Function that will be called with the input value if the case is determined to be the matching one. The
     *          result of this function will be the result of the case found.
     * @return instance of ResultCaseMatcher (maybe same as same object as this) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code s} or {@code f} is {@code null}.
     */
     ResultCaseMatcher<I, O> caseOf(BooleanSupplier s, Function<? super I, ? extends O> f) throws NullPointerException;

    /**
     * <p>
     * This method defines a case that applies if the given boolean parameter {@code test} is true. If the case is determined
     * to be the matching case, the provided consumer function will be called with the input value. The result of the
     * function will be the result of the matching case.
     * </p>
     * <p>
     * Be aware that the expression that evaluates to the value of the {@code test} parameter will <em>always</em> be
     * evaluated, even if a previous case matched already. It is recommended to use
     * {@link de.boereck.matcher.ResultCaseMatcher#caseOf(java.util.function.Predicate, java.util.function.Function) caseOf(Predicate, Function)} or
     * {@link de.boereck.matcher.ResultCaseMatcher#caseOf(java.util.function.BooleanSupplier, java.util.function.Function) caseOf(BooleanSupplier, Function)} instead. If the runtime
     * cost of the check is little this method, however, can produce more readable code.
     * </p>
     *
     * @param test boolean value that determines if this case is a found and if the consumer should be invoked.
     * @param f    will be invoked with the object to be matched if the test parameter is true.
     * @return instance of ResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if parameter {@code f} is {@code null}.
     */
     ResultCaseMatcher<I, O> caseOf(boolean test, Function<? super I, ? extends O> f) throws NullPointerException;

    /**
     * Defines a case that checks if the given predicate returns true when it is provided with the input object to the
     * match. If the case is determined to be the matching case, the provided runnable will be called.
     *
     * @param p predicate that defines if the case is a found, when provided with the input object.
     * @param supplier Will be called if the case was determined to be the matching case to return the result for the match.
     * @return instance of ResultCaseMatcher to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code supplier} is {@code null}.
     */
     ResultCaseMatcher<I,O> caseIs(Predicate<? super I> p, Supplier<? extends O> supplier) throws NullPointerException;

    /**
     * Defines a case that checks if the given predicate returns true when it is provided with the input object to the
     * match. If the case is determined to be the matching case, the provided runnable will be called.
     *
     * @param test boolean that defines if the case is a found, when provided with the input object.
     * @param supplier Will be called if the case was determined to be the matching case to return the result for the match.
     * @return instance of ResultCaseMatcher to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code supplier} is {@code null}.
     */
     ResultCaseMatcher<I,O> caseIs(boolean test, Supplier<? extends O> supplier) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.Optional} when called with the input
     * value. If the case is determined to be the matching case, the provided function {@code f} will be called with the the
     * object that is wrapped in the optional returned by function {@code p}. The result of the consumer function will be the
     * overall result of the case found.
     *
     * @param p function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param f will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *          matching case. The consumer will be called with the value wrapped in the optional object. The result of the
     *          function will be the result of the case found.
     * @param <T> type of object extracted by function {@code p}.
     * @return instance of ResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code f} is {@code null}.
     */
     <T> ResultCaseMatcher<I, O> caseObj(Function<? super I, Optional<T>> p, Function<? super T, ? extends O> f) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalInt} when called with the
     * input value. If the case is determined to be the matching case, the provided consumer function will be called with the
     * the int value that is wrapped in the optional returned by function {@code p}. The result of the consumer function will
     * be the overall result of the case found.
     *
     * @param p function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param f will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *          matching case. The consumer will be called with the value wrapped in the Optionalnt object. The result of
     *          the function will be the result of the case found.
     * @return instance of ResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code f} is {@code null}.
     */
     ResultCaseMatcher<I, O> caseInt(Function<? super I, OptionalInt> p, IntFunction<? extends O> f) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalLong} when called with the
     * input value. If the case is determined to be the matching case, the provided consumer function will be called with the
     * the long value that is wrapped in the optional returned by function {@code p}. The result of the consumer function
     * will be the overall result of the case found.
     *
     * @param p function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param f will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *          matching case. The consumer will be called with the value wrapped in the OptionaLong object. The result of
     *          the function will be the result of the case found.
     * @return instance of ResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code f} is {@code null}.
     */
     ResultCaseMatcher<I, O> caseLong(Function<? super I, OptionalLong> p, LongFunction<? extends O> f) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalDouble} when called with the
     * input object. If the case is determined to be the matching case, the provided consumer function will be called with
     * the the double value that is wrapped in the optional returned by function {@code p}. The result of the consumer
     * function will be the overall result of the case found.
     *
     * @param p function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param f will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *          matching case. The consumer will be called with the value wrapped in the OptionaDouble object. The result
     *          of the function will be the result of the case found.
     * @return instance of ResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code f} is {@code null}.
     */
     ResultCaseMatcher<I, O> caseDouble(Function<? super I, OptionalDouble> p, DoubleFunction<? extends O> f) throws NullPointerException;

}