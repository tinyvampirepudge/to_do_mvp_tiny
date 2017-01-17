package com.tiny.todomvptiny.tasks;

/**
 * Created by tiny on 17/1/7.
 * Used with the filter spinner in the tasks list.
 */
public enum TasksFilterType {
    /**
     * o not filter tasks.
     */
    ALL_TASKS,
    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS,
    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS
}
