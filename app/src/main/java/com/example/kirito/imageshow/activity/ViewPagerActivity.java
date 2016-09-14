package com.example.kirito.imageshow.activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.kirito.imageshow.R;
import com.example.kirito.imageshow.entity.Item;
import com.example.kirito.imageshow.fragment.PagerFragment;

import java.util.ArrayList;

/**
 * Created by kirito on 2016/9/12.
 */
public class ViewPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private int id;
    private int count;
    private ArrayList<Item> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        id = getIntent().getIntExtra("id",0);
        count = getIntent().getIntExtra("count",0);
        items = (ArrayList<Item>) getIntent().getBundleExtra("bundle").getSerializable("items");
        PagersAdapter adapter = new PagersAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(id);
    }

    private class PagersAdapter extends FragmentStatePagerAdapter{

        public PagersAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //向PagerFragment传递参数
            return new PagerFragment().getInstance(position,items);
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
