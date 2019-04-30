package com.example.breathe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {

    TextView task_title, task_date, task_time, task_desc, task_location;
    String stTitle, stDate;
    Button btnShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        btnShare = findViewById(R.id.btnShare);
        task_title = findViewById(R.id.textview_detail_title);
        task_date = findViewById(R.id.textview_detail_date);
        task_time = findViewById(R.id.textview_detail_time);
        task_desc = findViewById(R.id.textview_detail_description);
        task_location = findViewById(R.id.textview_detail_location);

        Tasks task = (Tasks) getIntent().getSerializableExtra("taskObject");



        task_title.setText(task.getTitle());
        task_date.setText(task.getDate());
        task_time.setText(task.getTime());
        task_desc.setText(task.getDescription());
        task_location.setText(task.getLocation());
        stTitle = task.getTitle();
        stDate = task.getDate();




    }

    public void btn_share_clicked(View view){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"ToDo: " + stTitle + "\nAt date: " + stDate);
        startActivity(Intent.createChooser(shareIntent, "Sharing Using"));
    }
}
