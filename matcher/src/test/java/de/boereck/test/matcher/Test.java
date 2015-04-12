package de.boereck.test.matcher;

import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.MatchHelpers.*;
import static de.boereck.matcher.helpers.ConsumerHelpers.*;

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

}
