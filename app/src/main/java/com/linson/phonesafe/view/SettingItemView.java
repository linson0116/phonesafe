package com.linson.phonesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linson.phonesafe.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/9/1.
 */

public class SettingItemView extends RelativeLayout {

    private final String namespace = "http://schemas.android.com/apk/res/com.linson.phonesafe";
    private CheckBox cb_update;
    private TextView tv_update_desc;
    private TextView tv_update_title;
    private String deson;
    private String desoff;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View v = View.inflate(context, R.layout.setting_item, this);
        cb_update = (CheckBox) v.findViewById(R.id.cb_update);
        tv_update_desc = (TextView) v.findViewById(R.id.tv_update_desc);
        tv_update_title = (TextView) v.findViewById(R.id.tv_update_title);
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attrs) {
//        for (int i = 0; i < attrs.getAttributeCount(); i++) {
//            String attributeName = attrs.getAttributeName(i);
//            Log.i(TAG, "initAttr: " + attributeName);
//        }
        deson = attrs.getAttributeValue(namespace, "deson");
        desoff = attrs.getAttributeValue(namespace, "desoff");
        Log.i(TAG, "initAttr: deson " + deson);
        Log.i(TAG, "initAttr: desoff " + desoff);
        tv_update_title.setText(attrs.getAttributeValue(namespace, "destitle"));
        if (isChecked()) {
            tv_update_desc.setText(deson);
        } else {
            tv_update_desc.setText(desoff);
        }
    }

    public boolean isChecked() {
        return cb_update.isChecked();
    }

    public void setChecked(boolean flag) {
        if (flag == true) {
            cb_update.setChecked(flag);
            tv_update_desc.setText(deson);
        } else {
            cb_update.setChecked(false);
            tv_update_desc.setText(desoff);
        }
    }
}
