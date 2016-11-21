package com.cconmausa.cconmausa;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


/**
 * Created by hanbyeolPark on 2016-11-13.
 */
public class WebViewInterface {

    private WebView mWebView;
    private Activity mContext;
    private final Handler handler = new Handler();

    public WebViewInterface(Activity activity, WebView view){
        mWebView = view;
        mContext = activity;
    }

    @JavascriptInterface
    public void callActivity(final String activityName){
        Intent intent;
        switch (activityName) {
            case "LOGIN": {
                Log.d("TEST", "before");
                intent = new Intent(mContext, Login.class);
                mContext.startActivity(intent);
                Log.d("TEST", "after");
                break;
            }
            case "CCONMA":{
                intent = new Intent(mContext, PopUpWebview.class);
                intent.putExtra("url", "https://cconmausa.myshopify.com/");
                mContext.startActivity(intent);
                break;
            }
            case "FOOD":{
                intent = new Intent(mContext, PopUpWebview.class);
                intent.putExtra("url", "https://cconmausa.myshopify.com/");
                mContext.startActivity(intent);
                break;
            }
            case "KITCHEN":{
                intent = new Intent(mContext, PopUpWebview.class);
                intent.putExtra("url", "https://cconmausa.myshopify.com/");
                mContext.startActivity(intent);
                break;
            }
            case "BEAUTY":{
                intent = new Intent(mContext, PopUpWebview.class);
                intent.putExtra("url", "https://cconmausa.myshopify.com/");
                mContext.startActivity(intent);
                break;
            }
            case "LIFE":{
                intent = new Intent(mContext, PopUpWebview.class);
                intent.putExtra("url", "https://cconmausa.myshopify.com/");
                mContext.startActivity(intent);
                break;
            }
            case "CULTURE":{
                intent = new Intent(mContext, PopUpWebview.class);
                intent.putExtra("url", "https://cconmausa.myshopify.com/");
                mContext.startActivity(intent);
                break;
            }
            case "SERVICE_CENTER":{
                intent = new Intent(mContext, PopUpWebview.class);
                intent.putExtra("url", "https://cconmausa.myshopify.com/");
                mContext.startActivity(intent);
                break;
            }
            case "CCONMA_KOREA":{
                intent = new Intent(mContext, PopUpWebview.class);
                intent.putExtra("url", "http://www.cconma.com/mobile/");
                mContext.startActivity(intent);
                break;
            }
            case "GODOWON":{
                String uri = "com.godowon.morningletter";
                intent = mContext.getPackageManager().getLaunchIntentForPackage(uri);
                if(intent == null) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + uri));
                }
                mContext.startActivity(intent);
                break;
            }

        }
    }

}
