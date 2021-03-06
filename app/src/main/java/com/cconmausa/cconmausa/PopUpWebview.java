package com.cconmausa.cconmausa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PopUpWebview extends AppCompatActivity {

    WebView mWebView;
    String curURL="";
    private ProgressBar progress;
    private WebSettings mWebSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.popup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mWebView.destroy();
                overridePendingTransition(R.anim.anim_hold, R.anim.anim_slide_out_to_right);
            }
        });

        mWebView = (WebView) findViewById(R.id.popup_webview);
        mWebSettings = mWebView.getSettings();

        progress = (ProgressBar) findViewById(R.id.popup_progress);
        String userAgent2 = mWebSettings.getUserAgentString();
        Log.d("userAgent2", userAgent2);
        //mWebSettings.setBuiltInZoomControls(true);
        //mWebSettings.setSupportZoom(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSaveFormData(false);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        final Context myApp = this;

        mWebView.setWebChromeClient(new CustomWebChromeClient(this));

        mWebView.setWebViewClient(new WebViewClient() {
                                      @Override
                                      public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                          Log.d("현재URL", curURL);
                                          Log.d("로드될URL",url);

                                          if(url.startsWith("http://www.cconma.com/mobile/index.pmv")){
                                              Intent clearIntent = new Intent(myApp, MainActivity.class);
                                              clearIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                              startActivity(clearIntent);
                                              overridePendingTransition(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_to_right);
                                              Log.d("TEST", "CLEAR INTENT");
                                              return true;
                                          }

                                          if(url.equalsIgnoreCase(curURL)){
                                              //return super.shouldOverrideUrlLoading(view,url);
                                              mWebView.reload();
                                          }

                                          if(curURL.startsWith("https://checkout.shopify.com/")){
                                              if(url.startsWith("https://checkout.shopify.com/")){
                                                  return super.shouldOverrideUrlLoading(view,url);
                                              }
                                              else{

                                                  Intent intent1;
                                                  if(url.contains("product/?pcode")) {
                                                      intent1 = new Intent(myApp, PopUpWebview_product.class);
                                                  }else{
                                                      intent1 = new Intent(myApp, PopUpWebview.class);
                                                  }
                                                  intent1.putExtra("url", url);
                                                  startActivity(intent1);
                                                  overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
                                                  return true;
                                              }

                                          }
                                          else if(curURL.replace("/","").equalsIgnoreCase(url.replace("/",""))||curURL.equalsIgnoreCase(url)||url.equalsIgnoreCase("https://cconmausa.myshopify.com/")) {
                                              return super.shouldOverrideUrlLoading(view, url);
                                          }else if(!curURL.contains("m/store")&&url.contains("m/store")){
                                              return super.shouldOverrideUrlLoading(view, url);
                                          }
                                          else{
                                              //activity로 띄우기


                                              Intent intent1;
                                              if(url.contains("product/?pcode")) {
                                                  intent1 = new Intent(myApp, PopUpWebview_product.class);
                                              }else{
                                                  intent1 = new Intent(myApp, PopUpWebview.class);
                                              }
                                              intent1.putExtra("url", url);
                                              Log.d("새창", url);
                                              startActivity(intent1);
                                              overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
                                              return true;
                                          }

                                      }

                                      public void onPageStarted(WebView view, String url,
                                                                android.graphics.Bitmap favicon) {
                                          super.onPageStarted(view, url, favicon); //페이지 로딩 시작
                                          progress.setVisibility(View.VISIBLE);
                                      }

                                      public void onPageFinished(WebView view, String url) { //페이지 로딩 완료
                                          super.onPageFinished(view, url);
                                          progress.setVisibility(View.GONE);
                                      }

                                      public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                          super.onReceivedError(view, errorCode, description, failingUrl);
                                          Log.d("LOG", "1stop loading");
                                          switch (errorCode) {
                                              case ERROR_AUTHENTICATION:               // 서버에서 사용자 인증 실패
                                                  Toast.makeText(myApp, "사용자인증실패", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_BAD_URL:                            // 잘못된 URL
                                                  Toast.makeText(myApp, "잘못된URL", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_CONNECT:                           // 서버로 연결 실패
                                                  Toast.makeText(myApp, "서버로 연결 실패", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_FAILED_SSL_HANDSHAKE:     // SSL handshake 수행 실패
                                                  Toast.makeText(myApp, " SSL handshake 수행 실패", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_FILE:                                   // 일반 파일 오류
                                                  Toast.makeText(myApp, "일반 파일 오류", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_FILE_NOT_FOUND:                // 파일을 찾을 수 없습니다
                                                  Toast.makeText(myApp, "파일을 찾을 수 없습니다", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_HOST_LOOKUP:            // 서버 또는 프록시 호스트 이름 조회 실패
                                                  Toast.makeText(myApp, "서버 또는 프록시 호스트 이름 조회 실패", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_IO:                               // 서버에서 읽거나 서버로 쓰기 실패
                                                  Toast.makeText(myApp, "서버에서 읽거나 서버로 쓰기 실패", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_PROXY_AUTHENTICATION:    // 프록시에서 사용자 인증 실패
                                                  Toast.makeText(myApp, "프록시에서 사용자 인증 실패", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_REDIRECT_LOOP:                // 너무 많은 리디렉션
                                                  Toast.makeText(myApp, " 너무 많은 리디렉션", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_TIMEOUT:                          // 연결 시간 초과
                                                  Toast.makeText(myApp, "연결 시간 초과", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_TOO_MANY_REQUESTS:            // 페이지 로드중 너무 많은 요청 발생
                                                  Toast.makeText(myApp, "페이지 로드중 너무 많은 요청 발생", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_UNKNOWN:                         // 일반 오류
                                                  Toast.makeText(myApp, "일반 오류", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_UNSUPPORTED_AUTH_SCHEME:  // 지원되지 않는 인증 체계
                                                  Toast.makeText(myApp, "지원되지 않는 인증 체계", Toast.LENGTH_LONG).show();
                                                  break;
                                              case ERROR_UNSUPPORTED_SCHEME:
                                                  Toast.makeText(myApp, "ERROR_UNSUPPORTED_SCHEME", Toast.LENGTH_LONG).show();
                                                  break;
                                          }
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
                                                  default:final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myApp);
                                                      alertDialogBuilder
                                                              .setMessage("네트워크 장애로 인해 앱을 실행할 수 없습니다.")
                                                              .setCancelable(false)
                                                              .setPositiveButton("다시시도",
                                                                      new DialogInterface.OnClickListener() {
                                                                          public void onClick(
                                                                                  DialogInterface dialog, int id) {
                                                                              // 프로그램을 종료한다
                                                                              dialog.cancel();
                                                                              recreate();
                                                                          }
                                                                      })
                                                              .setNegativeButton("종료",
                                                                      new DialogInterface.OnClickListener() {
                                                                          public void onClick(
                                                                                  DialogInterface dialog, int id) {
                                                                              // 다이얼로그를 취소한다
                                                                              dialog.cancel();
                                                                              finish();
                                                                          }
                                                                      });
                                                      AlertDialog alertDialog = alertDialogBuilder.create();
                                                      // 다이얼로그 보여주기
                                                      alertDialog.show();
                                          }


                                          Log.d("LOG", "11stop loading");
                                          mWebView.stopLoading();
                                          setContentView(R.layout.view_no_page);

                                          Log.d("LOG", "stop loading");
                                      }
                                  }
        );

        Intent intent = getIntent();
        curURL = intent.getStringExtra("url");
        mWebView.loadUrl(curURL);
    }

    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void finish() {
        super.finish();
        mWebView.destroy();
        overridePendingTransition(R.anim.anim_hold, R.anim.anim_slide_out_to_right);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_basket, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        Intent intent;
//
//        if (id == R.id.shopping_basket) {
//            intent = new Intent(this, ShoppingCart.class);
//            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//            overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
