package de.boereck.matcher.helpers;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;

public final class LongMatchHelpers {

    private LongMatchHelpers() {
        throw new IllegalStateException("Class IntMatchHelpers must not be instantiated");
    }

    public static LongPredicate gt(long compareWith) {
        return l -> l > compareWith;
    }

    public static LongPredicate gt(LongSupplier compareWith) {
        return l -> l > compareWith.getAsLong();
    }

    public static LongPredicate lt(long compareWith) {
        return l -> l < compareWith;
    }

    public static LongPredicate lt(LongSupplier compareWith) {
        return i -> i < compareWith.getAsLong();
    }

    public static LongPredicate positive() {
        return l -> l > 0;
    }

    public static LongPredicate negative() {
        return l -> l < 0;
    }

    public static LongPredicate devidableBy(long divisor) {
        return l -> (l % divisor) == 0;
    }

    /**
     * 
     * @param startIncluding
     * @param endIncluding
     * @return
     */
    public static LongPredicate inClosedRange(long startIncluding, long endIncluding) {
        if (startIncluding > endIncluding) {
            throw new InvalidParameterException("startIncluding must be <= endIncluding");
        }
        return l -> (l >= startIncluding && l <= endIncluding);
    }

    public static LongPredicate allExcept(long excluding, long... excludingMore) {
        Objects.requireNonNull(excluding);
        final int excludingMoreLen = excludingMore.length;
        // if we have just one exclude, we can have a simple check
        if (excludingMoreLen == 0) {
            return l -> l != excluding;
        } else {
            // defensive copy of parameters
            final long[] exclude = Arrays.copyOf(excludingMore, excludingMoreLen + 1);
            // add first parameter to new array, so we have only one array as search source
            exclude[excludingMoreLen] = excluding;
            // sort for faster find using binary search
            Arrays.sort(exclude);
            return i -> (Arrays.binarySearch(exclude, i) < 0);
        }
    }
}
