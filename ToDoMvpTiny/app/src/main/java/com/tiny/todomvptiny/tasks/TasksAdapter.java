package com.tiny.todomvptiny.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tiny.todomvptiny.R;
import com.tiny.todomvptiny.data.Task;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by tiny on 17/1/8.
 */
public class TasksAdapter extends BaseAdapter {
    private List<Task> mTasks;
    private TaskItemListener mItemListener;

    public TasksAdapter(List<Task> mTasks, TaskItemListener mItemListener) {
        this.mTasks = setLists(mTasks);
        this.mItemListener = mItemListener;
    }

    private List<Task> setLists(List<Task> lists) {
        mTasks = checkNotNull(lists);
        return lists;
    }

    public void replaceData(List<Task> tasks) {
        setLists(tasks);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTasks == null ? 0 : mTasks.size();
    }

    @Override
    public Task getItem(int position) {
        return mTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        if (rootView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rootView = inflater.inflate(R.layout.task_item, parent, false);
        }

        final Task task = getItem(position);
        TextView titleTv = (TextView) rootView.findViewById(R.id.title);
        titleTv.setText(task.getTitleForList());


        CheckBox completeCB = (CheckBox) rootView.findViewById(R.id.complete);
        //Active/completed task UI
        completeCB.setChecked(task.isCompleted());
        if (task.isCompleted()) {
            rootView.setBackgroundDrawable(parent.getContext().getResources()
                    .getDrawable(R.drawable.list_completed_touch_feedback));
        } else {
            rootView.setBackgroundDrawable(parent.getContext().getResources()
                    .getDrawable(R.drawable.touch_feedback));
        }

        completeCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!task.isCompleted()) {
                    mItemListener.onCompletedTaskClick(task);
                } else {
                    mItemListener.onActivateTaskClick(task);
                }
            }
        });

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onTaskClick(task);
            }
        });
        return rootView;
    }
}
