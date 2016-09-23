package com.linson.phonesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linson.phonesafe.R;
import com.linson.phonesafe.utils.SpUtils;

public class Setup3Activity extends BaseSetupActivity {

    private TextView tv_radius;
    private EditText et_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        tv_radius = (TextView)findViewById(R.id.tv_radius);
        initUI();
    }

    private void initUI() {
        et_tel = (EditText) findViewById(R.id.et_tel);
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
        SpUtils.setSafePhoneNumber(this,et_tel.getText().toString());
        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
        } else {
            et_tel.setText(data.getStringExtra("tel"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
