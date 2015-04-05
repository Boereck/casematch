package de.boereck.matcher.pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class MultiVar<T> implements Supplier<List<T>>, Consumer<T> {

    public static <T> MultiVar<T> multiVar() {
        return new MultiVar<T>();
    }
    
    private List<T> list = new ArrayList<T>();
    
    @Override
    public void accept(T t) {
        list.add(t);
    }

    @Override
    public List<T> get() {
        return val();
    }
    
    public List<T> val() {
        return Collections.unmodifiableList(list);
    }
    
}