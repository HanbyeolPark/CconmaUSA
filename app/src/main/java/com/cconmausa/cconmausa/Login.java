package com.cconmausa.cconmausa;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by hanbyeolPark on 2016-11-14.
 */
public class Login extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextPass;
    private Button btnLogin;
    private Button btnLogout;
    private Context context;
    private String result;
    final CookieManager cookieManager = CookieManager.getInstance();
    //final String url = "https://cconmausa.myshopify.com/account/login";
    //final String url2 = "https://cconmausa.myshopify.com/";
    final String url = "";
    final String url2 = "";
    final HttpClient http = new DefaultHttpClient();
    private String name ;
    private String pass ;
    HttpConnection httpConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextName = (EditText) findViewById(R.id.id);
        editTextPass = (EditText) findViewById(R.id.pass);
        btnLogin = (Button)findViewById(R.id.login_button);
        btnLogout = (Button)findViewById(R.id.logout_button);
        context = this;


        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                this);

// 제목셋팅


// AlertDialog 셋팅
        btnLogin.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                name = editTextName.getText().toString();
                pass = editTextPass.getText().toString();
                Log.d("click_name", name);
                Log.d("click_pass", pass);
                httpConnection = new HttpConnection(name, pass, url, url2);
                new Thread() {
                    @Override
                    public void run() {
                        httpConnection.setLogin();
                    }
                }.start();
                CookieSyncManager.getInstance().sync();
                try {
                    Thread.sleep(500);  //동기화 하는데 약간의 시간을 필요로 한다.
                } catch (InterruptedException e) {
                }
                finish();

            }
        });
        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                CookieSyncManager.createInstance(context);

                CookieManager.getInstance().removeAllCookie();
                CookieSyncManager.getInstance().startSync();

                httpConnection = new HttpConnection();
                new Thread() {
                    @Override
                    public void run() {
                        httpConnection.setLogin2();
                    }
                }.start();
                CookieSyncManager.getInstance().sync();
                try {
                    Thread.sleep(500);  //동기화 하는데 약간의 시간을 필요로 한다.
                } catch (InterruptedException e) {
                }
                finish();


                alertDialogBuilder
                        .setMessage("로그아웃되었습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 프로그램을 종료한다
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("취소",
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
        });

    }
}
