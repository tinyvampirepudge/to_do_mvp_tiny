package com.tiny.todomvptiny.mock.data;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.tiny.todomvptiny.data.Task;
import com.tiny.todomvptiny.data.source.TasksDataSource;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by tiny on 17/1/20.
 * Implementation of a remote data source with static access to the data for easy tasting.
 */

public class FakeTasksRemoteDataSource implements TasksDataSource {
    private static FakeTasksRemoteDataSource instance;

    private static final Map<String, Task> TASKS_SERVICE_DATA = new LinkedHashMap<>();

    private FakeTasksRemoteDataSource() {
    }

    public static FakeTasksRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new FakeTasksRemoteDataSource();
        }
        return instance;
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        callback.onTaskLoaded(Lists.<Task>newArrayList(TASKS_SERVICE_DATA.values()));
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallBack callBack) {
        Task task = TASKS_SERVICE_DATA.get(taskId);
        callBack.onTaskLoaded(task);
    }

    @Override
    public void saveTask(@NonNull Task task) {
        TASKS_SERVICE_DATA.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        TASKS_SERVICE_DATA.put(completedTask.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        //Not required for the remote data source.
    }

    @Override
    public void activateTask(@NonNull Task task) {
        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId(), false);
        TASKS_SERVICE_DATA.put(activeTask.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        //Not required for the remote data source.
    }

    @Override
    public void clearCompletedTasks() {
        Iterator<Map.Entry<String,Task>> it = TASKS_SERVICE_DATA.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,Task> entry =it.next();
            if(entry.getValue().isCompleted()){
                it.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        TASKS_SERVICE_DATA.remove(taskId);
    }
}
