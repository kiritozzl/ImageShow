package com.example.kirito.imageshow.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.kirito.imageshow.R;
import com.example.kirito.imageshow.entity.Item;
import com.example.kirito.imageshow.fragment.PagerFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kirito on 2016/9/12.
 */
public class ViewPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private int id;
    private int count;
    private ArrayList<Item> items;
    private PagersAdapter adapter;

    private File file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        id = getIntent().getIntExtra("id",0);
        count = getIntent().getIntExtra("count",0);
        items = (ArrayList<Item>) getIntent().getBundleExtra("bundle").getSerializable("items");

        file = new File(items.get(id).getPath());
        adapter = new PagersAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picture,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            //intent.setDataAndType(Uri.parse(items.get(id).getPath()),"image/*");
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(items.get(id).getPath()));
            startActivity(Intent.createChooser(intent,"share image"));
        }else if (item.getItemId() == R.id.delete_menu){
            final View view = findViewById(R.id.delete_menu);
            PopupMenu popupMenu = new PopupMenu(ViewPagerActivity.this,view);
            popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    file.delete();
                    viewPager.removeViewAt(id);
                    viewPager.setCurrentItem(id + 1);
                    return true;
                }
            });
            popupMenu.show();
        }
        return true;
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
