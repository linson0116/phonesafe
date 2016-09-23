package com.linson.phonesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.linson.phonesafe.R;

public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cb_protect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUI();
    }

    private void initUI() {
        cb_protect = (CheckBox) findViewById(R.id.cb_protect);
        cb_protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_protect.isChecked()) {
                    cb_protect.setText("防盗保护已经开启");
                } else {
                    cb_protect.setText("防盗保护已经关闭");
                }
            }
        });
    }

    @Override
    void showPreviousPage() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    void showNextPage() {
        if (cb_protect.isChecked()) {
            Intent intent = new Intent(this, Setup5Activity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "请开启防盗保护", Toast.LENGTH_SHORT).show();
        }
    }
}
