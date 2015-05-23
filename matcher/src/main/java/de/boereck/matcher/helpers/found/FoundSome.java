package de.boereck.matcher.helpers.found;

/**
 *  This class represents a result of searching for elements, e.g. in a collection, where
 *  some, but not all elements match a given criterion.
 */
public final class FoundSome extends Found {
    private final long count;

    FoundSome(long count) {
        this.count = count;
    }

    @Override
    public FindType type() {
        return FindType.some;
    }

    @Override
    public long count() {
        return count;
    }
}
