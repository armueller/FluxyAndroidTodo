package com.armueller.fluxytodo.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class TodoItemTest {

    @Test
    public void todoItemCreation() {
        TodoItem item = new TodoItem(1, "test", false);
        assertThat(item.getId()).isEqualTo(1);
        assertThat(item.getDescription()).isEqualTo("test");
        assertThat(item.isComplete()).isFalse();
    }
}