package de.boereck.matcher.helpers.found;

/**
 *  This class represents a result of searching for elements, e.g. in a collection, where
 *  no elements match a given criterion.
 */
public final class FoundNone extends Found {

    public static final FoundNone INSTANCE = new FoundNone();

    private FoundNone() {
    }

    @Override
    public FindType type() {
        return FindType.none;
    }

    @Override
    public long count() {
        return 0;
    }
}
