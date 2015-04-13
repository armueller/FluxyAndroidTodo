package com.armueller.fluxytodo.actions;

/**
 * Created by armueller on 4/10/15.
 */
public class TodoAction implements Action {
    private ActionTypes type;
    private DataBundle<DataKeys> bundle;

    public TodoAction(ActionTypes type) {
        this.type = type;
        this.bundle = new DataBundle<>();
    }

    public TodoAction(ActionTypes type, DataBundle<DataKeys> bundle) {
        this.type = type;
        this.bundle = bundle;
    }

    @Override
    public ActionTypes getType() {
        return type;
    }

    @Override
    public DataBundle<DataKeys> getData() {
        return bundle;
    }

    public enum ActionTypes implements ActionType {
        ADD,
        TOGGLE,
        TOGGLE_ALL,
        EDIT,
        DELETE,
        DELETE_ALL,
        UNDO_DELETE_ALL
    }

    public enum DataKeys implements DataKey {
        ID,
        DESCRIPTION,
    }
}
