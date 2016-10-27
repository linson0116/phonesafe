package com.linson.phonesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.linson.phonesafe.R;
import com.linson.phonesafe.db.domain.ProcessInfo;
import com.linson.phonesafe.utils.ProcessUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ProcessManagerActivity extends Activity {

    private TextView tv_process_num;
    private TextView tv_memory;
    private ListView lv_process;
    private ProcessAdapter mAdapter;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mAdapter = new ProcessAdapter();
            lv_process.setAdapter(mAdapter);
            super.handleMessage(msg);
        }
    };
    private List<ProcessInfo> mProcessList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);
        initUI();
        initData();
    }

    private void initData() {
        int processNum = ProcessUtils.getProcessNum(this);
        long total = ProcessUtils.getProcessTotal(this);
        long avail = ProcessUtils.getProcessAvailMem(this);
        String strTotal = Formatter.formatFileSize(this, total);
        String strAvail = Formatter.formatFileSize(this, avail);
        String process = "剩余/总共：" + strAvail + "/" + strTotal;
        tv_process_num.setText("进程总数：" + processNum);
        tv_memory.setText(process);
        getProcessData();
    }

    private void getProcessData() {
        mProcessList = new ArrayList<ProcessInfo>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                PackageManager pm = getPackageManager();
                List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();

                for (ActivityManager.RunningAppProcessInfo info : processes) {
                    ProcessInfo processInfo = new ProcessInfo();
                    processInfo.packageName = info.processName;
                    Log.i(TAG, "run: " + processInfo.packageName);

                    Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{info.pid});
                    long memory = processMemoryInfo[0].getTotalPrivateDirty() * 1024;
                    String strMemory = Formatter.formatFileSize(getApplicationContext(), memory);
                    processInfo.useMemory = strMemory;
                    try {
                        ApplicationInfo applicationInfo = pm.getApplicationInfo(processInfo.packageName, 0);
                        Drawable drawable = applicationInfo.loadIcon(pm);
                        processInfo.icon = drawable;
                        String name = applicationInfo.loadLabel(pm).toString();
                        processInfo.appName = name;
                        if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                            processInfo.isSystemProcess = true;
                        } else {
                            processInfo.isSystemProcess = false;
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        processInfo.appName = info.processName;
                        processInfo.icon = getResources().getDrawable(R.mipmap.ic_launcher);
                        processInfo.isSystemProcess = true;
                        e.printStackTrace();
                    }
                    mProcessList.add(processInfo);
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initUI() {
        tv_process_num = (TextView) findViewById(R.id.tv_process_num);
        tv_memory = (TextView) findViewById(R.id.tv_memory);
        lv_process = (ListView) findViewById(R.id.lv_process);
        lv_process.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProcessInfo processInfo = mProcessList.get(position);
                CheckBox cb = (CheckBox) view.findViewById(R.id.cb_selected);
                cb.setChecked(!processInfo.isChecked);
                processInfo.isChecked = !processInfo.isChecked;
            }
        });
        Button btn_all_choice = (Button) findViewById(R.id.btn_all_choice);
        btn_all_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ProcessInfo info : mProcessList) {
                    info.isChecked = true;
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        Button btn_invert_choice = (Button) findViewById(R.id.btn_invert_choice);
        btn_invert_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ProcessInfo info : mProcessList) {
                    info.isChecked = !info.isChecked;
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        Button btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killProcess();
            }
        });
    }

    private void killProcess() {
        List<ProcessInfo> killProcessList = new ArrayList<ProcessInfo>();
        for (ProcessInfo info : mProcessList) {
            if (info.isChecked) {
                killProcessList.add(info);
            }
        }
        for (ProcessInfo info : killProcessList) {
            if (mProcessList.contains(info)) {
                mProcessList.remove(info);
            }
        }
        mAdapter.notifyDataSetChanged();
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ProcessInfo info : killProcessList) {
            am.killBackgroundProcesses(info.packageName);
        }
        int num = mProcessList.size();
        tv_process_num.setText("进程总数：" + num);
    }

    static class ListHandle {
        ImageView iv_icon;
        TextView tv_app_name;
        TextView tv_memory;
        CheckBox cb_selected;
    }

    class ProcessAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mProcessList.size();
        }

        @Override
        public Object getItem(int position) {
            return mProcessList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListHandle handle = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.process_item, null);
                handle = new ListHandle();
                handle.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                handle.tv_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                handle.tv_memory = (TextView) convertView.findViewById(R.id.tv_memory);
                handle.cb_selected = (CheckBox) convertView.findViewById(R.id.cb_selected);
                convertView.setTag(handle);
            } else {
                handle = (ListHandle) convertView.getTag();
            }
            ProcessInfo processInfo = mProcessList.get(position);
            handle.tv_app_name.setText(processInfo.appName);
            handle.tv_memory.setText(processInfo.useMemory);
            handle.iv_icon.setBackground(processInfo.icon);
            handle.cb_selected.setChecked(processInfo.isChecked);
            return convertView;
        }
    }
}
