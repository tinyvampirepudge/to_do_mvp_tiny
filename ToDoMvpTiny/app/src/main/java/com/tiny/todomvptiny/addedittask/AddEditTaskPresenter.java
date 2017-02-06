package com.tiny.todomvptiny.addedittask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tiny.todomvptiny.data.Task;
import com.tiny.todomvptiny.data.source.TasksDataSource;

/**
 * Created by tiny on 17/2/4.
 */

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter,
        TasksDataSource.GetTaskCallBack {

    @NonNull
    private final TasksDataSource mTasksRepository;

    @NonNull
    private final AddEditTaskContract.View mAddTaskView;

    @Nullable
    private String mTaskId;

    private boolean mIsDataMissing;

    /**
     * Creates a presenter for the add/edit view.
     *
     * @param mTaskId                ID of the task to edit or null for a new task.
     * @param mTasksRepository       a repository of data for tasks.
     * @param mAddTaskView           the add/edit view.
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddEditTaskPresenter(@NonNull String mTaskId,
                                @NonNull TasksDataSource mTasksRepository,
                                @NonNull AddEditTaskContract.View mAddTaskView,
                                @NonNull boolean shouldLoadDataFromRepo) {
        this.mTaskId = mTaskId;
        this.mTasksRepository = mTasksRepository;
        this.mAddTaskView = mAddTaskView;
        this.mIsDataMissing = shouldLoadDataFromRepo;
    }

    @Override
    public void start() {
        //如果是已有的task，并且数据未加载。
        if (!isNewTask() && mIsDataMissing) {
            populateTask();
        }
    }

    private boolean isNewTask() {
        return mTaskId == null;
    }

    @Override
    public void saveTask(String title, String description) {
        if (isNewTask()) {
            createTask(title, description);
        } else {
            updateTask(title, description);
        }
    }

    @Override
    public void populateTask() {
        if (isNewTask()) {
            throw new RuntimeException("populateTask() was called but task task is new.");
        }
        mTasksRepository.getTask(mTaskId, this);
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    @Override
    public void onTaskLoaded(Task task) {
        //The view may not be able to handle UI updates anymore.
        if (mAddTaskView.isActive()) {
            mAddTaskView.setTitle(task.getTitle());
            mAddTaskView.setDescription(task.getDescription());
        }
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        //The view may not bo able to handle UI updates anymore.
        if (mAddTaskView.isActive()) {
            mAddTaskView.showEmptyTaskError();
        }
    }

    private void createTask(String title, String description) {
        Task newTask = new Task(title, description);
        if (newTask.isEmpty()) {
            mAddTaskView.showEmptyTaskError();
        } else {
            mTasksRepository.saveTask(newTask);
            mAddTaskView.showTaskList();
        }
    }

    private void updateTask(String title, String description) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is now.");
        }
        mTasksRepository.saveTask(new Task(title, description, mTaskId));
        mAddTaskView.showTaskList();//After an edit, go back to the list.
    }
}
