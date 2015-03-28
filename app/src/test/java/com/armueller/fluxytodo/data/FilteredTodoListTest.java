package com.armueller.fluxytodo.data;

import com.armueller.fluxytodo.models.TodoItem;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by armueller on 3/27/15.
 */
public class FilteredTodoListTest {
    private HashMap<Long, TodoItem> list;

    @Before
    public void setup() {
        list = new HashMap<Long, TodoItem>();
    }

    @Test
    public void createFilteredTodoListFromRawTodoListTest() {
        TodoItem t1 = new TodoItem(1, "T1", false);
        list.put(t1.getId(), t1);
        RawTodoList rawTodoList = new RawTodoList(list);

        FilteredTodoList filteredTodoList = new FilteredTodoList(FilteredTodoList.Filter.ALL, rawTodoList);

        assertThat(filteredTodoList.list).isNotNull();
    }

    @Test
    public void filteredTodoListHasAllTodosTest() {
        TodoItem t1 = new TodoItem(1, "T1", false);
        TodoItem t2 = new TodoItem(2, "T2", false);
        TodoItem t3 = new TodoItem(3, "T3", false);
        list.put(t1.getId(), t1);
        list.put(t2.getId(), t2);
        list.put(t3.getId(), t3);
        RawTodoList rawTodoList = new RawTodoList(list);

        FilteredTodoList filteredTodoList = new FilteredTodoList(FilteredTodoList.Filter.ALL, rawTodoList);

        assertThat(filteredTodoList.list.size()).isEqualTo(3);
        assertThat(filteredTodoList.list).contains(t1, t2, t3);
    }

    @Test
    public void filteredTodoListHasActiveTodosTest() {
        TodoItem t1 = new TodoItem(1, "T1", false);
        TodoItem t2 = new TodoItem(2, "T2", true);
        TodoItem t3 = new TodoItem(3, "T3", true);
        TodoItem t4 = new TodoItem(4, "T4", false);
        list.put(t1.getId(), t1);
        list.put(t2.getId(), t2);
        list.put(t3.getId(), t3);
        list.put(t4.getId(), t4);
        RawTodoList rawTodoList = new RawTodoList(list);

        FilteredTodoList filteredTodoList = new FilteredTodoList(FilteredTodoList.Filter.ACTIVE, rawTodoList);

        assertThat(filteredTodoList.list.size()).isEqualTo(2);
        assertThat(filteredTodoList.list).contains(t1, t4);
    }

    @Test
    public void filteredTodoListHasCompleteTodosTest() {
        TodoItem t1 = new TodoItem(1, "T1", false);
        TodoItem t2 = new TodoItem(2, "T2", true);
        TodoItem t3 = new TodoItem(3, "T3", true);
        TodoItem t4 = new TodoItem(4, "T4", false);
        list.put(t1.getId(), t1);
        list.put(t2.getId(), t2);
        list.put(t3.getId(), t3);
        list.put(t4.getId(), t4);
        RawTodoList rawTodoList = new RawTodoList(list);

        FilteredTodoList filteredTodoList = new FilteredTodoList(FilteredTodoList.Filter.COMPLETE, rawTodoList);

        assertThat(filteredTodoList.list.size()).isEqualTo(2);
        assertThat(filteredTodoList.list).contains(t2, t3);
    }

    @Test
    public void filteredTodoListIsSortedTest() {
        TodoItem t1 = new TodoItem(1, "T1", false);
        TodoItem t2 = new TodoItem(4, "T4", true);
        TodoItem t3 = new TodoItem(2, "T2", true);
        TodoItem t4 = new TodoItem(3, "T3", false);
        list.put(t1.getId(), t1);
        list.put(t2.getId(), t2);
        list.put(t3.getId(), t3);
        list.put(t4.getId(), t4);
        RawTodoList rawTodoList = new RawTodoList(list);

        FilteredTodoList filteredTodoList = new FilteredTodoList(FilteredTodoList.Filter.ALL, rawTodoList);

        assertThat(filteredTodoList.list.get(0).getId()).isEqualTo(1);
        assertThat(filteredTodoList.list.get(1).getId()).isEqualTo(2);
        assertThat(filteredTodoList.list.get(2).getId()).isEqualTo(3);
        assertThat(filteredTodoList.list.get(3).getId()).isEqualTo(4);
    }
}
