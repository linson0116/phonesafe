package com.linson.phonesafe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.linson.phonesafe.db.dao.BlackNumberDao;
import com.linson.phonesafe.db.domain.BlackNumberInfo;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertEquals;

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

        assertEquals("com.linson.phonesafe", appContext.getPackageName());
        BlackNumberDao instance = BlackNumberDao.getInstance(appContext);
        //instance.insert("1102",1+"");
//        instance.delete("1102");
        instance.update("1101", "2");
        ArrayList<BlackNumberInfo> arr = instance.findAll();
        Log.i(TAG, "useAppContext: " + arr);
    }
}
