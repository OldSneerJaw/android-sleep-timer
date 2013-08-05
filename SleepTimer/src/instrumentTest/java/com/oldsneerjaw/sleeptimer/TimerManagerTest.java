package com.oldsneerjaw.sleeptimer;

import android.content.Context;
import android.test.AndroidTestCase;

import org.mockito.Mockito;

/**
 * Test cases for {@link TimerManager}.
 *
 * @author Joel Andrews
 */
public class TimerManagerTest extends AndroidTestCase {

    public void testGetInstance_EqualContextsReturnSameInstance() {
        String packageName = "foo";

        Context context1 = Mockito.mock(Context.class);
        Mockito.when(context1.getPackageName()).thenReturn(packageName);

        Context context2 = Mockito.mock(Context.class);
        Mockito.when(context2.getPackageName()).thenReturn(packageName);

        TimerManager instance1 = TimerManager.getInstance(context1);
        TimerManager instance2 = TimerManager.getInstance(context2);

        assertSame(instance1, instance2);
    }

    public void testGetInstance_DifferentContextsReturnDifferentInstances() {
        Context context1 = Mockito.mock(Context.class);
        Mockito.when(context1.getPackageName()).thenReturn("foo");

        Context context2 = Mockito.mock(Context.class);
        Mockito.when(context2.getPackageName()).thenReturn("bar");

        TimerManager instance1 = TimerManager.getInstance(context1);
        TimerManager instance2 = TimerManager.getInstance(context2);

        assertNotSame(instance1, instance2);
    }
}
