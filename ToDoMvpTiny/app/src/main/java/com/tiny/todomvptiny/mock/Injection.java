package com.tiny.todomvptiny.mock;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tiny.todomvptiny.data.source.TasksRepository;
import com.tiny.todomvptiny.data.source.local.TasksLocalDataSource;
import com.tiny.todomvptiny.mock.data.FakeTasksRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by tiny on 17/1/20.
 * Enables injection of mock implements for
 * {@link com.tiny.todomvptiny.data.source.TasksDataSource} at compile time.
 * This is useful for testing, since it allows us to use a fake instance of the class to isolate the
 * dependencies and run a test hermetically.
 */

public class Injection {

    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        return TasksRepository.getInstance(FakeTasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(context));
    }
}
