/*
Copyright (c) 2013 Joel Andrews
Distributed under the MIT License: http://opensource.org/licenses/MIT
 */

package com.oldsneerjaw.sleeptimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * Handles broadcast events intended to pause music playback indefinitely.
 *
 * @author Joel Andrews
 */
public class PauseMusicReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = PauseMusicReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (pauseMusicPlayback(context)) {
            Log.i(LOG_TAG, "Successfully paused music playback");

            notify(context);
        } else {
            Log.e(LOG_TAG, "Failed to pause music playback");
        }

        TimerManager.getInstance(context).cancelTimer();
    }

    /**
     * Pauses all music playback on the device.
     *
     * @param context The context
     *
     * @return {@code true} if playback was successfully paused; otherwise, {@code false}
     */
    private boolean pauseMusicPlayback(Context context) {

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        AudioFocusListener listener = new AudioFocusListener(audioManager);

        // A well-behaved media player should relinquish audio focus (i.e. pause playback) when another app requests
        // focus: http://developer.android.com/training/managing-audio/audio-focus.html#HandleFocusLoss
        int audioFocusResult = audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d(LOG_TAG, "Audio focus GAINED");

            // Immediately release focus. If the previous owner is well behaved, it will remain paused indefinitely;
            // if not, then holding onto audio focus will only briefly delay it from resuming playback until this app
            // is automatically recycled by the OS
            audioManager.abandonAudioFocus(listener);

            return true;
        } else {
            Log.d(LOG_TAG, "Audio focus DENIED");

            return false;
        }
    }

    /**
     * Posts a status bar notification that music playback has been paused.
     *
     * @param context The context
     */
    private void notify(Context context) {
        PauseMusicNotifier notifier = new PauseMusicNotifier();
        notifier.notify(context);
    }


    /**
     * A listener for audio focus change events.
     */
    private class AudioFocusListener implements AudioManager.OnAudioFocusChangeListener {

        private AudioManager audioManager;

        public AudioFocusListener(AudioManager audioManager) {
            this.audioManager = audioManager;
        }

        @Override
        public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.d(LOG_TAG, "Audio focus REGAINED");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.d(LOG_TAG, "Audio focus LOST permanently");
                    audioManager.abandonAudioFocus(this);
                    break;
                default:
                    Log.d(LOG_TAG, "Audio focus changed: " + focusChange);
            }
        }
    }
}
