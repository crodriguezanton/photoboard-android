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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by pc1 on 23/10/2016.
 */

public class ImageViewerActivity extends Activity {

    private ArrayList<String> photoList;
    private FullScreenImage adapter;
    private ViewPager viewPager;
    private int imgSelected;
    private ImageButton btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        imgSelected = getIntent().getIntExtra("POSITION",0);
        images =  (ArrayList<String>)getIntent().getSerializableExtra("FULLSCREEN_IMAGES");
        adapter = new FullScreenImageAdapter(FullScreenImageActivity.this,images);
        viewPager = (ViewPager) findViewById(R.id.vp_image_pager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(imgSelected);
        btnDownload = (ImageButton) findViewById(R.id.btn_download_image);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(FullScreenImageActivity.this)
                        .load(images.get(viewPager.getCurrentItem()))
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

                    String url =  images.get(viewPager.getCurrentItem());
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