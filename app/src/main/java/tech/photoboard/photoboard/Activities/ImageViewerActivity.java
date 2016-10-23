package tech.photoboard.photoboard.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;


import tech.photoboard.photoboard.Adapter.FullScreenImageAdapter;
import tech.photoboard.photoboard.Photo;
import tech.photoboard.photoboard.R;

/**
 * Created by pc1 on 23/10/2016.
 */

public class ImageViewerActivity extends Activity {

    private ArrayList<Photo> photoList;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private int imgSelected;
    private ImageButton btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        imgSelected = getIntent().getIntExtra("POSITION",0);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Photo>>(){}.getType();
        photoList =  gson.fromJson(getIntent().getStringExtra("FULLSCREEN_IMAGES"), type);
        adapter = new FullScreenImageAdapter(ImageViewerActivity.this,photoList);
        viewPager = (ViewPager) findViewById(R.id.vp_image_pager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(imgSelected);
        btnDownload = (ImageButton) findViewById(R.id.btn_download_image);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(ImageViewerActivity.this)
                        .load(photoList.get(viewPager.getCurrentItem()).getPicture())
                        .skipMemoryCache()
                        .noFade()
                        .into(target);
            }
        });

    }
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File folder = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Photoboard");
                    folder.mkdirs();

                    String url =  photoList.get(viewPager.getCurrentItem()).getPicture();
                    if(url == null) return;
                    String name = url.substring(url.lastIndexOf("/")+1);
                    File file = new File(folder, name);
                    String finalPath = file.getAbsolutePath();
                    try
                    {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                        ostream.close();


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    galleryAddPic(finalPath);
                }
            }).start();
        }
        private void galleryAddPic(String mCurrentPhotoPath) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        }
        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            if (placeHolderDrawable != null) {
            }
        }
    };

}
//ELIAS SUBNORMAL