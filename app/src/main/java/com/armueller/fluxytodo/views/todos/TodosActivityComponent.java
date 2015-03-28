package com.armueller.fluxytodo.views.todos;

import android.app.Activity;

import com.armueller.fluxytodo.app.TodosApplication;
import com.armueller.fluxytodo.app.TodosApplicationComponent;
import com.armueller.fluxytodo.di.PerActivity;
import com.armueller.fluxytodo.views.todos.fragment.EditOrDeleteDialogFragment;
import com.armueller.fluxytodo.views.todos.fragment.TodosFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = TodosApplicationComponent.class, modules = TodosActivityModule.class)
public interface TodosActivityComponent {

    public final static class Initializer {
        public static TodosActivityComponent init(TodosActivity activity) {
            return Dagger_TodosActivityComponent.builder()
                    .todosApplicationComponent(((TodosApplication) activity.getApplication()).component())
                    .todosActivityModule(new TodosActivityModule(activity))
                    .build();
        }

    }

    void inject(TodosActivity todosActivity);
    void inject(TodosFragment todosFragment);
    void inject(EditOrDeleteDialogFragment editOrDeleteDialogFragment);

    // Expose the activity to sub-graphs.
    Activity activity();
}