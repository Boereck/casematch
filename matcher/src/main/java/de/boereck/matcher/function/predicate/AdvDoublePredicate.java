package de.boereck.matcher.function.predicate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;

/**
 * This extended DoublePredicate provides more default methods as boolean operators.
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
 */
@FunctionalInterface
public interface AdvDoublePredicate extends DoublePredicate {


    /**
     * Applies this predicate as a precondition for the given function. The returned function will return an empty
     * optional if this predicate evaluates to {@code false} for an input. Otherwise the returned function will return
     * an optional holding the output of the wrapped function {@code f} for the given input.
     * @param f wrapped mapping function. It will only be called if the predicate returns {@code true} for a given input.
     * @param <O> Type of output object of the wrapped function.
     * @return Function that will either return an empty optional (if this predicate returns {@code false} for an input),
     *  or an optional holding the output of function {@code f} (if this predicate returns {@code true} for an input).
     * @throws NullPointerException if {@code f} is {@code null}.
     */
    default <O> DoubleFunction<Optional<O>> preFor(DoubleFunction<O> f) throws NullPointerException {
        Objects.requireNonNull(f);
        return i -> this.test(i) ? Optional.ofNullable(f.apply(i)) : Optional.empty();
    }

    /**
     * Returns a predicate, returning the logical short-circuiting XOR of the outputs of this predicate and predicate {@code that}.
     * <br/>This is the truth table of the returned predicate:<br/><br/>
     *
     * <table class="truthtable">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="false"/>
     *         <td class="false"/>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="true"/>
     *         <td class="true"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="false"/>
     *         <td class="true"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="true"/>
     *         <td class="false"/>
     *     </tr>
     * </table>
     * @param that other predicate that returns that second argument of the logical XOR operation performed by the
     *             returned predicate.
     * @return predicate performing a logical XOR operation with the result of {@code this} predicate and {@code that}
     *  predicate.
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    default AdvDoublePredicate xor(DoublePredicate that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> this.test(i) ^ that.test(i);
    }

    /**
     * Returns a predicate that computes the logical NOR or of the outputs of this predicate and predicate {@code that}.
     * This is the truth table of the returned predicate: <br/><br/>
     *
     * <table class="truthtable">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="false"/>
     *         <td class="true"/>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="true"/>
     *         <td class="false"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="false"/>
     *         <td class="false"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="true"/>
     *         <td class="false"/>
     *     </tr>
     * </table>
     *
     * @param that other predicate that returns that second argument of the logical NOR operation performed by the
     *             returned predicate.
     * @return predicate performing a logical NOR operation with the result of {@code this} predicate and {@code that}
     *  predicate.
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    default AdvDoublePredicate nor(DoublePredicate that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> !this.test(i) && !that.test(i);
    }

    /**
     * Returns a predicate that provides the logical equality (XNOR) of this predicate and {@code that} predicate.
     * This means the returned predicate will return {@code true} if the output of this predicate and predicate
     * {@code that} provide the same output for the same input object. For all other
     * inputs the returned predicate will return {@code false}. <br/>
     * This is the truth table of the returned predicate:<br/><br/>
     *
     * <table class="truthtable">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="false"/>
     *         <td class="true"/>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="true"/>
     *         <td class="false"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="false"/>
     *         <td class="false"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="true"/>
     *         <td class="true"/>
     *     </tr>
     * </table>
     * @param that other predicate that returns that second argument of the logical XNOR operation performed by the
     *             returned predicate.
     * @return predicate performing a logical NOR operation with the result of {@code this} predicate and {@code that}
     *  predicate.
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    default AdvDoublePredicate xnor(DoublePredicate that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> this.test(i) == that.test(i);
    }

    /**
     * Returns a predicate that provides the logical implication of the output
     * of this predicate and the {@code other} predicate.
     * <br/>This is the truth table of the returned predicate:<br/><br/>
     * <table class="truthtable">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="false"/>
     *         <td class="true"/>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="true"/>
     *         <td class="true"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="false"/>
     *         <td class="false"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="true"/>
     *         <td class="true"/>
     *     </tr>
     * </table>
     * @param that other predicate that returns that second argument of the logical implication performed by the
     *             returned predicate.
     * @return predicate performing a logical implication with the result of {@code this} predicate and {@code that}
     *  predicate.
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    default AdvDoublePredicate implies(DoublePredicate that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> !this.test(i) || that.test(i);
    }

    /**
     * Provides a predicate, returning the logical short-circuiting AND of the outputs of this predicate and predicate {@code that}.
     * <br/>This is the truth table of the returned predicate:<br/><br/>
     *
     * <table class="truthtable">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="false"/>
     *         <td class="false"/>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="true"/>
     *         <td class="false"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="false"/>
     *         <td class="false"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="true"/>
     *         <td class="true"/>
     *     </tr>
     * </table>
     * @param that other predicate that returns that second argument of the logical AND operation performed by the
     *             returned predicate.
     * @return  predicate performing a logical AND operation with the result of {@code this} predicate and {@code that}
     *  predicate.
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    @Override
    default AdvDoublePredicate and(DoublePredicate that) throws NullPointerException {
        Objects.requireNonNull(that);
        return i -> test(i) && that.test(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default AdvDoublePredicate negate() {
        return i -> !test(i);
    }

    /**
     * Shortcut for {@link de.boereck.matcher.function.predicate.AdvDoublePredicate#negate()}
     *
     * @return result of {@link AdvDoublePredicate#negate() negate()}.
     */
    default AdvDoublePredicate not() {
        return negate();
    }

    /**
     * Provides a predicate, returning the logical short-circuiting OR of the outputs of this predicate and predicate {@code that}.
     * <br/>This is the truth table of the returned predicate:<br/><br/>
     *
     * <table class="truthtable">
     *     <tr>
     *         <th>Output this</th>
     *         <th>Output that</th>
     *         <th>Output returned</th>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="false"/>
     *         <td class="true"/>
     *     </tr>
     *     <tr>
     *         <td class="false"/>
     *         <td class="true"/>
     *         <td class="false"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="false"/>
     *         <td class="false"/>
     *     </tr>
     *     <tr>
     *         <td class="true"/>
     *         <td class="true"/>
     *         <td class="false"/>
     *     </tr>
     * </table>
     * @see DoublePredicate#or(DoublePredicate)
     * @return predicate performing a logical OR operation with the result of {@code this} predicate and {@code that}
     *  predicate
     * @throws NullPointerException if {@code that} is {@code null}.
     */
    @Override
    default AdvDoublePredicate or(DoublePredicate that) throws NullPointerException {
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
    default DoublePredicate requires(DoublePredicate that) throws NullPointerException {
        Objects.requireNonNull(that);
        return that.and(this)::test;
    }
}
