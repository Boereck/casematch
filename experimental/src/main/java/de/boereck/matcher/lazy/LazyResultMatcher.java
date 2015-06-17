package de.boereck.matcher.lazy;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;

import static de.boereck.matcher.lazy.LazyMatcher.lazyResultMatch;

/**
 * Provides a coupe of static functions to create a {@link LazyResultCaseMatcher} instance.
 * If these methods are included statically, this can lead to compact code that can be used
 * e.g. as arguments for higher order functions (such as mapping operations on {@link java.util.stream.Stream Streams}).
 */
public class LazyResultMatcher {

    /**
     * Shortcut for {@code LazyMatcher.<I,O>lazyResultMatch().caseOf(clazz, f) }
     * @param clazz
     * @param f
     * @param <I>
     * @param <O>
     * @param <T>
     * @return
     */
    public static <I,O,T> LazyResultCaseMatcher<I, O> caseOf(Class<T> clazz, Function<? super T, ? extends O> f) {
        return LazyMatcher.<I,O>lazyResultMatch().caseOf(clazz, f);
    }

    public static <I,O,T> LazyResultCaseMatcher<I, O> caseOf(Class<T> clazz, Predicate<? super T> condition, Function<? super T, ? extends O> f) throws NullPointerException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static <I,O> LazyResultCaseMatcher<I, O> caseOf(Predicate<? super I> p, Function<? super I, ? extends O> f) throws NullPointerException{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static <I,O> LazyResultCaseMatcher<I, O> caseOf(BooleanSupplier s, Function<? super I, ? extends O> f) throws NullPointerException{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static <I,O> LazyResultCaseMatcher<I, O> caseOf(boolean test, Function<? super I, ? extends O> f) throws NullPointerException{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static <I,O> LazyResultCaseMatcher<I,O> caseIs(Predicate<? super I> p, Supplier<? extends O> supplier) throws NullPointerException{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static <I,O> LazyResultCaseMatcher<I,O> caseIs(boolean test, Supplier<? extends O> supplier) throws NullPointerException{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static <I,O,T> LazyResultCaseMatcher<I, O> caseObj(Function<? super I, Optional<T>> p, Function<? super T, ? extends O> f) throws NullPointerException{
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public static <I,O> LazyResultCaseMatcher<I, O> caseInt(Function<? super I, OptionalInt> p, IntFunction<? extends O> f) throws NullPointerException{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static <I,O> LazyResultCaseMatcher<I, O> caseLong(Function<? super I, OptionalLong> p, LongFunction<? extends O> f) throws NullPointerException{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static <I,O> LazyResultCaseMatcher<I, O> caseDouble(Function<? super I, OptionalDouble> p, DoubleFunction<? extends O> f) throws NullPointerException{
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
