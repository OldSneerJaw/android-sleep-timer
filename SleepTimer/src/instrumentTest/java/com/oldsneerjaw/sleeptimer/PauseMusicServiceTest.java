package com.oldsneerjaw.sleeptimer;

import android.media.AudioManager;
import android.test.AndroidTestCase;

import org.mockito.Mockito;

/**
 * Test cases for {@link PauseMusicService}.
 */
public class PauseMusicServiceTest extends AndroidTestCase {

    private AudioManager mockAudioManager;
    private AudioManager.OnAudioFocusChangeListener mockListener;
    private PauseMusicService service;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mockAudioManager = Mockito.mock(AudioManager.class);
        mockListener = Mockito.mock(AudioManager.OnAudioFocusChangeListener.class);
        PauseMusicNotifier mockNotifier = Mockito.mock(PauseMusicNotifier.class);

        service = new PauseMusicService();
        service.onCreate(mockAudioManager, mockListener, mockNotifier);
    }

    public void testPauseMusicPlayback_Success() {
        Mockito.when(mockAudioManager.requestAudioFocus(mockListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN))
                .thenReturn(AudioManager.AUDIOFOCUS_REQUEST_GRANTED);

        assertTrue(service.pauseMusicPlayback());
    }

    public void testPauseMusicPlayback_Failed() {
        Mockito.when(mockAudioManager.requestAudioFocus(mockListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN))
                .thenReturn(AudioManager.AUDIOFOCUS_REQUEST_FAILED);

        assertFalse(service.pauseMusicPlayback());
    }

    public void testOnDestroy() {
        service.onDestroy();

        Mockito.verify(mockAudioManager).abandonAudioFocus(mockListener);
    }

    public void testAudioFocusListener() {
        PauseMusicService mockService = Mockito.mock(PauseMusicService.class);
        PauseMusicService.AudioFocusListener listener = mockService.new AudioFocusListener();

        listener.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS);

        Mockito.verify(mockService).stopAndReleaseAudioFocus();
    }
}
