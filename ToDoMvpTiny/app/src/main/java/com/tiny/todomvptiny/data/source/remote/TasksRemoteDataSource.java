package com.tiny.todomvptiny.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.tiny.todomvptiny.data.Task;
import com.tiny.todomvptiny.data.source.TasksDataSource;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by tiny on 17/1/20.
 * Implementation of the data source that adds a latency simulating network.
 */

public class TasksRemoteDataSource implements TasksDataSource {
    private static TasksRemoteDataSource instance;

    //延时时间
    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static final Map<String, Task> TASKS_SERVICE_DATA;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required. ");
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost! ");
    }

    private static void addTask(String title, String description) {
        Task newTask = new Task(title, description);
        TASKS_SERVICE_DATA.put(newTask.getId(), newTask);
    }

    private TasksRemoteDataSource() {
    }

    public static TasksRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new TasksRemoteDataSource();
        }
        return instance;
    }

    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTaskLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull final GetTaskCallBack callBack) {
        final Task task = TASKS_SERVICE_DATA.get(taskId);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.onTaskLoaded(task);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
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

    }

    @Override
    public void activateTask(@NonNull Task task) {
        Task activeTask = new Task(task.getTitle(),task.getDescription(),task.getId(),false);
        TASKS_SERVICE_DATA.put(activeTask.getId(),activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTasks() {
        Iterator<Map.Entry<String,Task>> it = TASKS_SERVICE_DATA.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,Task> entry = it.next();
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
