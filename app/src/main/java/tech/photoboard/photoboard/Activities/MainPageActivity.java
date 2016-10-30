package tech.photoboard.photoboard.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import tech.photoboard.photoboard.API.ApiClient;
import tech.photoboard.photoboard.API.RetrofitAPI;
import tech.photoboard.photoboard.API.TakePhotoResponse;
import tech.photoboard.photoboard.Adapter.GridViewAdapter;
import tech.photoboard.photoboard.Photo;
import tech.photoboard.photoboard.R;
import tech.photoboard.photoboard.Utils.ServerManager;

import static tech.photoboard.photoboard.API.ApiClient.getClient;

public class MainPageActivity extends AppCompatActivity implements BluetoothListDialogFragment.OnItemSelectedListener
        , ServerManager.getPhotosResquest{
    public static final int PHOTO_MODE = 10;
    public static final int BLUETOOTH_MODE = 11;
    public int currentMode;
    private ArrayList<Photo> photoList;
    private GridView gridview;
    private GridViewAdapter gridViewAdapter;
    private TakePhotoResponse requestData;
    ServerManager serverManager;

    FloatingActionButton btnTakePhoto;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    boolean bluetoothEnabledSocked;
    final static int BLUETOOTH_REQUEST_ENABLE = 1;

    public String[] photos = {
            "http://www.qdtricks.org/wp-content/uploads/2015/02/hd-wallpapers-1080p-for-mobile.png",
            "http://media.salemwebnetwork.com/cms/CROSSCARDS/31680-27279-june-2015-faith-bigger-than-fear-iphone-6-plus.jpg",
            "http://www.mobileswall.com/wp-content/uploads/2015/12/300-Play-the-Game-Who-is-to-Come-l.jpg",
            "http://cdn.wonderfulengineering.com/wp-content/uploads/2014/05/mobile-wallpaper-16-610x1084.jpg",
            "http://cdn.wonderfulengineering.com/wp-content/uploads/2014/07/black-tiger-mobile-phone-wallpaper.jpg",
            "http://cdn.wonderfulengineering.com/wp-content/uploads/2014/05/mobile-samsung-16-610x1084.jpg",
            "http://www.mobileswall.com/wp-content/uploads/2015/11/300-Black-Baroque-Pattern-l.jpg"
    };
    final RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Call<ArrayList<Photo>> getPhotos = retrofitAPI.getPicturesList();
        getPhotos.enqueue(new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                photoList = response.body();
                for(int i=0; i < photoList.size(); i++){
                    Log.e("Message", String.valueOf(photoList.get(i).getId()) + " " + photoList.get(i).getPicture());
                }
                onGetPhotoResponse(photoList);
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {

            }
        });


        //Bluetooth
        btnTakePhoto = (FloatingActionButton) findViewById(R.id.btn_take_capture);
        btnTakePhoto.setImageResource(R.drawable.ic_btn_bluetooth);
        currentMode = BLUETOOTH_MODE;
        bluetoothDevice = null;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //GridView
        photoList = new ArrayList<>();
        requestData = null;

        //serverManager = new ServerManager();
        //serverManager.getPhotos();

        gridview = (GridView) findViewById(R.id.gv_main_page);
        gridview.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        gridViewAdapter = new GridViewAdapter(MainPageActivity.this,photoList);
        gridview.setAdapter(gridViewAdapter);

        //Setting the functionality of the button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_take_capture);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(currentMode) {
                    case PHOTO_MODE:
                        Call<TakePhotoResponse> makeRequest = retrofitAPI.takePhotoRequest();
                        makeRequest.enqueue(new Callback<TakePhotoResponse>() {
                            @Override
                            public void onResponse(Call<TakePhotoResponse> call, Response<TakePhotoResponse> response) {
                                requestData = response.body();
                                if(requestData.getResponse().equals("OK")) {
                                    try {
                                        Thread.sleep(2000);
                                        Call<ArrayList<Photo>> getPhotos = retrofitAPI.getPicturesList();
                                        getPhotos.enqueue(new Callback<ArrayList<Photo>>() {
                                            @Override
                                            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                                                photoList = response.body();
                                                for(int i=0; i < photoList.size(); i++){
                                                    Log.e("Message", String.valueOf(photoList.get(i).getId()) + " " + photoList.get(i).getPicture());
                                                }
                                                onGetPhotoResponse(photoList);
                                            }

                                            @Override
                                            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {

                                            }
                                        });

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<TakePhotoResponse> call, Throwable t) {

                            }
                        });
                        break;
                    case BLUETOOTH_MODE:
                        Toast.makeText(MainPageActivity.this, "Searching Bluetooth", Toast.LENGTH_SHORT).show();
                        //Check if bluetooth is supported
                        if (bluetoothAdapter == null) {
                        } else {
                            //Check if bluetooth is enabled and try to enable it
                            if (!bluetoothAdapter.isEnabled()) {
                                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBluetoothIntent, BLUETOOTH_REQUEST_ENABLE);
                            }
                            //If it's enabled show the Dialog
                            else {
                                DialogFragment newFragment = new BluetoothListDialogFragment();
                                newFragment.show(getSupportFragmentManager(), "dialog");
                            }
                        }

                            //If it's not take the pictures

                        break;
                    default:
                        Toast.makeText(MainPageActivity.this, "Something wrong is going on here", Toast.LENGTH_SHORT).show();

                }
               /*nackbar.make(view, "What in the world is going on", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            */}
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(currentMode == PHOTO_MODE) {
                    Toast.makeText(MainPageActivity.this, "Changed to Bluetooth Functionality", Toast.LENGTH_SHORT).show();
                    btnTakePhoto.setImageResource(R.drawable.ic_btn_bluetooth);
                    currentMode = BLUETOOTH_MODE;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Toast.makeText(this, "Request sent", Toast.LENGTH_SHORT).show();

            Call<ArrayList<Photo>> getPhotos = retrofitAPI.getPicturesList();
            getPhotos.enqueue(new Callback<ArrayList<Photo>>() {
                @Override
                public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                    photoList = response.body();
                    for(int i=0; i < photoList.size(); i++){
                        Log.e("Message", String.valueOf(photoList.get(i).getId()) + " " + photoList.get(i).getPicture());
                    }
                    onGetPhotoResponse(photoList);
                }

                @Override
                public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {

                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(BluetoothDevice bd) {
        bluetoothDevice = bd;
        currentMode = PHOTO_MODE;
        btnTakePhoto.setImageResource(R.drawable.ic_btn_photo);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Manage the activity result for the BluetoothEnable
        if(requestCode == BLUETOOTH_REQUEST_ENABLE) {
            //Check if the intent to enable the bluetooth was successful
            if (resultCode == RESULT_CANCELED) {
                //If it is, show de DialogFragment
            } else if (resultCode == RESULT_OK) {
                DialogFragment newFragment = new BluetoothListDialogFragment();
                newFragment.show(getSupportFragmentManager(), "dialog");

            }
        }
    }

    @Override
    public void onGetPhotoResponse(ArrayList<Photo> photos) {

    }
}
