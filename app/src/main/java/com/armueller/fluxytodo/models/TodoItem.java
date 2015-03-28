package com.armueller.fluxytodo.models;

/**
 * Created by armueller on 3/8/15.
 */
public class TodoItem {
    private final long id;
    private final String description;
    private final boolean isComplete;

    public TodoItem(final long id, final String description, final boolean isComplete) {
        this.id = id;
        this.description = description;
        this.isComplete = isComplete;
    }

    public final long getId() {
        return id;
    }

    public final String getDescription() {
        return description;
    }

    public final boolean isComplete() {
        return isComplete;
    }
}
