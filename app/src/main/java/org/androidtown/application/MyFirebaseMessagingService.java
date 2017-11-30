package org.androidtown.application;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyMS";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived() 호출됨.");
        Map<String, String> resData = remoteMessage.getData();
        String store = resData.get("store");
        //String date = resData.get("date");
        String hour = resData.get("hour");
        String minute = resData.get("minute");
        sendToActivity(getApplicationContext(), store/*, date*/ , hour, minute);
    }

    private void sendToActivity(Context context, String store/*, String date*/ , String hour , String  minute) {
        Intent intent = new Intent(context, ReserveActivity.class);
        intent.putExtra("store", store);
        //intent.putExtra("date", date);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.food)
                .setContentTitle("Food Trip")
                .setContentText("\' "+store+" \'음식점 예약 알림이 왔습니다.")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
/*
        intent.putExtra("store", store);
        intent.putExtra("date", date);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);*/
    }

}