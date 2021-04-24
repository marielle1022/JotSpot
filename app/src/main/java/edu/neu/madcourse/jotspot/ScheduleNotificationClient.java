package edu.neu.madcourse.jotspot;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.Calendar;

// This code is cited from https://blog.blundellapps.co.uk/notification-for-a-user-chosen-time/

public class ScheduleNotificationClient {

    private ScheduleNotificationService scheduleBoundService;
    private Context scheduleContext;
    private boolean scheduleIsBound;

    public ScheduleNotificationClient(Context context) {
        scheduleContext = context;
    }

    public void doBindService() {
        scheduleContext.bindService(new Intent(scheduleContext, ScheduleNotificationService.class), scheduleConnection, Context.BIND_AUTO_CREATE);
        scheduleIsBound = true;
    }

    private ServiceConnection scheduleConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            scheduleBoundService = ((ScheduleNotificationService.ServiceBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            scheduleBoundService = null;
        }
    };

    public void setAlarmForNotification(Calendar reminderCalendar, String repeat){
        scheduleBoundService.setAlarm(reminderCalendar, repeat);
    }

    public void doUnbindService() {
        if (scheduleIsBound) {
            scheduleContext.unbindService(scheduleConnection);
            scheduleIsBound = false;
        }
    }

}