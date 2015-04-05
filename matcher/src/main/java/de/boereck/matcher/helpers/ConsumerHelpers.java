package de.boereck.matcher.helpers;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides static methods that may help with defining consumers of cases in the match case library<br/>
 * This class is not intended to be instantiated or sub-classed.
 * 
 * @author Max Bureck
 */
public final class ConsumerHelpers {

    private ConsumerHelpers() {
        throw new IllegalStateException("Class ConsumerHelper must not be instantiated");
    }

    public static <I> Consumer<I> thenThrow(Supplier<? extends RuntimeException> exSupplier) {
        Objects.requireNonNull(exSupplier);
        return i -> {
            throw exSupplier.get();
        };
    }

    public static IntConsumer thenThrowI(Supplier<? extends RuntimeException> exSupplier) {
        Objects.requireNonNull(exSupplier);
        return i -> {
            throw exSupplier.get();
        };
    }

    public static LongConsumer thenThrowL(Supplier<? extends RuntimeException> exSupplier) {
        Objects.requireNonNull(exSupplier);
        return i -> {
            throw exSupplier.get();
        };
    }

    public static DoubleConsumer thenThrowD(Supplier<? extends RuntimeException> exSupplier) {
        Objects.requireNonNull(exSupplier);
        return i -> {
            throw exSupplier.get();
        };
    }

    public static <I, O> Function<I, O> thenThrowF(Supplier<? extends RuntimeException> exSupplier) {
        Objects.requireNonNull(exSupplier);
        return i -> {
            throw exSupplier.get();
        };
    }

    /**
     * The "I don't care" function will provide a consumer that ignores the input value and calls the given runnable whenever
     * a value is provided to the consumer to be consumed.
     * 
     * @param r
     *            runnable that is wrapped into a consumer
     * @return consumer that calls runnable {@code r} whenever a value is provided to the consumer.
     */
    public static <I> Consumer<I> ignore(Runnable r) {
        Objects.requireNonNull(r);
        return i -> r.run();
    }

    public static IntConsumer ignoreI(Runnable r) {
        Objects.requireNonNull(r);
        return i -> r.run();
    }

    public static LongConsumer ignoreL(Runnable r) {
        Objects.requireNonNull(r);
        return i -> r.run();
    }

    public static DoubleConsumer ignoreD(Runnable r) {
        Objects.requireNonNull(r);
        return i -> r.run();
    }

    public static Consumer<String> logAsMsg(Logger logger, Level level) {
        Objects.requireNonNull(logger);
        Objects.requireNonNull(level);
        return s -> logger.log(level, s);
    }

    public static Consumer<Object> log(Logger logger, Level level, String msg) {
        Objects.requireNonNull(logger);
        Objects.requireNonNull(level);
        return o -> logger.log(level, msg, o);
    }

    public static Consumer<Object> logrb(Logger logger, Level level, String sourceClass, String sourceMethod, ResourceBundle bundle, String msg) {
        Objects.requireNonNull(logger);
        Objects.requireNonNull(level);
        return o -> logger.logrb(level, sourceClass, sourceMethod, bundle, msg, o);
    }

    public static Consumer<Throwable> logException(Logger logger, Level level, String msg) {
        Objects.requireNonNull(logger);
        Objects.requireNonNull(level);
        return ex -> logger.log(level, msg, ex);
    }

    public static Consumer<Throwable> logrbException(Logger logger, Level level, String sourceClass, String sourceMethod, ResourceBundle bundle, String msg) {
        Objects.requireNonNull(logger);
        Objects.requireNonNull(level);
        return ex -> logger.logrb(level, sourceClass, sourceMethod, bundle, msg, ex);
    }

    public static Consumer<Object> toStringLogAsMsg(Logger logger, Level level) {
        Objects.requireNonNull(logger);
        Objects.requireNonNull(level);
        return o -> logger.log(level, o.toString());
    }

    public static final Consumer<Object> ignore = o -> {
    };
    public static final IntConsumer ignoreI = o -> {
    };
    public static final LongConsumer ignoreL = o -> {
    };
    public static final DoubleConsumer ignoreD = o -> {
    };

    public static final Consumer<Object> sysout = System.out::println;
    public static final IntConsumer sysoutI = System.out::println;
    public static final LongConsumer sysoutL = System.out::println;
    public static final DoubleConsumer sysoutD = System.out::println;

    public static final Runnable sysout(String msg) {
        return () -> System.out.println(msg);
    }

    public static final Consumer<Object> sysoutFormat(String format) {
        Objects.requireNonNull(format);
        return o -> System.out.printf(format, o);
    }
    
    public static final IntConsumer sysoutFormatI(String format) {
        Objects.requireNonNull(format);
        return i -> System.out.printf(format, i);
    }
    
    public static final LongConsumer sysoutFormatL(String format) {
        Objects.requireNonNull(format);
        return l -> System.out.printf(format, l);
    }
    
    public static final DoubleConsumer sysoutFormatD(String format) {
        Objects.requireNonNull(format);
        return d -> System.out.printf(format, d);
    }

    public static final Consumer<Object> sysoutFormat(Locale locale, String format) {
        Objects.requireNonNull(format);
        return o -> System.out.printf(locale, format, o);
    }

    public static <I,O> Function<I,O> always(O o) {
        return i -> o;
    }
    
    public static final Consumer<Object> syserr = System.err::println;
    public static final IntConsumer syserrI = System.err::println;
    public static final LongConsumer syserrL = System.err::println;
    public static final DoubleConsumer syserrD = System.err::println;

    public static final Consumer<Object> syserrFormat(String format) {
        return o -> System.out.printf(format, o);
    }

    public static final Consumer<Object> syserrFormat(Locale locale, String format) {
        Objects.requireNonNull(format);
        return o -> System.out.printf(locale, format, o);
    }
}
