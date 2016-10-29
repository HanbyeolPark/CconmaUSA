package com.cconmausa.cconmausa;

import android.content.DialogInterface;
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
import android.widget.Toast;

public class Bottom_tab2 extends Fragment {
    private String curURL;
    private WebView mWebView;
    private WebSettings mWebSettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_tab2, container, false);
        mWebView = (WebView) view.findViewById(R.id.frag2_webview);
        //curURL = "file:///android_asset/webView1.html";
        curURL="http://www.naver.com";

        mWebSettings = mWebView.getSettings();
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSaveFormData(false);
        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        mWebView.setWebViewClient(new MyWebClient());
        mWebSettings.setJavaScriptEnabled(true);
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

       /* public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            switch (errorCode) {
                case ERROR_AUTHENTICATION:               // 서버에서 사용자 인증 실패
                case ERROR_BAD_URL:                            // 잘못된 URL
                case ERROR_CONNECT:                           // 서버로 연결 실패
                case ERROR_FAILED_SSL_HANDSHAKE:     // SSL handshake 수행 실패
                case ERROR_FILE:                                   // 일반 파일 오류
                case ERROR_FILE_NOT_FOUND:                // 파일을 찾을 수 없습니다
                case ERROR_HOST_LOOKUP:            // 서버 또는 프록시 호스트 이름 조회 실패
                case ERROR_IO:                               // 서버에서 읽거나 서버로 쓰기 실패
                case ERROR_PROXY_AUTHENTICATION:    // 프록시에서 사용자 인증 실패
                case ERROR_REDIRECT_LOOP:                // 너무 많은 리디렉션
                case ERROR_TIMEOUT:                          // 연결 시간 초과
                case ERROR_TOO_MANY_REQUESTS:            // 페이지 로드중 너무 많은 요청 발생
                case ERROR_UNKNOWN:                         // 일반 오류
                case ERROR_UNSUPPORTED_AUTH_SCHEME:  // 지원되지 않는 인증 체계
                case ERROR_UNSUPPORTED_SCHEME:
                    final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getContext());
                    alertDialogBuilder
                            .setMessage("ERROR")
                            .setCancelable(false)
                            .setPositiveButton("종료",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // 프로그램을 종료한다
                                            dialog.cancel();
                                        }
                                    })
                            .setNegativeButton("다시시도",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // 다이얼로그를 취소한다
                                            dialog.cancel();
                                        }
                                    });
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    // 다이얼로그 보여주기
                    alertDialog.show();
            }
        }*/
    }

}
