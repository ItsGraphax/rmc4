package enums;

public enum ResourcePackLetter {
    COOLDOWN_BAR("\uf000"),
    DUPE_TOKEN("\uf001");

    private final String text;

    ResourcePackLetter(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
