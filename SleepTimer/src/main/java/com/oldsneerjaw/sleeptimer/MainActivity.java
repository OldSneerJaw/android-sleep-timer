/*
Copyright (c) 2013 Joel Andrews
Distributed under the MIT License: http://opensource.org/licenses/MIT
 */

package com.oldsneerjaw.sleeptimer;

import android.content.Intent;
import android.app.Activity;

import java.util.Calendar;
import java.util.Date;

/**
 * The launching point for the sleep timer.
 *
 * @author Joel Andrews
 */
public class MainActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();

        launchActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                break;
            case RESULT_CANCELED:
                finish();

                return;
            default:
                throw new IllegalArgumentException("Argument resultCode must be be either RESULT_OK or RESULT_CANCELED");
        }
    }

    /**
     * Launches the appropriate activity depending on whether the sleep timer is currently running
     * ({@link CountdownActivity}) or not ({@link SetTimerActivity}).
     */
    private void launchActivity() {
        Calendar calendarNow = Calendar.getInstance();
        Date scheduledTime = TimerManager.getInstance(this).getScheduledTime();

        Intent intent;
        if (scheduledTime == null || scheduledTime.getTime() <= calendarNow.getTimeInMillis()) {
            // If the scheduled time occurs in the past, we treat it as if the timer is no longer running
            intent = new Intent(this, SetTimerActivity.class);
        } else {
            intent = new Intent(this, CountdownActivity.class);
        }

        startActivityForResult(intent, 0);
    }
}
