/*
Copyright (c) 2013 Joel Andrews
Distributed under the MIT License: http://opensource.org/licenses/MIT
 */

package com.oldsneerjaw.sleeptimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.Mockito;

/**
 * Test cases for {@link PauseMusicNotifierTest}.
 *
 * @author Joel Andrews
 */
public class PauseMusicNotifierTest extends AndroidMockingTestCase {

    private static final int NOTIFICATION_ID = 1;

    private static final String NOTIFICATION_TITLE = "foo";
    private static final String NOTIFICATION_TEXT = "bar";

    private NotificationManager mockNotificationManager;
    private PauseMusicNotifier notifier;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Context mockContext = Mockito.mock(Context.class);
        Mockito.when(mockContext.getPackageName()).thenReturn("com.oldsneerjaw.sleeptimer");
        Mockito.when(mockContext.getApplicationContext()).thenReturn(mockContext);

        mockNotificationManager = Mockito.mock(NotificationManager.class);

        Resources mockResources = Mockito.mock(Resources.class);
        Mockito.when(mockResources.getString(R.string.paused_music_notification_title)).thenReturn(NOTIFICATION_TITLE);
        Mockito.when(mockResources.getString(R.string.paused_music_notification_text)).thenReturn(NOTIFICATION_TEXT);

        notifier = new PauseMusicNotifier(mockContext, mockNotificationManager, mockResources);
    }

    public void testPostNotification() {
        notifier.postNotification();

        Mockito.verify(mockNotificationManager).notify(
                Mockito.eq(NOTIFICATION_ID),
                Mockito.argThat(new BaseMatcher<Notification>() {
                    @Override
                    public boolean matches(Object o) {
                        if (!(o instanceof Notification)) {
                            return false;
                        }

                        Notification candidate = (Notification) o;

                        return TextUtils.equals(NOTIFICATION_TITLE, candidate.tickerText)
                                && (candidate.icon == R.drawable.ic_launcher)
                                && (candidate.priority == NotificationCompat.PRIORITY_DEFAULT)
                                && (candidate.contentIntent != null)
                                && ((candidate.flags & Notification.FLAG_AUTO_CANCEL) != 0);
                    }

                    @Override
                    public void describeTo(Description description) {
                        // Describe the notification that was expected in the event of test failure
                        description.appendText("a notification with the correct title, icon, default priority, a pending intent and set to auto cancel");
                    }
                })
        );
    }

    public void testCancelNotification() {
        notifier.cancelNotification();

        Mockito.verify(mockNotificationManager).cancel(NOTIFICATION_ID);
    }
}
