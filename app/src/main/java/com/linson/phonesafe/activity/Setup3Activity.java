package com.linson.phonesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linson.phonesafe.R;

import static android.content.ContentValues.TAG;

public class Setup3Activity extends BaseSetupActivity {

    private TextView tv_radius;
    private TextView tv_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        tv_radius = (TextView)findViewById(R.id.tv_radius);
        initUI();
    }

    private void initUI() {
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_radius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),ContactsActivity.class),0);
            }
        });
    }

    @Override
    void showPreviousPage() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.previous_in_setup,R.anim.previous_out_setup);
    }

    @Override
    void showNextPage() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        tv_tel.setText(data.getStringExtra("tel"));
        super.onActivityResult(requestCode, resultCode, data);
    }
}
