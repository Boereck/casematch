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

    static <Z> EagerMatchable<Z> matchable(Z t) {
        return new EagerMatchable<Z>() {
            @Override
            public EagerNoResultCaseMatcher<Z> match() {
                return EagerMatcher.match(t);
            }

            @Override
            public <O> EagerResultCaseMatcher<Z, O> resultMatch() {
                return EagerMatcher.resultMatch(t);
            }
        };
    }

    @SuppressWarnings("unchecked")
    default EagerNoResultCaseMatcher<T> match() {
        return EagerMatcher.match((T) this);
    }

    @SuppressWarnings("unchecked")
    default <O> EagerResultCaseMatcher<T, O> resultMatch() {
        return EagerMatcher.resultMatch((T) this);
    }
}
