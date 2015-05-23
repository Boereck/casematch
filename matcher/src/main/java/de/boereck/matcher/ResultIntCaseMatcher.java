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
import java.util.function.IntPredicate;
import java.util.function.LongFunction;
import java.util.function.Supplier;

/**
 * <p>
 * This interface is used to define cases that can be used to found a given int value and define associated functions that
 * are executed when a case matches for the given value and return a result object. The result object will be returned by
 * closing methods. See below which methods of this class are closing methods.<br>
 * The value to be checked has to be provided during creation of the instance. Only the action of one of the given cases that
 * matches will be executed.
 * </p>
 * <p>
 * <p>
 * The case de.boereck.matcher interface does <em>not</em> give a guarantee if the cases are checked in the order they are specified. It
 * is also not guaranteed that remaining cases are evaluated when a matching case was found. Implementations may even execute
 * the found checks and actions asynchronously. This has to be clarified by the factory method providing the instance or by
 * sub-types providing a stricter API.
 * </p>
 * <p>
 * <p>
 * It is also not defined if the evaluation of case predicates or functions is done eager when a case method is called or
 * lazy when a closing method is called. Closing methods by this interface are:
 * <ul>
 * <li> {@link de.boereck.matcher.ResultIntCaseMatcher#otherwise(java.util.function.IntFunction) otherwise(IntFunction)}</li>
 * <li> {@link de.boereck.matcher.ResultIntCaseMatcher#otherwise(Object) otherwise(Object)}</li>
 * <li> {@link de.boereck.matcher.ResultIntCaseMatcher#otherwiseThrow(java.util.function.Supplier) otherwiseThrow(Supplier)}</li>
 * <li> {@link de.boereck.matcher.ResultIntCaseMatcher#ifResult(java.util.function.Consumer) ifResult(Consumer)}</li>
 * <li> {@link de.boereck.matcher.ResultIntCaseMatcher#orElse(Object) orElse(Object)}</li>
 * <li> {@link de.boereck.matcher.ResultIntCaseMatcher#orElse(java.util.function.Supplier) orElse(Supplier)}</li>
 * <li> {@link de.boereck.matcher.ResultIntCaseMatcher#orElseThrow(java.util.function.Supplier) orElseThrow(Supplier)}</li>
 * </ul>
 * Sub-types may define more closing methods. The effects of closing methods are always taking effect after all cases were
 * checked.
 * </p>
 *
 * @param <O> type of the result object returned from the case found
 * @author Max Bureck
 */
public interface ResultIntCaseMatcher<O> {

    /**
     * Defines a case that checks if the input value is eqal to the value of parameter {@code i}. If the case is determined
     * to be the matching case, the provided function {@code f} will be called with the input object casted to the type the
     * case is checking for. The result of the function will be the result of the case found.
     *
     * @param i value the input will be compared to.
     * @param f function that will be executed (with input value as parameter) when the case is determined to be the
     *          matching one. The result of this function will be the result of the case found.
     * @return instance of ResultIntCaseMatcher (maybe same as same object as this) to define further cases.
     * @throws NullPointerException might be thrown if parameter {@code f} is {@code null}.
     */
    public abstract <T> ResultIntCaseMatcher<O> caseOf(int i, IntFunction<? extends O> f) throws NullPointerException;

    /**
     * Defines a case that checks if the given predicate returns true when it is provided with the input value. If the case
     * is determined to be the matching case, the provided consumer method will be called with the input value.
     *
     * @param p        Checking predicate that defines if the case is a found, when provided with the input value.
     * @param f Function that will be called with the input value if the case is determined to be the matching one. The
     *                 result of this function will be the result of the case found.
     * @return instance of ResultIntCaseMatcher (maybe same as same object as this) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code f} is {@code null}.
     */
    public abstract ResultIntCaseMatcher<O> caseOf(IntPredicate p, IntFunction<? extends O> f) throws NullPointerException;

    /**
     * Defines a case that checks if the given supplier returns true. If the case is determined to be the matching case, the
     * provided consumer method will be called with the input value.
     *
     * @param s supplier that if returns true signals this case is a matching case.
     * @param f Function that will be called with the input value if the case is determined to be the matching one. The
     *          result of this function will be the result of the case found.
     * @return instance of ResultIntCaseMatcher (maybe same as same object as this) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code s} or {@code f} is {@code null}.
     */
    public abstract ResultIntCaseMatcher<O> caseOf(BooleanSupplier s, IntFunction<? extends O> f) throws NullPointerException;

