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
