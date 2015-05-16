package de.boereck.matcher.helpers.found;

/**
 * Created by mbu on 03.05.2015.
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
