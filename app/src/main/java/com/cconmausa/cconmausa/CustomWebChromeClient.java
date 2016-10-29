package com.cconmausa.cconmausa;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Tae-hwan on 8/4/16.
 */

public class CustomWebChromeClient extends WebChromeClient {
    private Activity activity;

    public CustomWebChromeClient(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, final String message, final JsResult result) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        }).setCancelable(false).create();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                result.cancel();
            }
        });
        if (activity.isFinishing()) {
            result.confirm();
        } else {

            //if(message.split("login")[0].equalsIgnoreCase("cconmausa://")){
            //if(message.split("logout")[0].equalsIgnoreCase("cconmausa://")){

            if(message.startsWith("cconmausa://")){
                if(message.contains("login")){
                    result.cancel();
                    Intent intent = new Intent(this.activity, LoginActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
                } else if(message.contains("logout")){
                    result.cancel();
                    Intent intent = new Intent(this.activity, LoginActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
                }
            }

            if(message.split(" ")[0].equalsIgnoreCase("학번")){ //tested in iTaxi
                result.cancel();
                Intent intent = new Intent(this.activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);

                return true;
            }

            if(message.split(" ")[0].equalsIgnoreCase("회원정보가")){ //tested in cconmaUSA
                result.cancel();
                Intent intent = new Intent(this.activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);

                return true;
            }

            alertDialog.show();
        }
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, final String message, final JsResult result) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setMessage(message).
                setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        }).setCancelable(false).create();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                result.cancel();
            }
        });

        if (activity.isFinishing()) {
            result.cancel();
        } else {

            if(message.split(" ")[0].equalsIgnoreCase("회원정보가")){
                result.cancel();
                Intent intent = new Intent(this.activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);

                return true;
            }

            alertDialog.show();
        }
        return true;
    }
}
