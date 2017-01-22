package com.tiny.todomvptiny.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by tiny on 17/1/20.
 * THe contract used for the db to save the tasks locally.
 */

public final class TasksPersistenceContract {
    private TasksPersistenceContract(){}

    /* Inner class that defines the table contents */
    public static abstract class TasksEntry implements BaseColumns{
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }
}
