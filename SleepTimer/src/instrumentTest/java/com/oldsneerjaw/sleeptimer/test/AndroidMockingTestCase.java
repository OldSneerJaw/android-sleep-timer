/*
Copyright (c) 2013 Joel Andrews
Distributed under the MIT License: http://opensource.org/licenses/MIT
 */

package com.oldsneerjaw.sleeptimer.test;

import android.test.AndroidTestCase;

/**
 * Base class for Android test cases that wish to use Mockito. Exists as a workaround because Android 4.3 introduced
 * changes that prevent Mockito 1.9.5 and dexmaker 1.0 from working. Read more
 * <a href="https://code.google.com/p/dexmaker/issues/detail?id=2">here</a>.
 */
public abstract class AndroidMockingTestCase extends AndroidTestCase {

    /**
     * The system property that stores the location of the cache directory for dexmaker.
     */
    private static final String DEXMAKER_CACHE_DIR_PROPERTY = "dexmaker.dexcache";

    private String originalDexmakerCacheDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        originalDexmakerCacheDir = System.getProperty(DEXMAKER_CACHE_DIR_PROPERTY);

        // Ignore the unnecessary Android Studio warning in the next statement:
        //noinspection ConstantConditions
        System.setProperty(DEXMAKER_CACHE_DIR_PROPERTY, getContext().getCacheDir().toString());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Restore the original value of the system property
        if (originalDexmakerCacheDir != null) {
            System.setProperty(DEXMAKER_CACHE_DIR_PROPERTY, originalDexmakerCacheDir);
        } else {
            System.clearProperty(DEXMAKER_CACHE_DIR_PROPERTY);
        }
    }
}
