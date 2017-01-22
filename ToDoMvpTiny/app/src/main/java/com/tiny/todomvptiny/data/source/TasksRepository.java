package com.tiny.todomvptiny.data.source;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.tiny.todomvptiny.data.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by tiny on 17/1/19.
 * Concrete implementation to load tasks from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and
 * data obtained from server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 * </p>
 * 将tasks从数据源加载进cache的具体实现。
 * 简单起见，这个类很简单的实现了本地已存在的数据和网络获取的数据之间的同步 --> 如果本地数据不存在就使用远程数据。
 */

public class TasksRepository implements TasksDataSource {
    private static TasksRepository instance = null;

    private final TasksDataSource mTasksRemoteDataSource;

    private final TasksDataSource mTasksLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Task> mCachedTasks;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility to it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    //Prevent direct instantiation
    private TasksRepository(TasksDataSource mTasksRemoteDataSource, TasksDataSource mTasksLocalDataSource) {
        this.mTasksRemoteDataSource = checkNotNull(mTasksRemoteDataSource);
        this.mTasksLocalDataSource = checkNotNull(mTasksLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param mTasksRemoteDataSource
     * @param mTasksLocalDataSource
     * @return
     */
    public static TasksRepository getInstance(TasksDataSource mTasksRemoteDataSource, TasksDataSource mTasksLocalDataSource) {
        if (instance == null) {
            instance = new TasksRepository(mTasksRemoteDataSource, mTasksLocalDataSource);
        }
        return instance;
    }

    /**
     * Used to force {@link #getInstance(TasksDataSource, TasksDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        instance = null;
    }

    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        checkNotNull(callback);

        //Respond immediately with cache if available and not dirty.
        if (mCachedTasks != null && !mCacheIsDirty) {
            callback.onTaskLoaded(new ArrayList<Task>(mCachedTasks.values()));
            return;
        }

        if (mCacheIsDirty) {
            //If the cache is dirty we need to fetch new data from the network.
            getTasksFromRemoteDataSource(callback);
        } else {
            //Query the local storage if available. If not, query the network.
            //第一次进入会走这个逻辑，从本地缓存加载逻辑。
            mTasksLocalDataSource.getTasks(new LoadTasksCallback() {
                @Override
                public void onTaskLoaded(List<Task> tasks) {
                    refreshCache(tasks);
                    //将加载到的数据传递给callback
                    callback.onTaskLoaded(new ArrayList<Task>(mCachedTasks.values()));
                }

                @Override
                public void onDataNoAvailable() {
                    //如果本地本地没有加载到数据，就从服务器获取。
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }
    }

    /**
     * 保存某个task, 依次添加到服务器，本地缓存，内存。
     *
     * @param task
     */
    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.saveTask(task);
        mTasksLocalDataSource.saveTask(task);

        //Do in memory cache update to keep the app UI up to date
        initCachedTasks();
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.completeTask(task);
        mTasksLocalDataSource.completeTask(task);

        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        //Do in memory cache update to keep the app UI up to date
        initCachedTasks();
        mCachedTasks.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.activateTask(task);
        mTasksLocalDataSource.activateTask(task);

        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());

        //Do in memory cache update to keep the app UI up to date
        initCachedTasks();
        mCachedTasks.put(task.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRemoteDataSource.clearCompletedTasks();
        mTasksLocalDataSource.clearCompletedTasks();

        //Do in memory cache update to keep the app UI up to date
        initCachedTasks();
        Iterator<Map.Entry<String, Task>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }


    /**
     * Gets tasks from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note:{@link GetTaskCallBack#onDataNotAvailable()} is fired if both data sources fail to get
     * the data.
     * <p>
     * 默认是从sqlite获取本地数据，如果数据库为空，就需要使用网络数据了，这里简化了这个实例。
     */
    @Override
    public void getTask(@NonNull final String taskId, @NonNull final GetTaskCallBack callBack) {
        checkNotNull(taskId);
        checkNotNull(callBack);

        Task cachedTask = getTaskWithId(taskId);
        //Respond immediately with cache if available
        if (cachedTask != null) {
            callBack.onTaskLoaded(cachedTask);
            return;
        }
        //Load from server/persisted if needed.

        //Is the task in the local data source? If not, query the network.
        mTasksLocalDataSource.getTask(taskId, new GetTaskCallBack() {

            @Override
            public void onTaskLoaded(Task task) {
                //Do in memory cache update to keep the app UI up to date
                initCachedTasks();
                mCachedTasks.put(task.getId(), task);
                callBack.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                mTasksRemoteDataSource.getTask(taskId, new GetTaskCallBack() {
                    @Override
                    public void onTaskLoaded(Task task) {
                        //Do in memory cache update to keep the app UI up to date
                        initCachedTasks();
                        mCachedTasks.put(task.getId(), task);
                        callBack.onTaskLoaded(task);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callBack.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void refreshTasks() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {
        mTasksRemoteDataSource.deleteAllTasks();
        mTasksLocalDataSource.deleteAllTasks();

        initCachedTasks();
        mCachedTasks.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        mTasksRemoteDataSource.deleteTask(checkNotNull(taskId));
        mTasksLocalDataSource.deleteTask(checkNotNull(taskId));

        mCachedTasks.remove(taskId);
    }

    /**
     * 从网络请求最新的数据，并刷新本地缓存和本地存储中的数据
     *
     * @param callback
     */
    private void getTasksFromRemoteDataSource(final LoadTasksCallback callback) {
        mTasksRemoteDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTaskLoaded(List<Task> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTaskLoaded(new ArrayList<Task>(mCachedTasks.values()));
            }

            @Override
            public void onDataNoAvailable() {
                callback.onDataNoAvailable();
            }
        });
    }

    /**
     * 清除内存中的缓存，将最新的数据加载进缓存。
     *
     * @param tasks
     */
    private void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new ArrayMap<>();
        }
        mCachedTasks.clear();
        for (Task task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }
        mCacheIsDirty = false;
    }

    /**
     * 删除数据库中的本地缓存，将请求到的数据添加进来。
     *
     * @param tasks
     */
    private void refreshLocalDataSource(List<Task> tasks) {
        mTasksLocalDataSource.deleteAllTasks();
        for (Task task : tasks) {
            mTasksLocalDataSource.saveTask(task);
        }
    }

    /**
     * 初始化mCachedTasks.
     */
    private void initCachedTasks() {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
    }

    @NonNull
    private Task getTaskWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(id);
        }
    }
}
