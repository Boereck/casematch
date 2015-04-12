package de.boereck.matcher.helpers;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

/**
 * This class provides static helper methods for matching collection objects.
 *
 * @author Max Bureck
 */
public class CollectionMatchHelpers {

    private CollectionMatchHelpers() {
        throw new IllegalStateException("Class CollectionMatchHelpers must not be instantiated");
    }

    /**
     * Returns a predicate checking if a collection contains the value passed in
     * as parameter {@code t}. The predicate will return {@code false} if the
     * collection passed to the returned predicate is {@code null}.
     *
     * @param t   element that is checked to be in a given collection by the returned predicate.
     * @param <I> Type of elements in the checked collection
     * @param <T> Type of the object that is checked to be in a collection
     * @return A predicate checking if object {@code t} is in a given collection.
     */
    public static <I, T extends I> AdvPredicate<Collection<I>> contains(T t) {
        return c -> c != null && c.contains(t);
    }

    /**
     * Returns a predicate that checks if a collection is not {@code null} and contains an element that matches the predicate
     * {@code p}.
     *
     * @param p   predicate that will be used to check if an element of a given collection
     *            matches.
     * @param <I> Types of elements in the collection to be checked by the returned predicate.
     * @return Predicate that checks if a collection is not {@code null} and contains an element matching the predicate {@code p}.
     */
    public static <I> AdvPredicate<Collection<I>> exists(Predicate<? super I> p) {
        Objects.requireNonNull(p);
        return c -> c != null && c.stream().anyMatch(p);
    }

    /**
     * Returns a predicate that checks if a collection is not {@code null} and all its elements match the predicate
     * {@code p}.
     *
     * @param p   predicate that will be used to check if all elements of a given collection
     *            matches.
     * @param <I> Types of elements in the collection to be checked by the returned predicate.
     * @return Predicate that checks if a collection is not {@code null} and contains an element matching the predicate {@code p}.
     */
    public static <I> AdvPredicate<Collection<I>> forAll(Predicate<? super I> p) {
        Objects.requireNonNull(p);
        return c -> c != null && c.stream().allMatch(p);
    }

    /**
     * Predicate checking if a collection is not {@code null} and not empty.
     */
    public static final AdvPredicate<Collection<?>> notEmpty = c -> c != null && !c.isEmpty();

    /**
     * Predicate checking if a collection is not {@code null} and not empty.
     */
    @SuppressWarnings("rawtypes") // explicitly introduced for raw types
    public static final AdvPredicate<Collection> rawNotEmpty = c -> c != null && !c.isEmpty();

    /**
     * References a function that takes an {@link Object} and checks if the object is instance of {@link Collection}.
     * If so, the function will return an {@link Optional} with the input object casted to Collection. Otherwise
     * the function will return an empty Optional.
     */
    public static final OptionalMapper<Object, Collection<?>> castToCollection = castToCollection();

    /**
     * References a function that takes an instance of I and checks if the object is instance of {@link Collection}.
     * If so, the function will return an {@link Optional} with the input object casted to Collection. Otherwise
     * the function will return an empty Optional.
     *
     * @param <I> Type I element is of that should be checked if it is instance of Collection.
     * @return Mapper function checking if an object, instance of I, is not {@code null} and instance of Collection. If so,
     * the mapper will return an Optional of the input object casted to Collection, otherwise it returns
     * an empty Optional.
     */
    public static final <I> OptionalMapper<I, Collection<?>> castToCollection() {
        return o -> o != null && (o instanceof Collection) ? Optional.of((Collection<?>) o) : Optional.empty();
    }

    /**
     * References a function that takes an instance of I and checks if the object is instance of {@link List}.
     * If so, the function will return an {@link Optional} with the input object casted to List. Otherwise
     * the function will return an empty Optional.
     *
     * @param <I> Type I element is of that should be checked if it is instance of List.
     * @return Mapper function checking if an object, instance of I, is not {@code null} and instance of List. If so,
     * the mapper will return an Optional of the input object casted to List, otherwise it returns
     * an empty Optional.
     */
    public static final <I> OptionalMapper<I, List<?>> castToList() {
        return o -> o != null && (o instanceof List) ? Optional.of((List<?>) o) : Optional.empty();
    }

    /**
     * References a function that takes an instance of I and checks if the object is instance of {@link Map}.
     * If so, the function will return an {@link Optional} with the input object casted to Map. Otherwise
     * the function will return an empty Optional.
     *
     * @param <I> Type I element is of that should be checked if it is instance of Map.
     * @return Mapper function checking if an object, instance of I, is not {@code null} and instance of Map. If so,
     * the mapper will return an Optional of the input object casted to Map, otherwise it returns
     * an empty Optional.
     */
    public static final <I> OptionalMapper<I, Map<?, ?>> castToMap() {
        return o -> o != null && (o instanceof Map) ? Optional.of((Map<?, ?>) o) : Optional.empty();
    }

    /**
     * Returns a Predicate checking if an input object of type I is not {@code null} and instance of
     * Collection.
     *
     * @param <I> type of object to be checked if instance of Collection
     * @return predicate checking if input object is instance of Collection.
     */
    public static final <I> TypeCheck<I, Collection<?>> isCollection() {
        return o -> o != null && (o instanceof Collection);
    }
}
