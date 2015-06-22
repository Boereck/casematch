package de.boereck.test.matcher.example;


import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.CollectionMatchHelpers.*;
import static de.boereck.matcher.helpers.ConsumerHelpers.*;
import static de.boereck.matcher.helpers.MatchHelpers.*;
import static de.boereck.matcher.helpers.StringMatchHelpers.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class SimpleMatchExample {

    public static void main(String[] args) {
        List<Object> l = asList("", "foo", null, singletonList("bar"));
        print(l);
    }

    private static final Predicate<Object> isEmptyString = isString.andTest(strIsEmpty);
    private static final Predicate<Object> isNullOrEmptyStr = isNull.or(isEmptyString);
    private static final Consumer<Object> printObject = toString.thenDo(sysout);

    public static void print(Object o) {

        match(o)
                .caseOf(isNullOrEmptyStr, ignore)
                .caseOf(isString, sysout)
                .caseObj(castToCollection, c -> c.forEach(e -> print(e)))
                .otherwise(printObject);

    }
}
