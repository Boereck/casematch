package de.boereck.test.matcher.example;

import de.boereck.matcher.helpers.CollectionMatchHelpers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.function.curry.CurryableBiFunction.*;
import static de.boereck.matcher.helpers.CollectionMatchHelpers.*;
import static de.boereck.matcher.helpers.ConsumerHelpers.*;
import static de.boereck.matcher.helpers.MatchHelpers.*;
import static java.util.stream.Collectors.*;

public class CollectionExamples {

    private static final BiFunction<String, String, Boolean> startsWith = String::startsWith;

    private static final Predicate<String> startsWithA = λ(startsWith)._2("a")::apply;

    private static String startingWithA(Collection<String> c) {
        return $(c).filter(startsWithA).map(s -> "'" + s + "'").collect(joining(", "));
    }

    public static void main(String[] args) {
        List<String> elements = Arrays.asList("a boat", "a plane", "car");
        resultMatch(elements)
                .caseOf( Ɐ(s -> s.startsWith("a")), c -> "all start with 'a'" )
                .caseOf( Ǝ(s -> s.startsWith("a")), c -> "elements starting with 'a': " + startingWithA(c) )
                .caseOf( __ ,                       c -> "no element starts with 'a'")
                .ifResult(sysout);
    }

}
