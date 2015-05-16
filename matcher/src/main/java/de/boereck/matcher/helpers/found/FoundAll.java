package de.boereck.matcher.helpers.found;

/**
 * Created by mbu on 03.05.2015.
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
