package de.boereck.test.matcher.example;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.ConsumerHelpers.*;
import static de.boereck.matcher.helpers.MatchHelpers.*;
import static de.boereck.matcher.helpers.StringMatchHelpers.*;

public class SimpleMatchExample {

    public static void main(String[] args) {
        List<String> l = Arrays.asList("", "foo", null, "bar");
        print(l);
    }

    private static final Predicate<Object> isEmptyString = isString.andTest(strIsEmpty);
    private static final Consumer<Object> printObject = toString.thenDo(sysout);

    public static void print(Object o) {
        match(o)
            .caseOf(isNull, ignore)
            .caseOf(isEmptyString, ignore)
            .caseOf(isString, sysout)
            .caseOf(Collection.class, c -> c.forEach(e -> print(e)))
            .otherwise(printObject);

    }
}
