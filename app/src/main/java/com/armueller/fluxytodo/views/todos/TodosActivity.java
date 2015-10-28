package com.armueller.fluxytodo.views.todos;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.armueller.fluxytodo.R;
import com.armueller.fluxytodo.actions.ActionCreator;
import com.armueller.fluxytodo.busses.DataBus;
import com.armueller.fluxytodo.data.FilteredTodoList;
import com.armueller.fluxytodo.stores.TodosActivityStore;
import com.armueller.fluxytodo.views.todos.fragment.TodosFragment;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class TodosActivity extends Activity {
    TodosActivityComponent component;

    //For view state
    @Inject
    TodosActivityStore todosActivityStore;

    @Inject
    DataBus dataBus;

    @Inject
    ActionCreator actionCreator;

    @Inject
    Resources r;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);

        component = TodosActivityComponent.Initializer.init(this);
        component.inject(this);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TodosFragment())
                    .commit();
        }
    }

    public TodosActivityComponent getComponent() {
        return component;
    }


    @Subscribe
    public void onListUpdated(final FilteredTodoList filteredTodoList) {
        Drawable newDrawable = null;
        switch (filteredTodoList.filter) {
            case ALL:
                newDrawable = r.getDrawable(R.drawable.ic_menu_view_all);
                break;
            case ACTIVE:
                newDrawable = r.getDrawable(R.drawable.ic_menu_view_active);
                break;
            case COMPLETE:
                newDrawable = r.getDrawable(R.drawable.ic_menu_view_complete);
                break;
        }
        if (menu != null) {
            menu.findItem(R.id.action_filter).setIcon(newDrawable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todos, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filterAll) {
            actionCreator.createViewAllTodosAction();
            return true;
        } else if (id == R.id.filterActive) {
            actionCreator.createViewActiveTodosAction();
            return true;
        } else if (id == R.id.filterComplete) {
            actionCreator.createViewCompleteTodosAction();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBus.register(this);
    }

    @Override
    protected void onPause() {
        todosActivityStore.onPause();
        super.onPause();
        dataBus.unregister(this);
    }
}
