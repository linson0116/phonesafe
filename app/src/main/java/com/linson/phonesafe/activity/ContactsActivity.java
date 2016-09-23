package com.linson.phonesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.linson.phonesafe.R;
import com.linson.phonesafe.utils.ConstantValues;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class ContactsActivity extends Activity {

    private ListView lv_contacts;
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initData();
        initUI();

    }

    private void initData() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cursor = contentResolver.query(uri, new String[]{"contact_id"}, null, null, null);
        list.clear();
        while (cursor.moveToNext()) {
            String contact_id = cursor.getString(0);
            Log.i(TAG, "initData: " + contact_id);
            Cursor cursor1 = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                    new String[]{"data1","mimetype"},
                    "raw_contact_id = ?",new String[]{contact_id},
                    null);
            HashMap<String, String> hashMap = new HashMap<String,String>();
            while (cursor1.moveToNext()) {
                String data = cursor1.getString(0);
                data = data.replace(" ","").replace("-","");
                String mimetype = cursor1.getString(1);
                Log.i(TAG, "initData: " + data +" " + mimetype);

                if (mimetype.equals(ConstantValues.TEL_NAME)) {
                    hashMap.put("name", data);
                } else if (mimetype.equals(ConstantValues.TEL)) {
                    hashMap.put("tel", data);
                }

            }
            list.add(hashMap);
        }
    }

    private void initUI() {
        lv_contacts = (ListView) findViewById(R.id.lv_contacts);
        lv_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: " + position);
                Intent intent = new Intent();
                intent.putExtra("tel", list.get(position).get("tel"));
                setResult(0,intent);
                finish();
            }
        });
        lv_contacts.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                Log.i(TAG, "getItem: " + position);
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = View.inflate(getApplicationContext(), R.layout.contacts_item, null);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                TextView tv_tel = (TextView) view.findViewById(R.id.tv_tel);
                HashMap<String, String> hashMap = list.get(position);
                tv_name.setText(hashMap.get("name"));
                tv_tel.setText(hashMap.get("tel"));
                return view;
            }
        });
    }
}
