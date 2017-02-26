package controller.notification;

/**
 * Created by Matan on 18/02/2017.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import controller.activities.MainActivity;
import controller.caldroid.CaldroidSampleActivity;

/**
 * This service is started when an Alarm has been raised
 *
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell
 */
public class NotifyService extends Service {

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "INTENT_NOTIFY";
    public static final String TITLE = "notification";
    public static final String CONTENT = "You have new event up coming";
    public static final String INTENT_NO = "INFO";
    // The system notification manager
    private NotificationManager mNM;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false)){
            showNotification(intent);
        }


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
    private void showNotification(Intent intent) {

        // Notification ID to allow for future updates
        final int MY_NOTIFICATION_ID = 112;

        // Notification Action Elements
        Intent mNotificationIntent;
        PendingIntent mContentIntent;

        // create the intent for notification
        mNotificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        mContentIntent = PendingIntent.getActivity(getApplicationContext(), 0, mNotificationIntent,
                Intent.FILL_IN_ACTION);

        // Define the Notification's expanded message and Intent:
        // Notification Sound and Vibration on Arrival
        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] mVibratePattern = { 0, 150, 150, 300 };

        Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext());

        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_warning);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentTitle(TITLE);
        notificationBuilder.setContentText(CONTENT);
        notificationBuilder.setContentIntent(mContentIntent).setSound(soundURI);
        notificationBuilder.setVibrate(mVibratePattern);

        // Pass the Notification to the NotificationManager:
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());

    }
}
