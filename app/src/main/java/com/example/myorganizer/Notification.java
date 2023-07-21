package com.example.myorganizer;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import androidx.core.app.NotificationCompat;
import kotlinx.coroutines.sync.Mutex;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.myorganizer.MainActivity.LIST_NAME;
import static com.example.myorganizer.MainActivity.TaskTable;

public class Notification {
    String ChannelId="MyOrganizer";
    NotificationChannel channel;

    //
    // Notification constructor
    //
    // In: GroupName       Channel name
    //     Name            Notification name
    //
    // Returns: Nothing
    //
    Notification() {
    channel=new NotificationChannel(ChannelId,"MyOrganizerChannel",NotificationManager.IMPORTANCE_HIGH);

    }

    void DisplayMessage(Context context, String Message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        builder.setContentTitle(Message);
        builder.setContentText(Message);
        builder.setChannelId(ChannelId);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(1, builder.build());
    }

}

//
// Thread class for notifications
//
class NotificationThread implements Runnable {
    ArrayList<ArrayList<Object>> tasks;
    Database db;
    int WaitTime = 30;
    Context _context;

    NotificationThread(Context context) {
        this._context = context;
    }

    //
    // Called when the thread class is created.
    // Loops forever
    //
    public void run() {
        Instant NewTime;
        Instant BeforeTime;
        Notification notification = new Notification();
        Context _context = null;
        int taskcount = 0;

        //Log.d("NOTIFICATIONS", "CREATED THREAD");

        db = new Database(MainActivity.TaskDatabase);

        while (true) {
            //
            // Wait until x seconds elapsed
            //
            NewTime = Instant.now();
            NewTime.plusSeconds(WaitTime);

            do {
                BeforeTime = Instant.now();

            } while (BeforeTime.isBefore(NewTime));

            // Log.d("NOTIFICATIONS","LOOPING THROUGH TASKS");
            if(tasks != null) tasks.clear();

            tasks = db.Select(TaskTable, null, null, null, null, null, null, null);


            //
            // Loop through tasks and if date and time are within range, display message
            //
            for (ArrayList<Object> t : tasks) {
                 //   Log.d("NOTIFICATIONS", String.valueOf(t));

                LocalTime time = LocalTime.now();
                LocalTime TaskTime = LocalTime.parse((CharSequence) t.get(MainActivity.LIST_START_TIME), DateTimeFormatter.ofPattern("HH:mm"));

                LocalDate date = LocalDate.now();
                String datestr = date.toString();
                String taskdate = String.valueOf(t.get(MainActivity.LIST_DATE));

                // Log.d("DATE",datestr+" "+taskdate+" "+time+" "+TaskTime);

                if ((datestr.equals(taskdate))) {
                    Log.d("DATE", "date ok");

                    if (time.isAfter(TaskTime)) {
                        Log.d("TIME", "time ok");

                        notification.DisplayMessage(this._context, String.valueOf(t.get(LIST_NAME)));
                        MainActivity.RemoveTask(taskcount);
                        taskcount--;
                    }

                    return;
                }
                    taskcount++;
                }
        }
    }
}
