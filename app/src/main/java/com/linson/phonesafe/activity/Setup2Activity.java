package com.linson.phonesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.linson.phonesafe.R;
import com.linson.phonesafe.utils.SpUtils;
import com.linson.phonesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView siv_setup2_bindsim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUI();
    }

    private void initUI() {
        siv_setup2_bindsim = (SettingItemView) findViewById(R.id.siv_setup2_bindsim);
        siv_setup2_bindsim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_setup2_bindsim.isChecked()) {
                    siv_setup2_bindsim.setChecked(false);
                } else {
                    siv_setup2_bindsim.setChecked(true);
                }
            }
        });
        if (SpUtils.getSIMNumber(getApplicationContext()).equals("")) {
            siv_setup2_bindsim.setChecked(false);
        } else {
            siv_setup2_bindsim.setChecked(true);
        }
    }



    @Override
    void showPreviousPage() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.previous_in_setup,R.anim.previous_out_setup);
    }

    @Override
    void showNextPage() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.next_in_setup,R.anim.next_out_setup);
    }



}
