package com.oldsneerjaw.sleeptimer;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import java.util.Calendar;
import java.util.Date;

/**
 * Test cases for {@link MainActivity}.
 *
 * @author Joel Andrews
 */
public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

    private MainActivity activity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        activity = startActivity(intent, null, null);
    }

    public void testGetActivityIntent_NoScheduledTime() {
        Date now = new Date(0);
        Intent result = activity.getActivityIntent(now, null);

        assertEquals(activity.getPackageName(), result.getComponent().getPackageName());
        assertEquals(SetTimerActivity.class.getName(), result.getComponent().getClassName());
    }

    public void testGetActivityIntent_FutureScheduledTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(2013, 7, 5, 15, 53, 48);
        Date now = calendar.getTime();

        calendar.set(2014, 8, 6, 16, 54, 49);
        Date scheduledTime = calendar.getTime();

        Intent result = activity.getActivityIntent(now, scheduledTime);

        assertEquals(activity.getPackageName(), result.getComponent().getPackageName());
        assertEquals(CountdownActivity.class.getName(), result.getComponent().getClassName());
    }

    public void testGetActivityIntent_PastScheduledTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(2013, 7, 5, 15, 53, 48);
        Date now = calendar.getTime();

        calendar.set(2012, 6, 4, 14, 52, 47);
        Date scheduledTime = calendar.getTime();

        Intent result = activity.getActivityIntent(now, scheduledTime);

        assertEquals(activity.getPackageName(), result.getComponent().getPackageName());
        assertEquals(SetTimerActivity.class.getName(), result.getComponent().getClassName());
    }

    public void testGetActivityIntent_PresentScheduledTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2013, 7, 5, 15, 53, 48);

        Date now = calendar.getTime();
        Date scheduledTime = calendar.getTime();

        Intent result = activity.getActivityIntent(now, scheduledTime);

        // Since "now" and the scheduled time are the same, we should be taken to schedule a new timer
        assertEquals(activity.getPackageName(), result.getComponent().getPackageName());
        assertEquals(SetTimerActivity.class.getName(), result.getComponent().getClassName());
    }
}
