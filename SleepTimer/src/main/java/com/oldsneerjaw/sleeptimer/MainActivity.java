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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

/**
 * The launching point for the sleep timer. Launches either {@link SetTimerActivity} or {@link CountdownActivity}
 * depending on whether the sleep timer is currently running.
 *
 * @author Joel Andrews
 */
public class MainActivity extends Activity {

    // TODO Move these into a common base class of SetTimerActivity and CountdownActivity
    public static final int MIN_HOURS = 0;
    public static final int MAX_HOURS = 9;
    public static final int MIN_MINUTES = 0;
    public static final int MAX_MINUTES = 59;

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent;
        if (TimerManager.getInstance(this).getScheduledTime() != null) {
            intent = new Intent(this, CountdownActivity.class);
        } else {
            intent = new Intent(this, SetTimerActivity.class);
        }

        startActivityForResult(intent, 0);
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
}
