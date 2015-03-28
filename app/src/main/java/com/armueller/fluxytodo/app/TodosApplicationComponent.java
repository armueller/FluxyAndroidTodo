package com.armueller.fluxytodo.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.armueller.fluxytodo.busses.ActionBus;
import com.armueller.fluxytodo.busses.DataBus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by armueller on 3/4/15.
 */
@Singleton
@Component(modules = {TodosApplicationModule.class})
public interface TodosApplicationComponent {

    public final static class Initializer {
        public static TodosApplicationComponent init(TodosApplication app) {
            return Dagger_TodosApplicationComponent.builder()
                    .todosApplicationModule(new TodosApplicationModule(app))
                    .build();
        }

    }

    void inject(TodosApplication app);

    // Exported for child-components.
    Application application();
    SharedPreferences sharedPreferences();
    Resources resources();
    ActionBus dataBus();
    DataBus actionBus();
}
