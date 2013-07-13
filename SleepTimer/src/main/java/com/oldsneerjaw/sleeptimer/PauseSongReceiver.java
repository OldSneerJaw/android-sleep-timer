package com.oldsneerjaw.sleeptimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// TODO Document this class
public class PauseSongReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("PauseSongReceiver", "PING");
    }
}
