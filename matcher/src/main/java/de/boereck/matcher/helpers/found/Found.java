package de.boereck.matcher.helpers.found;

/**
 * This class represents a result of searching for elements, e.g. in a collection,
 * that match a criterion.
 * <br><br>
 * If all elements in the search satisfy the condition, the Found must be instance of {@link FoundAll}.
 * Method {@link Found#type() type()} will return {@link FindType#all} will be returned in this case.
 * To create an instance of FoundAll, call method {@link Found#all(long)}.
 * <br><br>
 * If only elements in the search satisfy the condition, the Found must be instance of {@link FoundSome}.
 * Method {@link Found#type() type()} will return {@link FindType#all} will be returned in this case.
 * To create an instance of FoundSome, call method {@link Found#some(long)}.
 * <br><br>
 * If no element of the search satisfies the condition, the Found must be instance of {@link FoundNone}.
 * Method {@link Found#type() type()} will return {@link FindType#none} in this case.
 * If no element was checked, the result must still be none, even though no element was checked negative.
 * A singleton instance of FoundNone can be accessed via the static field {@link Found#NONE}.
 */
public abstract class Found {

    /**
     * Package protected constructor, so that no further classes can be defined
     */
    Found(){}

    public static final Found NONE = FoundNone.INSTANCE;

    /**
     *
     * @param count must be &gt; 0
     * @return instance of FoundSome with the given find count.
     * @throws IllegalArgumentException thrown if {@code count &lt;= 0 }
     */
    public static FoundSome some(long count) {
        if(count <= 0) {
            throw new IllegalArgumentException();
        }
        return new FoundSome(count);
    }

    /**
     *
     * @param count must be &gt; 0
     * @return instance of FoundAll with the given amount of elements
     * @throws IllegalArgumentException thrown if {@code count &lt;= 0 }
     */
    public static FoundAll all(long count) {
        if(count <= 0) {
            throw new IllegalArgumentException();
        }
        return new FoundAll(count);
    }

    /**
     * Enumeration of the find type. This type must match the
     * class of the Found instance. E.g. if this method returns
     * {@link FindType#some}, this instance must be instance of
     * {@link FoundSome}.
     * @return find type of this find.
     */
    public abstract FindType type();

    /**
     * Returns the amount of found elements.
     * @return amount of found elements.
     */
    public abstract long count();

    // TODO new class FoundHelpers with the following static methods:
    // TODO public static Predicate<Found> foundAtLeast(count n) ...
    // TODO public static Predicate<Found> foundAtMost(count n) ...
    // TODO public static Predicate<Found> foundAny(LongPredicate p) = ...
    // TODO public static Predicate<Found> foundSome(LongPredicate p) = f -> f.count() > 0 && p.test(f.count())
    // TODO public static Predicate<Found> foundAll(LongPredicate p) = ...
    // TODO public static OptionalLongMapper<Found> foundCount = ...
    // TODO public static OptionalLongMapper<Found> foundCount(LongPredicate p) = ...
}
