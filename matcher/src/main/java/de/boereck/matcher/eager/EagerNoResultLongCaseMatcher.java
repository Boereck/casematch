package de.boereck.matcher.eager;

import de.boereck.matcher.NoResultCaseMatcher;
import de.boereck.matcher.NoResultLongCaseMatcher;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;

/**
 * Closing methods by this interface are:
 * <ul>
 * <li> {@link de.boereck.matcher.eager.EagerNoResultLongCaseMatcher#otherwise(java.util.function.LongConsumer) otherwise(Consumer)}</li>
 * <li> {@link de.boereck.matcher.eager.EagerNoResultLongCaseMatcher#otherwiseThrow(java.util.function.Supplier) otherwiseThrow(Supplier)}</li>
 * </ul>
 */
public interface EagerNoResultLongCaseMatcher extends NoResultLongCaseMatcher {

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> EagerNoResultLongCaseMatcher caseOf(long reference, LongConsumer consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultLongCaseMatcher caseOf(LongPredicate p, LongConsumer consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultLongCaseMatcher caseOf(BooleanSupplier s, LongConsumer consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultLongCaseMatcher caseOf(boolean test, LongConsumer consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultLongCaseMatcher caseIs(LongPredicate p, Runnable then) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultLongCaseMatcher caseIs(boolean test, Runnable then) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> EagerNoResultLongCaseMatcher caseObj(LongFunction<Optional<T>> p, Consumer<T> consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultLongCaseMatcher caseInt(LongFunction<OptionalInt> p, IntConsumer consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultLongCaseMatcher caseLong(LongFunction<OptionalLong> p, LongConsumer consumer) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EagerNoResultLongCaseMatcher caseDouble(LongFunction<OptionalDouble> p, DoubleConsumer consumer) throws NullPointerException;

    /////////////////////
    // Closing Methods //
    /////////////////////

    /**
     * The given consumer will be called if all cases were checked and none of them matched. This is a closing method, some
     * implementations of the interface may require an closing method to be called after a sequence of case definitions. The
     * consumer will be called with the input value of the case found.
     *
     * @param consumer will be called with the input object if there was no matching case
     * @throws NullPointerException might be thrown if either parameter {@code consumer} is {@code null}.
     */
    public abstract void otherwise(LongConsumer consumer) throws NullPointerException;

    /**
     * If all cases were checked and there was no found so far, the given supplier will be called and the given throwable
     * will be thrown. This is a closing method, some implementations of the interface may require an closing method to be
     * called after a sequence of case definitions. The consumer will be called with the input object of the case found.
     *
     * @param exSupplier supplier of the exception to be thrown. For exceptions with parameterless constructors a method reference
     *                   can be used. E.g. {@code MyException::new}.
     * @throws NullPointerException might be thrown if either parameter {@code exSupplier} is {@code null}.
     */
    public abstract <X extends Throwable> void otherwiseThrow(Supplier<X> exSupplier) throws X, NullPointerException;
}
