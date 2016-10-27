package com.linson.phonesafe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.format.Formatter;
import android.util.Log;

import com.linson.phonesafe.utils.ProcessUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.ContentValues.TAG;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        long avail = ProcessUtils.getProcessAvailMem(appContext);
        String strAvail = Formatter.formatFileSize(appContext, avail);

        long total = ProcessUtils.getProcessTotal(appContext);
        String strTotal = Formatter.formatFileSize(appContext, total);
        Log.i(TAG, "useAppContext: " + strAvail + "--" + strTotal);
        long used = total - avail;
        String strUsed = Formatter.formatFileSize(appContext, used);
        Log.i(TAG, "useAppContext: used=" + strUsed);
    }

}
