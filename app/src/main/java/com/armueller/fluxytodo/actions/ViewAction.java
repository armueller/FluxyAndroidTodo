package com.armueller.fluxytodo.actions;

/**
 * Created by armueller on 3/8/15.
 */
public class ViewAction implements Action {
    private ActionTypes type;
    private DataBundle<DataKeys> bundle;

    public ViewAction(ActionTypes type) {
        this.type = type;
        this.bundle = new DataBundle<>();
    }

    public ViewAction(ActionTypes type, DataBundle<DataKeys> bundle) {
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
        VIEW_ALL,
        VIEW_ACTIVE,
        VIEW_COMPLETE,
        MARK_EDITABLE
    }

    public enum DataKeys implements DataKey {
        ID
    }
}
