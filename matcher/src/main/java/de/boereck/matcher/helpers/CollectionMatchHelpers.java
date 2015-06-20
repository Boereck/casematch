package de.boereck.matcher.helpers;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;
import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.function.testable.TestableToLongFunction;
import de.boereck.matcher.helpers.found.*;

import static de.boereck.matcher.eager.EagerMatcher.match;
import static java.util.stream.Collectors.toList;

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
     * This method is a shortcut for {@code c.stream()} if c is not {@code null}. If c is {@code null},
     * this method will return an empty stream.
     *
     * @param c   Collection to create a Stream from. If {@code null}, an empty stream will be returned.
     * @param <T> Type of elements in collection.
     * @return Stream created from collection {@code c}.
     */
    public static <T> Stream<T> $(Collection<T> c) {
        if (c == null) {
            return Stream.empty();
        }
        // else
        return c.stream();
    }

    /**
     * Shortcut for {@code filter($(c),clazz)}
     *
     * @param c     collection to be filtered
     * @param clazz filter type of elements that will remain in the returned Stream
     * @param <I>   type of input elements
     * @param <O>   type of output elements
     * @return Stream of elements in collection {@code c} that are of type O
     */
    public static <I, O> Stream<O> filter(Collection<I> c, Class<O> clazz) {
        return filter($(c), clazz);
    }

    /**
     * This is a shortcut for {@code (Stream<O>) s.filter(o -> clazz.isInstance(o))}.
     *
     * @param s     stream that's elements are filtered by type {@code clazz}. Must not be {@code null}.
     * @param clazz Type the elements of the input stream should be of to be part of the returned result stream.
     *              Must not be {@code null}.
     * @param <I>   Type of elements in input stream
     * @param <O>   Type of element remaining in the output stream
     * @return input stream filtered by type {@code O}
     * @throws NullPointerException when {@code clazz} or {@code s} is {@code null}.
     */
    @SuppressWarnings("unchecked") // we know the cast is safe, since all elements are of type O
    public static <I, O> Stream<O> filter(Stream<I> s, Class<O> clazz) throws NullPointerException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(s);
        return (Stream<O>) s.filter(clazz::isInstance);
    }

    /**
     * Returns a function filtering an input collection and returning the elements from the source
     * collection filtered by the given {@code type} in a new {@link List}. The optional returned by
     * the resulting function will be empty if the input collection is {@code null}, or the input collection
     * contains no element instance of {@code type}. If at least one element of the input collection is
     * instance of {@code type}, the output optional will hold a list of all elements of the input
     * collection instance of {@code type}.
     *
     * @param type class that input elements are filtered by. Must not be {@code null}.
     * @param <I>  element type of input collection
     * @param <O>  element type of output list
     * @return function filtering the input elements by the given {@code type}.
     * @throws NullPointerException if {@code type} is {@code null}.
     */
    @SuppressWarnings("unchecked") // we know cast is safe, we checked elements to be instance of O
    public static <I, O> OptionalMapper<Collection<I>, List<O>> filterCollection(Class<O> type) throws NullPointerException {
        Objects.requireNonNull(type);
        return c -> filter(c, type).collect(toNonEmptyList());
    }

    /**
     * Returns a function filtering an input collection and returning the elements from the source
     * collection matching predicate {@code test} in a new {@link List}. The optional returned by
     * the resulting function will be empty if the input collection is {@code null}, or the input collection
     * contains no element conforming to predicate {@code test}. If at least one element of the input collection is
     * conforming {@code test}, the output optional will hold a list of all elements of the input
     * collection matching {@code test}.
     *
     * @param test predicate that input elements are filtered by. Must not be {@code null}.
     * @param <I>  element type of input collection
     * @return function filtering the input elements by the given predicate {@code test}.
     * @throws NullPointerException if {@code test} is {@code null}.
     */
    public static <I> OptionalMapper<Collection<I>, List<I>> filterCollection(Predicate<I> test) {
        Objects.requireNonNull(test);
        return c -> $(c).filter(test).collect(toNonEmptyList());
    }

    /**
     * This method returns a {@link Collector} for {@link Stream}s, returning an {@link Optional} of
     * a list of all the elements that are part of the stream. If the list is empty (no element left in the stream),
     * the optional will be empty, otherwise it will contain the list with the collected elements of the stream.
     *
     * @param <I> Type of elements in stream
     * @return Collector, returning either an empty optional, if the stream it is used on does not contain elements,
     * or an optional holding the list of collected elements, if there were elements in the stream.
     */
    public static <I> Collector<I, ?, Optional<List<I>>> toNonEmptyList() {
        return Collectors.collectingAndThen(toList(), l -> l.size() > 0 ? Optional.of(l) : Optional.empty());
    }

    /**
     * This method traverses the collection and checks how many elements in that collection
     * are tested positive with the given predicate. Depending on the result, a subclass of
     * {@link Found} will be returned. This will either be {@link de.boereck.matcher.helpers.found.FoundAll},
     * {@link de.boereck.matcher.helpers.found.FoundNone} or {@link de.boereck.matcher.helpers.found.FoundSome}.
     * Be aware that under the covers java stream APIs may be used.
     *
     * @param c   collection that will be traversed and elements checked for predicate {@code p}.
     * @param p   Predicate, every element from {@code c} is checked with. Must not be {@code null}.
     * @param <T> Type of elements of {@code c}.
     * @return Found instance, based on how many elements in {@code c} pass predicate {@code p}.
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static <T> Found findCount(Collection<T> c, Predicate<? super T> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return $(c).collect(findCollector(p));
    }

    /**
     * This method will create a {@link Collector} that produces {@link Found} values
     * based on how many elements of a stream satisfy predicate {@code p}
     *
     * @param p   the Predicate elements of a stream should satisfy. Based on this condition
     *            the returned Collector returns a Find instance.
     * @param <T> Element type the returned Collector works on (type of stream)
     * @return Collector producing {@link Found} instance based on how many elements
     * of a stream satisfy condition {@code p}.
     */
    static <T> java.util.stream.Collector<T, ?, Found> findCollector(Predicate<T> p) {
        // mutable aggregate used in collector
        class FoundAggregate {
            boolean allMatching = true;
            long count = 0L;

            Found toFound() {
                if (count == 0L) {
                    return Found.NONE;
                } else {
                    if (allMatching) {
                        return Found.all(count);
                    } else {
                        return Found.some(count);
                    }
                }
            }
        }
        return new Collector<T, FoundAggregate, Found>() {

            @Override
            public Supplier<FoundAggregate> supplier() {
                return FoundAggregate::new;
            }

            @Override
            public BiConsumer<FoundAggregate, T> accumulator() {
                return (fa, t) -> {
                    // if the element matches the predicate, we can increase
                    // the count, otherwise not every element matches.
                    if (p.test(t)) {
                        fa.count++;
                    } else {
                        fa.allMatching = false;
                    }
                };
            }

            @Override
            public BinaryOperator<FoundAggregate> combiner() {
                return (a, b) -> {
                    // only if both aggregates recorded
                    // positive predicate evaluations for all
                    // elements, the same is true for the combined result
                    a.allMatching &= b.allMatching;
                    a.count += b.count;
                    return a;
                };
            }

            @Override
            public Function<FoundAggregate, Found> finisher() {
                return FoundAggregate::toFound;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Characteristics.UNORDERED);
            }
        };
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
     * Returns a function that counts how many elements of an input collection are tested positive with the given
     * predicate {@code p}. If the input collection is {@code null}, the returned count will be 0.
     *
     * @param p   used to check how many elements in the input collection match this predicate.
     * @param <I> Type of elements in the input collection
     * @return function that counts how many elements of an input collection are tested positive with the given
     * predicate {@code p}. If the input collection is {@code null}, the returned count will be 0.
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static <I> TestableToLongFunction<Collection<I>> count(Predicate<? super I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return c -> c == null ? 0 : c.stream().filter(p).count();
    }

    /**
     * This function returns a function that tests if and how many elements in a collection match
     * the given predicate {@code p}.
     *
     * @param p   Predicate that is used to check for elements in a given collection. Must not be {@code null}.
     * @param <I> Type of elements in input collection
     * @return Function counting the elements of an input collection that are tested
     * positive with the given predicate {@code p}.
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public static <I> TestableFunction<Collection<I>, Found> findCount(Predicate<? super I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return c -> findCount(c, p);
    }

    /**
     * This method returns a function that returns an optional of {@code Found}, that holds a value, if elements
     * were found, and an empty optional, if no elements were found that match predicate {@code p}.
     *
     * @param p   used to check how many elements match this predicate. Must not be {@code null}.
     * @param <I> Type of elements of input collection to be checked.
     * @return Function that returns an optional of {@code Found}, that holds a value, if elements
     * were found, and an empty optional, if no elements were found that match predicate {@code p}.
     * @throws NullPointerException if {@code p} is {@code null}.
     */
    public static <I> OptionalMapper<Collection<I>, Found> findCountExisting(Predicate<? super I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return findCount(p).filter(f -> !(f instanceof FoundNone));
    }

    /**
     * Returns a predicate that checks if a collection is not {@code null} and contains an element that matches the predicate
     * {@code p}.
     *
     * @param p   predicate that will be used to check if an element of a given collection
     *            matches. Must not be {@code null}.
     * @param <I> Types of elements in the collection to be checked by the returned predicate.
     * @return Predicate that checks if a collection is not {@code null} and contains an element matching the predicate {@code p}.
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public static <I> AdvPredicate<Collection<I>> exists(Predicate<? super I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return c -> c != null && c.stream().anyMatch(p);
    }

    /**
     * Redirect to method {@link CollectionMatchHelpers#exists(Predicate)}.
     *
     * @param p   predicate that will be used to check if an element of a given collection
     *            matches. Must not be {@code null}.
     * @param <I> Types of elements in the collection to be checked by the returned predicate.
     * @return Predicate that checks if a collection is not {@code null} and contains an element matching the predicate {@code p}.
     */
    public static <I> AdvPredicate<Collection<I>> Ǝ(Predicate<? super I> p) {
        return exists(p);
    }

    /**
     * Returns a function from collection to an optional of collection, containing the elements of the
     * input collection filtered by predicate {@code p}, if such elements exists. Otherwise the method
     * will return an empty optional.
     *
     * @param p   is used to check if an input collection contains elements that pass this predicate.
     * @param <I> type of elements in input collection
     * @return function filtering element of input collection by predicate {@code p} returning an optional
     * that holds the filtered elements, if such elements exist.
     * @throws NullPointerException will be thrown if {@code p} is {@code null}.
     */
    public static <I> OptionalMapper<Collection<I>, List<I>> filterExists(Predicate<? super I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return c -> c == null ? Optional.empty() : c.stream().filter(p).collect(toNonEmptyList());
    }

    /**
     * Returns a predicate that checks if a collection is not {@code null} and all its elements found the predicate
     * {@code p}.
     *
     * @param p   predicate that will be used to check if all elements of a given collection
     *            matches. Must not be {@code null}
     * @param <I> Types of elements in the collection to be checked by the returned predicate.
     * @return Predicate that checks if a collection is not {@code null} and contains an element matching the predicate {@code p}.
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public static <I> AdvPredicate<Collection<I>> forAll(Predicate<? super I> p) throws NullPointerException {
        Objects.requireNonNull(p);
        return c -> c != null && c.stream().allMatch(p);
    }

    /**
     * Redirect to method {@link CollectionMatchHelpers#forAll(Predicate)}.
     *
     * @param p   predicate that will be used to check if all elements of a given collection
     *            matches. Must not be {@code null}
     * @param <I> Types of elements in the collection to be checked by the returned predicate.
     * @return Predicate that checks if a collection is not {@code null} and contains an element matching the predicate {@code p}.
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public static <I> AdvPredicate<Collection<I>> Ɐ(Predicate<? super I> p) throws NullPointerException {
        return forAll(p);
    }

    /**
     * Returns a function mapping from a collection to an optional of a list. The optional
     * will contain the list of elements in the collection that are instance of O. If the input
     * collection does not contain any element that is instance of O, an empty optional will be
     * returned from the function.
     *
     * @param clazz Class of type O, used to checke if elements of input collection are of this type.
     *              Must not be {@code null}.
     * @param <I>   Element type of input collection
     * @param <O>   Element type of output list
     * @return Function performing a filtered map to list holding all elements of the input collection
     * that are instance of O.
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static <I, O> OptionalMapper<Collection<I>, List<O>> elementsOfType(Class<O> clazz) throws NullPointerException {
        Objects.requireNonNull(clazz);
        return i -> filter(i, clazz).collect(toNonEmptyList());
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
    public static <I> OptionalMapper<I, Collection<?>> castToCollection() {
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
    public static <I> OptionalMapper<I, List<?>> castToList() {
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
    public static <I> OptionalMapper<I, Map<?, ?>> castToMap() {
        return o -> o != null && (o instanceof Map) ? Optional.of((Map<?, ?>) o) : Optional.empty();
    }

    /**
     * Returns a function that takes a map and returns an optional, holding the value
     * associated with the given {@code key}, or an empty optional, if the input map is
     * {@code null} or there is no value or a {@code null} value associated with the {@code key}.
     *
     * @param key Be aware that it depends on the implementation of the input map if
     *            {@code null} keys are allowed or not. This method will not check if
     *            this parameter is {@code null} or not.
     * @param <K> Type of key of the input map
     * @param <V> Type of value of the input map
     * @return Function returning a non-empty Optional with the value of the input map
     * that is associated with the given key {@code key}. If the input map does not hold
     * a value for the given key, or the value is {@code null}, the Optional returned from
     * the function will be empty.
     */
    public static <K, V> OptionalMapper<Map<K, V>, V> hasValueFor(K key) {
        return m -> m == null ? Optional.empty() : Optional.ofNullable(m.get(key));
    }

    /**
     * Returns a Predicate checking if an input object of type I is not {@code null} and instance of
     * Collection.
     *
     * @param <I> type of object to be checked if instance of Collection
     * @return predicate checking if input object is instance of Collection.
     */
    public static <I> TypeCheck<I, Collection<?>> isCollection() {
        return o -> o != null && (o instanceof Collection);
    }

}
