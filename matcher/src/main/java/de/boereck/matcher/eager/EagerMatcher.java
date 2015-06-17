package de.boereck.matcher.eager;

import de.boereck.matcher.NoResultCaseMatcher;
import de.boereck.matcher.NoResultDoubleCaseMatcher;
import de.boereck.matcher.NoResultIntCaseMatcher;
import de.boereck.matcher.NoResultLongCaseMatcher;
import de.boereck.matcher.ResultCaseMatcher;
import de.boereck.matcher.ResultDoubleCaseMatcher;
import de.boereck.matcher.ResultIntCaseMatcher;
import de.boereck.matcher.ResultLongCaseMatcher;

/**
 * This class provides static methods to create matchers that can be used to perform checks on a given object or data type
 * and perform an action if a check succeeds or when no check matched.
 * <p>
 * All de.boereck.matcher implementations provided by methods of this class work eager, so all cases are evaluated one after the other
 * (in the order they are specified) directly when the case methods are called. After a found, no further checks provided in
 * from of predicates or functions are evaluated anymore. It is not needed to call a closing method on the provided
 * CaseMatches, if no result is required.
 * </p>
 * <p>
 * Advice: if you call methods to get/create Predicates or BooleanSuppliers for caseOf methods, better cache them in static
 * fields where possible. This prevents creating the same objects over and over.
 * </p>
 * <p>
 * This class is not intended to be instantiated or inherited from.
 * </p>
 *
 * @author Max Bureck
 */
public final class EagerMatcher {

    private EagerMatcher() {
        throw new IllegalStateException("Class CaseMatcher must not me instantiated");
    }

