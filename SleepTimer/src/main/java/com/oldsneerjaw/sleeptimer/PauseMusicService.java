package com.oldsneerjaw.sleeptimer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * <p>
 * A service that pauses all music playback on the device and ensures that it remains paused until playback is
 * explicitly started again.
 * </p>
 * <p>
 * Certain music apps (e.g. Rdio) do not adhere to Android's guidelines for
 * <a href="http://developer.android.com/training/managing-audio/audio-focus.html#HandleFocusLoss">
 *     handling the loss of audio focus
 * </a>
 * and will immediately reclaim audio focus as soon as it's available; therefore, this service claims audio focus and
 * attempts to keep it until another app explicitly requests audio focus. See
 * <a href="https://github.com/rdio/api/issues/90">Rdio API issue #90</a> for a more detailed description of the
 * problem.
 * </p>
 *
 * @author Joel Andrews
 */
public class PauseMusicService extends IntentService {

    private static final String LOG_TAG = PauseMusicService.class.getName();

    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener listener;
    private PauseMusicNotifier notifier;

    /**
     * Constructs an instance of {@link PauseMusicService}.
     */
    public PauseMusicService() {
        super(PauseMusicService.class.getName());
    }

    @Override
    public void onCreate() {
        onCreate(
                (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE),
                new AudioFocusListener(),
                new PauseMusicNotifier(getApplicationContext()));
    }

    /**
     * Initializes the service's dependencies.
     *
     * @param audioManager The audio manager to use
     * @param listener The audio focus change listener to use
     * @param notifier The pause music notifier to use
     */
    void onCreate(AudioManager audioManager, AudioManager.OnAudioFocusChangeListener listener, PauseMusicNotifier notifier) {
        super.onCreate();

        this.audioManager = audioManager;
        this.listener = listener;
        this.notifier = notifier;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Attempt to hold onto audio focus indefinitely; only if another app explicitly requests audio focus
        // will this service willingly let it go
        if (pauseMusicPlayback()) {
            Log.i(LOG_TAG, "Successfully paused music playback");

            notifier.postNotification();

            synchronized (this) {
                try {
                    // Wait indefinitely to ensure that the service is not recycled and that it keeps audio focus
                    wait();
                } catch (InterruptedException ex) {
                    Log.d(LOG_TAG, "Service interrupted", ex);

                    stopAndReleaseAudioFocus();
                }
            }
        } else {
            Log.e(LOG_TAG, "Failed to pause music playback");
        }
    }

    /**
     * Pauses all music playback on the device.
     *
     * @return {@code true} if playback was successfully paused; otherwise, {@code false}
     */
    boolean pauseMusicPlayback() {
        // Taking audio focus should force other apps to pause/stopAndReleaseAudioFocus music playback
        int audioFocusResult =
                audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Stops the service and releases audio focus.
     */
    void stopAndReleaseAudioFocus() {
        audioManager.abandonAudioFocus(listener);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (listener != null && audioManager != null) {
            audioManager.abandonAudioFocus(listener);
            listener = null;
            audioManager = null;
        }
    }

    /**
     * A listener for audio focus change events.
     */
    class AudioFocusListener implements AudioManager.OnAudioFocusChangeListener {

        @Override
        public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.d(LOG_TAG, "Audio focus lost permanently");

                    // Since audio focus has been permanently taken by another process (e.g. the user explicitly
                    // restarted music playback), the service can be stopped and audio focus released so this process
                    // does not automatically reclaim audio focus when/if the new owner relinquishes it
                    stopAndReleaseAudioFocus();

                    break;
                default:
                    Log.d(LOG_TAG, "Audio focus changed: " + focusChange);
            }
        }
    }
}
