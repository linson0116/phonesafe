package com.linson.phonesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linson.phonesafe.R;
import com.linson.phonesafe.utils.SpUtils;
import com.linson.phonesafe.utils.ToolsUtils;

public class HomeActivity extends Activity {
    private GridView gv_home;
    private String[] functionNames;
    private int[] functionImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        initData();
    }

    private void initData() {
        functionNames = new String[]{"手机安全", "黑名单", "软件管理", "进程管理", "5", "6", "7", "高级工具", "设置"};
        functionImages = new int[]{R.drawable.widget01,
                R.drawable.widget02,
                R.drawable.widget03,
                R.drawable.widget04,
                R.drawable.widget05,
                R.drawable.widget06,
                R.drawable.widget07,
                R.drawable.widget08,
                R.drawable.widget09};
        gv_home.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return functionNames.length;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View gridview_layout = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
                ImageView imageView = (ImageView) gridview_layout.findViewById(R.id.iv_icon_image);
                TextView textView = (TextView) gridview_layout.findViewById(R.id.tv_icon_name);
                imageView.setImageResource(functionImages[i]);
                textView.setText(functionNames[i]);
                return gridview_layout;
            }
        });
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 8:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                    case 0:
                        checkPsd();
                        break;
                    case 3:
                        //进程管理
                        startActivity(new Intent(getApplicationContext(), ProcessManagerActivity.class));
                        break;
                    case 7:
                        //高级工具
                        startActivity(new Intent(getApplicationContext(), AToolsActivity.class));
                        break;
                    case 2:
                        //软件管理
                        startActivity(new Intent(getApplicationContext(), SoftManagerActivity.class));
                        break;
                    case 1:
                        openBlackName();
                        break;
                }
            }
        });
    }

    private void openBlackName() {
        Intent intent = new Intent(this, BlackNameActivity.class);
        startActivity(intent);
    }

    private void checkPsd() {
        if (SpUtils.getPsd(this).equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.create();
            View view = View.inflate(this, R.layout.dialog_psd_confirm, null);
            dialog.setView(view);
            dialog.show();
            final EditText tv_psd = (EditText) view.findViewById(R.id.tv_psd);
            final EditText tv_psd_confirm = (EditText) view.findViewById(R.id.tv_psd_confirm);
            Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
            Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(tv_psd.getText()) && TextUtils.isEmpty(tv_psd_confirm.getText())) {
                        Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        String psd = tv_psd.getText().toString();
                        String psd_confirm = tv_psd_confirm.getText().toString();
                        if (psd.equals(psd_confirm)) {
                            //
                            SpUtils.setPsd(getApplicationContext(), ToolsUtils.md5Encode(psd));
                            dialog.dismiss();
                        } else {
                            Toast.makeText(HomeActivity.this, "密码不同", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.create();
            View view = View.inflate(this, R.layout.dialog_psd, null);
            dialog.setView(view);
            dialog.show();
            final EditText tv_psd = (EditText) view.findViewById(R.id.tv_psd);
            Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
            Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String psd = tv_psd.getText().toString();
                    psd = ToolsUtils.md5Encode(psd);
                    String psd_store = SpUtils.getPsd(getApplicationContext());
                    if (psd.equals(psd_store)) {
//                        Toast.makeText(HomeActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

    }
}
