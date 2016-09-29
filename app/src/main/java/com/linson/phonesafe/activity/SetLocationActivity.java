package com.linson.phonesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.linson.phonesafe.R;
import com.linson.phonesafe.utils.ConstantValues;
import com.linson.phonesafe.utils.SpUtils;

import static android.content.ContentValues.TAG;

public class SetLocationActivity extends Activity {

    private ImageView iv_set_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        initUI();
        initLocation();
    }

    private void initLocation() {
//        SpUtils.setInt(this, ConstantValues.LOCATION_X, 100);
//        SpUtils.setInt(this, ConstantValues.LOCATION_Y, 100);

        int locationX = SpUtils.getInt(this, ConstantValues.LOCATION_X);
        int locationY = SpUtils.getInt(this, ConstantValues.LOCATION_Y);
        Log.i(TAG, "initLocation: " + locationX);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = locationX;
        params.topMargin = locationY;
        iv_set_location.setLayoutParams(params);
//        iv_set_location.layout(200,200,400,400);
    }

    private void initUI() {
        iv_set_location = (ImageView) findViewById(R.id.iv_set_location);
        iv_set_location.setOnTouchListener(new View.OnTouchListener() {

            private int moveY;
            private int moveX;
            private int beginY;
            private int beginX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        beginX = (int) event.getRawX();
                        beginY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveX = (int) event.getRawX();
                        moveY = (int) event.getRawY();
                        int disX = moveX - beginX;
                        int disY = moveY - beginY;
                        int left = iv_set_location.getLeft() + disX;
                        int top = iv_set_location.getTop() + disY;
                        int bottom = iv_set_location.getBottom() + disY;
                        int right = iv_set_location.getRight() + disX;
                        iv_set_location.layout(left, top, right, bottom);

                        beginX = (int) event.getRawX();
                        beginY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtils.setInt(getApplication(),ConstantValues.LOCATION_X,(int)event.getRawX());
                        SpUtils.setInt(getApplication(),ConstantValues.LOCATION_Y, (int) event.getRawY());
                        Log.i(TAG, "onTouch: " + event.getRawX());
                        break;
                }
                return true;
            }
        });
    }
}
