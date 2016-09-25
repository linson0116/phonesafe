package com.linson.phonesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.linson.phonesafe.R;
import com.linson.phonesafe.service.AlarmMusicService;
import com.linson.phonesafe.view.SettingItemView;

import java.io.IOException;
import java.io.InputStream;

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initSettingUI();
    }

    private void initSettingUI() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (siv_update.isChecked()) {
                    siv_update.setChecked(false);
                } else {
                    siv_update.setChecked(true);
                }
            }
        });
    }

    public void play(View view) {
//        MediaPlayer player = MediaPlayer.create(this, R.raw.ylzs);
//        player.setLooping(true);
//        player.start();
        Intent intent = new Intent(this, AlarmMusicService.class);
        startService(intent);


    }
}
