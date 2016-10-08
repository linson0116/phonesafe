package com.linson.phonesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.linson.phonesafe.R;
import com.linson.phonesafe.utils.ConstantValues;
import com.linson.phonesafe.utils.SpUtils;

import static android.content.ContentValues.TAG;

public class SetLocationActivity extends Activity {

    private ImageView iv_set_location;
    private int mWindowWidth;
    private int mWindowHeight;
    private long[] mHits = new long[2];

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
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWindowWidth = manager.getDefaultDisplay().getWidth();
        mWindowHeight = manager.getDefaultDisplay().getHeight();
        int locationX = SpUtils.getInt(this, ConstantValues.LOCATION_X);
        int locationY = SpUtils.getInt(this, ConstantValues.LOCATION_Y);
        Log.i(TAG, "initLocation: " + locationX);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = locationX;
        Log.i(TAG, "initLocation: X" + locationX);
        params.topMargin = locationY;
        Log.i(TAG, "initLocation: Y" + locationY);
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

                        if (left < 0) {
                            return true;
                        }
                        if (right > mWindowWidth) {
                            return true;
                        }
                        if (top < 0) {
                            return true;
                        }
                        if (bottom > mWindowHeight - 25) {
                            return true;
                        }
                        iv_set_location.layout(left, top, right, bottom);

                        beginX = (int) event.getRawX();
                        beginY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtils.setInt(getApplication(), ConstantValues.LOCATION_X, iv_set_location.getLeft());
                        SpUtils.setInt(getApplication(), ConstantValues.LOCATION_Y, iv_set_location.getTop());
                        Log.i(TAG, "onTouch: " + iv_set_location.getLeft() + "-" + iv_set_location.getTop());
//                        Log.i(TAG, "onTouch: x,y" + event.getX()+"--" + event.getY());
                        break;
                }
                return false;
            }
        });
        iv_set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits,1,mHits,0,mHits.length-1);
                mHits[mHits.length - 1] = System.currentTimeMillis();
                if (mHits[mHits.length - 1] - mHits[0]<500) {
                    Log.i(TAG, "onClick: 双击");
                    int l =(mWindowWidth - iv_set_location.getWidth())/2;
                    int t = (mWindowHeight - iv_set_location.getHeight())/2;
                    int r = mWindowWidth/2 + iv_set_location.getWidth()/2;
                    int b = mWindowHeight/2 + iv_set_location.getHeight()/2;
                    iv_set_location.layout(l,t,r,b);
                    SpUtils.setInt(getApplication(),ConstantValues.LOCATION_X,iv_set_location.getLeft());
                    SpUtils.setInt(getApplication(),ConstantValues.LOCATION_Y,iv_set_location.getTop());

                }

            }
        });
    }
}
