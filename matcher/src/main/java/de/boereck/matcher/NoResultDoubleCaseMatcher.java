package de.boereck.matcher;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Supplier;

/**
 * <p>
 * This interface is used to define cases that can be used to match a given double value and define associated actions that are
 * executed when a case matches for the given value. The actions and therefore the whole matching will provide no result
 * value.<br>
 * The double value to be checked has to be provided during creation of the instance. Only the action of one of the given cases
 * that matches will be executed.
 * </p>
 * 
 * <p>
 * The case de.boereck.matcher interface does <em>not</em> give a guarantee if the cases are checked in the order they are specified. It
 * is also not guaranteed that remaining cases are evaluated when a matching case was found. Implementations may even execute
 * the match checks and actions asynchronously. This has to be clarified by the factory method providing the instance or by
 * sub-types providing a stricter API.
 * </p>
 * 
 * <p>
 * It is also not defined if the evaluation of case predicates or functions is done eager when a case method is called or
 * lazy when a closing method is called. Closing methods by this interface are:
 * <ul>
 * <li> {@link de.boereck.matcher.NoResultDoubleCaseMatcher#otherwise(java.util.function.DoubleConsumer) otherwise(Consumer)}</li>
 * <li> {@link de.boereck.matcher.NoResultDoubleCaseMatcher#otherwiseThrow(java.util.function.Supplier) otherwiseThrow(Supplier)}</li>
 * </ul>
 * Sub-types may define more closing methods. The effects of closing methods are always taking effect after all cases were
 * checked.
 * </p>
 * 
 * @author Max Bureck
 */
public interface NoResultDoubleCaseMatcher {


    /**
     * Defines a case that checks if the given predicate returns true when it is provided with the input value. If the case
     * is determined to be the matching case, the provided consumer method will be called with the input value.
     * 
     * @param p
     *            Checking predicate that defines if the case is a match, when provided with.
     * @param consumer
     *            Function that will be called with the input object if the case is determined to be the matching one.
     * @return instance of OfNoResultDoubleCaseMatcher to define further cases.
     * @throws NullPointerException
     *             might be thrown if either parameter {@code p} or {@code consumer} is {@code null}.
     */
    public abstract NoResultDoubleCaseMatcher caseOf(DoublePredicate p, DoubleConsumer consumer) throws NullPointerException;

    /**
     * Defines a case that checks if the given supplier returns true. If the case is determined to be the matching case, the
     * provided consumer method will be called with the input value.
     * 
     * @param s
     *            supplier that if returns true signals this case is a matching case.
     * @param consumer
     *            Function that will be called with the input value if the case is determined to be the matching one.
     * @return instance of NoResultDoubleCaseMatcher to define further cases.
     * @throws NullPointerException
     *             might be thrown if either parameter {@code p} or {@code consumer} is {@code null}.
     */
    public abstract NoResultDoubleCaseMatcher caseOf(BooleanSupplier s, DoubleConsumer consumer) throws NullPointerException;

