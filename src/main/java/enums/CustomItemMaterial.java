package enums;

// Enums
public enum CustomItemMaterial {
    DIAMOND_SHARD("diamond_shard"), TOKEN_CORE("core"), TOKEN("token"), DUPE_TOKEN_CORE("dupe_token_core");

    private final String text;

    CustomItemMaterial(final String text) {
        this.text = text;
    }


    public static CustomItemMaterial fromId(String input) {
        for (CustomItemMaterial v : values()) {
            if (v.text.equalsIgnoreCase(input)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Unknown custom item id:" + input);
    }

    @Override
    public String toString() {
        return text;
    }
}
