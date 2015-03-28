package com.armueller.fluxytodo.data;

import com.armueller.fluxytodo.models.TodoItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by armueller on 3/27/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class RawTodoListTest {

    @Test
    public void createRawTodoListFromNothing() {
        RawTodoList rawTodoList = new RawTodoList();
        assertThat(rawTodoList.list).isNotNull();
        assertThat(rawTodoList.list.size()).isEqualTo(0);
    }

    @Test
    public void createRawTodoListFromArrayListTest() {
        ArrayList<TodoItem> list = new ArrayList<TodoItem>(3);
        list.add(new TodoItem(1, "T1", false));
        list.add(new TodoItem(2, "T2", false));
        list.add(new TodoItem(3, "T3", false));

        RawTodoList rawTodoList = new RawTodoList(list);

        assertThat(rawTodoList.list).isNotNull();
        assertThat(rawTodoList.list.get(0).getId()).isEqualTo(1);
        assertThat(rawTodoList.list.get(1).getId()).isEqualTo(2);
        assertThat(rawTodoList.list.get(2).getId()).isEqualTo(3);
    }

    @Test
    public void createRawTodoListFromHashMapTest() {
        HashMap<Long, TodoItem> list = new HashMap<Long, TodoItem>();
        TodoItem t1 = new TodoItem(1, "T1", false);
        TodoItem t2 = new TodoItem(2, "T2", false);
        TodoItem t3 = new TodoItem(3, "T3", false);
        list.put(1L, t1);
        list.put(2L, t2);
        list.put(3L, t3);

        RawTodoList rawTodoList = new RawTodoList(list);

        assertThat(rawTodoList.list).isNotNull();
        assertThat(rawTodoList.list).contains(t1, t2, t3);
    }

}
