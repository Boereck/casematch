package de.boereck.matcher;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p> This interface is used to define cases that can be used to found a given object and define associated actions that are
 * executed when a case matches for the given object. The actions and therefore the whole matching will provide no result
 * value.<br>
 * The object to be checked has to be provided during creation of the instance. Only the action of one of the given cases
 * that matches will be executed.</p>
 * <p> This interface does <em>not</em> give a guarantee if the cases are checked in the order they are specified. It
 * is also not guaranteed that remaining cases are evaluated when a matching case was found. Implementations may even execute
 * the found checks and actions asynchronously. This has to be clarified by the factory method providing the instance or by
 * sub-types providing a stricter API. </p>
 * <p> It is also not defined if the evaluation of case predicates or functions is done eager when a case method is called or
 * lazy when a closing method is called (starting the evaluation of cases). This interface does not declare any closing methods,
 * sub-types may define their own closing methods. </p>
 *
 * @param <I> type of the input object to be matched
 * @author Max Bureck
 */
public interface NoResultCaseMatcher<I> {

    /**
     * Defines a case that checks if the input object is instance of the given class. If the case is determined to be the
     * matching case, the provided consumer method will be called with the input object casted to the type the case is
     * checking for.
     *
     * @param clazz    Type the input object is checked to be instance of
     * @param consumer action that will be executed (with input object as parameter) when the case is determined to be the
     *                 matching one.
     * @return instance of NoResultCaseMatcher to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code clazz} or {@code f} is {@code null}.
     */
    public abstract <T> NoResultCaseMatcher<I> caseOf(Class<T> clazz, Consumer<? super T> consumer) throws NullPointerException;

    /**
     * Defines a case that checks if the input object is instance of the given class and the predicate {@code condition}
     * evaluates to {@code true} for the input object. If the case is determined to be the  matching case, the provided
     * consumer method will be called with the input object casted to the type the case is checking for.
     *
     * @param clazz     Type the input object is checked to be instance of
     * @param condition predicate that must evaluate to true for the input object to determine this case as the matching
     *                  one
     * @param consumer  action that will be executed (with input object as parameter) when the case is determined to be the
     *                  matching one.
     * @return instance of NoResultCaseMatcher to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code clazz} or {@code f} is {@code null}.
     */
    public abstract <T> NoResultCaseMatcher<I> caseOf(Class<T> clazz, Predicate<? super T> condition, Consumer<? super T> consumer) throws NullPointerException;

    /**
     * Defines a case that checks if the given predicate returns true when it is provided with the input object. If the case
     * is determined to be the matching case, the provided consumer method will be called with the input object.
     *
     * @param p        Checking predicate that defines if the case is a found, when provided with the input object.
     * @param consumer Function that will be called with the input object if the case is determined to be the matching one.
     * @return instance of NoResultCaseMatcher to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code consumer} is {@code null}.
     */
    public abstract NoResultCaseMatcher<I> caseOf(Predicate<? super I> p, Consumer<? super I> consumer) throws NullPointerException;

    /**
     * Defines a case that checks if the given predicate returns true when it is provided with the input object to the
     * match. If the case is determined to be the matching case, the provided runnable will be called.
     *
     * @param p    predicate that defines if the case is a found, when provided with the input object.
     * @param then Will be called if the case was determined to be the matching case.
     * @return instance of NoResultCaseMatcher to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code then} is {@code null}.
     */
    public abstract NoResultCaseMatcher<I> caseIs(Predicate<? super I> p, Runnable then) throws NullPointerException;

    /**
     * Defines a case that checks if the given predicate returns true when it is provided with the input object to the
     * match. If the case is determined to be the matching case, the provided runnable will be called.
     *
     * @param test boolean that defines if the case is a found, when provided with the input object.
     * @param then Will be called if the case was determined to be the matching case.
     * @return instance of NoResultCaseMatcher to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code then} is {@code null}.
     */
    public abstract NoResultCaseMatcher<I> caseIs(boolean test, Runnable then) throws NullPointerException;

