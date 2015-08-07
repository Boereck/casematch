package de.boereck.test.matcher.helpers;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static de.boereck.matcher.helpers.ConsumerHelpers.*;
import static org.junit.Assert.*;

public class ConsumerHelpersTest {

    @Test(expected = NumberFormatException.class)
    public void testThenThrow() {
        thenThrow(NumberFormatException::new).accept("foo");
    }

    @Test(expected = NullPointerException.class)
    public void testThenThrowOnNull() {
        thenThrow(null);
    }


    @Test(expected = NumberFormatException.class)
    public void testThenThrowI() {
        thenThrowI(NumberFormatException::new).accept(42);
    }

    @Test(expected = NullPointerException.class)
    public void testThenThrowIOnNull() {
        thenThrowI(null);
    }

    @Test(expected = NumberFormatException.class)
    public void testThenThrowL() {
        thenThrowL(NumberFormatException::new).accept(42L);
    }

    @Test(expected = NullPointerException.class)
    public void testThenThrowLOnNull() {
        thenThrowL(null);
    }

    @Test(expected = NumberFormatException.class)
    public void testThenThrowD() {
        thenThrowD(NumberFormatException::new).accept(0.0d);
    }

    @Test(expected = NullPointerException.class)
    public void testThenThrowDOnNull() {
        thenThrowD(null);
    }

    @Test(expected = NumberFormatException.class)
    public void testThenThrowFun() {
        thenThrowFun(NumberFormatException::new).apply("foo");
    }

    @Test(expected = NullPointerException.class)
    public void testThenThrowFunOnNull() {
        thenThrowFun(null);
    }

    @Test
    public void testIgnore() {
        AtomicBoolean b = new AtomicBoolean(false);
        ignore(() -> b.set(true)).accept("foo");
        assertTrue(b.get());
    }

    @Test(expected = NullPointerException.class)
    public void testIgnoreOnNull() {
        ignore(null);
    }

    @Test
    public void testIgnoreI() {
        AtomicBoolean b = new AtomicBoolean(false);
        ignoreI(() -> b.set(true)).accept(42);
        assertTrue(b.get());
    }

    @Test(expected = NullPointerException.class)
    public void testIgnoreIOnNull() {
        ignoreI(null);
    }

    @Test
    public void testIgnoreL() {
        AtomicBoolean b = new AtomicBoolean(false);
        ignoreL(() -> b.set(true)).accept(42L);
        assertTrue(b.get());
    }

    @Test(expected = NullPointerException.class)
    public void testIgnoreLOnNull() {
        ignoreL(null);
    }

    @Test
    public void testIgnoreD() {
        AtomicBoolean b = new AtomicBoolean(false);
        ignoreD(() -> b.set(true)).accept(42.0d);
        assertTrue(b.get());
    }

    @Test(expected = NullPointerException.class)
    public void testIgnoreDOnNull() {
        ignoreD(null);
    }

    private static class TestLogHandler extends Handler {

        List<LogRecord> records = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }

