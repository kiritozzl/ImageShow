package com.example.kirito.imageshow.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kirito.imageshow.MainActivity;
import com.example.kirito.imageshow.R;
import com.example.kirito.imageshow.adapter.GridViewAdapter;
import com.example.kirito.imageshow.adapter.ListViewAdapter;
import com.example.kirito.imageshow.adapter.PopupAdapter;
import com.example.kirito.imageshow.entity.FileItem;
import com.example.kirito.imageshow.entity.Item;
import com.example.kirito.imageshow.support.LoadImages;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kirito on 2016/9/16.
 */
public class ListModeActivity extends AppCompatActivity {
    private String sd_path;
    private ListView lv;
    private List<FileItem> items;
    private String current_photo_path;
    ListPopupWindow popup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listmode);

        lv = (ListView) findViewById(R.id.lv);
        sd_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        LoadImages load = new LoadImages(this,1);
        load.setListListView(new LoadImages.ListView() {
            @Override
            public void setListFileItem(final List<FileItem> listFileItem) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListViewAdapter adapter = new ListViewAdapter(ListModeActivity.this,listFileItem);
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                FileItem fileItem = (FileItem) parent.getItemAtPosition(position);
                                Intent intent = new Intent(ListModeActivity.this, ListToGridActivity.class);
                                intent.putExtra("path",fileItem.getParent_path());

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
        MenuItem menuItem = menu.findItem(R.id.delete_menu);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.more){
            View view = findViewById(R.id.more);
            setListPopupWindow(view);
            /*PopupMenu popupMenu = new PopupMenu(MainActivity.this,view);
            popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.grid_item){
                    }
                    return true;
                }
            });
            popupMenu.show();*/
        }else if (item.getItemId() == R.id.camera){
            takePhoto();
        }
        return true;
    }

    private void setListPopupWindow(View view){
        popup = new ListPopupWindow(ListModeActivity.this);
        PopupAdapter adpter = new PopupAdapter(ListModeActivity.this,1, new PopupAdapter.onCheckListener() {
            @Override
            public void onChecked(int id) {
                //Log.e(TAG, "onChecked: mode---"+mode );
                if (id == 0){
                    Intent i = new Intent(ListModeActivity.this,MainActivity.class);
                    startActivity(i);
                    //ListModeActivity.this.finish();
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
