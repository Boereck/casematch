package de.boereck.matcher.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;


import static de.boereck.matcher.eager.EagerMatcher.*;
import static de.boereck.matcher.helpers.MatchHelpers.*;
import static de.boereck.matcher.helpers.CollectionMatchHelpers.*;
import static de.boereck.matcher.helpers.ConsumerHelpers.*;
import static de.boereck.matcher.helpers.StringMatchHelpers.*;
import static de.boereck.matcher.helpers.IntMatchHelpers.*;
import static de.boereck.matcher.helpers.DoubleMatchHelpers.*;
import static de.boereck.matcher.pattern.ObjectPattern.*;
import static de.boereck.matcher.pattern.Var.*;
import static de.boereck.matcher.pattern.IntVar.*;

public class ObjectPatternPlayground {

    static enum Sex {
        MALE, FEMALE, OTHER
    };

    static class Person {
        private int age;
        private final List<Person> children = new ArrayList<>();
        private Person spouse;
        private Sex sex;
        private String name;

        public Person(String name, int age, Sex sex, Person... children) {
            this(name, age, sex, Arrays.asList(children));
        }
        
        public Person(String name, int age, Sex sex, List<Person> children) {
            super();
            this.name = name;
            this.age = age;
            this.sex = sex;
            if(children != null) {
                this.children.addAll(children);
            }
        }

        public Sex getSex() {
            return sex;
        }

        public void setSex(Sex sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Person getSpouse() {
            return spouse;
        }

        public void setSpouse(Person spouse) {
            this.spouse = spouse;
        }

        public List<Person> getChildren() {
            return children;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    
    public static void main(String[] args) {
        
        Person paul = new Person("Paul", 19, Sex.MALE);
        Person john = new Person("John", 48, Sex.MALE, paul);
        Person agatha = new Person("Agatha", 42, Sex.FEMALE, paul);
        agatha.setSpouse(john);
        john.setSpouse(agatha);
        
        Person christina = new Person("Christina", 5, Sex.FEMALE);
        Person linda = new Person("Linda", 7, Sex.FEMALE);
        Person singleDad = new Person("Richard", 42, Sex.MALE, christina, linda);
        
        matchPerson(agatha);
        matchPerson(singleDad);
        matchPerson(linda);
    }

    private static void matchPerson(Person person) {
        
final Var<String> momName = var();
final IntVar momAge = intVar();
final Var<Person> child = var();

// capturing pattern
ObjectPattern<?> momWithGrownUpSon = pattern(Person.class)
        .check(Person::getSex, eq(Sex.FEMALE))
        .readI(momAge, Person::getAge)
        .read(momName, Person::getName)
        .exists(child, Person::getChildren, pattern(Person.class)
                .check(Person::getSex, eq(Sex.MALE))
                .checkI(Person::getAge, ge(18))
         );
        
        // non capturing pattern. could be singleton
        ObjectPattern<?> singleFather = pattern(Person.class)
                .check(Person::getSpouse, isNull)
                .check(Person::getChildren, notEmpty);
        
        match(person)
        .caseOf(momWithGrownUpSon, p -> printMotherSonMsg(child.val(), momName.val(), momAge.val()) )
        .caseOf(singleFather, p -> printSingleFather(p) )
        .otherwise(ignore(sysout("No Match")));
    }

    private static void printSingleFather(Person p) {
        System.out.println("Single Father named "+p.getName());
    }

    
    public static void printMotherSonMsg(Person child, String momName, int momAge) {
        System.out.println(momName);
    }
}
