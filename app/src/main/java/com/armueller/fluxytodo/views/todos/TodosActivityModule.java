package com.armueller.fluxytodo.views.todos;

import android.app.Activity;

import com.armueller.fluxytodo.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * A module to wrap the Activity state and expose it to the graph.
 */
@Module
public class TodosActivityModule {
    private final Activity activity;

    public TodosActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides
    @PerActivity
    Activity provideActivity() {
        return activity;
    }
}