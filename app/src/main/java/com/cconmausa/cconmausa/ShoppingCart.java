package com.cconmausa.cconmausa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Hanbyeol on 2016-07-15.
 */

public class ShoppingCart extends AppCompatActivity {

    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);
        final String curURL = "https://cconmausa.myshopify.com/cart";

        mWebView = (WebView)findViewById(R.id.cart_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /*if (Uri.parse(url).getHost().equals(curURL)) {
                    return false;
                }
                PopUpFragment newView = new PopUpFragment();
                Bundle bundle = new Bundle();
                FragmentManager fragManag = getSupportFragmentManager();
                FragmentTransaction fragTran = fragManag.beginTransaction();

                bundle.putString("url", url);
                newView.setArguments(bundle);
                fragTran.setCustomAnimations(R.anim.anim_slide_in_from_right, R.anim.anim_hold, R.anim.anim_hold, R.anim.anim_slide_out_to_right);
                fragTran.replace(R.id.shopping_cart_layout, newView);
                fragTran.addToBackStack(null);
                fragTran.commit();
                // fragManag.popBackStack();
                fragManag.executePendingTransactions();

                view.loadUrl(curURL);
                return true;*/

                Intent intent1 = new Intent(ShoppingCart.this, PopUpWebview.class);
                intent1.putExtra("url",url);
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
                return true;
            }
        });
        mWebView.loadUrl(curURL);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_hold, R.anim.anim_slide_out_to_right);
    }
}
