package de.boereck.matcher.pattern;

import de.boereck.matcher.function.predicate.AdvPredicate;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Var<T> implements Supplier<T>, Consumer<T> {
    private T t = null;

    public Var(T t) {
        this.t = t;
    }

    public Var() {
    }
    
    @Override
    public T get() {
        return t;
    }

    @Override
    public void accept(T t) {
        this.t = t;
    }

    public static <T> Var<T> var() {
        return new Var<>();
    }
    
    public T val() {
        return t;
    }

    public void set(T t) {
        accept(t);
    }

    public AdvPredicate<T> setWhen(Predicate<T> p) {
        return t -> setIfTrue(t,p);
    }

    public <V extends T> AdvPredicate<V> setWhen(Class<V> c) {
        return v -> setIfTrue(v,c::isInstance);
    }

    private boolean setIfTrue(T t, Predicate<T> p) {
        if(p.test(t)) {
            accept(t);
            return true;
        } else {
            return false;
        }
    }
}