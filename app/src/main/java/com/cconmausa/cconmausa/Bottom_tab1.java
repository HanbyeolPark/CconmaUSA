package com.cconmausa.cconmausa;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

public class Bottom_tab1 extends Fragment {
    Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_tab1, container, false);
        ViewPager viewPager = (ViewPager) (view.findViewById(R.id.fragment_bottom_tab1_viewpager));
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        viewPager.setOffscreenPageLimit(6);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.fragment_bottom_tab1_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        Vector<Band_menu> vector = new Vector<Band_menu>(5);
        adapter = new Adapter(getFragmentManager());
        Bundle extra = getArguments();
        String[] url = extra.getStringArray("url");
        String[] title = extra.getStringArray("title");

        for(int i=0; i<url.length;i++){
            Bundle bundle = new Bundle();
            bundle.putString("url", url[i]);
            vector.addElement(new Band_menu());
            vector.get(i).setArguments(bundle);
            adapter.addFragment(vector.get(i), title[i]);
        }
        viewPager.setAdapter(adapter);
    }
}
