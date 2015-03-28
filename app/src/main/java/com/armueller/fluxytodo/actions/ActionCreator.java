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

    public void createNewTodoAction(String newTodoDescription) {
        actionBus.post(new Actions.AddTodo(newTodoDescription));
    }

    public void createToggleTodoCompleteAction(long todoId) {
        actionBus.post(new Actions.ToggleTodoComplete(todoId));
    }

    public void createToggleAllTodosCompleteAction() {
        actionBus.post(new Actions.ToggleAllTodosComplete());
    }

    public void createSetTodoItemAsEditableAction(long todoId) {
        actionBus.post(new Actions.SetTodoItemAsEditable(todoId));
    }

    public void createEditTodoAction(long todoId, String newDescription) {
        actionBus.post(new Actions.EditTodo(todoId, newDescription));
    }

    public void createDeleteTodoAction(long todoId) {
        actionBus.post(new Actions.DeleteTodo(todoId));
    }

    public void createDeleteAllCompleteTodosAction() {
        actionBus.post(new Actions.DeleteAllCompleteTodos());
    }

    public void createUndoDeleteAllCompleteTodosAction() {
        actionBus.post(new Actions.UndoDeleteAllCompleteTodos());
    }

    public void createViewAllTodosAction() {
        actionBus.post(new Actions.ViewAllTodos());
    }

    public void createViewActiveTodosAction() {
        actionBus.post(new Actions.ViewActiveTodos());
    }

    public void createViewCompleteTodosAction() {
        actionBus.post(new Actions.ViewCompleteTodos());
    }
}
