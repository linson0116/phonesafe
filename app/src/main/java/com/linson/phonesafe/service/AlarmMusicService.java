package com.linson.phonesafe.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.linson.phonesafe.R;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class AlarmMusicService extends Service {
    MediaPlayer player;
    public AlarmMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
     return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player = MediaPlayer.create(getApplicationContext(), R.raw.ylzs);

        player.setLooping(true);
        player.start();
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                player.reset();
                player.start();
                Log.i(TAG, "onError: ");
                return true;
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: music service destory");
        super.onDestroy();
    }
}
