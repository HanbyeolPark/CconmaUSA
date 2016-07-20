package com.cconmausa.cconmausa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    TabLayout tabLayout_bottom;
    Adapter adapter;
    CustomViewPager viewPager;
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
        viewPager = (CustomViewPager) findViewById(R.id.custom_viewpager);

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        Toast.makeText(context, NetworkUtil.getConnectivityStatusString(context), Toast.LENGTH_LONG).show();
        if(!NetworkUtil.possible){
            alertDialogBuilder
                    .setMessage("네트워크 장애로 인해 앱을 실행할 수 없습니다.")
                    .setCancelable(false)
                    .setPositiveButton("다시 시도",
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
    @Override
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

            fragTran.setCustomAnimations(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
            fragTran.replace(R.id.main_layout, test);
            fragTran.addToBackStack(null);
            fragTran.commit();

            fragTran.hide(frag1);
            fragTran.hide(frag2);
            fragTran.hide(frag3);
            fragTran.hide(frag4);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ReadJSONFeed extends AsyncTask<String, String, String> {
        protected void onPreExecute() {}
        Vector<String> vector = new Vector<String>(3);
        Vector<String> vector2 = new Vector<String>(3);
        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            HttpPost httppost = new HttpPost(urls[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
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

        protected void onPostExecute(String result) {
            String stateInfo="";

            try{
                JSONObject object = new JSONObject(result);
                JSONArray countriesArray = new JSONArray(object.getString("band_menu"));
                tabLayout_bottom = (TabLayout) findViewById(R.id.main_tabs_bottom);

                for (int i =0 ; i<countriesArray.length();i++) {
                    JSONObject jObject = countriesArray.getJSONObject(i);
                    stateInfo+="Title: "+jObject.getString("title")+"\n";
                    stateInfo+="Url: "+jObject.getString("url")+"\n";
                    vector.addElement(jObject.getString("title"));
                    vector2.addElement(jObject.getString("url"));
                }
                setupViewPager(viewPager);
                viewPager.setOffscreenPageLimit(6);
                tabLayout_bottom.setupWithViewPager(viewPager);

            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
        private void TabLayoutBottomEvent() {
            tabLayout_bottom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    setCurrentTabFragment(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) { }

                @Override
                public void onTabReselected(TabLayout.Tab tab) { }
            });
        }

        private void setCurrentTabFragment(int tabPosition) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Bundle bundle = new Bundle();

            String[] title = new String[vector.size()];
            title = (String[])vector.toArray(title);
            String[] url = new String[vector2.size()];
            url = (String[])vector2.toArray(url);

            bundle.putStringArray("title",title);
            bundle.putStringArray("url",url);
            frag1.setArguments(bundle);

            switch (tabPosition) {
                case 0 :
                    ft.replace(R.id.tab_main, frag1);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    break;
                case 1 :
                    ft.replace(R.id.tab_main, frag2);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    break;
                case 2 :
                    ft.replace(R.id.tab_main, frag3);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    break;
                case 3 :
                    ft.replace(R.id.tab_main, frag4);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    break;
                default:
                    break;
            }
        }
        private void setupViewPager(ViewPager viewPager) {
            //get information about tabs from server
            //make Fragments as number of categories

            adapter = new Adapter(getSupportFragmentManager());
            frag1 = new Bottom_tab1();
            Bundle bundle = new Bundle();

            String[] title = new String[vector.size()];
            title = (String[])vector.toArray(title);
            String[] url = new String[vector2.size()];
            url = (String[])vector2.toArray(url);

            bundle.putStringArray("title",title);
            bundle.putStringArray("url",url);
            frag1.setArguments(bundle);
            Bottom_tab2 frag2 = new Bottom_tab2();
            Bottom_tab3 frag3 = new Bottom_tab3();
            Bottom_tab4 frag4 = new Bottom_tab4();
            adapter.addFragment(frag1, "1");
            adapter.addFragment(frag2, "2");
            adapter.addFragment(frag3, "3");
            adapter.addFragment(frag4, "4");

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
            intent = new Intent(this, Shopping_basket.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
