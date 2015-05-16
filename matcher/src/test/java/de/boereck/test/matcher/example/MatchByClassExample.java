package de.boereck.test.matcher.example;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.*;

import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.MatchHelpers.*;
import static de.boereck.matcher.helpers.ConsumerHelpers.*;
import static de.boereck.matcher.helpers.StringMatchHelpers.*;
import static de.boereck.matcher.helpers.CollectionMatchHelpers.*;

import static de.boereck.matcher.function.curry.CurryableBiFunction.*;
import static java.util.stream.Collectors.joining;

public class MatchByClassExample {

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

}