    /**
     * This method will return a {@link EagerNoResultCaseMatcher} object for the given input object. It allows definition of cases
     * and associated actions. The cases will be evaluated eagerly when being defined and in order of their definition. After
     * a matching case was found, no further case checks will be evaluated. Due to the eager evaluation it is not needed to
     * use a closing method to start evaluation.
     *
     * @param <I> Type of match input object
     * @param o object to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static <I> EagerNoResultCaseMatcher<I> match(I o) {
        return new NoResultCaseMatcherUnfinished<I>(o);
    }

    /**
     * Returns a {@link EagerResultCaseMatcher} for the given input object. It allows definition of cases and associated
     * functions, that will provide a value returned on closing methods. The cases will be evaluated eagerly when being
     * defined and in order of their definition. After a matching case and therefore result value was found, no further case
     * checks will be evaluated.
     *
     * @param <I> Type of match input object
     * @param i object to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static <I, O> EagerResultCaseMatcher<I, O> resultMatch(I i) {
        return new ResultCaseMatcherUnfinished<I, O>(i);
    }

    /**
     * Returns a {@link EagerResultCaseMatcher} for the given input object. It allows definition of cases and associated
     * functions, that will provide a value returned on closing methods. The type of the returned object can be defined by
     * parameter {@code outputType}. The cases will be evaluated eagerly when being defined and in order of their definition.
     * After a matching case and therefore result value was found, no further case checks will be evaluated.
     *
     * @param <I> Type of match input object
     * @param ouptutType expected type of the result object, that can be retrieved via closing methods.
     * @param i          object to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static <I, O> EagerResultCaseMatcher<I, O> resultMatch(Class<O> ouptutType, I i) {
        return resultMatch(i);
    }

    /**
     * This method will return a {@link NoResultIntCaseMatcher} object for the given input int value. It allows definition of
     * cases and associated actions. The cases will be evaluated eagerly when being defined and in order of their definition.
     * After a matching case was found, no further case checks will be evaluated. Due to the eager evaluation it is not
     * needed to use a closing method to start evaluation.
     *
     * @param i int value to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static EagerNoResultIntCaseMatcher match(int i) {
        return new NoResultIntCaseMatcherUnfinished(i);
    }

    /**
     * Returns a {@link EagerResultIntCaseMatcher} for the given input int value. It allows definition of cases and associated
     * functions, that will provide a value returned on closing methods. The cases will be evaluated eagerly when being
     * defined and in order of their definition. After a matching case and therefore result value was found, no further case
     * checks will be evaluated.
     *
     * @param <O> Type of match result object
     * @param i int value to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static <O> EagerResultIntCaseMatcher<O> resultMatch(int i) {
        return new ResultIntCaseMatcherUnfinished<O>(i);
    }

    /**
     * Returns a {@link EagerResultIntCaseMatcher} for the given input int value. It allows definition of cases and associated
     * functions, that will provide a value returned on closing methods. The type of the returned object can be defined by
     * parameter {@code outputType}. The cases will be evaluated eagerly when being defined and in order of their definition.
     * After a matching case and therefore result value was found, no further case checks will be evaluated.
     *
     * @param <O> Type of match result object
     * @param outputType expected type of the result object, that can be retrieved via closing methods.
     * @param i          int value to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static <O> EagerResultIntCaseMatcher<O> resultMatch(Class<O> outputType, int i) {
        return resultMatch(i);
    }

    /**
     * This method will return a {@link EagerNoResultLongCaseMatcher} object for the given input long value. It allows definition of
     * cases and associated actions. The cases will be evaluated eagerly when being defined and in order of their definition.
     * After a matching case was found, no further case checks will be evaluated. Due to the eager evaluation it is not
     * needed to use a closing method to start evaluation.
     *
     * @param l long value to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static EagerNoResultLongCaseMatcher match(long l) {
        return new NoResultLongCaseMatcherUnfinished(l);
    }

    /**
     * Returns a {@link EagerResultLongCaseMatcher} for the given input long value. It allows definition of cases and associated
     * functions, that will provide a value returned on closing methods. The cases will be evaluated eagerly when being
     * defined and in order of their definition. After a matching case and therefore result value was found, no further case
     * checks will be evaluated.
     *
     * @param <O> Type of match result object
     * @param l long value to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static <O> EagerResultLongCaseMatcher<O> resultMatch(long l) {
        return new ResultLongCaseMatcherUnfinished<O>(l);
    }

    /**
     * Returns a {@link EagerResultLongCaseMatcher} for the given input long value. It allows definition of cases and associated
     * functions, that will provide a value returned on closing methods. The type of the returned object can be defined by
     * parameter {@code outputType}. The cases will be evaluated eagerly when being defined and in order of their definition.
     * After a matching case and therefore result value was found, no further case checks will be evaluated.
     *
     * @param <O> Type of match result object
     * @param outputType expected type of the result object, that can be retrieved via closing methods.
     * @param l          long value to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static <O> EagerResultLongCaseMatcher<O> resultMatch(Class<O> outputType, long l) {
        return resultMatch(l);
    }

    /**
     * This method will return a {@link NoResultDoubleCaseMatcher} object for the given input double value. It allows definition of
     * cases and associated actions. The cases will be evaluated eagerly when being defined and in order of their definition.
     * After a matching case was found, no further case checks will be evaluated. Due to the eager evaluation it is not
     * needed to use a closing method to start evaluation.
     *
     * @param d double value to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static EagerNoResultDoubleCaseMatcher match(double d) {
        return new NoResultDoubleCaseMatcherUnfinished(d);
    }

    /**
     * Returns a {@link ResultDoubleCaseMatcher} for the given input double value. It allows definition of cases and associated
     * functions, that will provide a value returned on closing methods. The cases will be evaluated eagerly when being
     * defined and in order of their definition. After a matching case and therefore result value was found, no further case
     * checks will be evaluated.
     *
     * @param <O> Type of match result object
     * @param d double value to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static <O> EagerResultDoubleCaseMatcher<O> resultMatch(double d) {
        return new ResultDoubleCaseMatcherUnfinished<O>(d);
    }

    /**
     * Returns a {@link EagerResultDoubleCaseMatcher} for the given input double value. It allows definition of cases and associated
     * functions, that will provide a value returned on closing methods. The type of the returned object can be defined by
     * parameter {@code outputType}. The cases will be evaluated eagerly when being defined and in order of their definition.
     * After a matching case and therefore result value was found, no further case checks will be evaluated.
     *
     * @param outputType expected type of the result object, that can be retrieved via closing methods.
     * @param d          double value to find a matching case for
     * @return CaseMatcher object to define cases on that will be checked immediately.
     */
    public static <O> EagerResultDoubleCaseMatcher<O> resultMatch(Class<O> outputType, double d) {
        return resultMatch(d);
    }
}
