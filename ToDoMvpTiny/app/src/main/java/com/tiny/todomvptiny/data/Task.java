package com.tiny.todomvptiny.data;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Created by tiny on 17/1/6.
 * Immutable model class for a Task.
 */
public final class Task {
    @NonNull
    private final String mId;

    @NonNull
    private final String mTitle;

    @NonNull
    private final String mDescription;

    private final boolean mCompleted;

    public Task(@NonNull String mTitle, @NonNull String mDescription) {
        this(mTitle, mDescription, UUID.randomUUID().toString(), false);
    }

    public Task(@NonNull String mTitle, @NonNull String mDescription, @NonNull String mId) {
        this(mTitle, mDescription, mId, false);
    }

    public Task(@NonNull String mTitle, @NonNull String mDescription, boolean mCompleted) {
        this(mTitle, mDescription, UUID.randomUUID().toString(), mCompleted);
    }

    public Task(@NonNull String mTitle, @NonNull String mDescription, @NonNull String mId, boolean mCompleted) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mCompleted = mCompleted;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    /**
     * 返回title，如果title为空，则返回desc
     *
     * @return
     */
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mTitle)) {
            return mTitle;
        } else {
            return mDescription;
        }
    }

    @NonNull
    public String getDescription() {
        return mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    /**
     * 是否未完成
     *
     * @return
     */
    public boolean isActive() {
        return !mCompleted;
    }

    /**
     * task的title和desc是否均为空
     *
     * @return
     */
    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mTitle)
                && Strings.isNullOrEmpty(mDescription);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Task task = (Task) obj;
        return Objects.equal(mId, task.mId)
                && Objects.equal(mTitle, task.mTitle)
                && Objects.equal(mDescription, task.mDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mDescription);
    }

    @Override
    public String toString() {
        return "Task with title" + mTitle;
    }
}
