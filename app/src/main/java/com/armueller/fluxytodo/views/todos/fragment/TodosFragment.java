package com.armueller.fluxytodo.views.todos.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.armueller.fluxytodo.R;
import com.armueller.fluxytodo.actions.ActionCreator;
import com.armueller.fluxytodo.busses.DataBus;
import com.armueller.fluxytodo.data.FilteredTodoList;
import com.armueller.fluxytodo.google.FloatingActionButton;
import com.armueller.fluxytodo.models.TodoListManager;
import com.armueller.fluxytodo.views.todos.TodosActivity;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TodosFragment extends Fragment {
    // Potentially one list per fragment.
    @Inject
    TodoListManager todoListManager;

    @InjectView(R.id.todoList)
    ListView todoListView;

    @InjectView(R.id.newTodo)
    EditText newTodo;

    @InjectView(R.id.deleteButton)
    FloatingActionButton deleteButton;

    @Inject
    Activity activity;

    @Inject
    ActionCreator actionCreator;

    @Inject
    DataBus dataBus;

    @Inject
    TodoListArrayAdapter todoItemsAdapter;

    public TodosFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((TodosActivity) activity).getComponent().inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todos, container, false);
        ButterKnife.inject(this, rootView);

        todoListView.setAdapter(todoItemsAdapter);

        newTodo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    actionCreator.createNewTodoAction(v.getText().toString());
                    newTodo.setText("");
                    handled = true;
                }
                return handled;
            }
        });
        newTodo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    actionCreator.createSetTodoItemAsEditableAction(-1);
                }
            }
        });

        return rootView;
    }

    @OnClick(R.id.deleteButton)
    public void deleteAllCompleted() {
        if (deleteButton.isChecked()) {
            actionCreator.createDeleteAllCompleteTodosAction();
        } else {
            actionCreator.createUndoDeleteAllCompleteTodosAction();
        }
    }

    @Subscribe
    public void onListUpdated(final FilteredTodoList filteredTodoList) {
        todoItemsAdapter.clear();
        todoItemsAdapter.addAll(filteredTodoList.list);
    }

    @Subscribe
    public void shouldShowUndoButton(Boolean shouldShowUndoButton) {
        deleteButton.setChecked(shouldShowUndoButton);
    }

    @Override
    public void onPause() {
        todoListManager.onPause();
        dataBus.unregister(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onPause();
        dataBus.register(this);
    }
}