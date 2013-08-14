/*
Copyright (c) 2013 Joel Andrews
Distributed under the MIT License: http://opensource.org/licenses/MIT
 */

package com.oldsneerjaw.sleeptimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

/**
 * Creates status bar notifications that indicate music playback has been paused.
 *
 * @author Joel Andrews
 */
public class PauseMusicNotifier {

    private static final int NOTIFICATION_ID = 1;

    private final Context context;
    private final NotificationManager notificationManager;
    private final Resources resources;

    /**
     * Constructs an instance of {@link PauseMusicNotifier}.
     *
     * @param context The context
     */
    public PauseMusicNotifier(Context context) {
        this(context, (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE), context.getResources());
    }

    /**
     * Constructs an instance of {@link PauseMusicNotifier}.
     *
     * @param context The context
     * @param notificationManager The system notification manager
     * @param resources The app's resources
     */
    PauseMusicNotifier(Context context, NotificationManager notificationManager, Resources resources) {
        if (context == null) {
            throw new NullPointerException("Argument context cannot be null");
        } else if (notificationManager == null) {
            throw new NullPointerException("Argument notificationManager cannot be null");
        } else if (resources == null) {
            throw new NullPointerException("Argument resources cannot be null");
        }

        this.context = context.getApplicationContext();
        this.notificationManager = notificationManager;
        this.resources = resources;
    }

    /**
     * Returns a notification for use when music playback is paused.
     *
     * @return A {@link Notification}
     */
    private Notification create() {
        String title = resources.getString(R.string.paused_music_notification_title);
        String text = resources.getString(R.string.paused_music_notification_text);

        PendingIntent tapIntent =
                PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(tapIntent)
                .setAutoCancel(true);

        return builder.build();
    }

    /**
     * Posts a music paused notification to the system status bar.
     */
    public void postNotification() {
        Notification notification = create();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    /**
     * Cancels and removes the music paused notification, in any, from the system status bar.
     */
    public void cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
