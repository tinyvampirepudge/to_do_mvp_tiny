package com.tiny.todomvptiny.addedittask;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tiny.todomvptiny.R;
import com.tiny.todomvptiny.mock.Injection;
import com.tiny.todomvptiny.util.ActivityUtils;

/**
 * Displays an add or edit task screen.
 */
public class AddEditTaskActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    public static final String SHOULE_LOAD_DATA_FROM_REPO_KEY = "SHOULE_LOAD_DATA_FROM_REPO_KEY";

    private AddEditTaskPresenter mAddEditTaskPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        //Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        AddEditTaskFragment addEditTaskFragment =
                (AddEditTaskFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String taskId = getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK);

        if (addEditTaskFragment == null) {
            addEditTaskFragment = new AddEditTaskFragment();
            if (getIntent().hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK)) {
                ab.setTitle(R.string.edit_task);
                Bundle bundle = new Bundle();
                bundle.putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK, taskId);
                addEditTaskFragment.setArguments(bundle);
            } else {
                ab.setTitle(R.string.add_task);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditTaskFragment, R.id.contentFrame);
        }

        boolean shouldLoadDataFromRepo = true;
        //Prevent the presenter from loading data from the repository if this is a config change;
        if (savedInstanceState != null) {
            //Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULE_LOAD_DATA_FROM_REPO_KEY);
        }

        //Create the presenter
        mAddEditTaskPresenter = new AddEditTaskPresenter(
                taskId,
                Injection.provideTasksRepository(getApplicationContext()),
                addEditTaskFragment,
                shouldLoadDataFromRepo
        );

        addEditTaskFragment.setPresenter(mAddEditTaskPresenter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(SHOULE_LOAD_DATA_FROM_REPO_KEY, mAddEditTaskPresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
