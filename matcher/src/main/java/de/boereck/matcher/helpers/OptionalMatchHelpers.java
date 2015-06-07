package de.boereck.matcher.helpers;

import de.boereck.matcher.function.optionalmap.OptionalDoubleMapper;
import de.boereck.matcher.function.optionalmap.OptionalIntMapper;
import de.boereck.matcher.function.optionalmap.OptionalLongMapper;
import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

import java.util.*;
import java.util.function.*;

/**
 * Provides static helper functions for defining matches in CaseMatchers based on Optional values.
 * <p>
 * This class is not intended to be instantiated or sub-classed.
 * </p>
 *
 * @author Max Bureck
 */
public class OptionalMatchHelpers {

    private OptionalMatchHelpers() {
        throw new IllegalStateException();
    }

    /**
     * Function that can be used to take any Optional in a match and returns this as
     * an Optional&lt;Object&gt;. This cast is safe, since the value can only be read from
     * the Optional. If the input optional to the function is {@code null}, an empty optional
     * will be returned.
     */
    public static final OptionalMapper<Optional<?>,Object> some =
            i -> i == null ? Optional.empty() : (Optional)i;

    /**
     * Returns a function that is the identity function for Optional values,
     * but returns an empty Optional for a {@code null} input value. This is especially
     * usefully when trying to match over an optional value. This can be used to extract
     * the value from the optional in a caseObj case definition.
     * @param <I> type of element of optional
     * @return function that will return the same Optional it gets as input, as long as the input
     * is not {@code null}. On {@code null} input returns an empty Optional.
     */
    public static final <I> OptionalMapper<Optional<I>,I> some() {
        return i -> i == null ? Optional.<I>empty() : i;
    }

    /**
     * This is a small wrapper around {@link OptionalMatchHelpers#some()}, but takes
     * a class as parameter that is not used but simply used to define the generic type.
     * This can be usefull if further combinators are used on the returned OptionalMapper.
     * @see OptionalMatchHelpers#some()
     * @return result of call to {@link OptionalMatchHelpers#some()}.
     *
     */
    public static final <I> OptionalMapper<Optional<I>,I> some(Class<I> clazz) {
        return some();
    }

    /**
     * Returns a function that will return the input Optional filtered with the given predicate.
     * If the input object is {@code null} the function returns an empty Optional.
     * @param p predicate used to filter input Optional
     * @param <I> type of element held by optional
     * @return function returning the input optional filtered by predicate {@code p}, if the input is not {@code null}.
     *  If the input is {@code null}, an empty optional will be returned.
     * @throws NullPointerException if {@code p} is {@code null}.
     */
    public static final <I> OptionalMapper<Optional<I>,I> some(Predicate<I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return i -> i == null ? Optional.<I>empty() : i.filter(p);
    }

    /**
     * Returns a function that will map the content of the input optional to another optional and returns this optional.
     * If the input optional to the returned function is {@code null}, the function will return an empty optional.
     * @param extractor mapping from the value of the iput optional to another optional, being returned from the
     *                  result function.
     * @param <I> Type of object held by input optional
     * @param <O> Type of object held by output optional
     * @return function mapping from one option based on function {@code extractor} to another optional that is returned.
     * @throws NullPointerException if {@code extractor} is {@code null}.
     */
    public static final <I,O> OptionalMapper<Optional<I>,O> someFlat(Function<I, Optional<O>> extractor) throws NullPointerException {
        Objects.requireNonNull(extractor);
        return i -> i == null ? Optional.<O>empty() : i.flatMap(extractor);
    }

    /**
     * Returns a function that will map the content of the input optional, by calling  {@link Optional#map(Function) map}
     * on the input optional. If the input optional is {@code null}, an empty optional is being returned.
     * @param mapper will be passed as input parameter to the map function of the input optional to the returned function.
     * @param <I> Type of object held by input optional
     * @param <O> Type of object held by output optional
     * @return function mapping the value of the input option based {@code extractor} to another value and returns the resulting optional.
     * @throws NullPointerException if {@code mapper} is {@code null}.
     */
    public static final <I,O> OptionalMapper<Optional<I>,O> someMap(Function<I, O> mapper) throws NullPointerException {
        Objects.requireNonNull(mapper);
        return i -> i == null ? Optional.<O>empty() : i.map(mapper);
    }

