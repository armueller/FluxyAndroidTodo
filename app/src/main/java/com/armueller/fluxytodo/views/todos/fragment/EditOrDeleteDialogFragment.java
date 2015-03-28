package com.armueller.fluxytodo.views.todos.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import com.armueller.fluxytodo.R;
import com.armueller.fluxytodo.actions.ActionCreator;
import com.armueller.fluxytodo.views.todos.TodosActivity;

import javax.inject.Inject;

public class EditOrDeleteDialogFragment extends DialogFragment {
    @Inject
    Activity activity;

    @Inject
    Resources r;

    @Inject
    ActionCreator actionCreator;

    private long todoItemId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((TodosActivity)activity).getComponent().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoItemId = getArguments().getLong("todoItemId");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(r.getString(R.string.edit_delete_prompt));
        builder.setItems(new String[]{r.getString(R.string.edit), r.getString(R.string.delete)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    actionCreator.createSetTodoItemAsEditableAction(todoItemId);
                } else {
                    actionCreator.createDeleteTodoAction(todoItemId);
                }
            }
        });
        builder.setCancelable(true);
        builder.create();
        return builder.create();
    }
}