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

        // A well-behaved media player should relinquish audio focus (i.e. pause playback) when another app requests focus
        AudioFocusListener listener = new AudioFocusListener();
        int audioFocusResult = audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.i(PauseSongReceiver.class.getName(), "Audio focus GAINED");
        } else {
            Log.i(PauseSongReceiver.class.getName(), "Audio focus DENIED");
        }
    }

    private class AudioFocusListener implements AudioManager.OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int focusChange) {


            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.i(PauseSongReceiver.class.getName(), "Audio focus REGAINED");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.i(PauseSongReceiver.class.getName(), "Audio focus LOST");
                    break;
                default:
                    Log.i(PauseSongReceiver.class.getName(), "Audio focus changed: " + focusChange);
            }
        }
    }
}
