package de.boereck.matcher.eager;

import de.boereck.matcher.NoResultCaseMatcher;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;

/**
 * <p>Eager version of {@link NoResultCaseMatcher}.</p>
 * <p>This case matcher will evaluate the cases as soon as the case methods are called. The input object the cases
 * are defined for must be known upfront on creation of instances of this interface.  The evaluation order of cases
 * is guaranteed to be in the order of specification. Both checks for cases, as well as the associated actions will
 * perform on the same thread that is invoking the case methods.</p>
 * Closing methods by this interface are:
 * <ul>
 * <li> {@link de.boereck.matcher.eager.EagerNoResultCaseMatcher#otherwise(java.util.function.Consumer) otherwise(Consumer)}</li>
 * <li> {@link de.boereck.matcher.eager.EagerNoResultCaseMatcher#otherwiseThrow(java.util.function.Supplier) otherwiseThrow(Supplier)}</li>
 * </ul>
 *
 * @param <I> type of input to matcher
 * @author Max Bureck
 */
public interface EagerNoResultCaseMatcher<I> extends NoResultCaseMatcher<I> {

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> EagerNoResultCaseMatcher<I> caseOf(Class<T> clazz, Consumer<? super T> consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> EagerNoResultCaseMatcher<I> caseOf(Class<T> clazz, Predicate<? super T> condition, Consumer<? super T> consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultCaseMatcher<I> caseOf(Predicate<? super I> p, Consumer<? super I> consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultCaseMatcher<I> caseIs(Predicate<? super I> p, Runnable then) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultCaseMatcher<I> caseIs(boolean test, Runnable then) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultCaseMatcher<I> caseOf(BooleanSupplier s, Consumer<? super I> consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultCaseMatcher<I> caseOf(boolean test, Consumer<? super I> consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> EagerNoResultCaseMatcher<I> caseObj(Function<? super I, Optional<T>> p, Consumer<? super T> consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultCaseMatcher<I> caseInt(Function<? super I, OptionalInt> p, IntConsumer consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultCaseMatcher<I> caseLong(Function<? super I, OptionalLong> p, LongConsumer consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultCaseMatcher<I> caseDouble(Function<? super I, OptionalDouble> p, DoubleConsumer consumer) throws NullPointerException;

    /////////////////////
    // Closing Methods //
    /////////////////////


    /**
     * The given consumer will be called if all cases were checked and none of them matched. This is a closing method, some
     * implementations of the interface may require an closing method to be called after a sequence of case definitions. The
     * consumer will be called with the input object of the case found.
     *
     * @param consumer will be called with the input object if there was no matching case
     * @throws NullPointerException might be thrown if either parameter {@code consumer} is {@code null}.
     */
    public abstract void otherwise(Consumer<? super I> consumer) throws NullPointerException;

    /**
     * If all cases were checked and there was no found so far, the given supplier will be called and the given throwable
     * will be thrown. This is a closing method, some implementations of the interface may require an closing method to be
     * called after a sequence of case definitions. The consumer will be called with the input object of the case found.
     *
     * @param exSupplier supplier of the exception to be thrown. For exceptions with parameterless constructors a method reference
     *                   can be used. E.g. {@code MyException::new}.
     * @param <X>        type of exception that will be thrown if no other case matched.
     * @throws NullPointerException might be thrown if parameter {@code exSupplier} is {@code null} or if it provides {@code null} as a value.
     * @throws X                    if no other case matched.
     */
    public abstract <X extends Throwable> void otherwiseThrow(Supplier<X> exSupplier) throws X, NullPointerException;
}
