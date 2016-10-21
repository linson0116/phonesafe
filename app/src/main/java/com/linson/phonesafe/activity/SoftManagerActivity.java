package com.linson.phonesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.linson.phonesafe.R;
import com.linson.phonesafe.db.domain.AppInfo;
import com.linson.phonesafe.utils.AppUtils;

import java.util.List;

public class SoftManagerActivity extends Activity {

    private TextView tv_disk;
    private TextView tv_sdcard;
    private ListView lv_appinfo;
    private List<AppInfo> mAppInfoList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AppInfoAdapter adapter = new AppInfoAdapter();
            lv_appinfo.setAdapter(adapter);
            super.handleMessage(msg);
        }
    };
    private AppInfo mAppInfo;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager);
        initUI();
        initData();
    }

    private void initData() {
        String path_disk = Environment.getDataDirectory().getAbsolutePath();
        String path_sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        long disk_space = getSpaceSize(path_disk);
        long sdcard_space = getSpaceSize(path_sdcard);
        String disk_space_str = Formatter.formatFileSize(this, disk_space);
        String sdcard_space_str = Formatter.formatFileSize(this, sdcard_space);
        tv_disk.setText("内存：" + disk_space_str);
        tv_sdcard.setText("SD卡：" + sdcard_space_str);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAppInfoList = AppUtils.getAppInfoList(getApplication());
                handler.sendEmptyMessage(0);
            }
        }).start();

//        Log.i(TAG, "initData: " + path_disk);
//        Log.i(TAG, "initData: " + path_sdcard);
    }

    private long getSpaceSize(String path_disk) {
        StatFs statFs = new StatFs(path_disk);
        long availableBlocks = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        return blockSize * availableBlocks;
    }

    private void initUI() {
        tv_disk = (TextView) findViewById(R.id.tv_disk);
        tv_sdcard = (TextView) findViewById(R.id.tv_sdcard);
        lv_appinfo = (ListView) findViewById(R.id.lv_appinfo);
        lv_appinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAppInfo = mAppInfoList.get(position);
                showPopupWindow(view);
            }
        });
    }

    private void showPopupWindow(View view) {
        View popupWindow_layout = View.inflate(this, R.layout.popup_window, null);
        TextView tv_uninstall = (TextView) popupWindow_layout.findViewById(R.id.tv_uninstall);
        TextView tv_start = (TextView) popupWindow_layout.findViewById(R.id.tv_start);
        TextView tv_share = (TextView) popupWindow_layout.findViewById(R.id.tv_share);
        tv_uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:" + mAppInfo.packageName));
                startActivity(intent);
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();
                Intent launchIntentForPackage = pm.getLaunchIntentForPackage(mAppInfo.packageName);
                startActivity(launchIntentForPackage);
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过短信应用,向外发送短信
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "分享一个应用,应用名称为" + mAppInfo.appName);
                intent.setType("text/plain");
                startActivity(intent);
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });

        mPopupWindow = new PopupWindow(popupWindow_layout,
                LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
//        mPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        mPopupWindow.showAsDropDown(view, 100, -view.getHeight());

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1);
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        popupWindow_layout.startAnimation(animationSet);
    }

    static class Handle {
        TextView tv_package_name;
        TextView tv_app_type;
        ImageView iv_icon;
    }

    class AppInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAppInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Handle handle = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.appinfo_item, null);
                handle = new Handle();
                handle.tv_package_name = (TextView) convertView.findViewById(R.id.tv_package_name);
                handle.tv_app_type = (TextView) convertView.findViewById(R.id.tv_app_type);
                handle.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                convertView.setTag(handle);
            } else {
                handle = (Handle) convertView.getTag();
            }
            AppInfo info = mAppInfoList.get(position);
            handle.tv_package_name.setText(info.packageName);
            if (info.isSystem) {
                handle.tv_app_type.setText("系统应用");
            } else {
                handle.tv_app_type.setText("用户应用");
            }
            handle.iv_icon.setBackground(info.icon);
            return convertView;
        }
    }
}