    /**
     * <p>
     * This method defines a case that applies if the given boolean parameter {@code test} is true. If the case is determined
     * to be the matching case, the provided consumer function will be called with the input value.
     * </p>
     * 
     * <p>
     * Be aware that the expression that evaluates to the value of the {@code test} parameter will <em>always</em> be
     * evaluated, even if a previous case matched already. It is recommended to use
     * {@link NoResultCaseMatcher#caseOf(java.util.function.Predicate, java.util.function.Consumer) caseOf(Predicate, Consumer)} or
     * {@link NoResultCaseMatcher#caseOf(java.util.function.BooleanSupplier, java.util.function.Consumer) caseOf(BooleanSupplier, Consumer)} instead. If the
     * runtime cost of the check is little this method, however, can produce more readable code.
     * </p>
     * 
     * @param test
     *            boolean value that determines if this case is a match and if the consumer should be invoked.
     * @param consumer
     *            will be invoked with the object to be matched if the test parameter is true.
     * @return instance of NoResultCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException
     *             might be thrown if either parameter {@code consumer} is {@code null}.
     */
    public abstract NoResultDoubleCaseMatcher caseOf(boolean test, DoubleConsumer consumer) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.Optional} when called with the input
     * value. If the case is determined to be the matching case, the provided consumer function will be called with the the
     * value that is wrapped in the optional returned by function {@code p}.
     * 
     * @param p
     *            function that returns an optional that indicates if the case is a match, when the optional is not empty.
     * @param consumer
     *            will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *            matching case. The consumer will be called with the object wrapped in the optional object.
     * @return instance of NoResultDoubleCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException
     *             might be thrown if either parameter {@code p} or {@code consumer} is {@code null}.
     */
    public abstract <T> NoResultDoubleCaseMatcher caseObj(DoubleFunction<Optional<T>> p, Consumer<T> consumer) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalInt} when called with the
     * input value. If the case is determined to be the matching case, the provided consumer function will be called with the
     * the double value that is wrapped in the optional returned by function {@code p}.
     * 
     * @param p
     *            function that returns an optional that indicates if the case is a match, when the optional is not empty.
     * @param consumer
     *            will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *            matching case. The consumer will be called with the value wrapped in the OptionalInt object.
     * @return instance of NoResultDoubleCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException
     *             might be thrown if either parameter {@code p} or {@code consumer} is {@code null}.
     */
    public abstract NoResultDoubleCaseMatcher caseInt(DoubleFunction<OptionalInt> p, IntConsumer consumer) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalLong} when called with the
     * input value. If the case is determined to be the matching case, the provided consumer function will be called with the
     * the long value that is wrapped in the optional returned by function {@code p}.
     * 
     * @param p
     *            function that returns an optional that indicates if the case is a match, when the optional is not empty.
     * @param consumer
     *            will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *            matching case. The consumer will be called with the value wrapped in the OptionaLong object.
     * @return instance of NoResultDoubleCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException
     *             might be thrown if either parameter {@code p} or {@code consumer} is {@code null}.
     */
    public abstract NoResultDoubleCaseMatcher caseLong(DoubleFunction<OptionalLong> p, LongConsumer consumer) throws NullPointerException;

    /**
     * Defines a case that matches if the function {@code p} returns a non empty {@link java.util.OptionalDouble} when called with the
     * input value. If the case is determined to be the matching case, the provided consumer function will be called with the
     * the double value that is wrapped in the optional returned by function {@code p}.
     * 
     * @param p
     *            function that returns an optional that indicates if the case is a match, when the optional is not empty.
     * @param consumer
     *            will be called if the function {@code p} returns an non-empty optional and the case is determined to be the
     *            matching case. The consumer will be called with the value wrapped in the OptionaDouble object.
     * @return instance of NoResultDoubleCaseMatcher (which might the same as this object) to define further cases.
     * @throws NullPointerException
     *             might be thrown if either parameter {@code p} or {@code consumer} is {@code null}.
     */
    public abstract NoResultDoubleCaseMatcher caseDouble(DoubleFunction<OptionalDouble> p, DoubleConsumer consumer) throws NullPointerException;

    /**
     * The given consumer will be called if all cases were checked and none of them matched. This is a closing method, some
     * implementations of the interface may require an closing method to be called after a sequence of case definitions. The
     * consumer will be called with the input value of the case match.
     * 
     * @param consumer
     *            will be called with the input object if there was no matching case
     * @throws NullPointerException
     *             might be thrown if either parameter {@code consumer} is {@code null}.
     */
    public abstract void otherwise(DoubleConsumer consumer) throws NullPointerException;

    /**
     * If all cases were checked and there was no match so far, the given supplier will be called and the given throwable
     * will be thrown. This is a closing method, some implementations of the interface may require an closing method to be
     * called after a sequence of case definitions. The consumer will be called with the input object of the case match.
     * 
     * @param exSupplier
     *            supplier of the exception to be thrown. For exceptions with parameterless constructors a method reference
     *            can be used. E.g. {@code MyException::new}.
     * @throws NullPointerException
     *             might be thrown if either parameter {@code exSupplier} is {@code null}.
     */
    public abstract <X extends Throwable> void otherwiseThrow(Supplier<X> exSupplier) throws X, NullPointerException;
}
