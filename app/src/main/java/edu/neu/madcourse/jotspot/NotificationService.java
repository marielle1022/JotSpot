package edu.neu.madcourse.jotspot;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


// This code is cited from https://blog.blundellapps.co.uk/notification-for-a-user-chosen-time/

public class NotificationService extends Service {

    public class ServiceBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    public static final String INTENT_NOTIFY = "edu.neu.madcourse.jotspot.INTENT_NOTIFY";
    private static final String CHANNEL_ID = "jotspot_notification";


    // This code is cited from https://developer.android.com/training/notify-user/build-notification

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = getString(R.string.channel_name);
            String channelDescription = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate() {
        Log.i("NotificationService", "onCreate()");
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int id) {
        Log.i("LocalNotificationService", "Received id " + id + ": " + intent);
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new ServiceBinder();

    // This code is cited from https://developer.android.com/training/notify-user/build-notification

    private void showNotification() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, HomeScreenActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setCategory(NotificationCompat.CATEGORY_REMINDER);
        builder.setContentTitle("JotSpot Reminder");
        builder.setContentText("Not in the mood for a full entry? Try a one-sentence prompt!");
        builder.setSmallIcon(R.drawable.ic_launcher_js_foreground);
        builder.setContentIntent(contentIntent);
        builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());

    }






    @Override
    public void onDestroy() {

        final int TIME_TO_INVOKE = 5 * 1000;
        AlarmManager alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(this, 0, intent, 0);

        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                TIME_TO_INVOKE, TIME_TO_INVOKE, pendingIntent);

    }
}
