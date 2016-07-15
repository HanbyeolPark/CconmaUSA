package com.cconmausa.cconmausa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPass;
    private Button btnLogin;
    private Button btnLogout;
    private Context context;
    String sfName = "LoginFile";

    final CookieManager cookieManager = CookieManager.getInstance();
    final String url = "http://www.cconmausa.com/member/login_handle.php";
    final String url2 = "http://www.cconmausa.com/";
    final HttpClient http = new DefaultHttpClient();

    private String name ;
    private String pass ;

    final ResponseHandler<String> responseHandler= new ResponseHandler<String>() {
        @Override
        public String handleResponse(HttpResponse response)  throws ClientProtocolException, IOException {
            String result = "", line;
            HttpEntity entity=response.getEntity();

            List<Cookie> cookies = ((DefaultHttpClient)http).getCookieStore().getCookies();
            if (!cookies.isEmpty()) {
                for (int i = 0; i < cookies.size(); i++) {
                    String cookieString = cookies.get(i).getName() + "=" + cookies.get(i).getValue();
                    cookieManager.setCookie(url2, cookieString);
                    Log.d("cookie", cookieString);
                }
            }
            CookieSyncManager.getInstance().sync();
            try {
                Thread.sleep(500);  //동기화 하는데 약간의 시간을 필요로 한다.
            } catch (InterruptedException e) {   }

            finish();
            return "";
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextName = (EditText) findViewById(R.id.id);
        editTextPass = (EditText) findViewById(R.id.pass);
        btnLogin = (Button)findViewById(R.id.login_button);
        btnLogout = (Button)findViewById(R.id.logout_button);
        context = this;
        CookieSyncManager.createInstance(this);

        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.getInstance().startSync();

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
// 제목셋팅
// AlertDialog 셋팅
        btnLogin.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                name = editTextName.getText().toString();
                pass = editTextPass.getText().toString();


                new Thread() {
                    @Override
                    public void run() {
                        try {
                            // 서버에 전달할 파라메터 세팅
                            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                            nameValuePairs.add(new BasicNameValuePair("id", name));
                            nameValuePairs.add(new BasicNameValuePair("passwd", pass));
                            HttpParams params = http.getParams();
                            HttpConnectionParams.setConnectionTimeout(params, 5000);
                            HttpConnectionParams.setSoTimeout(params, 5000);
                            HttpPost httpPost = new HttpPost(url);
                            UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                            httpPost.setEntity(entityRequest);
                            http.execute(httpPost,responseHandler);
                        }catch(Exception e){}
                    }
                }.start();
            }
        });
        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                CookieSyncManager.createInstance(context);
                CookieManager.getInstance().removeAllCookie();
                CookieSyncManager.getInstance().startSync();
                alertDialogBuilder
                        .setMessage("로그아웃되었습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 프로그램을 종료한다
                                        dialog.cancel();
                                        finish();
                                    }
                                })
                        .setNegativeButton("취소",
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
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPause() {
        super.onPause();
        if (CookieSyncManager.getInstance() != null) {
            CookieSyncManager.getInstance().stopSync();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sf = getSharedPreferences(sfName, 0);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("name", name);
        editor.putString("passwd", pass);
        editor.commit();
    }
}
