package tech.photoboard.photoboard.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;


import tech.photoboard.photoboard.API.ApiClient;
import tech.photoboard.photoboard.Adapter.FullScreenImageAdapter;
import tech.photoboard.photoboard.Classes.Photo;
import tech.photoboard.photoboard.R;

/**
 * Created by pc1 on 23/10/2016.
 */

/* Photo Viewer:
*   - Zoom capability
*   - Download images to {SUBJECT} folder, in pictures
*   - Add photos to favorites
*/
public class ImageViewerActivity extends Activity {



    private ArrayList<Photo> photoList;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private int imgSelected;
    private ImageButton btnDownload;
    private ImageButton btnFavorite;
    private String currentSubject;
    private MySPHelper mySPHelper;
    private boolean currentPhotoIsFav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        mySPHelper = new MySPHelper(this);
        currentSubject = mySPHelper.getCurrentSubject();

        /* Retrieving Photos from Intent and give them to the Viewer adapter*/
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Photo>>() {}.getType();
        photoList = gson.fromJson(getIntent().getStringExtra("FULLSCREEN_IMAGES"), type);

        viewPager = (ViewPager) findViewById(R.id.vp_image_pager);
        adapter = new FullScreenImageAdapter(ImageViewerActivity.this, photoList);
        viewPager.setAdapter(adapter);

        imgSelected = getIntent().getIntExtra("POSITION", 0);
        viewPager.setCurrentItem(imgSelected);

        /* Giving functionality to the OnPageChangeListener:
         *   - Modify favorite button if photo has been added to favorites.
         */

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ArrayList<String> favPhotos = mySPHelper.getFavPhotos(currentSubject);
                if(favPhotos!= null && favPhotos.contains(""+photoList.get(position).getId())) {
                    currentPhotoIsFav = true;
                    btnFavorite.setBackgroundResource(R.drawable.ic_red_star);
                } else  {
                    currentPhotoIsFav = false;
                    btnFavorite.setBackgroundResource(R.drawable.ic_white_star);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /* Adding functionality to download button and favorite button
         * #btnDownload will download image to object target
         * #btnFavorite will add photo to favorite list in SharedPreferences
         * */
        btnDownload = (ImageButton) findViewById(R.id.btn_download_image);
        btnFavorite = (ImageButton) findViewById(R.id.btn_add_favorite);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDownload.setEnabled(false);
                Picasso.with(ImageViewerActivity.this)
                        .load(ApiClient.URL + photoList.get(viewPager.getCurrentItem()).getPicture())
                        .into(target);
                Toast.makeText(getApplicationContext(), "Added to " + currentSubject , Toast.LENGTH_SHORT).show();
                btnDownload.setEnabled(true);
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> favPhotos = mySPHelper.getFavPhotos(currentSubject);

                if (favPhotos==null) {
                    favPhotos = new ArrayList<String>();
                }

                if(!currentPhotoIsFav) {

                    currentPhotoIsFav = true;
                    btnFavorite.setBackgroundResource(R.drawable.ic_red_star);
                    favPhotos.add(""+photoList.get(viewPager.getCurrentItem()).getId());

                } else {
                    currentPhotoIsFav = false;
                    btnFavorite.setBackgroundResource(R.drawable.ic_white_star);
                    favPhotos.remove(""+photoList.get(viewPager.getCurrentItem()).getId());
                }
                mySPHelper.setFavPhotos(favPhotos);


            }
        });
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    String id =  photoList.get(viewPager.getCurrentItem()).getPicture();
                    if(id == null) return;
                    /* Creates a {Photoboard} folder and also a {Subject} folder inside.*/

                    File folder = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Photoboard");
                    folder.mkdirs();

                    File subfolder = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)+"/Photoboard",currentSubject);
                    subfolder.mkdirs();


                    String name = id.substring(id.lastIndexOf("/")+1);

                    File file = new File(subfolder, name);
                    String finalPath = file.getAbsolutePath();

                    try
                    {
                        /* Trying to save picture to sd-card*/

                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                        ostream.close();

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    /* Finally we add the picture to the gallery.*/
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