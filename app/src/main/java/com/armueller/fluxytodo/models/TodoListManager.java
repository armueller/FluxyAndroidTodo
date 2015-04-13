package com.armueller.fluxytodo.models;

import com.armueller.fluxytodo.actions.DataBundle;
import com.armueller.fluxytodo.actions.TodoAction;
import com.armueller.fluxytodo.actions.ViewAction;
import com.armueller.fluxytodo.busses.ActionBus;
import com.armueller.fluxytodo.busses.DataBus;
import com.armueller.fluxytodo.data.RawTodoList;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

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
    public void reactToTodoAction(TodoAction action) {
        DataBundle<TodoAction.DataKeys> data = action.getData();
        long id = (long) data.get(TodoAction.DataKeys.ID, -1L);
        String description = (String) data.get(TodoAction.DataKeys.DESCRIPTION, "");

        switch (action.getType()) {
            case ADD:
                addTodo(description);
                break;
            case TOGGLE:
                toggleTodoComplete(id);
                break;
            case TOGGLE_ALL:
                toggleAllTodosComplete();
                break;
            case EDIT:
                editTodo(id, description);
                break;
            case DELETE:
                deleteTodo(id);
                break;
            case DELETE_ALL:
                deleteAllCompleteTodos();
                break;
            case UNDO_DELETE_ALL:
                undoDeleteCompleteAllTodos();
                break;
        }

        dataBus.post(new RawTodoList(todoItems));
    }

    @Produce
    public RawTodoList produceTodoList() {
        return new RawTodoList(todoItems);
    }

    public final void addTodo(String description) {
        TodoItem newItem = new TodoItem(System.currentTimeMillis(), description, false);
        todoItems.put(newItem.getId(), newItem);
    }

    public final void toggleTodoComplete(long id) {
        TodoItem oldItem = todoItems.get(id);
        todoItems.put(oldItem.getId(), new TodoItem(oldItem.getId(), oldItem.getDescription(), !oldItem.isComplete()));
    }

    public final void toggleAllTodosComplete() {
        for (Long key : todoItems.keySet()) {
            TodoItem oldItem = todoItems.get(key);
            todoItems.put(key, new TodoItem(oldItem.getId(), oldItem.getDescription(), true));
        }
    }

    public final void editTodo(long id, String description) {
        TodoItem oldItem = todoItems.get(id);
        todoItems.put(oldItem.getId(), new TodoItem(oldItem.getId(), description, oldItem.isComplete()));
    }

    public final void deleteTodo(long id) {
        todoItems.remove(id);
    }

    public final void deleteAllCompleteTodos() {
        backupTodoItems.clear();
        backupTodoItems.putAll((HashMap<Long, TodoItem>) todoItems.clone());
        for (TodoItem item : ((HashMap<Long, TodoItem>)todoItems.clone()).values()) {
            if (item.isComplete()) {
                todoItems.remove(item.getId());
            }
        }
    }

    public final void undoDeleteCompleteAllTodos() {
        if (!backupTodoItems.isEmpty()) {
            todoItems.clear();
            todoItems.putAll((HashMap<Long, TodoItem>) backupTodoItems.clone());
            backupTodoItems.clear();
        }
    }
}
