package de.boereck.test.matcher.example;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.helpers.IntMatchHelpers;

import java.util.Optional;
import java.util.OptionalInt;

import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.ConsumerHelpers.*;
import static de.boereck.matcher.helpers.IntMatchHelpers.*;
import static de.boereck.matcher.helpers.OptionalMatchHelpers.*;

/**
 * Created by mbu on 03.06.2015.
 */
public class OptionalMatchExample {
    public static void main(String[] args) {

        // simple match testing if option contains value and prints it
        Optional<String> o = Optional.of("Foo");
        match(o)
                .caseObj(some(), sysout)
                .caseIs(none, sysout("None"));

        // showing a match for non existing value
        Optional<Object> o4 = Optional.empty();
        match(o4)
                .caseObj(some(), sysoutFormat("Unexpected object: %s\n"))
                .caseIs(none, sysout("None as expected"));

        // int match with additional filter
        OptionalInt o2 = OptionalInt.of(-3);
        match(o2)
                .caseInt(someI(positive), sysoutFormatI("Positive: %d\n"))
                .caseInt(someI(negative), sysoutFormatI("Negative: %d\n"))
                .caseIs(noneI, sysout("None"));

        // matching a nested optional and extract value
        Optional<Optional<String>> o3 = Optional.of(Optional.of("Bar"));
        match(o3)
                .caseObj(someFlat(some()), sysout)
                .otherwise(ignore(sysout("None")));
    }
}
