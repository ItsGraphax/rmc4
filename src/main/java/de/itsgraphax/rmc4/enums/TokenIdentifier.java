package de.itsgraphax.rmc4.enums;

public enum TokenIdentifier {
    UNKNOWN("unknown"),
    DUPE("dupe");

    private final String text;

    TokenIdentifier(final String text) {
        this.text = text;
    }


    public static TokenIdentifier fromId(String input) {
        for (TokenIdentifier v : values()) {
            if (v.text.equalsIgnoreCase(input)) {
                return v;
            }
        }
        return TokenIdentifier.UNKNOWN;
    }

    @Override
    public String toString() {
        return text;
    }
}
