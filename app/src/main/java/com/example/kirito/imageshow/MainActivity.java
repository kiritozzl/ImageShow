package com.example.kirito.imageshow;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;

import com.example.kirito.imageshow.activity.PictureActivity;
import com.example.kirito.imageshow.activity.ViewPagerActivity;
import com.example.kirito.imageshow.adapter.GridViewAdapter;
import com.example.kirito.imageshow.entity.Item;
import com.example.kirito.imageshow.support.LoadImages;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String sd_path;
    private GridView gv;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sd_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        gv = (GridView) findViewById(R.id.gv);
        LoadImages load = new LoadImages(this);
        load.setCallBack(new LoadImages.CallBack() {
            @Override
            public void setListItem(final List<Item> listItem) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GridViewAdapter adapter = new GridViewAdapter(MainActivity.this,listItem);
                        gv.setAdapter(adapter);
                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Item item = (Item) parent.getItemAtPosition(position);
                                //Log.e(TAG, "onItemClick: path---"+item.getPath() );
                                Intent intent = new Intent(MainActivity.this, ViewPagerActivity.class);
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
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.more){
            View view = findViewById(R.id.more);
            PopupMenu popupMenu = new PopupMenu(MainActivity.this,view);
            popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
            popupMenu.show();
        }
        return true;
    }

}
