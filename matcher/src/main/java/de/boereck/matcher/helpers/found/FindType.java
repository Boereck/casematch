package de.boereck.matcher.helpers.found;

/**
 * Type of a find of elements in a collection.
 */
public enum FindType {

    /**
     * Represents the case that all elements in a collection
     * match a certain criteria
     */
    all,

    /**
     * Represents the case that some, but not all elements in a collection
     * match a certain criteria
     */
    some,

    /**
     * Represents the case that no elements in a collection
     * match a certain criteria
     */
    none
}
