package de.boereck.matcher.function.predicate;

import de.boereck.matcher.function.optionalmap.OptionalMapper;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This extended {@link Predicate} provides more default methods as boolean operators.
 * <style type="text/css">
 *     .truthtable, .truthtable td, .truthtable th {
 *         border: 1px solid black;
 *         border-collapse: collapse;
 *     }
 *     .false {
 *         background-color:LightPink;
 *     }
 *     .false::after {
 *         content: 'false';
 *     }
 *     .true {
 *         background-color:LightGreen;
 *     }
 *     .true::after {
 *         content: 'true';
 *     }
 * </style>
 * @author Max Bureck
 * @param <I> type of input to predicate
 */
@FunctionalInterface
public interface AdvPredicate<I> extends Predicate<I> {

    // TODO add exception guarantees to ifThen and ifThenFlat

    /**
     * Applies this predicate as a precondition for the given function {@code f}. The returned function will return an empty
     * optional if this predicate evaluates to {@code false} for an input. Otherwise the returned function will return
     * an optional holding the output of the wrapped function {@code f} for the given input.
     * @param f wrapped mapping function. It will only be called if the predicate returns {@code true} for a given input.
     * @param <O> Type of output object of the wrapped function.
     * @return Function that will either return an empty optional (if this predicate returns {@code false} for an input),
     *  or an optional holding the output of function {@code f} (if this predicate returns {@code true} for an input).
     * @throws NullPointerException if {@code f} is {@code null}.
     */
    default <O> OptionalMapper<I,O> ifThen(Function<? super I, ? extends O> f) throws NullPointerException {
        Objects.requireNonNull(f);
        return i -> this.test(i) ? Optional.ofNullable(f.apply(i)) : Optional.empty();
    }

    /**
     * Applies this predicate as a precondition for the given function {@code f}. The returned function will return an empty
     * optional if this predicate evaluates to {@code false} for an input. Otherwise the returned function will return
     * the optional returned by the wrapped function {@code f} for the given input.
     * @param f wrapped mapping function. It will only be called if the predicate returns {@code true} for a given input.
     * @param <O> Type of value held by optional-output of the wrapped function.
     * @return Function that will either return an empty optional (if this predicate returns {@code false} for an input),
     *  or the optional returned by function {@code f} (if this predicate returns {@code true} for an input).
     * @throws NullPointerException if {@code f} is {@code null}.
     */
    default <O> OptionalMapper<I,O> ifThenFlat(Function<? super I, Optional<O>> f)throws NullPointerException {
        Objects.requireNonNull(f);
        return i -> this.test(i) ? f.apply(i) : Optional.empty();
    }

