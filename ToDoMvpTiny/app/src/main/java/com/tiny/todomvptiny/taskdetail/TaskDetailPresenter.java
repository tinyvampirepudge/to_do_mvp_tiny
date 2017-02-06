package com.tiny.todomvptiny.taskdetail;

import android.support.annotation.Nullable;

import com.google.common.base.Strings;
import com.tiny.todomvptiny.data.Task;
import com.tiny.todomvptiny.data.source.TasksDataSource;
import com.tiny.todomvptiny.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by tiny on 17/2/3.
 * Listens to user actions from the UI,retrieves the data and updates the UI as required.
 */

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private final TasksRepository mTasksRepository;

    private final TaskDetailContract.View mTasksDetailView;

    @Nullable
    private String mTaskId;

    public TaskDetailPresenter(@Nullable String mTaskId,
                               @Nullable TasksRepository mTasksRepository,
                               @Nullable TaskDetailContract.View mTasksDetailView) {
        this.mTasksRepository = checkNotNull(mTasksRepository);
        this.mTasksDetailView = checkNotNull(mTasksDetailView);
        this.mTaskId = checkNotNull(mTaskId);

        mTasksDetailView.setPresenter(this);
    }


    @Override
    public void start() {
        openTask();
    }

    private void openTask() {
        //非空判断
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTasksDetailView.showMissingTask();
            return;
        }

        //加载数据
        mTasksDetailView.setLoadingIndicator(true);
        mTasksRepository.getTask(mTaskId, new TasksDataSource.GetTaskCallBack() {
            @Override
            public void onTaskLoaded(Task task) {
                if (!mTasksDetailView.isActive()) {
                    return;
                }
                mTasksDetailView.setLoadingIndicator(false);
                if (null == task) {
                    mTasksDetailView.showMissingTask();
                } else {
                    showTask(task);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!mTasksDetailView.isActive()) {
                    return;
                }
                mTasksDetailView.showMissingTask();
            }
        });
    }

    @Override
    public void editTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTasksDetailView.showMissingTask();
            return;
        }
        mTasksDetailView.showEditTask(mTaskId);
    }

    @Override
    public void deleteTask() {
        if(Strings.isNullOrEmpty(mTaskId)){
            mTasksDetailView.showMissingTask();
            return;
        }
        mTasksRepository.deleteTask(mTaskId);
        mTasksDetailView.showTaskDeleted();
    }

    @Override
    public void completeTask() {
        if(Strings.isNullOrEmpty(mTaskId)){
            mTasksDetailView.showMissingTask();
            return;
        }
        mTasksRepository.completeTask(mTaskId);
        mTasksDetailView.showTaskMarkedComplete();
    }

    @Override
    public void activeTask() {
        if(Strings.isNullOrEmpty(mTaskId)){
            mTasksDetailView.showMissingTask();
            return;
        }
        mTasksRepository.activateTask(mTaskId);
        mTasksDetailView.showTaskMarkedActive();
    }

    private void showTask(@Nullable Task task) {
        String title = task.getTitle();
        String description = task.getDescription();

        if (Strings.isNullOrEmpty(title)) {
            mTasksDetailView.hideTitle();
        } else {
            mTasksDetailView.showTitle(title);
        }

        if (Strings.isNullOrEmpty(description)) {
            mTasksDetailView.hideDescription();
        } else {
            mTasksDetailView.showDescription(description);
        }
        mTasksDetailView.showCompletionStatus(task.isCompleted());
    }
}
