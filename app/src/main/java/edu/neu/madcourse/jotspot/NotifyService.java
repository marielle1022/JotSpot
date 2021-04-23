package edu.neu.madcourse.jotspot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.Normalizer;

public class NotifyService extends Service {

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    NotificationManager manager;
    Notification myNotication;
    // Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";
    // The system notification manager
    private NotificationManager mNM;
    private static final String CHANNEL_ID = "jotspot_notification";
    //private static final String channel_name = "jotspot";
    //private static final String channel_description = "jotspot reminder";


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        //mNM = (NotificationManager) createNotificationChannel();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification() {
        // This is the 'title' of the notification
//        CharSequence title = "Alarm!!";
//        // This is the icon to use on the notification
//        int icon = R.drawable.ic_launcher_js_foreground;
//        // This is the scrolling text of the notification
//        CharSequence text = "Your notification time is upon us.";
//        // What time to show on the notification
//        long time = System.currentTimeMillis();

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, HomeScreenActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setCategory(NotificationCompat.CATEGORY_REMINDER);
        builder.setContentTitle("JotSpot Reminder");
        builder.setContentText("This is your journaling reminder!");
        builder.setSmallIcon(R.drawable.ic_launcher_js_foreground);
        builder.setContentIntent(contentIntent);
        builder.setNumber(100);
        builder.build();

        //myNotication = builder.getNotification();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(11, builder.build());

        // The PendingIntent to launch our activity if the user selects this notification
        // PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, HomeScreenActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        // notification.setLatestEventInfo(this, title, text, contentIntent);

        // Clear the notification when it is pressed
        // notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Send the notification to the system.
        // mNM.notify(NOTIFICATION, notification);

        // Stop the service when we are finished
        // stopSelf();
    }
}
