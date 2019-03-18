package com.keithmaher.autismfriendlylocations;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented top_menu, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedBaseActivity {
    @Test
    public void useAppContext() {
        // Context of the app under top_menu.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.keithmaher.autismfriendlylocations", appContext.getPackageName());
    }
}
