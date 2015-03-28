package com.armueller.fluxytodo.stores;

import com.armueller.fluxytodo.actions.Actions;
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
    public void changeActiveFilter(Actions.ViewAction viewAction) {
        if (viewAction instanceof Actions.ViewActiveTodos) {
            activeFilter = FilteredTodoList.Filter.ACTIVE;
        } else if (viewAction instanceof Actions.ViewCompleteTodos) {
            activeFilter = FilteredTodoList.Filter.COMPLETE;
        } else {
            activeFilter = FilteredTodoList.Filter.ALL;
        }

        dataBus.post(new FilteredTodoList(activeFilter, rawTodoList));
    }

    @Subscribe
    public void setEditModeActiveForTodoId(Actions.SetTodoItemAsEditable setTodoItemAsEditable) {
        editModeActiveForTodoId = new Long(setTodoItemAsEditable.todoId);
        dataBus.post(editModeActiveForTodoId);
        //Probably not a great idea to post a primitive to the data bus...
        // But since nothing else is posting longs to the bus it will suffice
    }

    //If this action was triggered, it means the user is finished editing
    // and would like to save their changes.
    @Subscribe
    public void editTodo(Actions.EditTodo editTodoAction) {
        editModeActiveForTodoId = new Long(-1);
        dataBus.post(editModeActiveForTodoId);
    }

    @Produce
    public Long produceEditModeActiveForTodoId() {
        return editModeActiveForTodoId;
    }

    @Subscribe
    public void todoActionPerformed(Actions.TodoAction todoAction) {
        if (todoAction instanceof Actions.DeleteAllCompleteTodos) {
            shouldShowUndoButton = Boolean.TRUE;
        } else {
            shouldShowUndoButton = Boolean.FALSE;
        }

        dataBus.post(shouldShowUndoButton);
    }

    @Produce
    public Boolean produceShouldShowUndoButton() {
        return shouldShowUndoButton;
    }
}
