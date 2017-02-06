package com.tiny.todomvptiny.addedittask;

import com.tiny.todomvptiny.base.BasePresenter;
import com.tiny.todomvptiny.base.BaseView;

/**
 * Created by tiny on 17/2/4.
 * This specifies the contract between the view and the presenter
 */

public interface AddEditTaskContract {
    interface Presenter extends BasePresenter {
        void saveTask(String title, String description);

        void populateTask();

        boolean isDataMissing();
    }

    interface View extends BaseView<Presenter> {
        void showEmptyTaskError();

        void showTaskList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();
    }
}
