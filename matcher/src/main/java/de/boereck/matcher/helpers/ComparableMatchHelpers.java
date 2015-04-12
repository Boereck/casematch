package de.boereck.matcher.helpers;

import de.boereck.matcher.function.predicate.AdvPredicate;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * This class provides static methods that can be used in defining matches for case matches on {@link Comparable}
 * instances. This class is not intended to be instantiated or sub-classed.
 */
public class ComparableMatchHelpers {

    private ComparableMatchHelpers() {
        throw new IllegalStateException("Class ComparableMatchHelpers must not be instantiated");
    }

    // TODO comparison with Comparator

    /**
     * Returns a predicate checking if a comparable is smaller than the given
     * instance {@code t}.
     * @param t instance that the predicate compares against. This object must not be {@code null}.
     * @param <T> Type of object that will be compared with.
     * @return Predicate checking if objects are less thant {@code t}.
     * @throws NullPointerException will be thrown if {@code t} is {@code null}.
     */
    public static <T> AdvPredicate<Comparable<? super T>> lessThan(T t) throws NullPointerException {
        Objects.requireNonNull(t);
        return c -> c.compareTo(t) < 0;
    }

    /**
     * Returns a predicate checking if a comparable is bigger than the given
     * instance {@code t}.
     * @param t instance that the predicate compares against. This object must not be {@code null}.
     * @param <T> Type of object that will be compared with.
     * @return Predicate checking if objects are greater thant {@code t}.
     * @throws NullPointerException will be thrown if {@code t} is {@code null}.
     */
    public static <T> AdvPredicate<Comparable<? super T>> greaterThan(T t) throws NullPointerException {
        Objects.requireNonNull(t);
        return c -> c.compareTo(t) > -1;
    }

    /**
     * Returns a predicate checking if a comparable is equal to (checking via {@link Comparable#compareTo(Object) compareTo},
     * not via equals method) the given instance {@code t}.
     * @param t instance that the predicate compares against. This object must not be {@code null}.
     * @param <T> Type of object that will be compared with.
     * @return Predicate checking if objects are equal to {@code t}.
     * @throws NullPointerException will be thrown if {@code t} is {@code null}.
     */
    public static <T> AdvPredicate<Comparable<? super T>> equalTo(T t) throws NullPointerException {
        Objects.requireNonNull(t);
        return c -> c.compareTo(t) == 0;
    }

}
