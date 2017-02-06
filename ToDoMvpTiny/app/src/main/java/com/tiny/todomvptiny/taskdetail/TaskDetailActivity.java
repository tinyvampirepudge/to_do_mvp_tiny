package com.tiny.todomvptiny.taskdetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tiny.todomvptiny.R;
import com.tiny.todomvptiny.mock.Injection;
import com.tiny.todomvptiny.util.ActivityUtils;

/**
 * Displays task detail screen.
 */
public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        //Get the requested task id
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        //第一次获取到的为空。
        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInstance(taskId);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), taskDetailFragment,
                    R.id.contentFrame);
        }

        //Create the presenter
        new TaskDetailPresenter(taskId, Injection.provideTasksRepository(getApplicationContext()),
                taskDetailFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
