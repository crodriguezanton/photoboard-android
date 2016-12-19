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

/*Esta Actividad es la que se abre cuando pulsamos una foto,
 *basicamente es el visor de fotos.
 */
public class ImageViewerActivity extends Activity {

    //Declaracion de variables a usar:
    /*La lista de las fotos, el adaptador de la foto,
     * el viewPager (el contenedor que desliza las fotos hacia los lados)
     * la posicion de la foto seleccionada y el boton de descarga
     */


    private ArrayList<Photo> photoList;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private int imgSelected;
    private ImageButton btnDownload;
    private ImageButton btnFavorite;
    private String currentSubject;
    private MySPHelper mySPHelper;
    private boolean actualPhotoFavorite;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        mySPHelper = new MySPHelper(this);
        //Recogemos informacion de la actividad desde donde se llama a esta:
        imgSelected = getIntent().getIntExtra("POSITION", 0);
        currentSubject = mySPHelper.getCurrentSubject();

        //Cogemos la lista del Intent y la añadimos al adaptador.
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Photo>>() {
        }.getType();
        photoList = gson.fromJson(getIntent().getStringExtra("FULLSCREEN_IMAGES"), type);
        adapter = new FullScreenImageAdapter(ImageViewerActivity.this, photoList);

        //Creamos el viewPager con el adaptador de antes.
        viewPager = (ViewPager) findViewById(R.id.vp_image_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ArrayList<String> favPhotos = mySPHelper.getFavPhotos(currentSubject);
                if(favPhotos!= null && favPhotos.contains(""+photoList.get(position).getId())) {
                    Log.i("FavList contains",""+photoList.get(position).getId());
                    actualPhotoFavorite = true;
                    btnFavorite.setBackgroundResource(R.drawable.ic_red_star);
                } else  {
                    Log.i("FavList NOT contains",""+position);
                    actualPhotoFavorite = false;
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
        viewPager.setAdapter(adapter);

        //Seleccionamos la posicion deseada.
        viewPager.setCurrentItem(imgSelected);

        //Declaramos el boton de descarga y su funcionalidad
        btnDownload = (ImageButton) findViewById(R.id.btn_download_image);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(ImageViewerActivity.this)
                        .load(ApiClient.URL + photoList.get(viewPager.getCurrentItem()).getPicture())
                        .skipMemoryCache()
                        .noFade()
                        .into(target);
            }
        });
        /*Adding photo to favorites*/
        btnFavorite = (ImageButton) findViewById(R.id.btn_add_favorite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> favPhotos = new ArrayList<String>();
                if (mySPHelper.getFavPhotos(currentSubject)!=null) {
                    favPhotos = mySPHelper.getFavPhotos(currentSubject);
                }
                if(!actualPhotoFavorite) {
                    actualPhotoFavorite = true;
                    btnFavorite.setBackgroundResource(R.drawable.ic_red_star);
                    favPhotos.add(""+photoList.get(viewPager.getCurrentItem()).getId());

                    for(String s: favPhotos) {
                        Log.i("Message", s);
                    }
                    mySPHelper.setFavPhotos(favPhotos);
                } else {
                    /*Eliminar de favorito*/
                    actualPhotoFavorite = false;
                    btnFavorite.setBackgroundResource(R.drawable.ic_white_star);
                    favPhotos.remove(""+photoList.get(viewPager.getCurrentItem()).getId());
                    mySPHelper.setFavPhotos(favPhotos);
                }


            }
        });
    }
    //Target nos sirve para guardar la foto en la SD
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //Se crea una carpeta en el movil en caso de que no existiera.
                    File folder = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Photoboard");
                    File subfolder = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)+"/Photoboard",currentSubject);
                    folder.mkdirs();
                    subfolder.mkdirs();
                    String url =  photoList.get(viewPager.getCurrentItem()).getPicture();
                    if(url == null) return;
                    String name = url.substring(url.lastIndexOf("/")+1);
                    //declaramos un archivo con el nombre de la foto y la direccion.
                    File file = new File(subfolder, name);
                    String finalPath = file.getAbsolutePath();
                    try
                    {
                        //Se crea el archivo a traves del bitmap recivido por Picasso
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                        ostream.close();


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    //Añadimos la foto a la galeria del movil, sino hicieramos esto solo estaria
                    //en la carpeta de Photoboard
                    galleryAddPic(finalPath);
                    Toast.makeText(ImageViewerActivity.this, "Added to " + currentSubject , Toast.LENGTH_SHORT).show();
                }
            }).start();
        }
        private void galleryAddPic(String mCurrentPhotoPath) {
            //Añade la foto a la galeria.
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