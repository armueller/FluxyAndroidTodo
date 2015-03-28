package com.armueller.fluxytodo.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.armueller.fluxytodo.busses.ActionBus;
import com.armueller.fluxytodo.busses.DataBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TodosApplicationModule {
    private final TodosApplication application;

    public TodosApplicationModule(TodosApplication application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link com.armueller.fluxytodo.di.ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    Application application() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences providePreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return (Resources) application.getResources();
    }

    @Provides
    @Singleton
    DataBus provideDataBus() {
        return new DataBus();
    }

    @Provides
    @Singleton
    ActionBus provideActionBus() {
        return new ActionBus();
    }
}