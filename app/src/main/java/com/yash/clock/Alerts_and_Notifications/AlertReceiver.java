package com.yash.clock.Alerts_and_Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.media.MediaPlayer;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.yash.clock.R;


public class AlertReceiver extends BroadcastReceiver {
    public static MediaPlayer mediaPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {
        int mpID = intent.getIntExtra("mp_ID", R.raw.alarm);
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context,mpID);
            mediaPlayer.start();
        }
        Toast.makeText(context, "alarm", Toast.LENGTH_SHORT).show();
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
    }
}