    /**
     * <p>
     * This method defines a case that applies if the given boolean parameter {@code test} is true. If the case is determined
     * to be the matching case, the provided function {@code f} will be called with the input value. The result of the
     * function will be the result of the matching case.
     * </p>
     * <p>
     * <p>
     * Be aware that the expression that evaluates to the value of the {@code test} parameter will <em>always</em> be
     * evaluated, even if a previous case matched already. It is recommended to use
     * {@link de.boereck.matcher.ResultIntCaseMatcher#caseOf(java.util.function.IntPredicate, java.util.function.IntFunction) caseOf(IntPredicate, IntFunction)} or
     * {@link de.boereck.matcher.ResultIntCaseMatcher#caseOf(java.util.function.BooleanSupplier, java.util.function.IntFunction) caseOf(BooleanSupplier, IntFunction)} instead. If the
     * runtime cost of the check is little this method, however, can produce more readable code.
     * </p>
     *
     * @param test boolean value that determines if this case is a found and if the consumer should be invoked.
     * @param f    will be invoked with the object to be matched if the test parameter is true.
     * @return instance of ResultIntCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code f} is {@code null}.
     */
    public abstract ResultIntCaseMatcher<O> caseOf(boolean test, IntFunction<? extends O> f) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.Optional} when called with the input
     * int value. If the case is determined to be the matching case, the provided function {@code consumer} will be called
     * with the the object that is wrapped in the optional returned by function {@code p}. The result of the consumer
     * function will be the overall result of the case found.
     *
     * @param p        function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param f will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *                 matching case. The consumer will be called with the value wrapped in the optional object. The result of the
     *                 function will be the result of the case found.
     * @return instance of ResultIntCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code f} is {@code null}.
     */
    public abstract <T> ResultIntCaseMatcher<O> caseObj(IntFunction<Optional<T>> p, Function<? super T, ? extends O> f) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalInt} when called with the
     * input int value. If the case is determined to be the matching case, the provided consumer function will be called with
     * the the int value that is wrapped in the optional returned by function {@code p}. The result of the consumer function
     * will be the overall result of the case found.
     *
     * @param p        function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param f will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *                 matching case. The consumer will be called with the value wrapped in the Optionalnt object. The result of
     *                 the function will be the result of the case found.
     * @return instance of ResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code f} is {@code null}.
     */
    public abstract <T> ResultIntCaseMatcher<O> caseInt(IntFunction<OptionalInt> p, IntFunction<? extends O> f) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalLong} when called with the
     * input int value. If the case is determined to be the matching case, the provided consumer function will be called with
     * the the long value that is wrapped in the optional returned by function {@code p}. The result of the consumer function
     * will be the overall result of the case found.
     *
     * @param p        function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param f will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *                 matching case. The consumer will be called with the value wrapped in the OptionaLong object. The result of
     *                 the function will be the result of the case found.
     * @return instance of ResultIntCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code f} is {@code null}.
     */
    public abstract <T> ResultIntCaseMatcher<O> caseLong(IntFunction<OptionalLong> p, LongFunction<? extends O> f) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalDouble} when called with the
     * input int value. If the case is determined to be the matching case, the provided consumer function will be called with
     * the the double value that is wrapped in the optional returned by function {@code p}. The result of the consumer
     * function will be the overall result of the case found.
     *
     * @param p        function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param f will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *                 matching case. The consumer will be called with the value wrapped in the OptionaDouble object. The result
     *                 of the function will be the result of the case found.
     * @return instance of ResultIntCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code f} is {@code null}.
     */
    public abstract <T> ResultIntCaseMatcher<O> caseDouble(IntFunction<OptionalDouble> p, DoubleFunction<? extends O> f) throws NullPointerException;

    /**
     * Optional will be empty if no case matched or if the matching case returned null. This methods is a closing function.
     * Some implementations of this interface may need to call a closing method to trigger the evaluation of case matching.
     *
     * @return optional that may return the result of a case found. If the optional does not hold a value either no case
     * matched or the matching case returned a null value.
     */
    public abstract Optional<O> result();

    /**
     * If there was a case found and the result of the found is not {@code null} the given {@code consumer} is called with
     * the result value.
     *
     * @param consumer will be called with the result of the case found if the result was not {@code null}.
     * @throws NullPointerException might be thrown if either parameter {@code consumer} is {@code null}.
     */
    public abstract void ifResult(Consumer<? super O> consumer) throws NullPointerException;

    /**
     * If there was a case found and the result of the found is not {@code null} the given callback {@code onResult} is called with
     * the result value. If no result was found or the result is {@code null}, the callback {@code onAbsent} is called.
     * @param onResult will be called with the result of the case found if the result was not {@code null}.
     * @param onAbsent will be called if no case matched or the match returned {@code null}.
     * @throws NullPointerException  might be thrown if parameter {@code onResult} or {@code onAbsent} is {@code null}.
     */
    public abstract void then(Consumer<? super O> onResult, Runnable onAbsent) throws NullPointerException;

    /**
     * If there was no prior found, the method will return the provided value. Attention: This method will return
     * {@code null} if the result of the matching caseOf was null!
     *
     * @param o will be returned if there was no prior found. This value might be null.
     * @return If there was a case-found, the result will be returned, otherwise the given alternative value will be
     * returned. The returned value might be null!
     */
    public abstract O otherwise(O o);

    /**
     * If there was no prior found, the method will return a value provided by the given {@code supplier}. Attention: This
     * method will return {@code null} if the result of the matching caseOf was null!
     *
     * @param supplier will supply the result value if there was no prior found. This value might be null. The supplier itself
     *                 must not be {@code null}.
     * @return If there was a case-found, the result will be returned, otherwise the given alternative value will be returned
     * that is provided by the {@code supplier}. The returned value might be null!
     * @throws NullPointerException will be thrown if the {@code supplier} was null.
     */
    public abstract O otherwise(IntFunction<? extends O> supplier) throws NullPointerException;

    /**
     * If there was no found so far, the method will throw an exception. Be aware that this method will return {@code null}
     * if the matching case provided null as a result! If you want to throw an exception, when no case matched or the
     * matching case returned null, use {@link de.boereck.matcher.ResultIntCaseMatcher#orElseThrow(java.util.function.Supplier) orElseThrow(Supplier)}.
     *
     * @param exSupplier supplier of the exception to be thrown. For exceptions with parameterless constructors a method reference
     *                   can be used. E.g. {@code MyException::new}.
     * @return If there was a case-found, the result will be returned, the result value may be {@code null}.
     * @throws X                    if there was no matching case
     * @throws NullPointerException will be thrown if the exSupplier was {@code null} or the provided exception is {@code null}.
     * @see de.boereck.matcher.ResultIntCaseMatcher#orElseThrow(java.util.function.Supplier).
     */
    public abstract <X extends Throwable> O otherwiseThrow(Supplier<X> exSupplier) throws X, NullPointerException;

    /**
     * This method will return the given value, if there was no found, or the matching case returned {@code null}. The given
     * alternative value must not be {@code null}. The value returned from this function will never be {@code null}.
     *
     * @param o object that will be returned if there was no found, or the return value from found was {@code null}. This
     *          parameter must not be null.
     * @return either result of found, or parameter o. The returned value will never be null.
     * @throws NullPointerException will the thrown if parameter {@code o == null}
     */
    public abstract O orElse(O o) throws NullPointerException;

    /**
     * This method will return a value from the given supplier, if there was no found, or the matching case returned
     * {@code null}. The supplied alternative value must not be {@code null}. The value returned from this function will
     * never be {@code null}.
     *
     * @param supplier provides the value to be returned if there was no found or result of found was {@code null}. This parameter
     *                 must not be {@code null} and the values supplied by the supplied must not be {@code null}.
     * @return either result of found, or value provided from given supplier. The returned value will never be {@code null}.
     * @throws NullPointerException will be thrown if supplier is {@code null} or supplier supplies a {@code null} value.
     */
    public abstract O orElse(Supplier<? extends O> supplier) throws NullPointerException;

    /**
     * If the result is present and the result is not {@code null} the result will be returned. Otherwise an exception
     * provided by the given {@code exSupplier} will be thrown.
     *
     * @param exSupplier provides the exception to be thrown when no result or {@code null} result is available. This parameter must
     *                   not be {@code null} and the exception provided must not be null.
     * @return the result value if there was a found and the found provided a non {@code null} value.
     * @throws X                    Will be thrown if there was no found or the found provided a {@code null} result.
     * @throws NullPointerException will be thrown if the {@code exSupplier} is {@code null} or the provided exception is {@code null}.
     */
    public abstract <X extends Throwable> O orElseThrow(Supplier<X> exSupplier) throws X, NullPointerException;
}