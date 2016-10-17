package com.linson.phonesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linson.phonesafe.R;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class AToolsActivity extends Activity {

    private TextView tv_sms_backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        initUI();
    }

    private void initUI() {
        final ProgressBar pb_bar = (ProgressBar) findViewById(R.id.pb_bar);

        tv_sms_backup = (TextView) findViewById(R.id.tv_sms_backup);
        tv_sms_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(AToolsActivity.this);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.show();

                backupSms(new CallBack() {
                    @Override
                    public void setMax(int max) {
//                        pb_bar.setMax(max);
                        pd.setMax(max);
                    }

                    @Override
                    public void setProgress(int index) {
//                        pb_bar.setProgress(index);
                        pd.setProgress(index);
                    }

                    @Override
                    public void close() {
                        pd.dismiss();
                    }
                });
            }
        });
    }

    private void backupSms(final CallBack callBack) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //短信备份
                Cursor cursor = getContentResolver().query(Uri.parse("content://sms/")
                        , new String[]{"address", "date", "type", "body"}, null, null, null);
                int count = cursor.getCount();

//                pd.setMax(count);
                callBack.setMax(count);
                int index = 1;
                XmlSerializer xmlSerializer = Xml.newSerializer();
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "//" + "smss.xml";
                OutputStream os = null;
                try {
                    os = new FileOutputStream(path);
                    xmlSerializer.setOutput(os, "utf-8");
                    xmlSerializer.startDocument("utf-8", true);
                    xmlSerializer.startTag(null, "smss");
                    while (cursor.moveToNext()) {
                        xmlSerializer.startTag(null, "sms");

                        xmlSerializer.startTag(null, "address");
                        xmlSerializer.text(cursor.getString(0));
                        xmlSerializer.endTag(null, "address");

                        xmlSerializer.startTag(null, "date");
                        xmlSerializer.text(cursor.getString(1));
                        xmlSerializer.endTag(null, "date");

                        xmlSerializer.startTag(null, "type");
                        xmlSerializer.text(cursor.getString(2));
                        xmlSerializer.endTag(null, "type");

                        xmlSerializer.startTag(null, "body");
                        xmlSerializer.text(cursor.getString(3));
                        xmlSerializer.endTag(null, "body");

                        xmlSerializer.endTag(null, "sms");
                        Thread.sleep(500);
                        callBack.setProgress(index++);
//                        pd.setProgress(index++);
                    }
                    xmlSerializer.endTag(null, "smss");
                    xmlSerializer.endDocument();
                    Thread.sleep(500);
                    callBack.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    interface CallBack {
        void setMax(int max);

        void setProgress(int index);

        void close();
    }
}
