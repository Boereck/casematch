package de.boereck.matcher;


public class GuavaFunctions {
    private GuavaFunctions() {
        throw new IllegalStateException("Class GuavaFunctions must not me instantiated");
    }
    
    static <T> java.util.function.Predicate<T> is(com.google.common.base.Predicate<T> pred) {
        return pred::apply;
    }
    
    static <T> java.util.function.Predicate<T> when(com.google.common.base.Predicate<T> pred) {
        return pred::apply;
    }
}
