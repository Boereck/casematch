package de.boereck.matcher.helpers;

import static de.boereck.matcher.helpers.MatchHelpers.typed;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import de.boereck.matcher.function.optionalmap.OptionalMapper;
import de.boereck.matcher.function.predicate.AdvPredicate;

/**
 * Provides static helper functions for defining matches in CaseMatchers based on String values.
 * <p>
 * This class is not intended to be instantiated or sub-classed.
 * </p>
 *
 * @author Max Bureck
 */
public final class StringMatchHelpers {

    private StringMatchHelpers() {
        throw new IllegalStateException("Class StringMatchHelpers must not be instantiated");
    }

    /**
     * Predicate checking if an object is instance of {@link String}.
     */
    public static final TypeCheck<Object, String> isString = typed(String.class);

    /**
     * Returns true if the parameter {@code s} is {@code null} or if {@code s} is an empty String (see
     * {@link String#isEmpty()}.
     *
     * @param s to be checked if is null or empty
     * @return true if {@code s} is {@code null} or an empty String.
     */
    private static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Will return true if parameter {@code s} is not null and not empty.
     * @param s to be checked if not empty.
     * @return false if {@code s} is {@code null} or empty. True otherwise.
     */
    private static boolean notEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    /**
     * Predicate checking if the input string is either null or an empty String
     */
    public static final AdvPredicate<String> strIsEmpty = StringMatchHelpers::isEmpty;

    /**
     * Predicate checking if the input string is not null and not an empty String.
     */
    public static final AdvPredicate<String> strNotEmpty = StringMatchHelpers::notEmpty;

    /**
     * Returns predicate, checking if the tested string starts with the string passed in as parameter {@code other}. Input
     * values to the returned predicate can be {@code null}, in this case the predicate will return {@code false}.
     * @param other predicate inputs will be checked if they start with this String. This parameter must not be {@code null}.
     * @return predicate checking if input strings start with string {@code other}
     * @throws NullPointerException thrown, if {@code other} is {@code null}.
     */
    public static AdvPredicate<String> startsWith(String other) throws NullPointerException {
        Objects.requireNonNull(other);
        return str -> str != null && str.startsWith(other);
    }

    /**
     * Returns predicate, checking if the tested string starts with the string passed in as parameter {@code other}. The
     * input will be checked starting with the given offset {@code toffset}. Input values to the returned predicate can be
     * {@code null}, in this case the predicate will return {@code false}.
     *
     * @param other   predicate inputs will be checked if they start with this String.
     *                This parameter must not be {@code null}.
     * @param toffset defines where to begin looking in the input string to the predicate.
     * @return predicate checking if input strings start with string {@code other}
     * @throws NullPointerException thrown, if {@code other} is {@code null}.
     */
    public static Predicate<String> startsWith(String other, int toffset) throws NullPointerException {
        Objects.requireNonNull(other);
        return str -> str != null && str.startsWith(other, toffset);
    }

    /**
     * Returns a predicate checking if tested strings found the given regular expression {@code regEx}.
     *
     * @param regEx the regular expressions inputs to the returned predicate will be checked against.
     *              Must not be {@code null}.
     * @return predicate checking if tested strings are matching the given regular expression {@code regEx}
     * @throws NullPointerException thrown, if {@code regEx} is {@code null}.
     * @throws PatternSyntaxException if expression syntax of {@code regEx} is invalid.
     */
    public static AdvPredicate<String> matches(String regEx) throws NullPointerException, PatternSyntaxException {
        Objects.requireNonNull(regEx);
        final Pattern compiledPattern = Pattern.compile(regEx);
        return str -> compiledPattern.matcher(str).matches();
    }

