package com.cconmausa.cconmausa;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Bottom_tab2 extends Fragment {
    private String curURL;
    private WebView mWebView;
    private WebSettings mWebSettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_tab2, container, false);
        mWebView = (WebView) view.findViewById(R.id.frag2_webview);
        curURL = "file:///android_asset/webView1.html";

        mWebSettings = mWebView.getSettings();
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSaveFormData(false);
        //mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //mWebView.setWebChromeClient(new webViewChrome());

        //if(savedInstanceState == null){
        mWebView.setWebViewClient(new MyWebClient());
        mWebSettings.setJavaScriptEnabled(true);
        // } else {
        // mWebView.restoreState(savedInstanceState);
        //}
       // mWebView.addJavascriptInterface(new AndroidBridge(), "Fragment2_androidBridge");
        mWebView.loadUrl(curURL);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    public class MyWebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            Log.d("URL_CATCH", url);

            return super.shouldOverrideUrlLoading(view, url);
            //return true;
        }
    }

//    private class AndroidBridge {
//        @JavascriptInterface
//        public void changeView(final String url) {
//            //set new fragment.
//            Log.d("in activity", "test() is working");
//
//            WebViewFragment change = new WebViewFragment(url);
//            FragmentManager fm = getActivity().getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.addToBackStack(null);
//            ft.replace(R.id.frag2_replace, change);
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ft.commit();
//
//            //change in current mode- not working
//            //curURL = "file:///android_asset/webView2.html";
//            // mWebView.loadUrl(curURL);
//
//            //new activity
//            //Intent intent = new Intent(getActivity(), FragTestActivity.class);
//            //startActivity(intent);
//
//        }
//    }
}
