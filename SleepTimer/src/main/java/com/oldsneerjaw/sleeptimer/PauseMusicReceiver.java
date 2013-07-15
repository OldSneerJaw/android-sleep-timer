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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * Receives broadcast events intended to pause current audio playback.
 *
 * @author Joel Andrews
 */
public class PauseMusicReceiver extends BroadcastReceiver {

    private AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        // A well-behaved media player should relinquish audio focus (i.e. pause playback) when another app requests
        // focus: http://developer.android.com/training/managing-audio/audio-focus.html#HandleFocusLoss
        AudioFocusListener listener = new AudioFocusListener();
        int audioFocusResult = audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d(PauseMusicReceiver.class.getName(), "Audio focus GAINED");

            // Immediately release focus. If the previous owner is well behaved, it will remain paused indefinitely;
            // if not, then holding onto audio focus will only briefly delay it from resuming playback
            audioManager.abandonAudioFocus(listener);

            PauseMusicNotifier notifier = new PauseMusicNotifier();
            notifier.notify(context);
        } else {
            Log.e(PauseMusicReceiver.class.getName(), "Audio focus DENIED");
        }
    }

    /**
     * Listens for audio focus change events.
     */
    private class AudioFocusListener implements AudioManager.OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.d(PauseMusicReceiver.class.getName(), "Audio focus REGAINED");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.d(PauseMusicReceiver.class.getName(), "Audio focus LOST permanently");
                    audioManager.abandonAudioFocus(this);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.d(PauseMusicReceiver.class.getName(), "Audio focus LOST temporarily");
                    break;
                default:
                    Log.d(PauseMusicReceiver.class.getName(), "Audio focus changed: " + focusChange);
            }
        }
    }
}
