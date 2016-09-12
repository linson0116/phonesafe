package com.linson.phonesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.linson.phonesafe.R;

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }
    @Override
    void showPreviousPage() {
    }
    @Override
    void showNextPage() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.next_in_setup,R.anim.next_out_setup);
    }
}
