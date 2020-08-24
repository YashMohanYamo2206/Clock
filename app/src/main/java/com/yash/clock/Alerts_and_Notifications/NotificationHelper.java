package com.yash.clock.Alerts_and_Notifications;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.yash.clock.Activities.after_notification_tap;
import com.yash.clock.R;

import static androidx.core.app.NotificationCompat.DEFAULT_VIBRATE;
import static androidx.core.app.NotificationCompat.FLAG_SHOW_LIGHTS;


public class NotificationHelper extends ContextWrapper {
    public static  String channelID = "ALARM";
    public static  String channelName = "ALARM";
    private NotificationManager mManager;
    Intent activityIntent = new Intent(this, after_notification_tap.class);
    PendingIntent contentIntent = PendingIntent.getActivity(this,
            0, activityIntent, 0);
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setSound(uri,attributes);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Alarm!")
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .setContentText("Tap To Cancel")
                .setSmallIcon(R.drawable.ic_baseline_play_arrow_24)
                .setDefaults(DEFAULT_VIBRATE | FLAG_SHOW_LIGHTS);
    }
}