package com.tiny.todomvptiny.tasks;

import com.tiny.todomvptiny.data.Task;

/**
 * Created by tiny on 17/1/8.
 */
public interface TaskItemListener {
    void onTaskClick(Task clickedTask);
    void onCompletedTaskClick(Task compltetedTask);
    void onActivateTaskClick(Task activatedTask);
}
