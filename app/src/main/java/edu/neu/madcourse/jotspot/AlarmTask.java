package edu.neu.madcourse.jotspot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmTask implements Runnable{

    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;
    private String repeat;

    public AlarmTask(Context context, Calendar date, String repeat) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
        this.repeat = repeat;
    }

    @Override
    public void run() {
        // Request to start are service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);


        if (repeat.equals("Never") || repeat.equals("Repeat")) {
            // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
            am.set(AlarmManager.RTC, date.getTimeInMillis(), pendingIntent);
        } else if (repeat.equals("Daily")) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        } else if (repeat.equals("Weekly")) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        } else if (repeat.equals("Monthly")) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 30, pendingIntent);
        } else {
            am.set(AlarmManager.RTC, date.getTimeInMillis(), pendingIntent);
        }
    }

}
