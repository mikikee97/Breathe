package com.example.breathe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    String uid;

    public RecyclerView recyclerView;
    public ArrayList<Tasks> tasksList;
    public TaskListAdapter adapter;
    private FloatingActionButton addActivity;
    public static Activity fa;
    private TextView noActivityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskslist);
        noActivityText = (TextView) findViewById(R.id.noactivity_text);
        addActivity =  (FloatingActionButton) findViewById(R.id.add_activity_btn);
        fa = this;
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("User").child(uid).child("task");

        recyclerView = findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksList = new ArrayList<>();
        adapter = new TaskListAdapter(TaskListActivity.this,tasksList);
        recyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Tasks task = dataSnapshot1.getValue(Tasks.class);
                    tasksList.add(task);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TaskListActivity.this, "Oops....there is to tasks yet", Toast.LENGTH_SHORT).show();
            }
        });




    }


    public void fulldetail(ArrayList<Tasks> arrayListTask, int adapterPosition){
        Tasks taskObject = arrayListTask.get(adapterPosition);
        //put intent here n pass

        Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
        intent.putExtra("taskObject", taskObject);
        TaskListActivity.this.startActivity(intent);
    }


    public void add_clicked(View v){
        Intent intent = new Intent(this,InfoActivity.class);
        startActivity(intent);
    }
}
