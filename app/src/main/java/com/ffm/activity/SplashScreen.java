package com.ffm.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.hug.mma.constants.IntentConstants;
import com.hug.mma.util.AppUtil;
import com.hug.mma.util.Trace;

import androidx.annotation.RequiresApi;

public class SplashScreen extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel connectionChannel = new NotificationChannel(IntentConstants.CONNECTION_STATUS_CHANNEL_ID,
                IntentConstants.CONNECTION_STATUS_CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN);
        connectionChannel.enableLights(false);
        connectionChannel.enableVibration(false);
        connectionChannel.setShowBadge(false);
        connectionChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(connectionChannel);

    }

    @Override
    protected void onResume() {
        super.onResume();
        goNext();
    }

    private void goNext() {
        Trace.i("nexts");
        AppUtil.dashboard(this);
    }


}
