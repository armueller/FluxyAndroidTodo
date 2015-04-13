package com.armueller.fluxytodo.actions;

import com.armueller.fluxytodo.busses.ActionBus;
import com.squareup.otto.Subscribe;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.fest.assertions.api.Assertions.assertThat;


/**
 * Created by armueller on 3/26/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ActionCreatorTest {
    private static ActionBus actionBus;
    private static ActionCreator actionCreator;

    @BeforeClass
    public static void setup() {
        actionBus = new ActionBus();
        actionCreator = new ActionCreator(actionBus);
    }

    @Test
    public void createTodoActionTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        actionBus.register(new Object() {
            @Subscribe
            public void addTodo(TodoAction action) {
                assertThat(action.getData().get(TodoAction.DataKeys.DESCRIPTION, "")).isEqualTo("Test");
                testDone.set(true);
            }
        });

        actionCreator.createNewTodoAction("Test");

        while (!testDone.get()) ;
    }

    @Test
    public void createToggleTodoCompleteActionTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        actionBus.register(new Object() {
            @Subscribe
            public void addTodo(TodoAction action) {
                assertThat(action.getData().get(TodoAction.DataKeys.ID, -1)).isEqualTo(10);
                testDone.set(true);
            }
        });

        actionCreator.createToggleTodoCompleteAction(10);

        while (!testDone.get()) ;
    }

    @Test
    public void createToggleAllTodosCompleteActionTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        actionBus.register(new Object() {
            @Subscribe
            public void addTodo(TodoAction action) {
                assertThat(action.getType()).isEqualTo(TodoAction.ActionTypes.TOGGLE_ALL);
                testDone.set(true);
            }
        });

        actionCreator.createToggleAllTodosCompleteAction();

        while (!testDone.get()) ;
    }

    @Test
    public void createSetTodoItemAsEditableActionTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        actionBus.register(new Object() {
            @Subscribe
            public void addTodo(ViewAction action) {
                assertThat(action.getData().get(ViewAction.DataKeys.ID, -1)).isEqualTo(10);
                testDone.set(true);
            }
        });

        actionCreator.createSetTodoItemAsEditableAction(10);

        while (!testDone.get()) ;
    }

    @Test
    public void createEditTodoActionTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        actionBus.register(new Object() {
            @Subscribe
            public void addTodo(TodoAction action) {
                assertThat(action.getData().get(TodoAction.DataKeys.ID, -1)).isEqualTo(10);
                assertThat(action.getData().get(TodoAction.DataKeys.DESCRIPTION, "")).isEqualTo("New Description");
                testDone.set(true);
            }
        });

        actionCreator.createEditTodoAction(10, "New Description");

        while (!testDone.get()) ;
    }

    @Test
    public void createDeleteTodoActionTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        actionBus.register(new Object() {
            @Subscribe
            public void addTodo(TodoAction action) {
                assertThat(action.getData().get(TodoAction.DataKeys.ID, -1)).isEqualTo(10);
                testDone.set(true);
            }
        });

        actionCreator.createDeleteTodoAction(10);

        while (!testDone.get()) ;
    }

    @Test
    public void createDeleteAllCompleteTodosActionTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        actionBus.register(new Object() {
            @Subscribe
            public void addTodo(TodoAction action) {
                assertThat(action.getType()).isEqualTo(TodoAction.ActionTypes.DELETE_ALL);
                testDone.set(true);
            }
        });

        actionCreator.createDeleteAllCompleteTodosAction();

        while (!testDone.get()) ;
    }

    @Test
    public void createUndoDeleteAllCompleteTodosActionTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        actionBus.register(new Object() {
            @Subscribe
            public void addTodo(TodoAction action) {
                assertThat(action.getType()).isEqualTo(TodoAction.ActionTypes.UNDO_DELETE_ALL);
                testDone.set(true);
            }
        });

        actionCreator.createUndoDeleteAllCompleteTodosAction();

        while (!testDone.get()) ;
    }

    @Test
    public void createViewAllTodosActionTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        actionBus.register(new Object() {
            @Subscribe
            public void addTodo(ViewAction action) {
                assertThat(action.getType()).isEqualTo(ViewAction.ActionTypes.VIEW_ALL);
                testDone.set(true);
            }
        });

        actionCreator.createViewAllTodosAction();

        while (!testDone.get()) ;
    }

    @Test
    public void createViewActiveTodosActionTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        actionBus.register(new Object() {
            @Subscribe
            public void addTodo(ViewAction action) {
                assertThat(action.getType()).isEqualTo(ViewAction.ActionTypes.VIEW_ACTIVE);
                testDone.set(true);
            }
        });

        actionCreator.createViewActiveTodosAction();

        while (!testDone.get()) ;
    }

    @Test
    public void createViewCompleteTodosActionTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        actionBus.register(new Object() {
            @Subscribe
            public void addTodo(ViewAction action) {
                assertThat(action.getType()).isEqualTo(ViewAction.ActionTypes.VIEW_COMPLETE);
                testDone.set(true);
            }
        });

        actionCreator.createViewCompleteTodosAction();

        while (!testDone.get()) ;
    }
}
