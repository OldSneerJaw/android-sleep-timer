/*
Copyright (c) 2013 Joel Andrews
Distributed under the MIT License: http://opensource.org/licenses/MIT
 */

package com.oldsneerjaw.sleeptimer;

import android.test.AndroidTestCase;

/**
 * Base class for Android test cases that wish to use Mockito. Exists as a workaround because Mockito 1.9.5 does not
 * work with Android 4.3. Read more <a href="https://code.google.com/p/dexmaker/issues/detail?id=2">here</a>.
 */
public abstract class AndroidMockingTestCase extends AndroidTestCase {

    /**
     * The system property that stores the location of the cache directory for dexmaker.
     */
    private static final String DEXMAKER_CACHE_DIR_PROPERTY = "dexmaker.dexcache";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        //noinspection ConstantConditions
        System.setProperty(DEXMAKER_CACHE_DIR_PROPERTY, getContext().getCacheDir().toString());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        System.clearProperty(DEXMAKER_CACHE_DIR_PROPERTY);
    }
}
