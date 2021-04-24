package edu.neu.madcourse.jotspot;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class ScheduleNotificationService extends Service {

    // This code is cited from https://blog.blundellapps.co.uk/notification-for-a-user-chosen-time/

    public class ServiceBinder extends Binder {
        ScheduleNotificationService getService() {
            return ScheduleNotificationService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int id) {
        Log.i("ScheduleNotificationService", "Received id " + id + ": " + intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return scheduleBinder;
    }

    private final IBinder scheduleBinder = new ServiceBinder();

    public void setAlarm(Calendar reminderCalendar, String repeat) {
        new AlarmSetter(this, reminderCalendar, repeat).run();
    }
}
