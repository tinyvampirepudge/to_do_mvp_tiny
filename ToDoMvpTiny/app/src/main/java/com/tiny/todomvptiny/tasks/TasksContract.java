package com.tiny.todomvptiny.tasks;

import android.support.annotation.NonNull;

import com.tiny.todomvptiny.base.BasePresenter;
import com.tiny.todomvptiny.base.BaseView;
import com.tiny.todomvptiny.data.Task;

import java.util.List;

/**
 * Created by tiny on 17/1/6.
 * This specifies the contract between the view and the presenter.
 * Contract类指定了view和presenter的关系
 */
public interface TasksContract {
    interface View extends BaseView<Presenter> {
        //设置加载指示器的状态
        void setLoadingIndicator(boolean active);

        //显示task列表数据
        void showTasks(List<Task> tasks);

        //显示添加的task
        void showAddTask();

        //显示task详情的ui
        void showTaskDetailsUi(String taskId);

        //显示已完成的tasks
        void showTaskMarkedComplete();

        //显示未完成的tasks
        void showTasksMarkedActive();

        //显示已完成的task被清除掉的UI
        void showCompletedTasksCleared();

        //当加载task失败时的回调
        void showLoadingTasksError();

        //当没有task时显示的UI。
        void showNoTasks();

        //显示未完成类型
        void showActiveFilterLabel();

        //显示已完成类型
        void showCompletedFilterLabel();

        //显示所有类型
        void showAllFilterLabel();

        //显示没有未完成tasks的界面
        void showNoActiveTasks();

        //显示没有已完成tasks的界面
        void showNoCompletedTasks();

        //显示信息保存成功的界面
        void showSuccessfulSavedMessage();

        //是否活跃
        boolean isActive();

        //显示筛选看的选付款
        void showFilteringPopUpMenu();
    }

    interface Presenter extends BasePresenter {
        //请求结果
        void result(int requestCode, int resultCode);

        //加载tasks(是否强制)
        void loadTasks(boolean forceUpdate);

        //添加新的task
        void addNewTasks();

        //查看task详情
        void openTaskDetails(@NonNull Task requestedTask);

        //标记某个tasks已完成
        void completeTask(@NonNull Task completedTask);

        //标记某个task未完成
        void activateTask(@NonNull Task activeTask);

        //清除所有已标记完成的task。
        void clearCompletedTasks();

        //设置筛选的类型
        void setFiltering(TasksFilterType requestType);

        //获取筛选的类型
        TasksFilterType getFiltering();
    }
}
