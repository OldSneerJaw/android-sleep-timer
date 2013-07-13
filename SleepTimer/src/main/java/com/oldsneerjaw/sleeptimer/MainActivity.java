package com.oldsneerjaw.sleeptimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

// TODO Document this class
public class MainActivity extends Activity {

    private static final String HOUR_KEY = MainActivity.class.getName() + ".hours";
    private static final String MINUTE_KEY = MainActivity.class.getName() + ".minutes";
    private static final int DEFAULT_HOURS = 1;
    private static final int DEFAULT_MINUTES = 0;

    private TimePicker timePicker;

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        timePicker.setCurrentHour(sharedPrefs.getInt(HOUR_KEY, DEFAULT_HOURS));
        timePicker.setCurrentMinute(sharedPrefs.getInt(MINUTE_KEY, DEFAULT_MINUTES));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Starts a countdown timer based on the current settings.
     *
     * @param view The view that triggered this action
     */
    public void startTimer(View view) {

        Log.i(MainActivity.class.getName(), "Starting countdown timer");

        int hours = timePicker.getCurrentHour();
        int minutes = timePicker.getCurrentMinute();

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(HOUR_KEY, hours);
        editor.putInt(MINUTE_KEY, minutes);
        editor.commit();

        setAlarm(hours, minutes);
    }

    /**
     * Sets an alarm for the given number of hours and minutes in the future to pause audio output.
     *
     * @param hours The number of hours
     * @param minutes The number of minutes
     */
    private void setAlarm(int hours, int minutes) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hours);
        calendar.add(Calendar.MINUTE, minutes);

        PendingIntent intent =
                PendingIntent.getBroadcast(this, 0, new Intent(this, PauseSongReceiver.class), PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), intent);
    }

}
