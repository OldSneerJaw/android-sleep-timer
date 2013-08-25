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

import com.oldsneerjaw.sleeptimer.test.AndroidMockingTestCase;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.Mockito;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Test cases for {@link CountdownNotifier}.
 *
 * @author Joel Andrews
 */
public class CountdownNotifierTest extends AndroidMockingTestCase {

    private static final int NOTIFICATION_ID = 2;

    private static final String NOTIFICATION_TITLE = "foo";
    private static final String NOTIFICATION_TEXT = "bar";

    private NotificationManager mockNotificationManager;
    private CountdownNotifier notifier;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Context mockContext = Mockito.mock(Context.class);
        Mockito.when(mockContext.getPackageName()).thenReturn("com.oldsneerjaw.sleeptimer");
        Mockito.when(mockContext.getApplicationContext()).thenReturn(mockContext);

        mockNotificationManager = Mockito.mock(NotificationManager.class);

        Resources mockResources = Mockito.mock(Resources.class);
        Mockito.when(mockResources.getString(R.string.countdown_notification_title)).thenReturn(NOTIFICATION_TITLE);
        Mockito.when(mockResources.getString(R.string.countdown_notification_text)).thenReturn(NOTIFICATION_TEXT);

        DateFormat countdownTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

        notifier = new CountdownNotifier(mockContext, mockNotificationManager, mockResources, countdownTimeFormat);
    }

    public void testGetInstance_EqualContextsReturnSameInstance() {
        String packageName = "foo";

        Context context1 = Mockito.mock(Context.class);
        Mockito.when(context1.getPackageName()).thenReturn(packageName);

        Context context2 = Mockito.mock(Context.class);
        Mockito.when(context2.getPackageName()).thenReturn(packageName);

        CountdownNotifier instance1 = CountdownNotifier.get(context1);
        CountdownNotifier instance2 = CountdownNotifier.get(context2);

        assertSame(instance1, instance2);
    }

    public void testGetInstance_DifferentContextsReturnDifferentInstances() {
        Context context1 = Mockito.mock(Context.class);
        Mockito.when(context1.getPackageName()).thenReturn("foo");

        Context context2 = Mockito.mock(Context.class);
        Mockito.when(context2.getPackageName()).thenReturn("bar");

        CountdownNotifier instance1 = CountdownNotifier.get(context1);
        CountdownNotifier instance2 = CountdownNotifier.get(context2);

        assertNotSame(instance1, instance2);
    }

    public void testPostNotification() {
        Date countdownEnds = new Date(0);

        notifier.postNotification(countdownEnds);

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
                                && ((candidate.flags & Notification.FLAG_ONGOING_EVENT) != 0)
                                && ((candidate.flags & Notification.FLAG_AUTO_CANCEL) == 0);
                    }

                    @Override
                    public void describeTo(Description description) {
                        // Describe the notification that was expected in the event of test failure
                        description.appendText("a notification with the correct title, icon, default priority, a pending intent, set to ongoing and NOT auto cancel");
                    }
                })
        );
    }

    public void testCancelNotification() {
        notifier.cancelNotification();

        Mockito.verify(mockNotificationManager).cancel(NOTIFICATION_ID);
    }
}
