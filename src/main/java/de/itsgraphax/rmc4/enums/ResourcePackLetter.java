package de.itsgraphax.rmc4.enums;

public enum ResourcePackLetter {
    UNKNOWN("\uf000"),
    COOLDOWN_BAR("\uf001"),
    EMPTY_TOKEN("\uf002"),
    DUPE_TOKEN("\uf003"),
    DUPE_TOKEN_BROKEN("\uf004");

    private final String text;

    ResourcePackLetter(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
