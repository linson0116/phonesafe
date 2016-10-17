package com.linson.phonesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.widget.TextView;

import com.linson.phonesafe.R;
import com.linson.phonesafe.utils.AppUtils;

public class SoftManagerActivity extends Activity {

    private TextView tv_disk;
    private TextView tv_sdcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager);
        initUI();
        initData();
        AppUtils.getAppInfoList(this);
    }

    private void initData() {
        String path_disk = Environment.getDataDirectory().getAbsolutePath();
        String path_sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        long disk_space = getSpaceSize(path_disk);
        long sdcard_space = getSpaceSize(path_sdcard);
        String disk_space_str = Formatter.formatFileSize(this, disk_space);
        String sdcard_space_str = Formatter.formatFileSize(this, sdcard_space);
        tv_disk.setText("内存：" + disk_space_str);
        tv_sdcard.setText("SD卡：" + sdcard_space_str);

//        Log.i(TAG, "initData: " + path_disk);
//        Log.i(TAG, "initData: " + path_sdcard);
    }

    private long getSpaceSize(String path_disk) {
        StatFs statFs = new StatFs(path_disk);
        long availableBlocks = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        return blockSize * availableBlocks;
    }

    private void initUI() {
        tv_disk = (TextView) findViewById(R.id.tv_disk);
        tv_sdcard = (TextView) findViewById(R.id.tv_sdcard);
    }
}
