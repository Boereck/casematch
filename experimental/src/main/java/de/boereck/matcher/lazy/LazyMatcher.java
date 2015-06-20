package de.boereck.matcher.lazy;

import java.util.Optional;
import java.util.function.Function;

import static de.boereck.matcher.helpers.StringMatchHelpers.matches;

/**
 * Created by mbu on 16.06.2015.
 */
public class LazyMatcher {

    /**
     * Alternatively to this method, the static methods of {@link LazyResultMatcher} can be used to create instances
     * of {@link LazyResultCaseMatcher}.
     *
     * @param inType
     * @param outType
     * @param <I>
     * @param <O>
     * @return
     * @see LazyResultMatcher
     */
    public static <I,O> LazyResultCaseMatcher<I,O> lazyResultMatch(Class<I> inType, Class<O> outType) {
        return lazyResultMatch();
    }

    public static <I,O> LazyResultCaseMatcher<I,O> lazyResultMatch() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
