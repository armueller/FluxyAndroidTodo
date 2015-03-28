package com.armueller.fluxytodo.actions;

/**
 * Created by armueller on 3/8/15.
 */
public class Actions {
    public abstract static class TodoAction {

    }

    public static class AddTodo extends TodoAction {
        public final String description;
        public AddTodo(final String description) {
            this.description = description;
        }
    }

    public static class ToggleTodoComplete extends TodoAction {
        public final long todoId;
        public ToggleTodoComplete(final long todoId) {
            this.todoId = todoId;
        }
    }

    public static class ToggleAllTodosComplete extends TodoAction {
        public ToggleAllTodosComplete() {
        }
    }

    public static class SetTodoItemAsEditable extends TodoAction {
        public final long todoId;
        public SetTodoItemAsEditable(final long todoId) {
            this.todoId = todoId;
        }
    }

    public static class EditTodo extends TodoAction {
        public final long todoId;
        public final String todoDescription;
        public EditTodo(final long todoId, final String todoDescription) {
            this.todoId = todoId;
            this.todoDescription = todoDescription;
        }
    }

    public static class DeleteTodo extends TodoAction {
        public final long todoId;
        public DeleteTodo(final long todoId) {
            this.todoId = todoId;
        }
    }

    public static class DeleteAllCompleteTodos extends TodoAction {
        public DeleteAllCompleteTodos() {
        }
    }

    public static class UndoDeleteAllCompleteTodos extends TodoAction {
        public UndoDeleteAllCompleteTodos() {
        }
    }


    public abstract static class ViewAction {

    }

    public static class ViewAllTodos extends ViewAction {
        public ViewAllTodos() {
        }
    }

    public static class ViewActiveTodos extends ViewAction {
        public ViewActiveTodos() {
        }
    }

    public static class ViewCompleteTodos extends ViewAction {
        public ViewCompleteTodos() {
        }
    }
}
