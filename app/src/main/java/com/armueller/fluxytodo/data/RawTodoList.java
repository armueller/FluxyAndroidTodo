package com.armueller.fluxytodo.data;

import com.armueller.fluxytodo.models.TodoItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by armueller on 3/27/15.
 */
public class RawTodoList {
    public ArrayList<TodoItem> list;


    public RawTodoList() {
        list = new ArrayList<TodoItem>();
    }

    public RawTodoList(ArrayList<TodoItem> list) {
        this.list = new ArrayList<TodoItem>();
        this.list.addAll(list);
    }

    public RawTodoList(HashMap<Long, TodoItem> list) {
        this.list = new ArrayList<TodoItem>();
        this.list.addAll(list.values());
    }
}
