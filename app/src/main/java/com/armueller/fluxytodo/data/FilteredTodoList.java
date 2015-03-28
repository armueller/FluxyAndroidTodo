package com.armueller.fluxytodo.data;

import com.armueller.fluxytodo.models.TodoItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by armueller on 3/27/15.
 */
public class FilteredTodoList {
    public ArrayList<TodoItem> list;
    public Filter filter;

    public FilteredTodoList(Filter filter, RawTodoList rawTodoList) {
        this.filter = filter;
        this.list = makeFiltered(filter, rawTodoList);
    }

    private ArrayList<TodoItem> makeFiltered(Filter filter, RawTodoList rawTodoList) {
        ArrayList<TodoItem> newList = new ArrayList<TodoItem>();

        switch (filter) {
            case ALL:
                newList.addAll(rawTodoList.list);
                break;
            case ACTIVE:
                for (TodoItem item : rawTodoList.list) {
                    if (!item.isComplete()) {
                        newList.add(item);
                    }
                }
                break;
            case COMPLETE:
                for (TodoItem item : rawTodoList.list) {
                    if (item.isComplete()) {
                        newList.add(item);
                    }
                }
                break;
            default:
                break;
        }

        Collections.sort(newList, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem lhs, TodoItem rhs) {
                return (int) (lhs.getId() - rhs.getId());
            }
        });

        return newList;
    }

    public enum Filter {
        ALL, ACTIVE, COMPLETE
    }
}
