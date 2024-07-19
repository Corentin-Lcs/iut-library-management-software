package application.server.entities.types;

import java.util.Optional;

public enum DocumentState {
    FREE(1, "DISPONIBLE"),
    RESERVED(2, "RESERVE"),
    BORROWED(3, "EMPRUNTE");

    private final int stateId;
    private final String name;

    DocumentState(int stateId, String name) {
        this.stateId = stateId;
        this.name = name;
    }

    public static Optional<DocumentState> fromInt(int stateId) {
        for (DocumentState state : DocumentState.values()) {
            if (state.getStateId() == stateId) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }

    public int getStateId() {
        return stateId;
    }

    public String getName() {
        return name;
    }
}
