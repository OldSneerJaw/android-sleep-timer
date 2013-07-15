/*
Copyright 2013 Joel Andrews

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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

    private static final String NOTIFICATION_TAG = PauseMusicNotifier.class.getName() + ".MusicPaused";
    private static final int NOTIFICATION_ID = 0;

    /**
     * Constructs an instance of {@link PauseMusicNotifier}.
     */
    public PauseMusicNotifier() { }

    /**
     * Returns a notification for use when music playback is paused.
     *
     * @param context The context
     *
     * @return A {@link Notification}
     */
    private Notification create(final Context context) {
        Resources resources = context.getResources();

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
     * Posts a music paused notification to the status bar.
     *
     * @param context The context
     */
    public void notify(final Context context) {
        Notification notification = create(context);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notification);
    }
}
