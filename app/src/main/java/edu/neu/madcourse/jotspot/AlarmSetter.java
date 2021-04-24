package edu.neu.madcourse.jotspot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;


// This code is cited from https://blog.blundellapps.co.uk/notification-for-a-user-chosen-time/


public class AlarmSetter implements Runnable{

    private final Calendar reminderDate;
    private final AlarmManager aManager;
    private final Context context;
    private String repeat;

    public AlarmSetter(Context context, Calendar date, String repeat) {
        this.context = context;
        this.aManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.reminderDate = date;
        this.repeat = repeat;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra(NotificationService.INTENT_NOTIFY, true);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        if (repeat.equals("Never") || repeat.equals("Repeat")) {
            aManager.set(AlarmManager.RTC, reminderDate.getTimeInMillis(), pendingIntent);
        } else if (repeat.equals("Daily")) {
            aManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderDate.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        } else if (repeat.equals("Weekly")) {
            aManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderDate.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        } else if (repeat.equals("Monthly")) {
            aManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderDate.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 30, pendingIntent);
        } else {
            aManager.set(AlarmManager.RTC, reminderDate.getTimeInMillis(), pendingIntent);
        }
    }

}
