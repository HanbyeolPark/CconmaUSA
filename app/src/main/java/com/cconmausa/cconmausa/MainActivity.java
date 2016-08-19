package com.cconmausa.cconmausa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer;
    TabLayout tabLayout;
    Adapter adapter;
    ViewPager viewPager;
    Context context;

    Bottom_tab1 frag1 = new Bottom_tab1();
    Bottom_tab2 frag2 = new Bottom_tab2();
    Bottom_tab3 frag3 = new Bottom_tab3();
    Bottom_tab4 frag4 = new Bottom_tab4();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
       // CLoading.showLoading(context);
        viewPager = (ViewPager) findViewById(R.id.fragment_bottom_tab1_viewpager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.mipmap.cconma_logo);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        WebView mWebView;
        WebSettings mWebSettings;
        ProgressBar progress;
        mWebView = (WebView) findViewById(R.id.popup_webview);
        mWebSettings = mWebView.getSettings();

        progress = (ProgressBar) findViewById(R.id.web_progress);
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

        mWebView.loadUrl("http://se.naver.com");
        registerGcm();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        Toast.makeText(context, NetworkUtil.getConnectivityStatusString(context), Toast.LENGTH_LONG).show();
        if(!NetworkUtil.possible){
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
        new ReadJSONFeed().execute("http://itaxi.handong.edu/init.php");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    /*@Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_home) {
            //intent = new Intent(this, MainActivity.class);
            // startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_login) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
        } else if(id == R.id.nav_fragTest){
            Bottom_tab3 test = new Bottom_tab3();

            fragTran.setCustomAnimations(R.anim.anim_slide_in_from_right, R.anim.anim_hold, R.anim.anim_hold, R.anim.anim_slide_out_to_right);
            fragTran.replace(R.id.main_layout, test);
            fragTran.addToBackStack(null);
            fragTran.commit();

//            fragTran.hide(frag1);
//            fragTran.hide(frag2);
//            fragTran.hide(frag3);
//            fragTran.hide(frag4);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    private class ReadJSONFeed extends AsyncTask<String, String, String> {
        protected void onPreExecute() {}
        Vector<String> vector = new Vector<String>(3);
        Vector<String> vector2 = new Vector<String>(3);
        @Override
        protected String doInBackground(String... urls) {
            HttpConnection httpConnection = new HttpConnection();
            return httpConnection.getContent(urls[0]);
        }

        protected void onPostExecute(String result) {
            String stateInfo="";
            Bundle bundle = new Bundle();

            try{
                JSONObject object = new JSONObject(result);
                JSONArray countriesArray = new JSONArray(object.getString("band_menu"));
                tabLayout = (TabLayout) findViewById(R.id.fragment_bottom_tab1_tabs);

                Vector<Band_menu> bandmenuvector = new Vector<Band_menu>(5);
                adapter = new Adapter(getSupportFragmentManager());

                for (int i =0; i<countriesArray.length();i++) {
                    JSONObject jObject = countriesArray.getJSONObject(i);
                    stateInfo += "Title: "+jObject.getString("title")+"\n";
                    stateInfo += "Url: "+jObject.getString("url")+"\n";

                    vector.addElement(jObject.getString("title"));
                    vector2.addElement(jObject.getString("url"));

//                    JSONObject jObject = countriesArray.getJSONObject(i);
//
//                    vector.addElement(jObject.getString("title"));
//                    vector2.addElement(jObject.getString("url"));
//
//                    String[] title = new String[vector.size()];
//                    title = (String[]) vector.toArray(title);
//                    String[] url = new String[vector2.size()];
//                    url = (String[])vector2.toArray(url);
//
//                    bundle.putStringArray("title", title);
//                    bundle.putStringArray("url", url);
//
//                    bandmenuvector.addElement(new Band_menu());
//                    bandmenuvector.get(i).setArguments(bundle);
//                    adapter.addFragment(bandmenuvector.get(i), jObject.getString("title"));
                }

                viewPager.setAdapter(adapter);
                setupViewPager(viewPager);
                viewPager.setOffscreenPageLimit(6);
                tabLayout.setupWithViewPager(viewPager);

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void setupViewPager(ViewPager viewPager) {
            //get information about tabs from server
            //make Fragments as number of categories



            Vector<Band_menu> band_menus = new Vector<Band_menu>(10);

            String[] title = new String[vector.size()];
            title = (String[])vector.toArray(title);
            String[] url = new String[vector2.size()];
            url = (String[])vector2.toArray(url);

            for(int i=0; i<url.length;i++){
                Bundle bundle = new Bundle();
                bundle.putString("url", url[i]);
                band_menus.addElement(new Band_menu());
                band_menus.get(i).setArguments(bundle);
                adapter.addFragment(band_menus.get(i), title[i]);
            }
            viewPager.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_basket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.shopping_basket) {
            intent = new Intent(this, ShoppingCart.class);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void registerGcm() {
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        final String regId = GCMRegistrar.getRegistrationId(this);
        Log.d("TEST", "GCM registered, id= " + regId);

        if (regId.equals("")) {
            GCMRegistrar.register(this, "252553880865"); //google project number
            Log.d("TEST", "GCM registered, id= " + regId);

        } else {
            Log.d("TEST", "GCM already registered, id= " + regId);
        }
    }
}
