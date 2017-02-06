package com.tiny.todomvptiny.statistics;

import android.support.annotation.NonNull;

import com.tiny.todomvptiny.data.Task;
import com.tiny.todomvptiny.data.source.TasksDataSource;
import com.tiny.todomvptiny.data.source.TasksRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by tiny on 17/2/6.
 */

public class StatisticsPresenter implements StatisticsContract.Presenter {
    private final TasksRepository mTasksRepository;
    private final StatisticsContract.View mStatisticsView;

    public StatisticsPresenter(@NonNull TasksRepository mTasksRepository,
                               @NonNull StatisticsContract.View mStatisticsView) {
        this.mTasksRepository = checkNotNull(mTasksRepository, "tasksRepository cannot be null!");
        this.mStatisticsView = checkNotNull(mStatisticsView, "StatisticsView cannot be null!");

        mStatisticsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadStatistics();
    }

    private void loadStatistics() {
        mStatisticsView.setProgressIndicator(true);

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTaskLoaded(List<Task> tasks) {
                int activateTasks = 0;
                int completedTasks = 0;

                for (Task task : tasks) {
                    if (task.isCompleted()) {
                        completedTasks++;
                    } else {
                        activateTasks++;
                    }
                }

                if(!mStatisticsView.isActive()){
                    return;
                }
                mStatisticsView.setProgressIndicator(false);

                mStatisticsView.showStatistics(activateTasks,completedTasks);
            }

            @Override
            public void onDataNoAvailable() {
                if(!mStatisticsView.isActive()){
                    return;
                }
                mStatisticsView.showLoadingStatisticsError();
            }
        });
    }
}
