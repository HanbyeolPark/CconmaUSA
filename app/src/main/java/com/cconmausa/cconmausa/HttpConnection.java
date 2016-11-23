package com.cconmausa.cconmausa;

import android.util.Log;
import android.webkit.CookieManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymg on 2016-07-20.
 */
public class HttpConnection  {
    String name;
    String pass;
    String url;
    String dest_url;
    HttpClient http = new DefaultHttpClient();
    CookieManager cookieManager = CookieManager.getInstance();
    HttpConnection(){
        this.name = "";
        this.pass = "";
        this.url = "";
        this.dest_url = "";

    }
    HttpConnection(String name,String pass, String url, String dest_url){
        this.name = name;
        this.pass = pass;
        this.url = url;
        this.dest_url = dest_url;

    }


    public String getContent(String url){

        StringBuilder builder = new StringBuilder();
        HttpPost httppost = new HttpPost(url);
        try {
            HttpResponse response = http.execute(httppost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    ResponseHandler<String> responseHandler= new ResponseHandler<String>() {
        @Override
        public String handleResponse(HttpResponse response)  throws ClientProtocolException, IOException {
            String result = "", line;
            HttpEntity entity=response.getEntity();


            List<Cookie> cookies = ((DefaultHttpClient)http).getCookieStore().getCookies();
            if (!cookies.isEmpty()) {
                for (int i = 0; i < cookies.size(); i++) {
                    String cookieString = cookies.get(i).getName() + "=" + cookies.get(i).getValue();
                    CookieManager.getInstance().setCookie(dest_url, cookieString);
                    Log.d("쿠키값", CookieManager.getInstance().getCookie(dest_url));
                }
            }



            return "";
        }
    };
    public  void setLogin(){
        try {
            this.dest_url="https://cconmausa.myshopify.com/";
            this.url = "https://cconmausa.myshopify.com/account/login";

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            Log.d(name, pass);
            nameValuePairs.add(new BasicNameValuePair("customer[email]", name));
            nameValuePairs.add(new BasicNameValuePair("customer[password]", pass));
            HttpParams params = http.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpPost httpPost = new HttpPost(url);
            UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            httpPost.setEntity(entityRequest);
            http.execute(httpPost, responseHandler);
        }catch(Exception e){}
    }

    public  void setLogin2(){
        try {

            this.dest_url="https://cconmausa.myshopify.com/";
            this.url = "https://cconmausa.myshopify.com/password";

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("password", "cconma16"));
            HttpParams params = http.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpPost httpPost = new HttpPost(url);
            UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            httpPost.setEntity(entityRequest);
            http.execute(httpPost,responseHandler);
        }catch(Exception e){}
    }

}


