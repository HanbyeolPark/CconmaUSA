package com.cconmausa.cconmausa;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Hanbyeol on 2016-07-21.
 */
public class PopUpFragment extends Fragment {

    WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_pop_up, container, false);
            mWebView = (WebView) view.findViewById(R.id.pop_up_frag_webview);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            mWebView.getSettings().setLoadWithOverviewMode(true);//웹뷰에 딱맞게 페이지 크기 조절
            mWebView.getSettings().setUseWideViewPort(true);//웹뷰에 딱맞게 페이지 크기 조절

            final Bundle extra = getArguments();
            final String curURL = extra.getString("url");
            mWebView.loadUrl(curURL);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (Uri.parse(url).getHost().equals(curURL)) {
                        return false;
                    }
                    PopUpFragment newView = new PopUpFragment();
                    Bundle bundle = new Bundle();
                    FragmentManager fragManag = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragTran = fragManag.beginTransaction();

                    bundle.putString("url", url);
                    newView.setArguments(bundle);
                    fragTran.setCustomAnimations(R.anim.anim_slide_in_from_right, R.anim.anim_hold, R.anim.anim_hold, R.anim.anim_slide_out_to_right);
                    fragTran.replace(R.id.pop_up_frag_layout, newView);
                    fragTran.addToBackStack(null);
                    fragTran.commit();
                    // fragManag.popBackStack();
                    fragManag.executePendingTransactions();

                    view.loadUrl(curURL);
                    return true;
                }
            });

        return view;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}