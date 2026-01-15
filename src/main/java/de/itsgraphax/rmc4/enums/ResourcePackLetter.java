package de.itsgraphax.rmc4.enums;

public enum ResourcePackLetter {
    UNKNOWN("\uf000"),
    COOLDOWN_BAR("\uf001"),
    EMPTY("\uf002"),
    EMPTY_TOKEN("\uf00a"),
    DUPE_TOKEN("\uf00b"),
    DUPE_TOKEN_BROKEN("\uf00c"),
    DASH_TOKEN("\uf00d"),
    DASH_TOKEN_BROKEN("\uf00e"),
    N0("\uf014"),
    N1("\uf015"),
    N2("\uf016"),
    N3("\uf017"),
    N4("\uf018"),
    N5("\uf019"),
    N6("\uf01a"),
    N7("\uf01b"),
    N8("\uf01c"),
    N9("\uf01d"),
    NM("\uf01e"),
    NS("\uf01f"),
    NR("\uf020"),
    NE("\uf021"),
    NA("\uf022"),
    ND("\uf023"),
    NY("\uf024"),
    NMIN("\uf025");

    private final String text;

    ResourcePackLetter(final String text) {
        this.text = text;
    }

    public static String fromChar(char c) {
        if (c == '-') {
            return NMIN.toString();
        } else if (c == ' ') {
            return EMPTY.toString();
        }

        ResourcePackLetter value;
        try {
            value = valueOf("N" + c);
        } catch (IllegalArgumentException e) {
            value = UNKNOWN;
        }

        return value.toString();
    }

    @Override
    public String toString() {
        return text;
    }
}
