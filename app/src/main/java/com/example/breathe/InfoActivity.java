package com.example.breathe;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;


public class InfoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private TextView dateDisplay, timeDisplay;
    private Button pickDate, pickTime, save;
    private EditText titleText, locationText, descriptionNameText;
    private String time, date;

    FirebaseDatabase database;
    DatabaseReference ref;
    Tasks tasks;
    FirebaseAuth firebaseAuth;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(), R.color.blue, null)));
        bar.setTitle("New Activity");


        dateDisplay = (TextView) findViewById(R.id.dateDisplay);
        pickDate = (Button) findViewById(R.id.pickDate);
        titleText = (EditText) findViewById(R.id.editTitle);
        locationText = (EditText) findViewById(R.id.editLocation);
        timeDisplay = (TextView) findViewById(R.id.Time);
        descriptionNameText = (EditText) findViewById(R.id.editDescription);
        save = (Button) findViewById(R.id.save_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        ref = database.getReference( "User").child(uid).child("task");
        tasks = new Tasks();

    }




    //show date dialog
    public void pickDate_clicked(View view) {

        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    //show time dialog
    public void pickTime_clicked(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    //submit button clicked
    public void save_btn_clicked(View view) {


      final String editTitle = titleText.getText().toString();
        final String location = locationText.getText().toString();
        final String editDescription = descriptionNameText.getText().toString();
        //input validations
        if (TextUtils.isEmpty(editTitle) && TextUtils.isEmpty(editDescription)) {
            titleText.setError("Please enter title");
            descriptionNameText.setError("Please enter description");
            return;
        } else if (TextUtils.isEmpty(editTitle)) {
            titleText.setError("Please enter activity title");
            return;
        } else if (TextUtils.isEmpty(editDescription)) {
            descriptionNameText.setError("Please enter description");
            return;
        } else if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        //set empty string so it does not show null
        if (TextUtils.isEmpty(time)) {
            time = "";
        }

        //show alert dialog to recheck their inputs
        AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
        builder.setMessage("Save this?\n\nTitle : " + editTitle + "\nLocation : " + location + "\nDescription : " + editDescription +
                "\nDate : " + date + "\nTime : " + time)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Tasks tasks = new Tasks(titleText.getText().toString(),dateDisplay.getText().toString(),timeDisplay.getText().toString(),
                                descriptionNameText.getText().toString(),locationText.getText().toString());
                        String uploadId = ref.push().getKey();
                        ref.child(uploadId).setValue(tasks);
                        // ref.child("title").push().setValue(titleText.getText().toString());
                        Toast.makeText(InfoActivity.this,"Saved...", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(InfoActivity.this, "To-do Activity Added!", Toast.LENGTH_SHORT).show();
                        finish();
                        TaskListActivity.fa.finish();
                    }
                })
                .setNegativeButton("Cancel", null );

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //set date input
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dateDisplay.setText(date);
    }

    //set time input
    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min = "";
        if (minute < 10)
            min = "0" + minute;
        else
            min = String.valueOf(minute);

        // Append in a StringBuilder
        time = new StringBuilder().append(hour).append(':')
                .append(min).append(" ").append(timeSet).toString();

        timeDisplay.setText(time);
    }

    //if user pressed back button, show alertdialog
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
        builder.setMessage("Go back without saving?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InfoActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }}
//------------------------------------------------------------------------------------------



