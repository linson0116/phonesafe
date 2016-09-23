package com.linson.phonesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.linson.phonesafe.R;
import com.linson.phonesafe.utils.SpUtils;

public class Setup5Activity extends Activity {

    private TextView tv_safeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup5);
        initUI();
        initData();
    }

    private void initUI() {
        tv_safeNum = (TextView) findViewById(R.id.tv_safeNum);
    }

    private void initData() {
        String safePhoneNumber = SpUtils.getSafePhoneNumber(this);
        tv_safeNum.setText(safePhoneNumber);
    }
}
