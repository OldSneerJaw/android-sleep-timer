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

// TODO Document this class
public class PauseSongReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        // A well-behaved media player should relinquish audio focus (i.e. pause playback) when another app requests
        // focus: http://developer.android.com/training/managing-audio/audio-focus.html#HandleFocusLoss
        AudioFocusListener listener = new AudioFocusListener();
        int audioFocusResult = audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d(PauseSongReceiver.class.getName(), "Audio focus GAINED");

            // Immediately release focus. If the previous owner is well behaved, it will remain paused indefinitely;
            // if not, then holding onto audio focus will only briefly delay it from resuming playback
            audioManager.abandonAudioFocus(listener);
        } else {
            Log.e(PauseSongReceiver.class.getName(), "Audio focus DENIED");
        }
    }

    /**
     * Listens for audio focus change events.
     */
    private static class AudioFocusListener implements AudioManager.OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.d(PauseSongReceiver.class.getName(), "Audio focus REGAINED");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.d(PauseSongReceiver.class.getName(), "Audio focus LOST");
                    break;
                default:
                    Log.d(PauseSongReceiver.class.getName(), "Audio focus changed: " + focusChange);
            }
        }
    }
}
