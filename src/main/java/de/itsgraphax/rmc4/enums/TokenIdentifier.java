package de.itsgraphax.rmc4.enums;

import de.itsgraphax.rmc4.Token;
import de.itsgraphax.rmc4.tokens.Dash;
import de.itsgraphax.rmc4.tokens.TokenDefinition;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public enum TokenIdentifier {
    UNKNOWN("unknown", null),
    DUPE("dupe", null),
    DASH("dash", Dash::trigger);

    private final String text;
    private final TokenDefinition definition;

    TokenIdentifier(String text, TokenDefinition definition) {
        this.text = text;
        this.definition = definition;
    }

    public void trigger(JavaPlugin plugin, Player player, Token token) {
        definition.trigger(plugin, player, token);
    }

    public boolean triggerable() {
        return definition != null;
    }

    /**
     * Returns a TokenIdentifier from a given string id
     */
    public static TokenIdentifier fromId(String input) {
        for (TokenIdentifier v : values()) {
            if (v.text.equalsIgnoreCase(input)) return v;
        }
        return UNKNOWN;
    }

    /**
     * Returns the internal string used in keys and the resource pack
     */
    @Override
    public String toString() {
        return text;
    }
}
