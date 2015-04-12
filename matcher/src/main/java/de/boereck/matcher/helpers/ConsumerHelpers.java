package de.boereck.matcher.helpers;

import org.omg.CORBA.NO_IMPLEMENT;

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

    /**
     * This method creates a consumer that ignores the input and always throws an exception provided by the given supplier
     * {@code exSupplier}. Since the case match API works on Consumers, which do not declare checked excheptions,
     * an the methods woking with consumers also do not declare checked exceptions, only RuntimeExceptions may be thrown.
     * The supplier can be created from parameterless constructors of exception classes by reference to the constructor
     * like this: {@code ExceptionClass::new}.
     * @param exSupplier supplier of exceptions to be thrown when the returned consumer is invoked. The supplier msut not
     *                   be {@code null} and must not supply a {@code null} reference.
     * @param <I> Type of object to be consumed.
     * @return Consumer that will ignore inputs and throws exceptions supplied by {@code exSupplier} instead.
     * @throws NullPointerException will be thrown if {@code exSupplier} is {@code null}.
     */
    public static <I> Consumer<I> thenThrow(Supplier<? extends RuntimeException> exSupplier) throws NullPointerException {
        Objects.requireNonNull(exSupplier);
        return i -> {
            throw exSupplier.get();
        };
    }

    /**
     * This method creates an IntConsumer that ignores the input and always throws an exception provided by the given supplier
     * {@code exSupplier}. Since the case match API works on Consumers, which do not declare checked excheptions,
     * an the methods woking with consumers also do not declare checked exceptions, only RuntimeExceptions may be thrown.
     * The supplier can be created from parameterless constructors of exception classes by reference to the constructor
     * like this: {@code ExceptionClass::new}.
     * @param exSupplier supplier of exceptions to be thrown when the returned consumer is invoked. The supplier msut not
     *                   be {@code null} and must not supply a {@code null} reference.
     * @return Consumer that will ignore inputs and throws exceptions supplied by {@code exSupplier} instead.
     * @throws NullPointerException will be thrown if {@code exSupplier} is {@code null}.
     */
    public static IntConsumer thenThrowI(Supplier<? extends RuntimeException> exSupplier) throws NullPointerException {
        Objects.requireNonNull(exSupplier);
        return i -> {
            throw exSupplier.get();
        };
    }

    /**
     * This method creates a LongConsumer that ignores the input and always throws an exception provided by the given supplier
     * {@code exSupplier}. Since the case match API works on Consumers, which do not declare checked excheptions,
     * an the methods woking with consumers also do not declare checked exceptions, only RuntimeExceptions may be thrown.
     * The supplier can be created from parameterless constructors of exception classes by reference to the constructor
     * like this: {@code ExceptionClass::new}.
     * @param exSupplier supplier of exceptions to be thrown when the returned consumer is invoked. The supplier msut not
     *                   be {@code null} and must not supply a {@code null} reference.
     * @return Consumer that will ignore inputs and throws exceptions supplied by {@code exSupplier} instead.
     * @throws NullPointerException will be thrown if {@code exSupplier} is {@code null}.
     */
    public static LongConsumer thenThrowL(Supplier<? extends RuntimeException> exSupplier) throws NullPointerException {
        Objects.requireNonNull(exSupplier);
        return i -> {
            throw exSupplier.get();
        };
    }

    /**
     * This method creates a DoubleConsumer that ignores the input and always throws an exception provided by the given supplier
     * {@code exSupplier}. Since the case match API works on Consumers, which do not declare checked excheptions,
     * an the methods woking with consumers also do not declare checked exceptions, only RuntimeExceptions may be thrown.
     * The supplier can be created from parameterless constructors of exception classes by reference to the constructor
     * like this: {@code ExceptionClass::new}.
     * @param exSupplier supplier of exceptions to be thrown when the returned consumer is invoked. The supplier msut not
     *                   be {@code null} and must not supply a {@code null} reference.
     * @return Consumer that will ignore inputs and throws exceptions supplied by {@code exSupplier} instead.
     * @throws NullPointerException will be thrown if {@code exSupplier} is {@code null}.
     */
    public static DoubleConsumer thenThrowD(Supplier<? extends RuntimeException> exSupplier) throws NullPointerException {
        Objects.requireNonNull(exSupplier);
        return i -> {
            throw exSupplier.get();
        };
    }

    /**
     * This method creates a Function that ignores the input and always throws an exception provided by the given supplier
     * {@code exSupplier}. Since the case match API works on Consumers, which do not declare checked excheptions,
     * an the methods woking with consumers also do not declare checked exceptions, only RuntimeExceptions may be thrown.
     * The supplier can be created from parameterless constructors of exception classes by reference to the constructor
     * like this: {@code ExceptionClass::new}.
     * @param exSupplier supplier of exceptions to be thrown when the returned consumer is invoked. The supplier msut not
     *                   be {@code null} and must not supply a {@code null} reference.
     * @return Function that will ignore inputs and throws exceptions supplied by {@code exSupplier} instead. The function
     * will not return a regular return value.
     * @throws NullPointerException will be thrown if {@code exSupplier} is {@code null}.
     */
    public static <I, O> Function<I, O> thenThrowF(Supplier<? extends RuntimeException> exSupplier) throws NullPointerException {
        Objects.requireNonNull(exSupplier);
        return i -> {
            throw exSupplier.get();
        };
    }

    /**
     * The returned consumer will ignore the input value and calls the given runnable whenever
     * a value is provided to the consumer to be consumed.
     * @param r runnable that is wrapped into a consumer. It must not be {@code null}.
     * @return consumer that calls runnable {@code r} whenever a value is provided to the consumer.
     * @throws NullPointerException will be thrown if {@code r} is {@code null}.
     */
    public static <I> Consumer<I> ignore(Runnable r) throws NullPointerException {
        Objects.requireNonNull(r);
        return i -> r.run();
    }

    /**
     * The returned IntConsumer will ignore the input value and calls the given runnable whenever
     * a value is provided to the consumer to be consumed.
     * @param r runnable that is wrapped into a consumer. It must not be {@code null}.
     * @return consumer that calls runnable {@code r} whenever a value is provided to the consumer.
     * @throws NullPointerException will be thrown if {@code r} is {@code null}.
     */
    public static IntConsumer ignoreI(Runnable r) {
        Objects.requireNonNull(r);
        return i -> r.run();
    }

    /**
     * The returned LongConsumer will ignore the input value and calls the given runnable whenever
     * a value is provided to the consumer to be consumed.
     * @param r runnable that is wrapped into a consumer. It must not be {@code null}.
     * @return consumer that calls runnable {@code r} whenever a value is provided to the consumer.
     * @throws NullPointerException will be thrown if {@code r} is {@code null}.
     */
    public static LongConsumer ignoreL(Runnable r) {
        Objects.requireNonNull(r);
        return i -> r.run();
    }

    /**
     * The returned DoubleConsumer will ignore the input value and calls the given runnable whenever
     * a value is provided to the consumer to be consumed.
     * @param r runnable that is wrapped into a consumer. It must not be {@code null}.
     * @return consumer that calls runnable {@code r} whenever a value is provided to the consumer.
     * @throws NullPointerException will be thrown if {@code r} is {@code null}.
     */
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

    /**
     * Reference to {@code System.out.println(Object)}
     */
    public static final Consumer<Object> sysout = System.out::println;

    /**
     * Reference to {@code System.out.println(int)}
     */
    public static final IntConsumer sysoutI = System.out::println;

    /**
     * Reference to {@code System.out.println(long)}
     */
    public static final LongConsumer sysoutL = System.out::println;

    /**
     * Reference to {@code System.out.println(double)}
     */
    public static final DoubleConsumer sysoutD = System.out::println;

    /**
     * Returns a Runnable that will call {@code System.out.println} with the given message.
     * @param msg message to be printed to System.out when the returned runnable is called.
     * @return Runnable that will print message {@code msg} to System.out.
     */
    public static final Runnable sysout(String msg) {
        return () -> System.out.println(msg);
    }

    /**
     * Returns a Consumer that will print the input reference to formatted to System.err using the given
     * {@code format}.
     * @param format used to print inputs to the returned Consumer formatted to System.err. Must not be {@code null}.
     * @return Consumer that prints inputs formatted to System.err.
     * @throws NullPointerException will be thrown if {@code format} is {@code null}.
     */
    public static final Consumer<Object> sysoutFormat(String format) throws NullPointerException {
        Objects.requireNonNull(format);
        return o -> System.out.printf(format, o);
    }

    /**
     * Returns an IntConsumer that will print the input reference to formatted to System.out using the given
     * {@code format}.
     * @param format used to print inputs to the returned IntConsumer formatted to System.err. Must not be {@code null}.
     * @return IntConsumer that prints inputs formatted to System.err.
     * @throws NullPointerException will be thrown if {@code format} is {@code null}.
     */
    public static final IntConsumer sysoutFormatI(String format) {
        Objects.requireNonNull(format);
        return i -> System.out.printf(format, i);
    }

    /**
     * Returns an LongConsumer that will print the input reference to formatted to System.out using the given
     * {@code format}.
     * @param format used to print inputs to the returned LongConsumer formatted to System.err. Must not be {@code null}.
     * @return LongConsumer that prints inputs formatted to System.err.
     * @throws NullPointerException will be thrown if {@code format} is {@code null}.
     */
    public static final LongConsumer sysoutFormatL(String format) {
        Objects.requireNonNull(format);
        return l -> System.out.printf(format, l);
    }

    /**
     * Returns an DoubleConsumer that will print the input reference to formatted to System.out using the given
     * {@code format}.
     * @param format used to print inputs to the returned DoubleConsumer formatted to System.err. Must not be {@code null}.
     * @return DoubleConsumer that prints inputs formatted to System.err.
     * @throws NullPointerException will be thrown if {@code format} is {@code null}.
     */
    public static final DoubleConsumer sysoutFormatD(String format) {
        Objects.requireNonNull(format);
        return d -> System.out.printf(format, d);
    }

    /**
     * Returns a Consumer that will print the input reference to formatted to System.out using the given
     * {@code format} and {@code locale}.
     * @param locale locale that the formatting will use.
     * @param format used to print inputs to the returned Consumer formatted to System.out. Must not be {@code null}.
     * @return Consumer that prints inputs formatted to System.out.
     * @throws NullPointerException will be thrown if {@code format} is {@code null}.
     */
    public static final Consumer<Object> sysoutFormat(Locale locale, String format) {
        Objects.requireNonNull(format);
        return o -> System.out.printf(locale, format, o);
    }

    /**
     * Returns a Function that always returns value {@code o}, no matter what the input to the function is.
     * @param o instance that will always be returned by the returned function.
     * @param <I> Input type to the returned function
     * @param <O> Output type of the returned function
     * @return function always returning {@code o}.
     */
    public static <I, O> Function<I, O> always(O o) {
        return i -> o;
    }

    /**
     * Reference to {@code System.err::println(Object)}.
     */
    public static final Consumer<Object> syserr = System.err::println;

    /**
     * Reference to {@code System.err::println(int)}.
     */
    public static final IntConsumer syserrI = System.err::println;

    /**
     * Reference to {@code System.err::println(long)}.
     */
    public static final LongConsumer syserrL = System.err::println;

    /**
     * Reference to {@code System.err::println(double)}.
     */
    public static final DoubleConsumer syserrD = System.err::println;

    /**
     * Returns a Consumer that will print the input reference to formatted to System.err using the given
     * {@code format}.
     * @param format used to print inputs to the returned Consumer formatted to System.err.
     * @return Consumer that prints inputs formatted to System.err.
     * @throws NullPointerException will be thrown if {@code format} is {@code null}.
     */
    public static final Consumer<Object> syserrFormat(String format) throws NullPointerException {
        Objects.requireNonNull(format);
        return o -> System.err.printf(format, o);
    }

    /**
     * Returns a Consumer that will print the input reference to formatted to System.err using the given
     * {@code format} and {@code locale}.
     * @param locale locale that the formatting will use
     * @param format used to print inputs to the returned Consumer formatted to System.err. Must not be {@code null}.
     * @return Consumer that prints inputs formatted to System.err.
     * @throws NullPointerException will be thrown if {@code format} is {@code null}.
     */
    public static final Consumer<Object> syserrFormat(Locale locale, String format) {
        Objects.requireNonNull(format);
        return o -> System.err.printf(locale, format, o);
    }
}