    @Test
    public void testLogAsMsg() {
        Logger testLogger = Logger.getAnonymousLogger();
        TestLogHandler handler = new TestLogHandler();
        testLogger.addHandler(handler);
        final String msg = "foo";
        logAsMsg(testLogger, Level.WARNING).accept(msg);
        assertEquals(1, handler.records.size());
        assertEquals(msg, handler.records.get(0).getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testLogAsMsgLoggerNull() {
        logAsMsg(null, Level.WARNING);
    }

    @Test(expected = NullPointerException.class)
    public void testLogAsMsgLevelNull() {
        logAsMsg(Logger.getAnonymousLogger(), null);
    }

    @Test
    public void testLog() {
        Logger testLogger = Logger.getAnonymousLogger();
        TestLogHandler handler = new TestLogHandler();
        testLogger.addHandler(handler);
        final String msg = "hello {0}";
        final Object o = "world";
        final String expected = "hello world";
        log(testLogger,Level.INFO, msg).accept(o);

        assertEquals(1, handler.records.size());
        final LogRecord record = handler.records.get(0);
        assertEquals(msg, record.getMessage());
        assertTrue(Arrays.equals(new Object[]{o}, record.getParameters()));
    }

    @Test(expected = NullPointerException.class)
    public void testLogLoggerNull() {
        Logger testLogger = Logger.getAnonymousLogger();
        log(null, Level.INFO, "foo");
    }

    @Test(expected = NullPointerException.class)
    public void testLogLevelNull() {
        Logger testLogger = Logger.getAnonymousLogger();
        log(testLogger, null, "foo");
    }

    public void testToStringLogAsMsg() {
        Logger testLogger = Logger.getAnonymousLogger();
        TestLogHandler handler = new TestLogHandler();
        testLogger.addHandler(handler);
        String str = "foo";
        Object bla = new Object() {
            public String toString() {
                return str;
            }
        };
        toStringLogAsMsg(testLogger, Level.WARNING).accept(bla);
        assertEquals(1, handler.records.size());
        final LogRecord record = handler.records.get(0);
        assertEquals(str, record.getMessage());
    }

    @Test
    public void testSysout() {
        final PrintStream out = System.out;
        try {
            ByteArrayOutputStream newOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(newOut));
            String str = "foo";
            sysout(str).run();
            // println will produce a line break, since we do not want to
            // get into line breaks on different OSes, we trim the printed line
            assertEquals(str, new String(newOut.toByteArray()).trim());
        }finally {
            System.setOut(out);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSysoutFormatWithFormatStringNull() {
        sysoutFormat(null);
    }

    @Test
    public void testSysoutFormat() {
        final PrintStream out = System.out;
        try {
            ByteArrayOutputStream newOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(newOut));
            String str = "hello %s";
            String expected = "hello world";
            sysoutFormat(str).accept("world");
            assertEquals(expected, new String(newOut.toByteArray()));
        }finally {
            System.setOut(out);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSysoutFormatIWithFormatStringNull() {
        sysoutFormatI(null);
    }

    @Test
    public void testSysoutFormatI() {
        final PrintStream out = System.out;
        try {
            ByteArrayOutputStream newOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(newOut));
            String str = "hello %d";
            int val = 42;
            String expected = String.format(str, val);
            sysoutFormatI(str).accept(val);
            assertEquals(expected, new String(newOut.toByteArray()));
        }finally {
            System.setOut(out);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSysoutFormatLWithFormatStringNull() {
        sysoutFormatL(null);
    }

    @Test
    public void testSysoutFormatL() {
        final PrintStream out = System.out;
        try {
            ByteArrayOutputStream newOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(newOut));
            String str = "hello %d";
            long val = 42L;
            String expected = String.format(str, val);
            sysoutFormatL(str).accept(val);
            assertEquals(expected, new String(newOut.toByteArray()));
        }finally {
            System.setOut(out);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSysoutFormatDWithFormatStringNull() {
        sysoutFormatD(null);
    }

    @Test
    public void testSysoutFormatD() {
        final PrintStream out = System.out;
        try {
            ByteArrayOutputStream newOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(newOut));
            String str = "hello %.1f";
            double val = 0.0d;
            String expected = String.format(str, val);
            sysoutFormatD(str).accept(val);
            assertEquals(expected, new String(newOut.toByteArray()));
        }finally {
            System.setOut(out);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSysoutFormatLocaleWithFormatStringNull() {
        sysoutFormat(Locale.ENGLISH, null);
    }

    @Test
    public void testSysoutFormatLocale() {
        final PrintStream out = System.out;
        try {
            ByteArrayOutputStream newOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(newOut));
            String str = "hello %.1f";
            double val = 0.0d;
            Locale locale = Locale.ENGLISH;
            String expected = String.format(locale, str, val);
            sysoutFormat(locale, str).accept(val);
            assertEquals(expected, new String(newOut.toByteArray()));
        }finally {
            System.setOut(out);
        }
    }

    @Test
    public void testAlwaysNull() {
        Function<Object, Object> f = always(null);
        assertNull(f.apply(null));
        assertNull(f.apply("foo"));
    }

    @Test
    public void testAlwaysString() {
        String expected = "bar";
        Function<Object, Object> f = always(expected);
        assertEquals(expected, f.apply(null));
        assertEquals(expected, f.apply("foo"));
    }

    @Test(expected = NullPointerException.class)
    public void testSyserrFormatLocaleWithFormatStringNull() {
        syserrFormat(Locale.ENGLISH, null);
    }

    @Test
    public void testSyserrFormatLocale() {
        final PrintStream out = System.err;
        try {
            ByteArrayOutputStream newOut = new ByteArrayOutputStream();
            System.setErr(new PrintStream(newOut));
            String str = "hello %.1f";
            double val = 0.0d;
            Locale locale = Locale.ENGLISH;
            String expected = String.format(locale, str, val);
            syserrFormat(locale, str).accept(val);
            assertEquals(expected, new String(newOut.toByteArray()));
        }finally {
            System.setErr(out);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSyserrFormatWithFormatStringNull() {
        syserrFormat(null);
    }

    @Test
    public void testSyserrFormat() {
        final PrintStream out = System.err;
        try {
            ByteArrayOutputStream newOut = new ByteArrayOutputStream();
            System.setErr(new PrintStream(newOut));
            String str = "hello %s";
            String expected = "hello world";
            syserrFormat(str).accept("world");
            assertEquals(expected, new String(newOut.toByteArray()));
        }finally {
            System.setErr(out);
        }
    }
}
