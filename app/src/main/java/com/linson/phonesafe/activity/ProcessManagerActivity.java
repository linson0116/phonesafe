package com.linson.phonesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.linson.phonesafe.R;
import com.linson.phonesafe.utils.ProcessUtils;

public class ProcessManagerActivity extends Activity {

    private TextView tv_process_num;
    private TextView tv_memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);
        initUI();
        initData();
    }

    private void initData() {
        int processNum = ProcessUtils.getProcessNum(this);
        tv_process_num.setText("进程总数：" + processNum);
    }

    private void initUI() {
        tv_process_num = (TextView) findViewById(R.id.tv_process_num);
        tv_memory = (TextView) findViewById(R.id.tv_memory);
    }
}
