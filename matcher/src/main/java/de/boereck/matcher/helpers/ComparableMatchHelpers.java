package de.boereck.matcher.helpers;

import java.util.Objects;
import java.util.function.Predicate;

public class ComparableMatchHelpers {

    private ComparableMatchHelpers() {
        throw new IllegalStateException("Class ComparableMatchHelpers must not be instantiated");
    }

    // TODO comparison with Comparator

    public static <T> Predicate<Comparable<? super T>> lessThan(T t) {
        Objects.requireNonNull(t);
        return c -> c.compareTo(t) < 0;
    }

    public static <T> Predicate<Comparable<? super T>> greaterThan(T t) {
        Objects.requireNonNull(t);
        return c -> c.compareTo(t) > -1;
    }

    public static <T> Predicate<Comparable<? super T>> equalTo(T t) {
        Objects.requireNonNull(t);
        return c -> c.compareTo(t) == 0;
    }

}
