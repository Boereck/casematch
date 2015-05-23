package de.boereck.matcher.helpers.found;

/**
 *  This class represents a result of searching for elements, e.g. in a collection, where
 *  all elements match a given criterion.
 */
public final class FoundAll extends Found {

    private final long count;

    FoundAll(long count) {
        this.count = count;
    }

    @Override
    public FindType type() {
        return FindType.all;
    }

    @Override
    public long count() {
        return count;
    }
}
