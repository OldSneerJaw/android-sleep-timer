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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the sleep timer.
 * <p>
 *     Cannot be instantiated directly; call {@link TimerManager#getInstance(android.content.Context)}
 *     instead.
 * </p>
 *
 * @author Joel Andrews
 */
public class TimerManager {

    private static final String SCHEDULED_TIME_KEY = TimerManager.class.getName() + ".scheduledTime";

    private static Map<String, TimerManager> allInstances = new HashMap<String, TimerManager>();

    private Context context;
    private SharedPreferences sharedPreferences;

    /**
     * Constructs an instance of TimerManager. Cannot be instantiated directly; call
     * {@link TimerManager#getInstance(android.content.Context)} instead.
     *
     * @param context The context. Must not be null.
     */
    private TimerManager(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Gets an instance of this class for the specified context.
     *
     * @param context The context. Must not be null.
     *
     * @return A {@link TimerManager}
     */
    public static TimerManager getInstance(Context context) {
        if (context == null) {
            throw new NullPointerException("Argument context cannot be null");
        }

        if (!allInstances.containsKey(context.getPackageName())) {
            allInstances.put(context.getPackageName(), new TimerManager(context));
        }

        return allInstances.get(context.getPackageName());
    }

    /**
     * Sets a timer for the given number of hours and minutes in the future to pause music playback.
     * If a timer is already set for the current context, this will replace it.
     *
     * @param hours The number of hours. Must be non-negative.
     * @param minutes The number of minutes. Must be non-negative.
     */
    public void setTimer(int hours, int minutes) {
        if (hours < 0) {
            throw new IllegalArgumentException("Argument hours cannot be negative");
        } else if (minutes < 0) {
            throw new IllegalArgumentException("Argument minutes cannot be negative");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hours);
        calendar.add(Calendar.MINUTE, minutes);

        // NOTE: If an alarm has already been set in this context, this will automatically replace it
        PendingIntent intent = getBroadcastIntent();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), intent);

        // Save the currently scheduled time
        sharedPreferences.edit()
                .putLong(SCHEDULED_TIME_KEY, calendar.getTimeInMillis())
                .commit();
    }

    /**
     * Cancels the timer for the current context. If no timer is currently set, this will do nothing.
     */
    public void cancelTimer() {
        PendingIntent intent = getBroadcastIntent();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(intent);

        sharedPreferences.edit()
                .remove(SCHEDULED_TIME_KEY)
                .commit();
    }

    /**
     * Returns a {@link android.app.PendingIntent} that can be used to broadcast or cancel a pending pause music event.
     *
     * @return A {@link android.app.PendingIntent}
     */
    private PendingIntent getBroadcastIntent() {
        return PendingIntent.getBroadcast(context, 0, new Intent(context, PauseMusicReceiver.class), 0);
    }

    /**
     * Returns the time that the timer is set to expire.
     *
     * @return A {@link Date}, or null if no time is currently set
     */
    public Date getScheduledTime() {
        long millis = sharedPreferences.getLong(SCHEDULED_TIME_KEY, Long.MIN_VALUE);

        if (millis < Calendar.getInstance().getTimeInMillis()) {
            // The time is set in the past; treat it as though nothing is scheduled
            return null;
        } else {
            return new Date(millis);
        }
    }
}
