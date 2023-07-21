package com.example.myorganizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    ListView lView;
    ArrayAdapter lAdapter;
    static ArrayList<ArrayList<Object>> taskList;
    public static Database db;
    static String TaskDatabase;
    static String TaskTable = "TaskTable";

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    private static ArrayList<Integer> removedItems;

    static int LIST_NAME = 0;
    static int LIST_DATE = 1;
    static int LIST_START_TIME = 2;
    static int LIST_END_TIME = 3;
    static int LIST_LOCATION = 4;
    static int LIST_LIGHTS = 5;
    static int LIST_VIBRATE = 6;

    // When Activity is created

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskList = new ArrayList<ArrayList<Object>>();        // Create list of database rows

        // Get button to add task and add event handler

        Button AddTaskButton = (Button) findViewById(R.id.AddTaskButton);

        AddTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add task
                Intent intent = new Intent(MainActivity.this, EditTask.class);
                startActivity(intent);

            }
        });


        //
        // Get data from database
        //
        TaskDatabase = getApplicationContext().getDatabasePath("TaskDatabase").toString();
        db = new Database(TaskDatabase);     // Create database object

        db.CreateTable(TaskTable, "Name", "VARCHAR(255)", "Date", "VARCHAR(255)", "StartTime", "VARCHAR(255)", "EndTime", "VARCHAR(255)", "Location", "VARCHAR(255)", "Lights", "INTEGER", "Vibrate", "INTEGER");       // Create table

       //db.Add(TaskTable, "'Test'", "'2023-03-03'", "'12:00'", "'12:00'", "'Here'", 0, 0);
       //db.Add(TaskTable, "'Test 2'", "'2023-03-04'", "'12:00'", "'14:00'", "'There'", 0, 0);
       //db.Add(TaskTable, "'Test 3'", "'2023-03-08'", "'13:00'", "'19:00'", "'Everywhere'", 0, 0);

        //db.Update(TaskTable, "NAME='Test'", "Name", "'ABC'");

        // Read database into list
        taskList = db.Select(TaskTable, null, null, null, null, null, null, null);


        //
        // Create recyclerview
        //

        data = new ArrayList<DataModel>();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        ///recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new CustomAdapter(data);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        int datacount = 0;

        for (ArrayList<Object> row : taskList) {

            data.add(new DataModel(
                    row.get(LIST_NAME).toString(),
                    row.get(LIST_DATE).toString(),
                    row.get(LIST_START_TIME).toString(),
                    row.get(LIST_END_TIME).toString(),
                    row.get(LIST_LOCATION).toString()
            ));

            adapter.notifyItemInserted(datacount++);
        }

        Thread thread=new Thread(new NotificationThread(this));
        thread.start();


    }

    //
    //  Remove task
    //
    static void RemoveTask(int position) {
        String Name = data.get(position).getName();

        Database db = new Database(TaskDatabase);
        db.Delete(TaskTable, "name=" + "'" + Name + "'", null);

        taskList.remove(position);
        data.remove(position);

        adapter.notifyItemRemoved(position);
    }

//  Add task

    public static void AddTask(Database db, String Name, String Date, String StartTime, String EndTime, String Location) {
        // Update database
        db.Add(MainActivity.TaskTable, "'" + Name + "'", "'" + Date + "'", "'" + StartTime + "'", "'" + EndTime + "'", "'" + Location + "'", 0, 0);

        // Update data list
        data.add(new DataModel(
                Name,
                Date,
                StartTime,
                EndTime,
                Location
        ));

        // Add to list of tasks

        ArrayList<Object> temp = new ArrayList<Object>();
        temp.add(LIST_NAME, Name);
        temp.add(LIST_DATE, Date);
        temp.add(LIST_START_TIME, StartTime);
        temp.add(LIST_END_TIME, EndTime);
        temp.add(LIST_LOCATION, Location);
        taskList.add(temp);

        adapter.notifyItemInserted(data.size() - 1);
    }

//  Add task

    public static void UpdateTask(Database db, String OldName, String Name, String Date, String StartTime, String EndTime, String Location) {
        // Update database
        db.Update(MainActivity.TaskTable, "NAME=" + "'"+OldName+"'", "Name", "'" + Name + "'", "Date", "'" + Date + "'", "StartTime=", "'" + StartTime + "'", "EndTime", "'" + EndTime + "'", "Location", "'" + Location + "'");

        int taskcount = 0;

        for (DataModel d : data) {

            if (d.name.equals(OldName)) {
                Log.d("UPDATE TASK","UPDATED");
                Log.d("UPDATE",OldName);

                data.add(taskcount, new DataModel(
                        Name,
                        Date,
                        StartTime,
                        EndTime,
                        Location
                ));

                adapter.notifyDataSetChanged();


                for (ArrayList<Object> t : taskList) {

                    if (t.get(LIST_NAME).equals(OldName)) {
                        Log.d("TASKCOUNT",String.valueOf(taskcount));

                        t.set(LIST_NAME, Name);
                        t.set(LIST_DATE, Date);
                        t.set(LIST_START_TIME, StartTime);
                        t.set(LIST_END_TIME, EndTime);
                        t.set(LIST_LOCATION, Location);
                        taskList.add(taskcount, t);

                        return;
                    }
                }

            }
        }
    }
}






