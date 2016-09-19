package com.example.kirito.imageshow;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;

import com.example.kirito.imageshow.activity.ListModeActivity;
import com.example.kirito.imageshow.activity.ViewPagerActivity;
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

public class MainActivity extends AppCompatActivity {
    private String sd_path;
    private GridView gv;
    private String current_photo_path;
    ListPopupWindow popup;

    private boolean isCheck = false;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sd_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        gv = (GridView) findViewById(R.id.gv);
        LoadImages load = new LoadImages(this,0);
        load.setCallBack(new LoadImages.CallBack() {
            @Override
            public void setListItem(final List<Item> listItem) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GridViewAdapter adapter = new GridViewAdapter(MainActivity.this, listItem, new GridViewAdapter.checkListener() {
                            @Override
                            public void setCheck(boolean check) {
                                Log.e(TAG, "setCheck: check---"+check );
                                isCheck = check;
                            }
                        });
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isCheck){
            menu.add(0,1,Menu.NONE,"delete").setIcon(R.drawable.trash);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.more){
            View view = findViewById(R.id.more);
            setListPopupWindow(view);

        }else if (item.getItemId() == R.id.camera){
            takePhoto();
        }
        return true;
    }

    private void setListPopupWindow(View view){
        popup = new ListPopupWindow(MainActivity.this);
        PopupAdapter adpter = new PopupAdapter(MainActivity.this,0, new PopupAdapter.onCheckListener() {
            @Override
            public void onChecked(int id) {
                //Log.e(TAG, "onChecked: mode---"+mode );
                if (id == 1){
                    Intent i = new Intent(MainActivity.this,ListModeActivity.class);
                    startActivity(i);
                    //MainActivity.this.finish();
                }
            }
        });
        popup.setAdapter(adpter);
        popup.setAnchorView(view);
        popup.setWidth(470);
        popup.setHeight(210);
        popup.setModal(true);
        popup.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //popup.dismiss();
    }

    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null){
                Uri uri = Uri.fromFile(photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(intent,1);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(current_photo_path);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            //Log.e(TAG, "onActivityResult: uri---"+uri );
            sendBroadcast(intent);
        }
    }

    private File createImageFile()throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String image_file_name = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStorageDirectory().getAbsoluteFile();
        File image = File.createTempFile(image_file_name,".jpg",storageDir);
        current_photo_path = "file://" + image.getAbsolutePath();
        //Log.e(TAG, "createImageFile: current_photo_path---"+current_photo_path );
        return image;
    }

}
