package org.androidtown.application;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
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
        String data = resData.get("data");
        sendToActivity(getApplicationContext(), data);
    }

    private void sendToActivity(Context context, String data) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("data", data);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }

}