    /**
     * Returns a predicate, returning the logical XOR of the outputs of this predicate and predicate {@code that}.
     * <p>Any exceptions thrown during evaluation of either predicate are thrown to the caller of the returned combined
     * predicate. If this predicate throws an Exception {@code that} predicate will not be evaluated.</p>
     * This is the truth table of the returned predicate:<br><br>
     *
     * <table class="truthtable" summary="Truthtable for XOR">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="false"></td>
     *         <td class="true"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *         <td class="false"></td>
     *     </tr>
     * </table>
     * @param that other predicate that returns that second argument of the logical XOR operation performed by the
     *             returned predicate.
     * @return predicate performing a logical XOR operation with the result of {@code this} predicate and {@code that}
     *  predicate.
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    default AdvPredicate<I> xor(Predicate<? super I> that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> this.test(i) ^ that.test(i);
    }

    /**
     * Returns a predicate that computes the logical NOR or of the outputs of this predicate and predicate {@code that}.
     * <p>Any exceptions thrown during evaluation of either predicate are thrown to the caller of the returned combined
     * predicate. If this predicate throws an Exception {@code that} predicate will not be evaluated.</p>
     * This is the truth table of the returned predicate: <br><br>
     *
     * <table class="truthtable" summary="Truthtable for NOR">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *         <td class="true"></td>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="true"></td>
     *         <td class="false"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *         <td class="false"></td>
     *     </tr>
     * </table>
     *
     * @param that other predicate that returns that second argument of the logical NOR operation performed by the
     *             returned predicate.
     * @return predicate performing a logical NOR operation with the result of {@code this} predicate and {@code that}
     *  predicate.
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    default AdvPredicate<I> nor(Predicate<? super I> that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> !this.test(i) && !that.test(i);
    }

    /**
     * Returns a predicate that provides the logical equality (XNOR) of this predicate and {@code that} predicate.
     * This means the returned predicate will return {@code true} if the output of this predicate and predicate
     * {@code that} provide the same output for the same input object. For all other
     * inputs the returned predicate will return {@code false}. <br>
     * <p>Any exceptions thrown during evaluation of either predicate are thrown to the caller of the returned combined
     * predicate. If this predicate throws an Exception {@code that} predicate will not be evaluated.</p>
     * This is the truth table of the returned predicate:<br><br>
     *
     * <table class="truthtable" summary="Truthtable for XNOR">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *         <td class="true"></td>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="true"></td>
     *         <td class="false"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *     </tr>
     * </table>
     * @param that other predicate that returns that second argument of the logical XNOR operation performed by the
     *             returned predicate.
     * @return predicate performing a logical XNOR operation with the result of {@code this} predicate and {@code that}
     *  predicate.
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    default AdvPredicate<I> xnor(Predicate<? super I> that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> this.test(i) == that.test(i);
    }

    /**
     * Returns a predicate that provides the logical implication of the output of this predicate and the {@code other}
     * predicate.
     * <p>Any exceptions thrown during evaluation of either predicate are thrown to the caller of the returned combined
     * predicate. If this predicate throws an Exception {@code that} predicate will not be evaluated.</p>
     * This is the truth table of the returned predicate:<br><br>
     * <table class="truthtable" summary="Truthtable for Implication">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *         <td class="true"></td>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *     </tr>
     * </table>
     * @param that other predicate that returns that second argument of the logical implication performed by the
     *             returned predicate.
     * @return predicate performing a logical implication with the result of {@code this} predicate and {@code that}
     *  predicate.
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    default AdvPredicate<I> implies(Predicate<? super I> that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> !this.test(i) || that.test(i);
    }

    /**
     * Provides a predicate, returning the logical short-circuiting AND of the outputs of this predicate and predicate {@code that}.
     * <p>Any exceptions thrown during evaluation of either predicate are thrown to the caller of the returned combined
     * predicate. If this predicate throws an Exception {@code that} predicate will not be evaluated.</p>
     * This is the truth table of the returned predicate:<br><br>
     *
     * <table class="truthtable" summary="Truthtable for AND">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="true"></td>
     *         <td class="false"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *     </tr>
     * </table>
     * @param that other predicate that returns that second argument of the logical AND operation performed by the
     *             returned predicate.
     * @return  predicate performing a logical AND operation with the result of {@code this} predicate and {@code that}
     *  predicate.
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    @Override
    default AdvPredicate<I> and(Predicate<? super I> that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> test(i) && that.test(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default AdvPredicate<I> negate() {
        return i -> !test(i);
    }

    /**
     * Shortcut for {@link de.boereck.matcher.function.predicate.AdvPredicate#negate()}
     *
     * @return result of {@link AdvPredicate#negate() negate()}.
     */
    default AdvPredicate<I> not() {
        return negate();
    }

    /**
     * Provides a predicate, returning the logical short-circuiting OR of the outputs of this predicate and predicate {@code that}.
     * <p>Any exceptions thrown during evaluation of either predicate are thrown to the caller of the returned combined
     * predicate. If this predicate throws an Exception {@code that} predicate will not be evaluated.</p>
     * <br>This is the truth table of the returned predicate:<br><br>
     *
     * <table class="truthtable" summary="Truthtable for OR">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *         <td class="false"></td>
     *     </tr>
     *     <tr>
     *         <td class="false"></td>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="false"></td>
     *         <td class="true"></td>
     *     </tr>
     *     <tr>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *         <td class="true"></td>
     *     </tr>
     * </table>
     * @see Predicate#or(Predicate)
     * @return predicate performing a logical OR operation with the result of {@code this} predicate and {@code that}
     *  predicate
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    @Override
    default AdvPredicate<I> or(Predicate<? super I> that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> test(i) || that.test(i);
    }

    /**
     * Defines precondition that has to hold before checking
     * this predicate. The returned predicate is the result of
     * {@code that.and(this)}.
     *
     * @param that precondition for this predicate.
     * @return result of {@code that.and(this)}.
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    default AdvPredicate<I> requires(Predicate<? super I> that) {
        return i -> that.test(i) && this.test(i);
    }

    /**
     * The returned predicate will check if the input is {@code null} and return {@code false}, if so. Otherwise it
     * will call this AdvPredicate and return the result.
     * <p>Any exceptions thrown during evaluation of {@code this} predicate are thrown to the caller of the returned
     * predicate.</p>
     * @return predicate returning {@code false} if the input is {@code null}, otherwise the result of this predicate
     *  with the same input.
     */
    default AdvPredicate<I> nullToFalse() {
        return i -> i != null && this.test(i);
    }

    /**
     * The returned predicate will check if the input is {@code null} and return {@code true}, if so. Otherwise it
     * will call this AdvPredicate and return the result.
     * <p>Any exceptions thrown during evaluation of {@code this} predicate are thrown to the caller of the returned
     * predicate.</p>
     * @return predicate returning {@code true} if the input is {@code null}, otherwise the result of this predicate
     *  with the same input.
     */
    default AdvPredicate<I> nullToTrue() {
        return i -> i == null || this.test(i);
    }
}
