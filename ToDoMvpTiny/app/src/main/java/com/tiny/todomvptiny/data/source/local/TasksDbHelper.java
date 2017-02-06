package com.tiny.todomvptiny.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tiny on 17/1/20.
 */

public class TasksDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Tasks.db";
    public static final String TEXT_TYPE = " TEXT";
    public static final String BOOLEAN_TYPE = " INTEGER";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TasksPersistenceContract.TasksEntry.TABLE_NAME + " (" +
                    TasksPersistenceContract.TasksEntry._ID + TEXT_TYPE + " PRIVATE KEY," +
                    TasksPersistenceContract.TasksEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    TasksPersistenceContract.TasksEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    TasksPersistenceContract.TasksEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    TasksPersistenceContract.TasksEntry.COLUMN_NAME_COMPLETED + BOOLEAN_TYPE +
                    " )";

    public TasksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
