package de.boereck.matcher.pattern;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public final class IntVar implements IntSupplier, IntConsumer {

    public static IntVar intVar() {
        return new IntVar();
    }
    
    int val;
    
    public IntVar(int val) {
        this.val = val;
    }

    public IntVar() {
    }
    
    @Override
    public void accept(int value) {
        val = value;
    }

    @Override
    public int getAsInt() {
        return val;
    }
    
    public int val() {
        return val;
    }
    
}