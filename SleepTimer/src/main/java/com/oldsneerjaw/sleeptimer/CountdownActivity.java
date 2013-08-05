/*
Copyright (c) 2013 Joel Andrews
Distributed under the MIT License: http://opensource.org/licenses/MIT
 */

package com.oldsneerjaw.sleeptimer;

import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Displays a dynamic countdown timer.
 *
 * @author Joel Andrews
 */
public class CountdownActivity extends Activity {

    private static final String LOG_TAG = CountdownActivity.class.getName();

    private TextView timeRemainingView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_countdown);

        timeRemainingView = (TextView) findViewById(R.id.time_remaining_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.countdown, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * Initializes and starts the countdown timer.
     */
    private void startTimer() {
        Calendar calendarNow = Calendar.getInstance();
        Date scheduledTime = TimerManager.getInstance(this).getScheduledTime();

        if (scheduledTime == null || scheduledTime.getTime() <= calendarNow.getTimeInMillis()) {
            // The timer has already expired; return to the caller
            prepareReturnToSender();

            return;
        }

        long timerMillis = scheduledTime.getTime() - calendarNow.getTimeInMillis();

        countDownTimer = new MyCountDownTimer(timerMillis).start();
    }

    /**
     * Stops the countdown timer.
     *
     * @param view The view that triggered this action
     */
    public void stopTimer(View view) {
        Log.d(LOG_TAG, "Sleep timer canceled by view " + view.getId());

        TimerManager.getInstance(this).cancelTimer();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        Toast.makeText(this, R.string.timer_cancelled, Toast.LENGTH_SHORT).show();

        // Finish the activity
        prepareReturnToSender();
    }

    /**
     * Sets the result to {@link #RESULT_OK} and finishes execution. Callers should immediately return after invoking
     * this method.
     */
    private void prepareReturnToSender() {
        setResult(RESULT_OK);
        finish();
    }


    /**
     * A countdown timer that updates the time remaining text view every 1 second.
     */
    private class MyCountDownTimer extends CountDownTimer {

        // The number of milliseconds between updates of the countdown timer
        private static final long TICK_INTERVAL = 1000;

        public MyCountDownTimer(long millisInFuture) {
            super(millisInFuture, TICK_INTERVAL);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long secondsRemaining = millisUntilFinished / 1000;
            String timeRemainingString = DateUtils.formatElapsedTime(secondsRemaining);

            CountdownActivity.this.timeRemainingView.setText(timeRemainingString);
        }

        @Override
        public void onFinish() {
            CountdownActivity.this.prepareReturnToSender();
        }
    }
}
