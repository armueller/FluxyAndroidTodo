package com.armueller.fluxytodo.models;

import com.armueller.fluxytodo.busses.ActionBus;
import com.armueller.fluxytodo.actions.Actions;
import com.armueller.fluxytodo.busses.DataBus;
import com.armueller.fluxytodo.data.RawTodoList;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by armueller on 3/9/15.
 */
public class TodoListManager {
    private final HashMap<Long, TodoItem> backupTodoItems;
    private final HashMap<Long, TodoItem> todoItems;
    private final DataBus dataBus;

    @Inject
    public TodoListManager(ActionBus actionBus, DataBus dataBus) {
        backupTodoItems = new HashMap<Long, TodoItem>();
        todoItems = new HashMap<Long, TodoItem>();
        this.dataBus = dataBus;
        dataBus.register(this);
        actionBus.register(this);
    }

    @Subscribe
    public void addTodo(Actions.AddTodo addTodoAction) {
        TodoItem newItem = new TodoItem(System.currentTimeMillis(), addTodoAction.description, false);
        todoItems.put(newItem.getId(), newItem);

        dataBus.post(new RawTodoList(todoItems));
    }

    @Subscribe
    public void toggleTodoComplete(Actions.ToggleTodoComplete toggleTodoCompleteAction) {
        TodoItem oldItem = todoItems.get(toggleTodoCompleteAction.todoId);
        todoItems.put(oldItem.getId(), new TodoItem(oldItem.getId(), oldItem.getDescription(), !oldItem.isComplete()));

        dataBus.post(new RawTodoList(todoItems));
    }

    @Subscribe
    public void toggleAllTodosComplete(Actions.ToggleAllTodosComplete toggleAllTodoCompleteAction) {
        for (Long key : todoItems.keySet()) {
            TodoItem oldItem = todoItems.get(key);
            todoItems.put(key, new TodoItem(oldItem.getId(), oldItem.getDescription(), true));
        }

        dataBus.post(new RawTodoList(todoItems));
    }

    @Subscribe
    public void editTodo(Actions.EditTodo editTodoAction) {
        TodoItem oldItem = todoItems.get(editTodoAction.todoId);
        todoItems.put(oldItem.getId(), new TodoItem(oldItem.getId(), editTodoAction.todoDescription, oldItem.isComplete()));

        dataBus.post(new RawTodoList(todoItems));
    }

    @Subscribe
    public void deleteTodo(Actions.DeleteTodo deleteTodoAction) {
        todoItems.remove(deleteTodoAction.todoId);

        dataBus.post(new RawTodoList(todoItems));
    }

    @Subscribe
    public void deleteAllCompleteTodos(Actions.DeleteAllCompleteTodos deleteAllCompleteTodosAction) {
        backupTodoItems.clear();
        backupTodoItems.putAll((HashMap<Long, TodoItem>) todoItems.clone());
        for (TodoItem item : ((HashMap<Long, TodoItem>)todoItems.clone()).values()) {
            if (item.isComplete()) {
                todoItems.remove(item.getId());
            }
        }

        dataBus.post(new RawTodoList(todoItems));
    }

    @Subscribe
    public void undoDeleteCompleteAllTodos(Actions.UndoDeleteAllCompleteTodos undoDeleteAllCompleteTodosAction) {
        if (!backupTodoItems.isEmpty()) {
            todoItems.clear();
            todoItems.putAll((HashMap<Long, TodoItem>) backupTodoItems.clone());
            backupTodoItems.clear();

            dataBus.post(new RawTodoList(todoItems));
        }
    }

    @Produce
    public RawTodoList produceTodoList() {
        return new RawTodoList(todoItems);
    }
}
