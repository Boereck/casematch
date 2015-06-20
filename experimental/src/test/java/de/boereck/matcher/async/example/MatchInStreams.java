package de.boereck.matcher.async.example;


import de.boereck.matcher.lazy.LazyResultCaseMatcher;
import de.boereck.matcher.lazy.MatchingFunction;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.boereck.matcher.helpers.StringMatchHelpers.matching;
import static de.boereck.matcher.lazy.LazyResultMatcher.caseObj;
import static java.util.function.Function.identity;

/**
 * Created by mbu on 16.06.2015.
 */
public class MatchInStreams {

    public static void main(String[] args) {
        List<String> l = Stream.of("Foo", "Bar")
                .map(
                        caseObj(matching("Foo"), String::toUpperCase)
                                .caseObj(matching("Bar"), identity())
                                .otherwise("???")
                ).collect(Collectors.toList());
    }
}
