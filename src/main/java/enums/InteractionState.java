package enums;

public enum InteractionState {
    NONE(0),
    SELECTING_EQUIP_SLOT(1);

    private final int id;

    InteractionState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
