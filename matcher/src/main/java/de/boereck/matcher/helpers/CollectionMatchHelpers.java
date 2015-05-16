package de.boereck.matcher.helpers;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;
import de.boereck.matcher.function.testable.TestableFunction;
import de.boereck.matcher.helpers.found.Found;

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

    public static <I, O> Stream<O> filter(Stream<I> s, Class<O> clazz) {
        Objects.requireNonNull(clazz);
        return s.filter(o -> clazz.isInstance(o)).map(o -> (O) o);
    }

    /**
     * This stream Collector
     *
     * @param <I>
     * @return
     */
    public static <I> Collector<I, ?, Optional<List<I>>> toNonEmptyList() {
        return Collectors.collectingAndThen(Collectors.toList(), l -> l.size() > 0 ? Optional.of(l) : Optional.empty());
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
     */
    public static <T> Found findCount(Collection<T> c, Predicate<? super T> p) {
        Objects.nonNull(p);
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
    private static <T> java.util.stream.Collector<T, ?, Found> findCollector(Predicate<T> p) {
        // mutable aggreagate used in collector
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

    public static <I> ToLongFunction<Collection<I>> count(Predicate<? super I> p) {
        Objects.requireNonNull(p);
        return c -> c == null ? 0 : c.stream().filter(p).count();
    }

    /**
     * @param p   Predicate that is used to check for Must not be {@code null}
     * @param <I>
     * @return Function counting the elements of an input collection that are tested
     * positive with the given predicate {@code p}.
     */
    public static <I> TestableFunction<Collection<I>, Found> findCount(Predicate<? super I> p) {
        Objects.requireNonNull(p);
        return c -> findCount(c, p);
    }

    /**
     * Returns a predicate that checks if a collection is not {@code null} and contains an element that matches the predicate
     * {@code p}.
     *
     * @param p   predicate that will be used to check if an element of a given collection
     *            matches. Must not be {@code null}.
     * @param <I> Types of elements in the collection to be checked by the returned predicate.
     * @return Predicate that checks if a collection is not {@code null} and contains an element matching the predicate {@code p}.
     */
    public static <I> AdvPredicate<Collection<I>> exists(Predicate<? super I> p) {
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
     * Returns a predicate that checks if a collection is not {@code null} and all its elements found the predicate
     * {@code p}.
     *
     * @param p   predicate that will be used to check if all elements of a given collection
     *            matches. Must not be {@code null}
     * @param <I> Types of elements in the collection to be checked by the returned predicate.
     * @return Predicate that checks if a collection is not {@code null} and contains an element matching the predicate {@code p}.
     */
    public static <I> AdvPredicate<Collection<I>> forAll(Predicate<? super I> p) {
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
     */
    public static <I> AdvPredicate<Collection<I>> Ɐ(Predicate<? super I> p) {
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