    /**
     * Predicate checking if an Optional is either null or empty.
     */
    public static final AdvPredicate<Optional<?>> none = o -> o == null || !o.isPresent();

    /**
     * Function that can be used to take an OptionalInt in a match and returns this again.
     * If the input optional to the function is {@code null}, an empty optional will be returned.
     */
    public static final OptionalIntMapper<OptionalInt> someI =
            i -> i == null ? OptionalInt.empty() : i;


    /**
     * Returns a function that will return the input OptionalInt filtered with the given predicate.
     * If the input object is {@code null} the function returns an empty OptionalInt.
     * @param p predicate used to filter input OptionalInt
     * @param <I> type of element held by optional
     * @return function returning the input optional filtered by predicate {@code p}, if the input is not {@code null}.
     *  If the input is {@code null}, an empty optional will be returned.
     * @throws NullPointerException if {@code p} is {@code null}.
     */
    public static final <I> OptionalIntMapper<OptionalInt> someI(IntPredicate p) throws NullPointerException {
        Objects.requireNonNull(p);
        return i -> {
            if(i == null) {
                return OptionalInt.empty();
            }
            if(i.isPresent() && p.test(i.getAsInt())){
                return i;
            } else {
                return OptionalInt.empty();
            }
        };
    }

    /**
     * Predicate checking if an OptionalInt is either {@code null} or empty.
     */
    public static final AdvPredicate<OptionalInt> noneI = o -> o == null || !o.isPresent();

    /**
     * Function that can be used to take an OptionalLong in a match and returns this again.
     * If the input optional to the function is {@code null}, an empty optional will be returned.
     */
    public static final OptionalLongMapper<OptionalLong> someL =
            i -> i == null ? OptionalLong.empty() : i;


    /**
     * Returns a function that will return the input OptionalLong filtered with the given predicate.
     * If the input object is {@code null} the function returns an empty OptionalLong.
     * @param p predicate used to filter input OptionalLong
     * @param <I> type of element held by optional
     * @return function returning the input optional filtered by predicate {@code p}, if the input is not {@code null}.
     *  If the input is {@code null}, an empty optional will be returned.
     * @throws NullPointerException if {@code p} is {@code null}.
     */
    public static final <I> OptionalLongMapper<OptionalLong> someL(LongPredicate p) throws NullPointerException {
        Objects.requireNonNull(p);
        return i -> {
            if(i == null) {
                return OptionalLong.empty();
            }
            if(i.isPresent() && p.test(i.getAsLong())){
                return i;
            } else {
                return OptionalLong.empty();
            }
        };
    }

    /**
     * Predicate checking if an OptionalLong is either {@code null} or empty.
     */
    public static final AdvPredicate<OptionalLong> noneL = o -> o == null || !o.isPresent();

    /**
     * Function that can be used to take an OptionalDouble in a match and returns this again.
     * If the input optional to the function is {@code null}, an empty optional will be returned.
     */
    public static final OptionalDoubleMapper<OptionalDouble> someD =
            i -> i == null ? OptionalDouble.empty() : i;


    /**
     * Returns a function that will return the input OptionalDouble filtered with the given predicate.
     * If the input object is {@code null} the function returns an empty OptionalDouble.
     * @param p predicate used to filter input OptionalDouble
     * @param <I> type of element held by optional
     * @return function returning the input optional filtered by predicate {@code p}, if the input is not {@code null}.
     *  If the input is {@code null}, an empty optional will be returned.
     * @throws NullPointerException if {@code p} is {@code null}.
     */
    public static final <I> OptionalDoubleMapper<OptionalDouble> someD(DoublePredicate p) throws NullPointerException {
        Objects.requireNonNull(p);
        return i -> {
            if(i == null) {
                return OptionalDouble.empty();
            }
            if(i.isPresent() && p.test(i.getAsDouble())){
                return i;
            } else {
                return OptionalDouble.empty();
            }
        };
    }

    /**
     * Predicate checking if an OptionalDouble is either {@code null} or empty.
     */
    public static final AdvPredicate<OptionalDouble> noneD = o -> o == null || !o.isPresent();

}
