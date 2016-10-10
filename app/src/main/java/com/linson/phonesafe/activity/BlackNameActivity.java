package com.linson.phonesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.linson.phonesafe.R;
import com.linson.phonesafe.db.dao.BlackNumberDao;
import com.linson.phonesafe.db.domain.BlackNumberInfo;

import java.util.ArrayList;

public class BlackNameActivity extends Activity {

    private BlackNumberDao mDao;
    private ArrayList<BlackNumberInfo> mArrayList;

    private Button btn_add;
    private ListView lv_blacknames;
    private BlackNumberAdapter mAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mAdapter = new BlackNumberAdapter();
            lv_blacknames.setAdapter(mAdapter);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_name);
        initUI();
        initData();
    }

    private void initData() {
        mDao = BlackNumberDao.getInstance(getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                mArrayList = mDao.findAll();
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initUI() {
        btn_add = (Button) findViewById(R.id.bt_add);
        lv_blacknames = (ListView) findViewById(R.id.lv_blacknames);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_blacknumber_add, null);
        EditText et_tel = (EditText) view.findViewById(R.id.et_tel);
        Button btn_add = (Button) view.findViewById(R.id.btn_add);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    class BlackNumberAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.black_name_item, null);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode);
            tv_phone.setText(mArrayList.get(position).phone);
            String mode = mArrayList.get(position).mode;
            switch (mode) {
                case "1":
                    tv_mode.setText("短信拦截");
                    break;
                case "2":
                    tv_mode.setText("电话拦截");
                    break;
                case "3":
                    tv_mode.setText("全部拦截");
                    break;
            }
            return view;
        }
    }
}
