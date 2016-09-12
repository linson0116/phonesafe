package com.linson.phonesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/9/12.
 */

public abstract class BaseSetupActivity extends Activity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getX() - e1.getX()>0) {
                    Log.i(TAG, "onFling: " + "right");
                    showPreviousPage();
                }
                if (e2.getX() - e1.getX() < 0) {
                    Log.i(TAG, "onFling: " + "left");
                    showNextPage();
                }
                Log.i(TAG, "onFling: e1 " + e1.getX());
                Log.i(TAG, "onFling: e2 " + e2.getX());
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public void previousPage(View view) {
        showPreviousPage();
    }

    public void nextPage(View view) {
        showNextPage();
    }

    abstract void showPreviousPage();

    abstract void showNextPage();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
