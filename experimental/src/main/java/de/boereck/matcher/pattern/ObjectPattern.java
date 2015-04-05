package de.boereck.matcher.pattern;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class ObjectPattern<T> implements Predicate<Object> {

    public static <T> ObjectPattern<T> pattern(Class<T> clazz) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public ObjectPattern<T> assign(Consumer<? super T> var) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public ObjectPattern<T> assertThat(Predicate<? super T> check) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public <V> ObjectPattern<T> read(Consumer<? super V> var, Function<? super T,V> map) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public <V> ObjectPattern<T> readI(IntConsumer var, ToIntFunction<? super T> map) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public ObjectPattern<T> assertThat(Consumer<? super T> var, Predicate<? super T> check) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public <V> ObjectPattern<T> checkPattern(Function<? super T,V> map, Function<ObjectPattern<V>,Predicate<? super V>> check) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public <V> ObjectPattern<T> exists(Function<? super T, ? extends Collection<V>> map, Predicate<? super V> pred) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public <V> ObjectPattern<T> exists(Consumer<? super V> var, Function<? super T, ? extends Collection<V>> map, Predicate<? super V> pred) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public <V> ObjectPattern<T> existing(Consumer<? super V> var, Function<? super T, ? extends Collection<V>> map, Predicate<? super V> pred) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public <V> ObjectPattern<T> forAll(Function<? super T, ? extends Collection<V>> map, Predicate<? super V> pred) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ObjectPattern<T> check(Predicate<T> pred) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public <V> ObjectPattern<T> check(Function<? super T,V> map, Predicate<? super V> pred) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public <V> ObjectPattern<T> check(Consumer<? super V> var, Function<? super T,V> map, Predicate<? super V> pred) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public ObjectPattern<T> checkI(ToIntFunction<T> map, IntPredicate pred) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public ObjectPattern<T> checkI(ToIntFunction<T> map, int i) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public ObjectPattern<T> checkI(IntConsumer var, ToIntFunction<T> map, int i) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public ObjectPattern<T> checkI(ToIntFunction<T> map, IntSupplier is) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean test(Object o) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    

}
