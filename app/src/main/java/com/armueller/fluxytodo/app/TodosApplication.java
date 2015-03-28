package com.armueller.fluxytodo.app;

import android.app.Application;
import android.location.LocationManager;

import javax.inject.Inject;

/**
 * Created by armueller on 3/4/15.
 */
public class TodosApplication extends Application {
    private TodosApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = TodosApplicationComponent.Initializer.init(this);
    }

    public TodosApplicationComponent component() {
        return component;
    }
}
