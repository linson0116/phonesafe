package com.linson.phonesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.linson.phonesafe.R;
import com.linson.phonesafe.utils.SpUtils;
import com.linson.phonesafe.view.SettingItemView;

public class Setup2Activity extends Activity {

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

    public void nextPage(View view) {
        Toast.makeText(this, "next", Toast.LENGTH_SHORT).show();
    }

    public void previousPage(View view) {
        Toast.makeText(this, "previous", Toast.LENGTH_SHORT).show();
    }

}
