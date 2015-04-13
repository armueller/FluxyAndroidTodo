package com.armueller.fluxytodo.stores;

import com.armueller.fluxytodo.actions.DataBundle;
import com.armueller.fluxytodo.actions.TodoAction;
import com.armueller.fluxytodo.actions.ViewAction;
import com.armueller.fluxytodo.busses.ActionBus;
import com.armueller.fluxytodo.busses.DataBus;
import com.armueller.fluxytodo.data.FilteredTodoList;
import com.armueller.fluxytodo.data.RawTodoList;
import com.armueller.fluxytodo.models.TodoItem;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by armueller on 3/27/15.
 */
public class TodosActivityStoreTest {
    private ActionBus actionBus;
    private DataBus dataBus;
    private TodosActivityStore store;

    @Before
    public void setup() {
        actionBus = new ActionBus();
        dataBus = new DataBus(ThreadEnforcer.ANY);
        store = new TodosActivityStore(actionBus, dataBus);
    }

    //Test:
    //todoActionPerformed

    @Test(timeout = 100)
    public void updateRawTodoListConnectedTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);
        RawTodoList rawTodoList = new RawTodoList();

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(FilteredTodoList filteredTodoList) {
                assertThat(filteredTodoList.list.size()).isEqualTo(0);
                testDone.set(true);
            }
        });

        dataBus.post(rawTodoList);

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void changeActiveFilterToAllTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);
        ArrayList<TodoItem> list = new ArrayList<TodoItem>();
        list.add(new TodoItem(1, "Test", false));
        list.add(new TodoItem(2, "Test", true));
        list.add(new TodoItem(3, "Test", false));
        RawTodoList rawTodoList = new RawTodoList(list);
        dataBus.post(rawTodoList);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(FilteredTodoList filteredTodoList) {
                assertThat(filteredTodoList.list.size()).isEqualTo(3);
                assertThat(filteredTodoList.filter).isEqualTo(FilteredTodoList.Filter.ALL);
                testDone.set(true);
            }
        });

        dataBus.post(new ViewAction(ViewAction.ActionTypes.VIEW_ALL));
        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void changeActiveFilterToActiveTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);
        ArrayList<TodoItem> list = new ArrayList<TodoItem>();
        list.add(new TodoItem(1, "Test", false));
        list.add(new TodoItem(2, "Test", true));
        list.add(new TodoItem(3, "Test", false));
        RawTodoList rawTodoList = new RawTodoList(list);
        dataBus.post(rawTodoList);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(FilteredTodoList filteredTodoList) {
                assertThat(filteredTodoList.list.size()).isEqualTo(2);
                assertThat(filteredTodoList.filter).isEqualTo(FilteredTodoList.Filter.ACTIVE);
                testDone.set(true);
            }
        });

        dataBus.post(new ViewAction(ViewAction.ActionTypes.VIEW_ACTIVE));
        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void changeActiveFilterToCompleteTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);
        ArrayList<TodoItem> list = new ArrayList<TodoItem>();
        list.add(new TodoItem(1, "Test", false));
        list.add(new TodoItem(2, "Test", true));
        list.add(new TodoItem(3, "Test", false));
        RawTodoList rawTodoList = new RawTodoList(list);
        dataBus.post(rawTodoList);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(FilteredTodoList filteredTodoList) {
                assertThat(filteredTodoList.list.size()).isEqualTo(1);
                assertThat(filteredTodoList.filter).isEqualTo(FilteredTodoList.Filter.COMPLETE);
                testDone.set(true);
            }
        });

        dataBus.post(new ViewAction(ViewAction.ActionTypes.VIEW_COMPLETE));
        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void setEditModeActiveForTodoIdTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        DataBundle<ViewAction.DataKeys> bundle = new DataBundle<>();
        bundle.put(ViewAction.DataKeys.ID, 2);
        dataBus.post(new ViewAction(ViewAction.ActionTypes.MARK_EDITABLE, bundle));

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(Long todoId) {
                assertThat(todoId).isEqualTo(2);
                testDone.set(true);
            }
        });

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void setEditModeInactiveForTodoIdTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(TodoAction todoAction) {

                assertThat(todoAction.getType()).isEqualTo(TodoAction.ActionTypes.EDIT);
                assertThat(todoAction.getData().get(TodoAction.DataKeys.ID, -1)).isEqualTo(2);
                assertThat(todoAction.getData().get(TodoAction.DataKeys.DESCRIPTION, "")).isEqualTo("Test");
                testDone.set(true);
            }
        });

        DataBundle<TodoAction.DataKeys> bundle = new DataBundle<>();
        bundle.put(TodoAction.DataKeys.ID, 2);
        bundle.put(TodoAction.DataKeys.DESCRIPTION, "Test");
        dataBus.post(new TodoAction(TodoAction.ActionTypes.EDIT, bundle));
        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void shouldNotShowUndoButtonTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        dataBus.post(new TodoAction(TodoAction.ActionTypes.UNDO_DELETE_ALL));

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(Boolean shouldShowUndoButton) {
                assertThat(shouldShowUndoButton).isFalse();
                testDone.set(true);
            }
        });

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }

    @Test(timeout = 100)
    public void shouldShowUndoButtonTest() {
        final AtomicBoolean testDone = new AtomicBoolean(false);

        dataBus.post(new TodoAction(TodoAction.ActionTypes.DELETE_ALL));

        dataBus.register(new Object() {
            @Subscribe
            public void onListUpdated(Boolean shouldShowUndoButton) {
                assertThat(shouldShowUndoButton).isTrue();
                testDone.set(true);
            }
        });

        // Wait for test to finish or timeout
        while (!testDone.get()) ;
    }
}