    /**
     * Returns a function mapping from {@code String} to {@code Optional&lt;String&gt;}, based on the given
     * regular expression {@code regEx}. The returned function will return an empty optional if the input string
     * to the function does not match the given regular expression. If the regular expression does match, the
     * returned optional will contain the input string.
     * @param regEx regular expression that is being checked in the returned function. Must not be {@code null}.
     * @return function mapping from {@code String} to {@code Optional&lt;String&gt;}, based on the given
     *         regular expression {@code regEx}.
     * @throws NullPointerException thrown, if {@code regEx} is {@code null}.
     * @throws PatternSyntaxException if expression syntax of {@code regEx} is invalid.
     */
    public static OptionalMapper<String, String> matching(String regEx) throws NullPointerException, PatternSyntaxException  {
        Objects.requireNonNull(regEx);
        final Pattern compiledPattern = Pattern.compile(regEx);
        return s -> {
            final Matcher m = compiledPattern.matcher(s);
            return m.matches() ? Optional.of(s) : Optional.empty();
        };
        // s -> System.out.println(s)
    }

    /**
     * Returns a function mapping from {@code String} to {@code Optional&lt;Matcher&gt;}, based on the given
     * regular expression {@code regEx}. The returned function will return an empty optional if the input string
     * to the function does not match the given regular expression. If the regular expression does match, the
     * returned optional will contain the Matcher that is the result of the input string matching against regular
     * expression {@code regEx}.
     * @param regEx regular expression that is being checked in the returned function. Must not be {@code null}.
     * @return function mapping from {@code String} to {@code Optional&lt;Matcher&gt;}, based on the given
     *         regular expression {@code regEx}.
     * @throws NullPointerException thrown, if {@code regEx} is {@code null}.
     * @throws PatternSyntaxException if expression syntax of {@code regEx} is invalid.
     */
    public static OptionalMapper<String, Matcher> matcher(String regEx) throws NullPointerException, PatternSyntaxException {
        Objects.requireNonNull(regEx);
        final Pattern compiledPattern = Pattern.compile(regEx);
        return s -> {
            final Matcher m = compiledPattern.matcher(s);
            return m.matches() ? Optional.of(m) : Optional.empty();
        };
    }

    /**
     * Chars with special meaning in regular expressions
     */
    private static final char[] specialChar = {'.', '$', '|', '(', ')', '[', '{', '^', '?', '*', '+', '\\'};

    static {
        // sort special characters, so binary search can be used
        Arrays.sort(specialChar);
    }

    private static boolean isSingleCharNotSpecial(String regEx) {
        return regEx.length() == 1 && Arrays.binarySearch(specialChar, regEx.charAt(0)) < 0;
    }

    /**
     * Returns a function that takes string and splits it according to the regular expression {@code regEx}. Tf the input to
     * the function is null, the returned Optional will be empty, otherwise the returned Optional will contain an array of
     * strings computed from the splitting of the input string.
     * <p>
     * This method <em>or</em> the returned method may throw an PatternSyntaxException if the regular expression's syntax is
     * invalid
     * </p>
     * @param regEx the delimiting regular expression for splitting the input array. Must not be {@code null}.
     * @return function splitting an input string according to parameter {@code regEx}
     * @throws PatternSyntaxException May be thrown if {@code regEx}' syntax is invalid.
     * @throws NullPointerException thrown, if {@code regEx} is {@code null}.
     */
    public static OptionalMapper<String, String[]> split(String regEx) throws PatternSyntaxException, NullPointerException {
        // TODO check if fast path is actually faster
        Objects.requireNonNull(regEx);
        // fast path if regEx is one char and not a special char
        if (isSingleCharNotSpecial(regEx)) {
            return i -> i == null ? Optional.empty() : Optional.of(i.split(regEx));
        } else {
            // compile pattern and use for split
            final Pattern splitPattern = Pattern.compile(regEx);
            return i -> i == null ? Optional.empty() : Optional.of(splitPattern.split(i));
        }
    }

