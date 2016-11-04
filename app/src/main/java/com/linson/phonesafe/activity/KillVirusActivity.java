package com.linson.phonesafe.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linson.phonesafe.R;
import com.linson.phonesafe.db.dao.KillVirusDao;
import com.linson.phonesafe.utils.ToolsUtils;

import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class KillVirusActivity extends Activity {

    private static final int GET_APP_NAME = 100;
    private ImageView iv_background;
    private ImageView iv_process;
    private List<String> virusList;
    private LinearLayout ll_add;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_APP_NAME:
                    String appName = (String) msg.obj;
                    TextView tv = new TextView(getApplicationContext());
                    tv.setText(appName);
                    tv.setTextColor(Color.RED);
                    ll_add.addView(tv, 0);
                    Log.i(TAG, "handleMessage: " + appName);
                    break;
            }
        }
    };
    private ProgressBar pb_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kill_virus);
        initUI();
        initAnimation();
        initData();

    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ToolsUtils.asset2app(getApplication(), "antivirus.db");
                virusList = KillVirusDao.getVirusList();
                PackageManager pm = getPackageManager();
                List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_SIGNATURES
                        + PackageManager.GET_UNINSTALLED_PACKAGES);
                pb_bar.setMax(packages.size());
                int i = 0;
                for (PackageInfo info : packages) {
                    String appName = (String) info.applicationInfo.loadLabel(pm);
                    Message message = mHandler.obtainMessage();
                    message.obj = appName;
                    message.what = GET_APP_NAME;
                    try {
                        Thread.sleep(new Random().nextInt(500));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                    pb_bar.setProgress(i);
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    private void initAnimation() {
        RotateAnimation animation = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setRepeatCount(Animation.INFINITE);
        iv_process.startAnimation(animation);
    }

    private void initUI() {
        iv_background = (ImageView) findViewById(R.id.iv_background);
        iv_process = (ImageView) findViewById(R.id.iv_process);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
    }
}
