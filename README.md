[![Build Status](https://api.travis-ci.org/Boereck/casematch.svg)](https://travis-ci.org/Boereck/casematch)
[![Coverage Status](https://coveralls.io/repos/Boereck/casematch/badge.svg?branch=master&service=github)](https://coveralls.io/github/Boereck/casematch?branch=master)
[![License](https://img.shields.io/github/license/Boereck/casematch.svg)](https://github.com/Boereck/casematch/blob/master/LICENSE)
 
Case Matcher
============
This is a Java 8 library providing a fluent API to define case matching in an object oriented and functional way. 
This library is no replacement for traditional switch case statements in Java, but allows a richer definition of cases on 
primitive and object reference values. This allows more readable code than if-then-else cascades, but comes with a cost of 
runtime and memory overhead. The library will *not* provide a matching that is as powerful and compact as pattern matching 
known from functional languages (or languages with more functional aspects) such as Scala. Especially object decomposition is missing 
and may never be provided. Never the less the goal is to find readable representations of common cases in Java development. 

The following examples give an overview over the eager matching capabilities. Lazy matchers and other features are currently
in design stages and can be found in the "experimental" sub-project.

Browse JavaDoc:
* [Current snapshot](http://boereck.github.io/casematch/javadoc/matcher/0.5-SNAPSHOT/index.html)

State of Implementation
-----------------------

The current version has the purpose of gathering feedback, especially with regard to the provided helper functions.
The API may still break from version to version, since the project is not tested much with real world problems.
So please create issues or provide pull requests making the API better and maybe easier to use.

How to Use Case Matcher in Your Build
-------------------------------------
There is currently only a snapshot repository, there is no stable release available.

### Maven ###

To add a dependency to a maven build, add the following repository to your POM file:

```xml
<repositories>
  <repository>
    <id>Sonatype OSSRH Snapshots</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  </repository>
</repositories>
```

And add the following dependency:
```xml
<dependency>
  <groupId>com.github.boereck</groupId>
  <artifactId>casematch-matcher</artifactId>
  <version>0.5-SNAPSHOT</version>
</dependency>
```

### Gradle ###

To use the library in your gradle build, add the snapshot repository with this command:
```groovy
repositories {
    maven {
        url 'https://oss.sonatype.org/content/repositories/snapshots'
    }
}
```

And add the dependency as follows:
```groovy
dependencies {
    compile 'com.github.boereck:casematch-matcher:0.5-SNAPSHOT'
}
```

TODO: Give hints how to include in maven/tycho build.

Defining Matches
----------------

To start default eager matches, import the static methods from the class "de.boereck.matcher.eager.EagerMatcher"

```java 
import static de.boereck.matcher.eager.EagerMatcher.*;
```

There are methods to start matches on objects as well as on the primitive types int, long, and double.
There are two kind of matches: Matches that *do* and matches that *do not* return a value.

The returned objects from a match method allow the definition of cases in a fluent style API. The cases usually define a 
condition under which a case can be regarded as a matching case and and action that is being performed when the case is 
determined to be the matching one. The value the match is defined on will be passed to the action. Here is an example of 
a simple match on an int value with no result value:

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

As you can see there are a few predefined predicates that are ready to be used. In this case we use "positive" and "negative", 
which are defined as static final fields in class "IntMatchHelpers". Have a look at the other helper classes in package 
"de.boereck.matcher.helpers" which define several helper functions that can be used to write compact yet readable conditions. 
Due to the way how in Java type inference is done and how generics work, some helper methods have special versions for the 
primitive types int, long, and double. The versions specific for int usually have a suffix of the letter 'I', the version 
for long of letter 'L' and the versions for double of letter 'D'.

There are also case definitions (methods named caseOf) that allow the condition to return a Java 8 Optional instead of a boolean value. 
In this definitions a case is regarded a matching case if the Optional returned from the condition does contain a value. 
The value will then be passed to the action defined for the case. This allows extracting values from input objects.
TODO: EXAMPLE!

In matches that do return a value, the case actions must return a value of the return type. With a so called closing method 
it is possible to retrieve the result value from the CaseMatch object.

### Class Based Matching ###
A common use case for using case matchers is to distinguish between sub-classes of objects. In Java this is usually solved 
by using if-then-else cascades with instanceof checks and manual casts to the respective types. This is error prone since 
the target type has to be mentioned twice.
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

import java.util.Arrays;
import java.util.List;

public class Test {

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
            .caseObj(castToCollection, c -> c.forEach(Test::print))
            .otherwise(printObject);

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
                System.out.println(o.toString());
            }
        }
    }
```

Building Case Matcher
---------------------
If you want to build the Case Matcher library on your own, first download and install Gradle (https://gradle.org/) at least in 
version 2.1. Simply clone this git repository, switch on the shell into the working directory of the repository 
and execute the command "gradle". When the build is successful, the main library "casematch-matcher-<VERSION>.jar" 
is located in folder "matcher\build\libs".

Contributions
--------------
Pull requests are welcome, but it may take a while until they are reviewed. Large pull request may be rejected,
not because they are bad, but because they have to be maintained by the main committer. For now, since the project
is a one man endeavor, this may happen; but this may change if more people get interested in committing and taking
responsibility for the code base. Pull requests providing functionality that can be easily expressed using regular
switch-case statements in java will also be rejected.

Code that is contributed will automatically be under Apache 2.0 license like the rest of the code. When issuing a pull
request, the user agrees that his changes will be made available under this license. To make the code available under
a different license would need the approval of all contributors.