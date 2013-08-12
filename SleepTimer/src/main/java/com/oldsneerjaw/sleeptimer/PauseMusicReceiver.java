/*
Copyright (c) 2013 Joel Andrews
Distributed under the MIT License: http://opensource.org/licenses/MIT
 */

package com.oldsneerjaw.sleeptimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Handles broadcast events intended to pause music playback indefinitely.
 *
 * @author Joel Andrews
 */
public class PauseMusicReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        pauseMusic(context, TimerManager.getInstance(context));
    }

    /**
     * Pauses all music playback on the device.
     *
     * @param context The context in which the receiver is running
     *
     * @param timerManager The pause music timer manager
     */
    void pauseMusic(Context context, TimerManager timerManager) {
        // The service will be responsible for actually pausing playback and ensuring it remains paused until explicitly
        // restarted
        context.startService(new Intent(context, PauseMusicService.class));

        timerManager.cancelTimer();
    }
}
