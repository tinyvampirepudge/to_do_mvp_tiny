package com.tiny.todomvptiny.data.source;

import android.support.annotation.NonNull;

import com.tiny.todomvptiny.data.Task;

import java.util.List;

/**
 * Created by tiny on 17/1/19.
 * Main entry point for accessing tasks data.
 * <p>
 * For simplicity, only getTasks() and getTask() have callbacks. Consider adding callbacks to
 * other methods to inform the user of network/database errors or successful operations.
 * For example, when a new task is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 * </p>
 * <p>
 * 这是获取tasks数据的入口。
 * 简单起见，只有getTasks()和getTask()方法由回调。可以自行给其他方法添加回调，比方说数据库活网络操作成功或失败
 * 时通知用户。
 * 例如，当一个新的task被创建时，它被同步存储在缓存中，但通常每个数据库或者网络操作都应该执行在不同的线程中。
 */

public interface TasksDataSource {
    interface LoadTasksCallback {
        void onTaskLoaded(List<Task> tasks);

        void onDataNoAvailable();
    }

    interface GetTaskCallBack {
        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    //批量获取Tasks
    void getTasks(@NonNull LoadTasksCallback callback);

    //根据id获取Task
    void getTask(@NonNull String taskId, @NonNull GetTaskCallBack callBack);

    //存储单个task
    void saveTask(@NonNull Task task);

    //标记某个task已完成,根据task对象
    void completeTask(@NonNull Task task);

    //标记某个task已完成,根据id
    void completeTask(@NonNull String taskId);

    //标记某个task未完成，根据task对象
    void activateTask(@NonNull Task task);

    //标记某个task未完成，根据id
    void activateTask(@NonNull String taskId);

    //清空所有已完成的Task
    void clearCompletedTasks();

    //刷新
    void refreshTasks();

    //删除所有的tasks
    void deleteAllTasks();

    //根据id删除某个task
    void deleteTask(@NonNull String taskId);
}
