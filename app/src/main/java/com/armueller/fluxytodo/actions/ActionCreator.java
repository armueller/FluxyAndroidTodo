package com.armueller.fluxytodo.actions;

import com.armueller.fluxytodo.busses.ActionBus;

import javax.inject.Inject;

/**
 * Created by armueller on 3/9/15.
 */
public class ActionCreator {
    private ActionBus actionBus;

    @Inject
    public ActionCreator(ActionBus actionBus) {
        this.actionBus = actionBus;
    }

    public final void createNewTodoAction(final String newTodoDescription) {
        DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
        bundle.put(TodoAction.DataKeys.DESCRIPTION, newTodoDescription);
        actionBus.post(new TodoAction(TodoAction.ActionTypes.ADD, bundle));
    }

    public final void createToggleTodoCompleteAction(long todoId) {
        DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
        bundle.put(TodoAction.DataKeys.ID, todoId);
        actionBus.post(new TodoAction(TodoAction.ActionTypes.TOGGLE, bundle));
    }

    public final void createToggleAllTodosCompleteAction() {
        actionBus.post(new TodoAction(TodoAction.ActionTypes.TOGGLE_ALL));
    }

    public final void createEditTodoAction(long todoId, String newDescription) {
        DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
        bundle.put(TodoAction.DataKeys.ID, todoId);
        bundle.put(TodoAction.DataKeys.DESCRIPTION, newDescription);
        actionBus.post(new TodoAction(TodoAction.ActionTypes.EDIT, bundle));
    }

    public final void createDeleteTodoAction(long todoId) {
        DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
        bundle.put(TodoAction.DataKeys.ID, todoId);
        actionBus.post(new TodoAction(TodoAction.ActionTypes.DELETE, bundle));
    }

    public final void createDeleteAllCompleteTodosAction() {
        actionBus.post(new TodoAction(TodoAction.ActionTypes.DELETE_ALL));
    }

    public final void createUndoDeleteAllCompleteTodosAction() {
        actionBus.post(new TodoAction(TodoAction.ActionTypes.UNDO_DELETE_ALL));
    }


    public final void createViewAllTodosAction() {
        actionBus.post(new ViewAction(ViewAction.ActionTypes.VIEW_ALL));
    }

    public final void createViewActiveTodosAction() {
        actionBus.post(new ViewAction(ViewAction.ActionTypes.VIEW_ACTIVE));
    }

    public final void createViewCompleteTodosAction() {
        actionBus.post(new ViewAction(ViewAction.ActionTypes.VIEW_COMPLETE));
    }

    public final void createSetTodoItemAsEditableAction(long todoId) {
        DataBundle<ViewAction.DataKeys> bundle = new DataBundle<>();
        bundle.put(ViewAction.DataKeys.ID, todoId);
        actionBus.post(new ViewAction(ViewAction.ActionTypes.MARK_EDITABLE, bundle));
    }
}
