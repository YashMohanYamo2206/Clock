package com.yash.clock.Activities;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.yash.clock.Fragments.alarm;
import com.yash.clock.Fragments.page_adapter;
import com.yash.clock.Fragments.stopWatch;
import com.yash.clock.Fragments.timer;
import com.yash.clock.R;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //pagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public void setUpViewPager(ViewPager viewPager){
        page_adapter page_adapter = new page_adapter(getSupportFragmentManager(),3);
        page_adapter.addFragment(new alarm(),"ALARM");
        page_adapter.addFragment(new stopWatch(),"STOP WATCH");
        page_adapter.addFragment(new timer(),"TIMER");
        viewPager.setAdapter(page_adapter);
    }
}