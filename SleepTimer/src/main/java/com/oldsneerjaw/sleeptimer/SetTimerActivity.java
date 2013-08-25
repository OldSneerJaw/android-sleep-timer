/*
Copyright (c) 2013 Joel Andrews
Distributed under the MIT License: http://opensource.org/licenses/MIT
 */

package com.oldsneerjaw.sleeptimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
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

    private TimerManager timerManager;
    private SharedPreferences sharedPreferences;
    private NumberPicker hoursPicker;
    private NumberPicker minutesPicker;


    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState, TimerManager.get(this), PreferenceManager.getDefaultSharedPreferences(this));
    }

    /**
     * Initializes the activity's dependencies.
     *
     * @param savedInstanceState The activity's previous state
     * @param timerManager The timer manager to use
     * @param sharedPreferences The shared preferences to use
     */
    protected void onCreate(Bundle savedInstanceState, TimerManager timerManager, SharedPreferences sharedPreferences) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_timer);

        // Prevent the soft keyboard from appearing until explicitly launched by the user
        // Source: http://stackoverflow.com/a/2059394
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.timerManager = timerManager;
        this.sharedPreferences = sharedPreferences;

        hoursPicker = (NumberPicker) findViewById(R.id.hours_picker);
        hoursPicker.setMinValue(MIN_HOURS);
        hoursPicker.setMaxValue(MAX_HOURS);
        hoursPicker.setValue(sharedPreferences.getInt(HOURS_KEY, DEFAULT_HOURS));

        minutesPicker = (NumberPicker) findViewById(R.id.minutes_picker);
        minutesPicker.setMinValue(MIN_MINUTES);
        minutesPicker.setMaxValue(MAX_MINUTES);
        minutesPicker.setValue(sharedPreferences.getInt(MINUTES_KEY, DEFAULT_MINUTES));
    }

    /**
     * Starts a countdown timer based on the current settings.
     *
     * @param view The view that triggered this action
     */
    public void startTimer(View view) {
        Log.d(LOG_TAG, "Sleep timer started by view " + view.getId());

        // Explicitly clear focus from the number pickers to ensure that the stored values get updated.
        // Source: http://stackoverflow.com/a/13409571
        hoursPicker.clearFocus();
        minutesPicker.clearFocus();

        int hours = hoursPicker.getValue();
        int minutes = minutesPicker.getValue();

        // The currently selected values should become the new defaults
        setDefaultTimerLength(hours, minutes);

        timerManager.setTimer(hours, minutes);

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
