package com.linson.phonesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.linson.phonesafe.R;
import com.linson.phonesafe.service.AlarmMusicService;
import com.linson.phonesafe.service.PhoneStatusService;
import com.linson.phonesafe.view.ArrowItemView;
import com.linson.phonesafe.view.SettingItemView;

import static android.content.ContentValues.TAG;

public class SettingActivity extends Activity {

    private WindowManager mWM;
    private View viewToast;
    private ArrowItemView aiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initSettingUI();
    }

    private void initSettingUI() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (siv_update.isChecked()) {
                    siv_update.setChecked(false);
                } else {
                    siv_update.setChecked(true);
                }
            }
        });
        aiv = (ArrowItemView) findViewById(R.id.aiv);
        aiv.setTitle("设置风格");
        aiv.setDesc("颜色");
        aiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("设置").setSingleChoiceItems(new String[]{"1", "2"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aiv.setDesc(which + "");
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setIcon(R.drawable.bind).show();

            }
        });
    }

    public void play(View view) {
//        MediaPlayer player = MediaPlayer.create(this, R.raw.ylzs);
//        player.setLooping(true);
//        player.start();
        Intent intent = new Intent(this, AlarmMusicService.class);
        startService(intent);
    }

    public void beginPhoneService(View view) {
        startService(new Intent(getApplicationContext(), PhoneStatusService.class));
    }

    public void endPhoneService(View view) {
        stopService(new Intent(getApplicationContext(), PhoneStatusService.class));
    }

    public void openToast(View view) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //在响铃的时候显示吐司,和电话类型一致
        Log.i(TAG, "openToast: " + params.type);
        //params.type = WindowManager.LayoutParams.TYPE_PHONE;

        //指定吐司的所在位置(将吐司指定在左上角)
        params.gravity = Gravity.LEFT + Gravity.TOP;

        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        viewToast = View.inflate(getApplicationContext(), R.layout.toast_phone, null);
        mWM.addView(viewToast, params);
    }

    public void closeToast(View view) {
        mWM.removeView(viewToast);
    }

    public void setLocation(View view) {
        Intent intent = new Intent(this,SetLocationActivity.class);
        startActivity(intent);
    }
}
