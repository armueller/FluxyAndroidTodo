package com.armueller.fluxytodo.views.todos.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.armueller.fluxytodo.R;
import com.armueller.fluxytodo.actions.ActionCreator;
import com.armueller.fluxytodo.busses.DataBus;
import com.armueller.fluxytodo.models.TodoItem;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TodoListArrayAdapter extends ArrayAdapter<TodoItem> {
    private final Activity activity;
    @Inject
    ActionCreator actionCreator;
    private final LayoutInflater inflater;
    private final DataBus dataBus;

    @Inject
    public TodoListArrayAdapter(Activity context, DataBus dataBus) {
        super(context, R.layout.todo_item);
        this.activity = context;
        this.inflater = context.getLayoutInflater();
        this.dataBus = dataBus;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        final TodoItem item = getItem(position);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
            holder.item = item;
        } else {
            view = inflater.inflate(R.layout.todo_item, parent, false);
            holder = new ViewHolder(view, item);
            view.setTag(holder);
        }
        inflateHolderData(holder, item);
        setHolderListeners(holder, item);

        return view;
    }

    private void inflateHolderData(final ViewHolder holder, final TodoItem item) {
        holder.todoDescription.setText(item.getDescription());
        holder.todoEditDescription.setText(item.getDescription());
        holder.todoCheckBox.setChecked(item.isComplete());
    }

    private void setHolderListeners(final ViewHolder holder, final TodoItem item) {
        holder.todoDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogFragment editOrDeleteDialog = new EditOrDeleteDialogFragment();
                Bundle args = new Bundle();
                args.putLong("todoItemId", item.getId());
                editOrDeleteDialog.setArguments(args);
                editOrDeleteDialog.show(activity.getFragmentManager(), String.valueOf(item.getId()));
                return true;
            }
        });

        holder.todoEditDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    actionCreator.createEditTodoAction(item.getId(), v.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });

        holder.todoCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCreator.createToggleTodoCompleteAction(item.getId());
            }
        });
    }

    class ViewHolder {
        @InjectView(R.id.todoDescription)
        TextView todoDescription;

        @InjectView(R.id.todoEditDescription)
        EditText todoEditDescription;

        @InjectView(R.id.todoCheckBox)
        CheckBox todoCheckBox;

        private TodoItem item;

        public ViewHolder(View view, TodoItem item) {
            ButterKnife.inject(this, view);
            this.item = item;
            dataBus.register(this);
        }

        @Subscribe
        public void setEditModeActiveForTodoId(Long todoId) {
            if (item.getId() == todoId.longValue()) {
                todoDescription.setVisibility(View.INVISIBLE);
                todoEditDescription.setVisibility(View.VISIBLE);
                todoEditDescription.requestFocus();
                todoEditDescription.setSelection(todoEditDescription.getText().length());
            } else {
                todoDescription.setVisibility(View.VISIBLE);
                todoEditDescription.setVisibility(View.INVISIBLE);
            }
        }
    }
}