/*
Copyright (c) 2013 Joel Andrews
Distributed under the MIT License: http://opensource.org/licenses/MIT
 */

package com.oldsneerjaw.sleeptimer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.oldsneerjaw.sleeptimer.test.AndroidMockingTestCase;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.Mockito;

/**
 * Test cases for {@link PauseMusicReceiver}.
 *
 * @author Joel Andrews
 */
public class PauseMusicReceiverTest extends AndroidMockingTestCase {

    public void testPauseMusic() {
        Context mockContext = Mockito.mock(Context.class);
        TimerManager mockTimerManager = Mockito.mock(TimerManager.class);
        CountdownNotifier mockNotifier = Mockito.mock(CountdownNotifier.class);

        new PauseMusicReceiver().pauseMusic(mockContext, mockTimerManager, mockNotifier);

        Mockito.verify(mockTimerManager).cancelTimer();
        Mockito.verify(mockNotifier).cancelNotification();
        Mockito.verify(mockContext).startService(Mockito.argThat(new BaseMatcher<Intent>() {
            @Override
            public boolean matches(Object o) {
                Intent candidate = (Intent) o;

                ComponentName component = candidate.getComponent();
                return TextUtils.equals(PauseMusicService.class.getName(), component.getClassName());
            }

            @Override
            public void describeTo(Description description) {
                // In the event of a test failure, this describes what was expected
                description.appendText("explicit intent to launch " + PauseMusicService.class.getName());
            }
        }));
    }
}