    /**
     * Returns a function that takes string and splits it according to the regular expression {@code regEx}. Tf the input to
     * the function is null, the returned Optional will be empty, otherwise the returned Optional will contain an array of
     * strings computed from the splitting of the input string. The input string will be split in at most as many elements as
     * specified in parameter {@code limit}. This affects the length of the array returned from the split.
     * <p>
     * This method <em>or</em> the returned method may throw an PatternSyntaxException if the regular expression's syntax is
     * invalid
     * </p>
     * @param regEx the delimiting regular expression for splitting the input array
     * @param limit maximum amount of elements in result of split
     * @return function splitting an input string according to parameter {@code regEx}
     * @throws PatternSyntaxException May be thrown if the expression's syntax is invalid
     * @throws NullPointerException thrown, if {@code regEx} is {@code null}.
     */
    public static OptionalMapper<String, String[]> split(String regEx, int limit) throws NullPointerException, PatternSyntaxException {
        // fast path if regEx is one char and not a special char
        Objects.requireNonNull(regEx);
        if (isSingleCharNotSpecial(regEx)) {
            return i -> i == null ? Optional.empty() : Optional.of(i.split(regEx, limit));
        } else {
            // compile pattern and use for split
            final Pattern splitPattern = Pattern.compile(regEx);
            return i -> i == null ? Optional.empty() : Optional.of(splitPattern.split(i, limit));
        }
    }

    /**
     * Provides a predicate checking if a string equals the given string {@code comp} ignoring the case of both input and
     * reference string. Both reference and input string are allowed to be {@code null}.
     *
     * @param comp reference string input strings are compared with, ignoring case
     * @return a predicate checking if an input string equals the string {@code comp}.
     */
    public static AdvPredicate<String> eqIgnoreCase(String comp) {
        return s -> (s == null) ? comp == null : s.equalsIgnoreCase(comp);
    }

    /**
     * Provides a predicate checking if a string has at least the length {@code minLen}. The input parameter to the predicate
     * may be {@code null}. In this case the predicate will return false.
     *
     * @param minLen the input string to the returned predicate will be checked to have at least the length equal to this
     *               parameter
     * @return predicate checking input strings to have at least length {@code minLen}.
     */
    public static AdvPredicate<String> minLength(int minLen) {
        return s -> s != null && s.length() >= minLen;
    }

    /**
     * Provides a predicate checking if a string has at least the length of the value provided by {@code minSupplier}. The
     * input parameter to the predicate may be {@code null}. In this case the predicate will return false.
     *
     * @param minSupplier the input string to the returned predicate will be checked to have at least the length equal to value
     *                    provided by this supplier.
     * @return predicate checking input strings to have at least length equal to value provided by {@code minSupplier}.
     * @throws NullPointerException thrown, if {@code minSupplier} is {@code null}.
     */
    public static AdvPredicate<String> minLength(IntSupplier minSupplier) throws NullPointerException {
        Objects.requireNonNull(minSupplier);
        return s -> s != null && s.length() >= minSupplier.getAsInt();
    }

    /**
     * Provides a predicate checking if a string has at most the length {@code maxLen}. The input parameter to the predicate
     * may be {@code null}. In this case the predicate will return false.
     *
     * @param maxLen the input string to the returned predicate will be checked to have at most the length equal to this
     *               parameter
     * @return predicate checking input strings to have at most length {@code maxLen}.
     */
    public static AdvPredicate<String> maxLength(int maxLen) {
        return s -> s != null && s.length() <= maxLen;
    }

    /**
     * Provides a predicate checking if a string has at most the length of the value provided by {@code maxSupplier}. The
     * input parameter to the predicate may be {@code null}. In this case the predicate will return false.
     *
     * @param maxSupplier the input string to the returned predicate will be checked to have at most the length equal to value
     *                    provided by this supplier.
     * @return predicate checking input strings to have at most length equal to value provided by {@code maxSupplier}.
     * @throws NullPointerException thrown, if {@code maxSupplier} is {@code null}.
     */
    public static AdvPredicate<String> maxLength(IntSupplier maxSupplier) throws NullPointerException {
        Objects.requireNonNull(maxSupplier);
        return s -> s != null && s.length() <= maxSupplier.getAsInt();
    }
}
