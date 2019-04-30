package com.example.breathe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> implements Serializable {

    Context context;
    ArrayList<Tasks> tasks;

    public TaskListAdapter(Context con, ArrayList<Tasks> arrayTasks){
       context = con;
       tasks = arrayTasks;
    }


    @NonNull
    @Override
    public TaskListAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.tasks_list_row,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder viewHolder, int i) {
        viewHolder.titletv.setText(tasks.get(i).getTitle());
        viewHolder.datetv.setText(tasks.get(i).getDate());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    class TaskViewHolder extends RecyclerView.ViewHolder{
        TextView titletv,datetv;
        public TaskViewHolder(final View itemView){
            super(itemView);
            titletv = (TextView) itemView.findViewById(R.id.title_tv);
            datetv = (TextView) itemView.findViewById(R.id.date_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ((TaskListActivity)context).fulldetail(tasks,getAdapterPosition());
                }
            });

        }



        }
    }

