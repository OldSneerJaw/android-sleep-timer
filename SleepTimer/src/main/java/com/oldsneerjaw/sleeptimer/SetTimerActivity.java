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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.Toast;

/**
 * Schedule the sleep timer.
 *
 * @author Joel Andrews
 */
public class SetTimerActivity extends Activity {

    private static final String LOG_TAG = SetTimerActivity.class.getName();

    private static final String HOURS_KEY = MainActivity.class.getName() + ".hours";
    private static final String MINUTES_KEY = MainActivity.class.getName() + ".minutes";

    private static final int MIN_HOURS = 0;
    private static final int MAX_HOURS = 9;
    private static final int MIN_MINUTES = 0;
    private static final int MAX_MINUTES = 59;

    // By default, the timer will be set to one hour
    private static final int DEFAULT_HOURS = 1;
    private static final int DEFAULT_MINUTES = 0;

    private NumberPicker hoursPicker;
    private NumberPicker minutesPicker;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);

        // Prevent the soft keyboard from appearing until explicitly launched by the user
        // Source: http://stackoverflow.com/a/2059394
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        hoursPicker = (NumberPicker) findViewById(R.id.hours_picker);
        hoursPicker.setMinValue(MIN_HOURS);
        hoursPicker.setMaxValue(MAX_HOURS);
        hoursPicker.setValue(sharedPreferences.getInt(HOURS_KEY, DEFAULT_HOURS));

        minutesPicker = (NumberPicker) findViewById(R.id.minutes_picker);
        minutesPicker.setMinValue(MIN_MINUTES);
        minutesPicker.setMaxValue(MAX_MINUTES);
        minutesPicker.setValue(sharedPreferences.getInt(MINUTES_KEY, DEFAULT_MINUTES));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.set_timer, menu);
        return true;
    }

    /**
     * Starts a countdown timer based on the current settings.
     *
     * @param view The view that triggered this action
     */
    public void startTimer(View view) {

        Log.d(LOG_TAG, "Starting sleep timer");

        int hours = hoursPicker.getValue();
        int minutes = minutesPicker.getValue();

        // The currently selected values should become the new defaults
        setDefaultTimerLength(hours, minutes);

        TimerManager.getInstance(this).setTimer(hours, minutes);

        Toast.makeText(this, R.string.timer_started, Toast.LENGTH_SHORT).show();

        // Finish the activity
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Sets the default timer length to the specified number of hours and minutes.
     *
     * @param hours The number of hours
     * @param minutes The number of minutes
     */
    private void setDefaultTimerLength(int hours, int minutes) {
        sharedPreferences.edit()
                .putInt(HOURS_KEY, hours)
                .putInt(MINUTES_KEY, minutes)
                .commit();
    }

}
