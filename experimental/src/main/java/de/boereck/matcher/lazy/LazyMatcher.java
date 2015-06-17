package de.boereck.matcher.lazy;

import java.util.Optional;
import java.util.function.Function;

import static de.boereck.matcher.helpers.StringMatchHelpers.matches;

/**
 * Created by mbu on 16.06.2015.
 */
public class LazyMatcher {



    public static <I,O> LazyResultCaseMatcher<I,O> lazyResultMatch(Class<I> inType, Class<O> outType) {
        return lazyResultMatch();
    }

    public static <I,O> LazyResultCaseMatcher<I,O> lazyResultMatch() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