    /**
     * Defines a case that checks if the given supplier returns true. If the case is determined to be the matching case, the
     * provided consumer method will be called with the input object.
     *
     * @param s        supplier that if returns true signals this case is a matching case.
     * @param consumer Function that will be called with the input object if the case is determined to be the matching one.
     * @return instance of NoResultCaseMatcher to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code s} or {@code consumer} is {@code null}.
     */
    public abstract NoResultCaseMatcher<I> caseOf(BooleanSupplier s, Consumer<? super I> consumer) throws NullPointerException;

    /**
     * <p>
     * This method defines a case that applies if the given boolean parameter {@code test} is true. If the case is determined
     * to be the matching case, the provided consumer function will be called with the input object.
     * </p>
     * <p>
     * Be aware that the expression that evaluates to the value of the {@code test} parameter will <em>always</em> be
     * evaluated, even if a previous case matched already. It is recommended to use
     * {@link de.boereck.matcher.NoResultCaseMatcher#caseOf(java.util.function.Predicate, java.util.function.Consumer) caseOf(Predicate, Consumer)} or
     * {@link de.boereck.matcher.NoResultCaseMatcher#caseOf(java.util.function.BooleanSupplier, java.util.function.Consumer) caseOf(BooleanSupplier, Consumer)} instead. If the
     * runtime cost of the check is little this method, however, can produce more readable code.
     * </p>
     *
     * @param test     boolean value that determines if this case is a found and if the consumer should be invoked.
     * @param consumer will be invoked with the object to be matched if the test parameter is true.
     * @return instance of NoResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code consumer} is {@code null}.
     */
    public abstract NoResultCaseMatcher<I> caseOf(boolean test, Consumer<? super I> consumer) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.Optional} when called with the input
     * object. If the case is determined to be the matching case, the provided consumer function will be called with the the
     * object that is wrapped in the optional returned by function {@code p}.
     *
     * @param p        function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param consumer will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *                 matching case. The consumer will be called with the value wrapped in the optional object.
     * @return instance of NoResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code consumer} is {@code null}.
     */
    public abstract <T> NoResultCaseMatcher<I> caseObj(Function<? super I, Optional<T>> p, Consumer<? super T> consumer) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalInt} when called with the
     * input object. If the case is determined to be the matching case, the provided consumer function will be called with
     * the the int value that is wrapped in the optional returned by function {@code p}.
     *
     * @param p        function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param consumer will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *                 matching case. The consumer will be called with the value wrapped in the Optionalnt object.
     * @return instance of NoResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code consumer} is {@code null}.
     */
    public abstract NoResultCaseMatcher<I> caseInt(Function<? super I, OptionalInt> p, IntConsumer consumer) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalLong} when called with the
     * input object. If the case is determined to be the matching case, the provided consumer function will be called with
     * the the long value that is wrapped in the optional returned by function {@code p}.
     *
     * @param p        function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param consumer will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *                 matching case. The consumer will be called with the value wrapped in the OptionaLong object.
     * @return instance of NoResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code consumer} is {@code null}.
     */
    public abstract NoResultCaseMatcher<I> caseLong(Function<? super I, OptionalLong> p, LongConsumer consumer) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalDouble} when called with the
     * input object. If the case is determined to be the matching case, the provided consumer function will be called with
     * the the double value that is wrapped in the optional returned by function {@code p}.
     *
     * @param p        function that returns an optional that indicates if the case is a found, when the optional is not empty.
     * @param consumer will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *                 matching case. The consumer will be called with the value wrapped in the OptionaDouble object.
     * @return instance of NoResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException might be thrown if either parameter {@code p} or {@code consumer} is {@code null}.
     */
    public abstract NoResultCaseMatcher<I> caseDouble(Function<? super I, OptionalDouble> p, DoubleConsumer consumer) throws NullPointerException;

}