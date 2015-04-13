package de.boereck.test.matcher;

import java.util.Collection;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.MatchHelpers.*;
import static de.boereck.matcher.helpers.ConsumerHelpers.*;
import static de.boereck.matcher.helpers.StringMatchHelpers.*;
import static de.boereck.matcher.helpers.CollectionMatchHelpers.*;

public class Test {

    public static abstract class Vehicle {
    }

    public static class Car extends Vehicle {
        String color;
    }

    public static class Boat extends Vehicle {
        String velocity;
    }

    public static class Plane extends Vehicle {
        String kind;
    }

    public static void main(String[] args) {
        Vehicle vehicle = new Boat() {
            {
                velocity = "slow";
            }
        };
        resultMatch(vehicle)
                .caseOf(Car.class, c -> "This is a " + c.color + " car")
                .caseOf(Boat.class, b -> "This is a " + b.velocity + " boat")
                .caseOf(Plane.class, p -> "This is a " + p.kind + " plane")
                .caseOf(any, always("Unknown vehicle :("))
                .ifResult(sysout);
    }

    public static void print(Object o) {
        match(o)
                .caseOf(isNull.or(isString.andTest(strIsEmpty)), ignore)
                .caseOf(isString, sysout)
                .caseObj(castToCollection.filter(c -> c.stream().anyMatch(notNull)), c -> c.forEach(Test::print))
                .otherwise(toString.thenDo(Test::print));
    }

}
