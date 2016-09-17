package com.example.kirito.imageshow.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.kirito.imageshow.R;
import com.example.kirito.imageshow.adapter.GridViewAdapter;
import com.example.kirito.imageshow.adapter.PopupAdapter;
import com.example.kirito.imageshow.entity.Item;
import com.example.kirito.imageshow.support.LoadImages;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListToGridActivity extends AppCompatActivity {
    private String sd_path;
    private GridView gv;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sd_path = getIntent().getStringExtra("path");
        gv = (GridView) findViewById(R.id.gv);
        LoadImages load = new LoadImages(this,0);
        load.setCallBack(new LoadImages.CallBack() {
            @Override
            public void setListItem(final List<Item> listItem) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GridViewAdapter adapter = new GridViewAdapter(ListToGridActivity.this,listItem);
                        gv.setAdapter(adapter);
                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Item item = (Item) parent.getItemAtPosition(position);
                                //Log.e(TAG, "onItemClick: path---"+item.getPath() );
                                Intent intent = new Intent(ListToGridActivity.this, ViewPagerActivity.class);
                                intent.putExtra("id",position);
                                intent.putExtra("count",listItem.size());

                                //传递ArrayList<Item>
                                ArrayList<Item> items = (ArrayList<Item>) listItem;
                                Bundle args = new Bundle();
                                args.putSerializable("items",items);
                                intent.putExtra("bundle",args);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
        load.execute(sd_path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

}
