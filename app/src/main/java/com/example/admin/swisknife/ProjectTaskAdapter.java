package com.example.admin.swisknife;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 8/12/2017.
 */

public class ProjectTaskAdapter extends RecyclerView.Adapter<ProjectTaskAdapter.ViewHolder> {

    List<ProjectTask> taskList = new ArrayList<>();

    public ProjectTaskAdapter(List<ProjectTask> taskList) {
        this.taskList = taskList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTaskNumber, tvTaskName, tvTaskDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTaskNumber = (TextView) itemView.findViewById(R.id.tvTaskNumber);
            tvTaskName = (TextView) itemView.findViewById(R.id.tvTaskName);
            tvTaskDescription = (TextView) itemView.findViewById(R.id.tvTaskDescription);

        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekend_task_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ProjectTask task = taskList.get(position);

        holder.tvTaskNumber.setText("" + task.getTaskNumber());
        holder.tvTaskName.setText("" + task.getTaskName());
        holder.tvTaskDescription.setText("" + task.getTaskDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageEvent event = new MessageEvent();
                event.setCustomMessage("" + position);
                EventBus.getDefault().post(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
