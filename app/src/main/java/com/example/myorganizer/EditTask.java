package com.example.myorganizer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditTask extends AppCompatActivity implements Serializable {
    public int calYear,calMonth,calDay,calStartHour,calStartMinute,calEndHour,calEndMinute;
    public String Name;
    public String Date;
    public String StartTime;
    public String EndTime;
    public String Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Get extra information from intent (if there is any) and set interface items

        Intent intent = getIntent();
        String IntentTaskName = intent.getStringExtra("name");

        EditText TaskEdit = (EditText) findViewById(R.id.TaskNameTextBox);          // set task name edit box if editing task
        TaskEdit.setText(IntentTaskName);

        // Get information passed to the activity from MainActivity

        String IntentTaskDate = intent.getStringExtra("date");
        String IntentTaskStartTime=intent.getStringExtra("startTime");
        String IntentTaskEndTime=intent.getStringExtra("endTime");
        String IntentTaskLocation=intent.getStringExtra("location");

        //
        // If existing task is edited, when the date
        // is passed via an intent
        //
        if(IntentTaskDate != null) {
            SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
            try {
                java.util.Date parsedDate=sdf.parse(IntentTaskDate);

                calYear=parsedDate.getYear();
                calMonth=(parsedDate.getMonth()+1);
                calDay=parsedDate.getDay();

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else {
            final Calendar c = Calendar.getInstance();
            calYear = c.get(Calendar.YEAR);
            calMonth = (c.get(Calendar.MONTH)+1);
            calDay = c.get(Calendar.DAY_OF_MONTH);

            Log.d("MONTH", String.valueOf(calMonth));
        }

        //
        // If existing task is edited, when the start time
        // is passed via an intent
        //
        if(IntentTaskStartTime != null) {
            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
            try {
                java.util.Date parsedDate=sdf.parse(IntentTaskStartTime);

                calStartHour=parsedDate.getHours();
                calStartMinute=parsedDate.getMinutes();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else {
            final Calendar c = Calendar.getInstance();
            calStartHour = c.get(Calendar.HOUR);
            calStartMinute = c.get(Calendar.MINUTE);
        }

        // ditto end time

        if(IntentTaskEndTime != null) {
            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
            try {
                java.util.Date parsedDate=sdf.parse(IntentTaskEndTime);

                calEndHour=parsedDate.getHours();
                calEndMinute=parsedDate.getMinutes();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else {
            final Calendar c = Calendar.getInstance();
            calEndHour = c.get(Calendar.HOUR);
            calEndMinute = c.get(Calendar.MINUTE);
        }
        // Get button and create event handler for change date

        Button ChangeDateButton = (Button) findViewById(R.id.ChangeDateButton);
          ChangeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 DatePickerDialog dpd = new DatePickerDialog(EditTask.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                      calYear=year;
                      calMonth=month;
                      calDay=day;
                    }

                }, calYear, calMonth,calDay);

                dpd.show();
             }
           });

//************************************

        // Get button and create event handler for change start time

        Button ChangeStartTimeButton = (Button) findViewById(R.id.changeStartTimeButton);

        ChangeStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tpd = new TimePickerDialog(EditTask.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        calStartHour=hour;
                        calStartMinute=minute;
                    }

                }, calStartHour, calStartMinute,true);

                tpd.show();
            }
        });


        // Get button and create event handler for change end time

        Button ChangeEndTimeButton = (Button) findViewById(R.id.changeEndTimeButton);

        ChangeEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tpd = new TimePickerDialog(EditTask.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        calEndHour=hour;
                        calEndMinute=minute;
                    }

                }, calEndHour, calEndMinute,true);

                tpd.show();
            }
        });


        // Get button and create event handle for set location button

        Button SetLocationButton = (Button) findViewById(R.id.SetLocationButton);

        SetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditTask.this, SelectLocation.class);
                startActivity(intent);

            }
        });

        // Get button and create event handle for OK button

        Button EditTaskOKButton= (Button) findViewById(R.id.EditTaskOK);

        // Set event handler for OK button
        EditTaskOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db=new Database(MainActivity.TaskDatabase);

                // Get values and add to database

                Button ChangeDateButton = (Button) findViewById(R.id.ChangeDateButton);
                Button ChangeStartTimeButton = (Button) findViewById(R.id.changeStartTimeButton);
                Button ChangeEndTimeButton = (Button) findViewById(R.id.changeEndTimeButton);
                Button SetLocationButton = (Button)  findViewById(R.id.SetLocationButton);
                EditText TaskEdit = (EditText) findViewById(R.id.TaskNameTextBox);          // set task name edit box if editing task

                // Update list

                Name= String.valueOf(TaskEdit.getText());

                Log.d("new date",String.valueOf(calStartHour)+" "+String.valueOf(calStartMinute)+" "+String.valueOf(calEndHour)+" "+String.valueOf(calEndMinute));

                Date=String.format("%02d-%02d-%02d",calYear,calMonth,calDay);
                StartTime=String.format("%02d:%02d",calStartHour,calEndMinute);
                EndTime=String.format("%02d:%02d",calEndHour,calEndMinute);
                Location= "Here";

                Log.d("DATETIME",Date+" "+StartTime+" "+EndTime);

                if(IntentTaskName == null) {            // No task
                    MainActivity.AddTask(db, Name, Date, StartTime, EndTime, Location);
                }
                else {      // Update task

                    MainActivity.UpdateTask(db, IntentTaskName, Name, Date, StartTime, EndTime, Location);
                }

                finish();
                }
        });

        Button EditTaskCancelButton= (Button) findViewById(R.id.EditTaskCancelButton);

        // Set event handler for OK button
        EditTaskCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();            // Return to previous activity
            }
        });

    }
}
