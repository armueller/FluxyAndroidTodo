package com.armueller.fluxytodo.stores;

import com.armueller.fluxytodo.actions.DataBundle;
import com.armueller.fluxytodo.actions.TodoAction;
import com.armueller.fluxytodo.actions.ViewAction;
import com.armueller.fluxytodo.busses.ActionBus;
import com.armueller.fluxytodo.busses.DataBus;
import com.armueller.fluxytodo.data.FilteredTodoList;
import com.armueller.fluxytodo.data.RawTodoList;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * Created by armueller on 3/19/15.
 */
public class TodosActivityStore {
    private Long editModeActiveForTodoId;
    private Boolean shouldShowUndoButton;

    private DataBus dataBus;
    private RawTodoList rawTodoList;
    private FilteredTodoList.Filter activeFilter;

    @Inject
    public TodosActivityStore(ActionBus actionBus, DataBus dataBus) {
        this.dataBus = dataBus;
        dataBus.register(this);
        actionBus.register(this);
        editModeActiveForTodoId = new Long(-1);
        shouldShowUndoButton = Boolean.FALSE;

        activeFilter = FilteredTodoList.Filter.ALL;
        rawTodoList = new RawTodoList();
    }

    @Subscribe
    public void updateRawTodoList(RawTodoList rawTodoList) {
        this.rawTodoList = rawTodoList;
        dataBus.post(new FilteredTodoList(activeFilter, rawTodoList));
    }

    @Subscribe
    public final void reactToViewAction(ViewAction action) {
        DataBundle<ViewAction.DataKeys> data = action.getData();
        long id = (long) data.get(ViewAction.DataKeys.ID, -1);

        switch (action.getType()) {
            case VIEW_ALL:
                activeFilter = FilteredTodoList.Filter.ACTIVE;
                dataBus.post(new FilteredTodoList(activeFilter, rawTodoList));
                break;
            case VIEW_ACTIVE:
                activeFilter = FilteredTodoList.Filter.COMPLETE;
                dataBus.post(new FilteredTodoList(activeFilter, rawTodoList));
                break;
            case VIEW_COMPLETE:
                activeFilter = FilteredTodoList.Filter.ALL;
                dataBus.post(new FilteredTodoList(activeFilter, rawTodoList));
                break;
            case MARK_EDITABLE:
                editModeActiveForTodoId = new Long(id);
                dataBus.post(editModeActiveForTodoId);
                break;
        }
    }

    @Produce
    public Long produceEditModeActiveForTodoId() {
        return editModeActiveForTodoId;
    }

    @Subscribe
    public void reactToTodoAction(TodoAction action) {
        shouldShowUndoButton = Boolean.FALSE;

        if (action.getType() == TodoAction.ActionTypes.EDIT) {
            editModeActiveForTodoId = new Long(-1);
            dataBus.post(editModeActiveForTodoId);
        } else if (action.getType() == TodoAction.ActionTypes.DELETE_ALL) {
            shouldShowUndoButton = Boolean.TRUE;
        }

        dataBus.post(shouldShowUndoButton);
    }

    @Produce
    public Boolean produceShouldShowUndoButton() {
        return shouldShowUndoButton;
    }
}
