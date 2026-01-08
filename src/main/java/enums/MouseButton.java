package enums;

import org.bukkit.event.block.Action;

public enum MouseButton {
    LEFT,
    RIGHT,
    NONE;

    public static MouseButton simplifyActionEnum(Action action) {
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            return LEFT;
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            return RIGHT;
        }
        return NONE;
    }
}
