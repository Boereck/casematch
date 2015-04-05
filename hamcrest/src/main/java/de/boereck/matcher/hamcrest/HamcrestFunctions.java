package de.boereck.matcher.hamcrest;

import java.util.function.Predicate;

import org.hamcrest.Matcher;

public final class HamcrestFunctions {

    private HamcrestFunctions() {
        throw new IllegalStateException("Class HamcrestFunctions must not me instantiated");
    }
    
    static <T> Predicate<T> is(Matcher<T> matcher) {
        return matcher::matches;
    }
    
    static <T> Predicate<T> when(Matcher<T> matcher) {
        return matcher::matches;
    }
    
}
