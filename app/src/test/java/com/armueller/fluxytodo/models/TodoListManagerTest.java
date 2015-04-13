package com.armueller.fluxytodo.models;

import com.armueller.fluxytodo.actions.DataBundle;
import com.armueller.fluxytodo.actions.TodoAction;
import com.armueller.fluxytodo.actions.ViewAction;
import com.armueller.fluxytodo.busses.ActionBus;
import com.armueller.fluxytodo.busses.DataBus;
import com.armueller.fluxytodo.data.RawTodoList;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by armueller on 3/14/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class TodoListManagerTest {
    private ActionBus actionBus;
    private DataBus dataBus;
    private TodoListManager list;

    @Before
    public void setup() {
        actionBus = new ActionBus();
        dataBus = new DataBus(ThreadEnforcer.ANY);
        list = new TodoListManager(actionBus, dataBus);
    }

    @Test(timeout = 100)
    public void createTodoTest() throws InterruptedException {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        createTodos(1);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(RawTodoList rawTodoList) {
                assertThat(rawTodoList.list.size()).isEqualTo(1);
                assertThat(rawTodoList.list.get(0).getDescription()).isEqualTo("Testing 0");
                assertThat(rawTodoList.list.get(0).isComplete()).isFalse();
                testDone.set(true);
            }
        });

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    private void createTodos(final int numberOfTodos) throws InterruptedException {
        for (int i = 0; i < numberOfTodos; i++) {
            DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
            bundle.put(TodoAction.DataKeys.DESCRIPTION, "Testing " + i);
            actionBus.post(new TodoAction(TodoAction.ActionTypes.ADD, bundle));
            Thread.sleep(10);
        }
    }

    @Test(timeout = 100)
    public void createMultipleTodosTest() throws InterruptedException {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        createTodos(4);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(RawTodoList rawTodoList) {
                assertThat(rawTodoList.list.size()).isEqualTo(4);
                testDone.set(true);
            }
        });

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void completeTodoTest() throws InterruptedException {
        final AtomicBoolean testDone = new AtomicBoolean(false);
        final ArrayList<TodoItem> todoItems = new ArrayList<TodoItem>();

        createTodos(2);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(RawTodoList rawTodoList) {
                todoItems.clear();
                todoItems.addAll(rawTodoList.list);
                if (!todoItems.get(1).isComplete()) {
                    DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
                    bundle.put(TodoAction.DataKeys.ID, todoItems.get(1).getId());
                    actionBus.post(new TodoAction(TodoAction.ActionTypes.TOGGLE, bundle));
                } else {
                    assertThat(todoItems.get(0).isComplete()).isFalse();
                    assertThat(todoItems.get(1).isComplete()).isTrue();
                    testDone.set(true);
                }
            }
        });

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void completeAllTodosTest() throws InterruptedException {
        final AtomicBoolean testDone = new AtomicBoolean(false);
        final ArrayList<TodoItem> todoItems = new ArrayList<TodoItem>();

        createTodos(3);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(RawTodoList rawTodoList) {
                todoItems.clear();
                todoItems.addAll(rawTodoList.list);
                if (!todoItems.get(0).isComplete()) {
                    actionBus.post(new TodoAction(TodoAction.ActionTypes.TOGGLE_ALL));
                } else {
                    assertThat(todoItems.get(0).isComplete()).isTrue();
                    assertThat(todoItems.get(1).isComplete()).isTrue();
                    assertThat(todoItems.get(2).isComplete()).isTrue();
                    testDone.set(true);
                }
            }
        });

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void editTodoTest() throws InterruptedException {
        final AtomicBoolean testDone = new AtomicBoolean(false);
        final ArrayList<TodoItem> todoItems = new ArrayList<TodoItem>();

        createTodos(1);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(RawTodoList rawTodoList) {
                todoItems.clear();
                todoItems.addAll(rawTodoList.list);

                if (todoItems.get(0).getDescription().equals("Testing 0")) {
                    DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
                    bundle.put(TodoAction.DataKeys.ID, todoItems.get(0).getId());
                    bundle.put(TodoAction.DataKeys.DESCRIPTION, "New Todo Description");
                    actionBus.post(new TodoAction(TodoAction.ActionTypes.EDIT, bundle));
                } else {
                    assertThat(todoItems.get(0).getDescription()).isEqualTo("New Todo Description");
                    testDone.set(true);
                }
            }
        });

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void deleteTodoTest() throws InterruptedException {
        final AtomicBoolean testDone = new AtomicBoolean(false);
        final AtomicLong deletedId = new AtomicLong(-1);
        final ArrayList<TodoItem> todoItems = new ArrayList<TodoItem>();

        createTodos(3);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(RawTodoList rawTodoList) {
                todoItems.clear();
                todoItems.addAll(rawTodoList.list);

                if (todoItems.size() == 3) {
                    deletedId.set(todoItems.get(1).getId());
                    DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
                    bundle.put(TodoAction.DataKeys.ID, todoItems.get(1).getId());
                    actionBus.post(new TodoAction(TodoAction.ActionTypes.DELETE, bundle));
                } else {
                    assertThat(todoItems.size()).isEqualTo(2);
                    assertThat(todoItems.get(0).getId()).isNotEqualTo(deletedId.get());
                    assertThat(todoItems.get(1).getId()).isNotEqualTo(deletedId.get());
                    testDone.set(true);
                }
            }
        });

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void deleteAllCompleteTodosTest() throws InterruptedException {
        final AtomicBoolean testDone = new AtomicBoolean(false);
        final AtomicBoolean checkedTodo1 = new AtomicBoolean(false);
        final AtomicBoolean checkedTodo3 = new AtomicBoolean(false);
        final AtomicBoolean deleted = new AtomicBoolean(false);
        final ArrayList<TodoItem> todoItems = new ArrayList<TodoItem>();

        createTodos(4);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(RawTodoList rawTodoList) {
                todoItems.clear();
                todoItems.addAll(rawTodoList.list);

                if (!checkedTodo1.get()) {
                    checkedTodo1.set(true);
                    DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
                    bundle.put(TodoAction.DataKeys.ID, todoItems.get(1).getId());
                    actionBus.post(new TodoAction(TodoAction.ActionTypes.TOGGLE, bundle));
                    return;
                }
                if (!checkedTodo3.get()) {
                    checkedTodo3.set(true);
                    DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
                    bundle.put(TodoAction.DataKeys.ID, todoItems.get(3).getId());
                    actionBus.post(new TodoAction(TodoAction.ActionTypes.TOGGLE, bundle));
                    return;
                }
                if (!deleted.get() && checkedTodo1.get() && checkedTodo3.get()) {
                    deleted.set(true);
                    actionBus.post(new TodoAction(TodoAction.ActionTypes.DELETE_ALL));
                    return;
                }
                if (deleted.get()) {
                    assertThat(todoItems.size()).isEqualTo(2);
                    testDone.set(true);
                }
            }
        });

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void undoDeleteAllCompleteTodosTest() throws InterruptedException {
        final AtomicBoolean testDone = new AtomicBoolean(false);
        final AtomicBoolean checkedTodo1 = new AtomicBoolean(false);
        final AtomicBoolean checkedTodo3 = new AtomicBoolean(false);
        final AtomicBoolean deletedTodos = new AtomicBoolean(false);
        final AtomicBoolean undidDelete = new AtomicBoolean(false);
        final ArrayList<TodoItem> todoItems = new ArrayList<TodoItem>();

        createTodos(4);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(RawTodoList rawTodoList) {
                todoItems.clear();
                todoItems.addAll(rawTodoList.list);

                if (!checkedTodo1.get()) {
                    checkedTodo1.set(true);
                    DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
                    bundle.put(TodoAction.DataKeys.ID, todoItems.get(1).getId());
                    actionBus.post(new TodoAction(TodoAction.ActionTypes.TOGGLE, bundle));
                    return;
                }
                if (!checkedTodo3.get()) {
                    checkedTodo3.set(true);
                    DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
                    bundle.put(TodoAction.DataKeys.ID, todoItems.get(3).getId());
                    actionBus.post(new TodoAction(TodoAction.ActionTypes.TOGGLE, bundle));
                    return;
                }
                if (!deletedTodos.get() && checkedTodo1.get() && checkedTodo3.get()) {
                    deletedTodos.set(true);
                    actionBus.post(new TodoAction(TodoAction.ActionTypes.DELETE_ALL));
                    return;
                }
                if (!undidDelete.get() && deletedTodos.get()) {
                    undidDelete.set(true);
                    actionBus.post(new TodoAction(TodoAction.ActionTypes.UNDO_DELETE_ALL));
                    return;
                }

                if (undidDelete.get() && deletedTodos.get()) {
                    assertThat(todoItems.size()).isEqualTo(4);
                    testDone.set(true);
                }
            }
        });

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }
}
