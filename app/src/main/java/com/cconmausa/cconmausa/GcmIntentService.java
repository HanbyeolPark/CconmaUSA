package com.cconmausa.cconmausa;

/**
 * Created by Hanbyeol on 2016-08-19.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import org.apache.http.client.fluent.Content;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class GcmIntentService extends GCMBaseIntentService {

    @Override
    protected void onError(Context arg0, String arg1) {
        Log.i(TAG, "Received error: ");
    }

    @Override
    protected void onMessage(Context context, Intent intent) { //실질적으로 GCM에서 메시지를 받는 부분

       // String msg = intent.getStringExtra("msg");
        //Log.e("getMessage", "getMessage:" + msg);
        //generateNotification(context,msg);

        String title = intent.getStringExtra("title");
       // String body = intent.getStringExtra("body");
        String imageUrl = intent.getStringExtra("imageUrl");
        String proUrl = intent.getStringExtra("proUrl");

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class); //클릭했을 때 띄울 부분
        notificationIntent.putExtra("push_url", proUrl);
        //Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(proUrl));
        //Intent notificationIntent = new Intent(context, PopUpWebview_product.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        try {
            title = URLDecoder.decode(title, "EUC-KR"); //decoding
           // body = URLDecoder.decode(body, "EUC-KR");
           // imageUrl = URLDecoder.decode(imageUrl, "EUC-KR");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        try{
            URL url = new URL(imageUrl);
            Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            NotificationCompat.BigPictureStyle notificationStyle = new NotificationCompat.BigPictureStyle();
            notificationStyle.setBigContentTitle(title);
            notificationStyle.bigPicture(bigPicture);

            BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.push);
            Bitmap bitmap = drawable.getBitmap();

            notification
                    .setAutoCancel(true)
                    .setContentTitle(title)
                   // .setContentText(body)
                    .setTicker("YOU'VE GOT NEWS~")
                    .setStyle(notificationStyle)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.push)
                    .setLargeIcon(bitmap)
                    .build();

            nm.notify(1234, notification.build());
            if(bigPicture != null && !bigPicture.isRecycled()){
                bigPicture.recycle();
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }



       /* Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher) //푸쉬 알림 아이콘
                .setContentTitle(title) //푸쉬 알림 제목
                .setContentText(body) //푸쉬 알림 내용
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE) //소리, 진동 효과
                .setTicker("꽃마USA 푸시 - 성공했어요!"); //처음에 상단바에 잠깐 뜰 때 보이는 내용
        Notification notification = builder.build();
        nm.notify(1234, notification);*/
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

//original
/*public class GcmIntentService extends GCMBaseIntentService {

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
        Intent notificationIntent = new Intent(context, MainActivity.class); //클릭했을 때 띄울 부분
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

}*/