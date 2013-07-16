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

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

public class CountdownActivity extends Activity {

    private static final String LOG_TAG = CountdownActivity.class.getName();

    private Date scheduledTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_countdown);

        // TODO Handle null
        scheduledTime = TimerManager.getInstance(this).getScheduledTime();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.countdown, menu);
        return true;
    }

    /**
     * Stops the countdown timer.
     *
     * @param view The view that triggered this action
     */
    public void stopTimer(View view) {
        Log.d(LOG_TAG, "Canceling sleep timer");

        TimerManager.getInstance(this).cancelTimer();

        Toast.makeText(this, R.string.timer_cancelled, Toast.LENGTH_SHORT).show();
    }

}
