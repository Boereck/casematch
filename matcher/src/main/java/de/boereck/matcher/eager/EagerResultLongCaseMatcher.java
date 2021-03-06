package de.boereck.matcher.eager;

import de.boereck.matcher.ResultLongCaseMatcher;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;

/**
 * <p>Eager version of {@link ResultLongCaseMatcher}.</p>
 * <p>This case matcher will evaluate the cases as soon as the case methods are called. The input object the cases
 * are defined for must be known upfront on creation of instances of this interface. The evaluation order of cases
 * is guaranteed to be in the order of specification. Both checks for cases, as well as the associated actions will
 * perform on the same thread that is invoking the case methods.</p>
 * Closing methods by this interface are:
 * <ul>
 * <li> {@link de.boereck.matcher.eager.EagerResultLongCaseMatcher#otherwise(java.util.function.LongFunction) otherwise(LongFunction)}</li>
 * <li> {@link de.boereck.matcher.eager.EagerResultLongCaseMatcher#otherwise(Object) otherwise(Object)}</li>
 * <li> {@link de.boereck.matcher.eager.EagerResultLongCaseMatcher#otherwiseThrow(java.util.function.Supplier) otherwiseThrow(Supplier)}</li>
 * <li> {@link de.boereck.matcher.eager.EagerResultLongCaseMatcher#ifResult(java.util.function.Consumer) ifResult(Consumer)}</li>
 * <li> {@link de.boereck.matcher.eager.EagerResultLongCaseMatcher#orElse(Object) orElse(Object)}</li>
 * <li> {@link de.boereck.matcher.eager.EagerResultLongCaseMatcher#orElse(java.util.function.Supplier) orElse(Supplier)}</li>
 * <li> {@link de.boereck.matcher.eager.EagerResultLongCaseMatcher#orElseThrow(java.util.function.Supplier) orElseThrow(Supplier)}</li>
 * </ul>
 * @author Max Bureck
 */
public interface EagerResultLongCaseMatcher<O> extends ResultLongCaseMatcher<O> {

    /**
     * {@inheritDoc}
     */
    @Override
     EagerResultLongCaseMatcher<O> caseOf(long i, LongFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
     EagerResultLongCaseMatcher<O> caseOf(LongPredicate p, LongFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
     EagerResultLongCaseMatcher<O> caseOf(BooleanSupplier s, LongFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
     EagerResultLongCaseMatcher<O> caseOf(boolean test, LongFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
     EagerResultLongCaseMatcher<O> caseIs(LongPredicate p, Supplier<? extends O> supplier) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
     EagerResultLongCaseMatcher<O> caseIs(boolean test, Supplier<? extends O> supplier) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
     <T> EagerResultLongCaseMatcher<O> caseObj(LongFunction<Optional<T>> p, Function<? super T, ? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
     EagerResultLongCaseMatcher<O> caseInt(LongFunction<OptionalInt> p, IntFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
     EagerResultLongCaseMatcher<O> caseLong(LongFunction<OptionalLong> p, LongFunction<? extends O> f) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
     EagerResultLongCaseMatcher<O> caseDouble(LongFunction<OptionalDouble> p, DoubleFunction<? extends O> f) throws NullPointerException;

    /////////////////////
    // Closing Methods //
    /////////////////////

    /**
     * Optional will be empty if no case matched or if the matching case returned null. This methods is a closing function.
     * Some implementations of this interface may need to call a closing method to trigger the evaluation of case matching.
     *
     * @return optional that may return the result of a case found. If the optional does not hold a value either no case
     * matched or the matching case returned a null value.
     */
     Optional<O> result();

    /**
     * If there was a case found and the result of the found is not {@code null} the given {@code consumer} is called with
     * the result value.
     *
     * @param consumer will be called with the result of the case found if the result was not {@code null}.
     * @throws NullPointerException might be thrown if either parameter {@code consumer} is {@code null}.
     */
     void ifResult(Consumer<? super O> consumer) throws NullPointerException;

    /**
     * If there was a case found and the result of the found is not {@code null} the given callback {@code onResult} is called with
     * the result value. If no result was found or the result is {@code null}, the callback {@code onAbsent} is called.
     *
     * @param onResult will be called with the result of the case found if the result was not {@code null}.
     * @param onAbsent will be called if no case matched or the match returned {@code null}.
     * @throws NullPointerException might be thrown if parameter {@code onResult} or {@code onAbsent} is {@code null}.
     */
     void then(Consumer<? super O> onResult, Runnable onAbsent) throws NullPointerException;

    /**
     * If there was no prior found, the method will return the provided value. Attention: This method will return
     * {@code null} if the result of the matching caseOf was null!
     *
     * @param o will be returned if there was no prior found. This value might be null.
     * @return If there was a case-found, the result will be returned, otherwise the given alternative value will be
     * returned. The returned value might be null!
     */
     O otherwise(O o);

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
     O otherwise(LongFunction<? extends O> supplier) throws NullPointerException;

    /**
     * If there was no found so far, the method will throw an exception. Be aware that this method will return {@code null}
     * if the matching case provided null as a result! If you want to throw an exception, when no case matched or the
     * matching case returned null, use {@link de.boereck.matcher.eager.EagerResultLongCaseMatcher#orElseThrow(java.util.function.Supplier) orElseThrow(Supplier)}.
     *
     * @param exSupplier supplier of the exception to be thrown. For exceptions with parameterless constructors a method reference
     *                   can be used. E.g. {@code MyException::new}.
     * @return If there was a case-found, the result will be returned, the result value may be {@code null}.
     * @throws X                    if there was no matching case
     * @throws NullPointerException will be thrown if the exSupplier was {@code null} or the provided exception is {@code null}.
     * @see de.boereck.matcher.eager.EagerResultLongCaseMatcher#orElseThrow(Supplier)
     * @param <X> type of exception that will be thrown if no other case matched.
     */
     <X extends Throwable> O otherwiseThrow(Supplier<X> exSupplier) throws X, NullPointerException;

    /**
     * This method will return the given value, if there was no found, or the matching case returned {@code null}. The given
     * alternative value must not be {@code null}. The value returned from this function will never be {@code null}.
     *
     * @param o object that will be returned if there was no found, or the return value from found was {@code null}. This
     *          parameter must not be null.
     * @return either result of found, or parameter o. The returned value will never be null.
     * @throws NullPointerException will the thrown if parameter {@code o == null}
     */
     O orElse(O o) throws NullPointerException;

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
     O orElse(Supplier<? extends O> supplier) throws NullPointerException;

    /**
     * If the result is present and the result is not {@code null} the result will be returned. Otherwise an exception
     * provided by the given {@code exSupplier} will be thrown.
     *
     * @param exSupplier provides the exception to be thrown when no result or {@code null} result is available. This parameter must
     *                   not be {@code null} and the exception provided must not be null.
     * @param <X>        type of exception that may be thrown if no match found.
     * @return the result value if there was a found and the found provided a non {@code null} value.
     * @throws X                    Will be thrown if there was no found or the found provided a {@code null} result.
     * @throws NullPointerException will be thrown if the {@code exSupplier} is {@code null} or the provided exception is {@code null}.
     */
     <X extends Throwable> O orElseThrow(Supplier<X> exSupplier) throws X, NullPointerException;
}
