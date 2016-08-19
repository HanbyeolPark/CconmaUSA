package com.cconmausa.cconmausa;

/**
 * Created by Hanbyeol on 2016-08-19.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class GcmIntentService extends GCMBaseIntentService {

    @Override
    protected void onError(Context arg0, String arg1) {
        Log.i(TAG, "Received error: ");
    }

    @Override
    protected void onMessage(Context context, Intent intent) { //실질적으로 GCM에서 메시지를 받는 부분

        String msg = intent.getStringExtra("msg");
        Log.e("getMessage", "getMessage:" + msg);
        //generateNotification(context,msg);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        try {
            msg = URLDecoder.decode(msg, "EUC-KR"); //decoding
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher) //푸쉬 알림 아이콘
                .setContentTitle("꽃마USA 푸시 알림") //푸쉬 알림 제목
                .setContentText(msg) //푸쉬 알림 내용
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE) //소리, 진동 효과
                .setTicker("꽃마USA 푸시 - 성공했어요!"); //처음에 상단바에 잠깐 뜰 때 보이는 내용
        Notification notification = builder.build();
        nm.notify(1234, notification);
    }

    @Override
    protected void onRegistered(Context context, String reg_id) {
        Log.e("GCM INTENTSERVICE", "키등록 : " + reg_id);
    }

    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        Log.e("(GCM INTENTSERVICE","키제거 : 제거되었습니다.");
    }

}
