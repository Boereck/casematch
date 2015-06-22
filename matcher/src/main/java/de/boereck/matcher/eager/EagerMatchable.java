package de.boereck.matcher.eager;

/**
 * Can be implemented by classes that want to be able to provide matching as
 * methods directly available on instances. Implementing classes may provide
 * own implementations of the default methods and own implementations of the
 * returned eager matchers.
 * <p>The disadvantage of this interface is that subclasses of the implementing class
 * can only match on the super type</p>
 * <p>Another use of this interface is to call method {@link EagerMatchable#matchable(Object) matchable(Object)}
 * to pass an object around that allows matching on a wrapped object.</p>
 *
 * @param <T> must be the same type as the class implementing the interface!
 * @author Max Bureck
 */
public interface EagerMatchable<T> {

    /**
     * Creates an instance of {@code EagerMatchable&lt;T&gt;} that can create case matchers for the given
     * input {@code t}.
     * @param z object an instance of EagerMatchable is created for.
     * @param <Z> type of input parameter {@code z}
     * @return instance of EagerMatchable that can be used to match on input parameter {@code z}
     */
    static <Z> EagerMatchable<Z> matchable(Z z) {
        return new EagerMatchable<Z>() {
            @Override
            public EagerNoResultCaseMatcher<Z> match() {
                return EagerMatcher.match(z);
            }

            @Override
            public <O> EagerResultCaseMatcher<Z, O> resultMatch() {
                return EagerMatcher.resultMatch(z);
            }
        };
    }

    /**
     * Creates an instance of {@link EagerNoResultCaseMatcher} that can be used to match on
     * the instance of {@code T} this matchable is defined for.
     * @return instance of EagerNoResultCaseMatcher to match on instance of {@code T}
     */
    @SuppressWarnings("unchecked")
    default EagerNoResultCaseMatcher<T> match() {
        return EagerMatcher.match((T) this);
    }

    /**
     * Creates an instance of {@link EagerResultCaseMatcher} that can be used to match on
     * the instance of {@code T} this matchable is defined for.
     * @return instance of EagerResultCaseMatcher to match on instance of {@code T}
     */
    @SuppressWarnings("unchecked")
    default <O> EagerResultCaseMatcher<T, O> resultMatch() {
        return EagerMatcher.resultMatch((T) this);
    }
}
