Case Matcher
============
This is a Java 8 library providing a fluent API to define case matching in an object oriented and functional way. This library is no replacement for traditional switch case statements in Java, but allows a richer definition of cases on primitive and object values. This allows more readable code than if-then-else cascades, but comes with a cost of runtime and memory overhead. The library will not provide a matching that is as powerful and compact as pattern matching known from functional languages (or languages with more functional aspects) such as Scala. Never the less the goal is to find readable representations of common cases in Java development.

// TODO Javadoc Link

Defining Matches
----------------

To start default eager matches, import the static methods from the class "de.boereck.matcher.eager.EagerMatcher"

```java 
import static de.boereck.matcher.eager.EagerMatcher.*;
```

There are methods to start matches on objects as well as on the primitive types int, long, and double.
There are two kind of matches: Matches that *do* and matches that *do not* return a value.

The returned objects from a match method allow the definition of cases in a fluent style API. The cases usually define a condition under which a case can be regarded as a matching case and and action that is being performed when the case is determined to be the matching one. The value the match is defined on will be passed to the action. Here is an example of a simple match on an int value with no result value:

```java 
import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.IntMatchHelpers.*;

public class Test {

    public static void main(String[] args) {
        match(42)
        .caseOf(negative, i -> System.out.printf("%d is a negative value", i))
        .caseOf(0, i -> System.out.println("Exactly zero"))
        .caseOf(positive, i -> System.out.printf("%d is a positive value", i));
    }

}
```

As you can see there are a few predefined predicates that are ready to be used. In this case we use "positive" and "negative", which are defined as static final fields in class "IntMatchHelpers". Have a look at the other helper classes in package "de.boereck.matcher.helpers" which define several helper functions that can be used to write compact yet readable conditions. Due to the way how in Java type inference is done and how generics work, some helper methods have special versions for the primitive types int, long, and double. The versions specific for int usually have a suffix of the letter 'I', the version for long of letter 'L' and the versions for double of letter 'D'.

There are also case definitions that allow the condition to return a Java 8 Optional instead of a boolean value. In this definitions a case is regarded a matching case if the Optional returned from the condition does contain a value. The value will then be passed to the action. TODO: EXAMPLE!

In matches that do return a value, the case actions must return a value of the return type. With a so called closing method it is possible to retrieve the result value from the CaseMatch object.

### Class Based Matching ###
A common use case for using case matchers is to distinguish between sub-classes of objects. In Java this is usually solved by using if-then-else cascades with instanceof checks and manual casts to the respective types. This is error prone since the target type has to be mentioned twice.
Case matches can perform the casts automatically, without having to mention the target type twice.

TODO describe 
```java 
import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.MatchHelpers.*;
import static de.boereck.matcher.helpers.ConsumerHelpers.*;

public class Test {
    
    public static abstract class Vehicle{}
    public static class Car extends Vehicle{ String color; }
    public static class Boat extends Vehicle{ String velocity; }
    public static class Plane extends Vehicle{ String size; }

    public static void main(String[] args) {
        Vehicle vehicle = new Boat(){
            {velocity = "slow";}
        };
        resultMatch(vehicle)
        .caseOf(Car.class, c -> "This is a " + c.color + " car")
        .caseOf(Boat.class, b -> "This is a " + b.velocity + " boat")
        .caseOf(Plane.class, p -> "This is a " + p.size + " plane")
        .caseOf(any, always("Unknown vehicle"))
        .ifResult(sysout);
    }
    
}
```


TypeCheck
// TODO describe match(instanceOf(Car.class).and)

```java 
import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.StringMatchHelpers.*;
import static de.boereck.matcher.helpers.MatchHelpers.*;
import static de.boereck.matcher.helpers.CollectionMatchHelpers.*;
import static de.boereck.matcher.helpers.ConsumerHelpers.*;

import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<?> list = Arrays.asList("Foo", "", new StringBuilder("Bar"));
        print(list);
        
    }
    
    public static void print(Object o) {
        match(o)
        .caseOf(isNull.or(isString.andTest(strIsEmpty)), ignore)
        .caseOf(isString, sysout)
        .caseObj(castToCollection, c -> c.forEach(Test::print))
        .otherwise(toString.thenDo(Test::print));
    }

}

```

for comparison, this is how the print function would look like using traditional if-else statements:
```java 
    public static void print(Object o) {
        if(o == null) {
            return;
        }
        if(o instanceof String) {
            String s = (String) o;
            if(s.isEmpty()) {
                return;
            }
            // else
            System.out.println(s);
        } else {
            if(o instanceof Collection<?>) {
                Collection<?> c = (Collection<?>) o;
                c.forEach(Test::print);
            } else {
                print(o.toString());
            }
        }
    }
```
