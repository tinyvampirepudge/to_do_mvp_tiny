package com.tiny.todomvptiny.statistics;

import com.tiny.todomvptiny.base.BasePresenter;
import com.tiny.todomvptiny.base.BaseView;

/**
 * Created by tiny on 17/2/6.
 * This specifies the contract between the view and presenter.
 */

public interface StatisticsContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {
        void setProgressIndicator(boolean active);

        void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks);

        void showLoadingStatisticsError();

        boolean isActive();
    }
}
