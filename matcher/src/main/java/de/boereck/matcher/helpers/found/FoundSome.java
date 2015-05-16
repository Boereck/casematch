package de.boereck.matcher.helpers.found;

/**
 * Created by mbu on 03.05.2015.
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
