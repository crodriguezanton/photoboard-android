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
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.photoboard.photoboard.API.ApiClient;
import tech.photoboard.photoboard.API.RetrofitAPI;
import tech.photoboard.photoboard.Classes.TakePhotoResponse;
import tech.photoboard.photoboard.Adapter.GridViewAdapter;
import tech.photoboard.photoboard.Classes.Photo;
import tech.photoboard.photoboard.R;

public class MainPageActivity extends AppCompatActivity implements BluetoothListDialogFragment.OnItemSelectedListener {

    public static final int BLUETOOTH_REQUEST_ENABLE = 1;
    public static final int PHOTO_MODE = 10;
    public static final int BLUETOOTH_MODE = 11;
    final RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);


    private int currentMode;
    private ArrayList<Photo> photoList;
    private GridView gridview;
    private GridViewAdapter gridViewAdapter;
    private TakePhotoResponse requestData;
    private FloatingActionButton btnTakePhoto;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;

    boolean photoReceived;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_refresh);
        toolbar.setNavigationIcon(R.drawable.icon_app_photoboard);
        setSupportActionBar(toolbar);

        //Bluetooth
        btnTakePhoto = (FloatingActionButton) findViewById(R.id.btn_take_capture);
        btnTakePhoto.setImageResource(R.drawable.ic_btn_bluetooth);
        currentMode = BLUETOOTH_MODE;
        bluetoothDevice = null;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //GridView
        photoList = new ArrayList<>();
        gridview = (GridView) findViewById(R.id.gv_main_page);
        gridview.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        gridViewAdapter = new GridViewAdapter(MainPageActivity.this, photoList);
        gridview.setAdapter(gridViewAdapter);

        //Update GridView
        getPhotosFromServer();

        //Setting the functionality of the button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_take_capture);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentMode) {

                    case PHOTO_MODE:

                        takePhotoRequest();
                        break;

                    case BLUETOOTH_MODE:

                        //Check if bluetooth is supported
                        if (bluetoothAdapter == null) {

                            Toast.makeText(MainPageActivity.this, "Bluetooth is not supported", Toast.LENGTH_SHORT).show();

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

                        break;

                    default:

                        Toast.makeText(MainPageActivity.this, "Internal error", Toast.LENGTH_SHORT).show();
                        break;

                }
               /*snackbar.make(view, "What in the world is going on", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            */
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (currentMode == PHOTO_MODE) {
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

            getPhotosFromServer();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Manage the activity result for the BluetoothEnable
        if (requestCode == BLUETOOTH_REQUEST_ENABLE) {
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
    public void onItemSelected(BluetoothDevice bd) {
        bluetoothDevice = bd;
        currentMode = PHOTO_MODE;
        btnTakePhoto.setImageResource(R.drawable.ic_btn_photo);

    }

    public void getPhotosFromServer() {
        Call<ArrayList<Photo>> getPhotos = retrofitAPI.getPicturesList();
        getPhotos.enqueue(new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                photoList = response.body();
                for (int i = 0; i < photoList.size(); i++) {
                    Log.e("Message", String.valueOf(photoList.get(i).getId()) + " " + photoList.get(i).getPicture());
                }
                gridViewAdapter.updateList(photoList);
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {

            }
        });
    }

    public void takePhotoRequest() {
        Call<TakePhotoResponse> takePhotoResponse = retrofitAPI.takePhotoRequest();
        takePhotoResponse.enqueue(new Callback<TakePhotoResponse>() {
            @Override
            public void onResponse(Call<TakePhotoResponse> call, Response<TakePhotoResponse> response) {
                requestData = response.body();
                if (requestData.getResponse()) {
                    new Thread(new Worker(requestData.getId())).start();
                }
            }

            @Override
            public void onFailure(Call<TakePhotoResponse> call, Throwable t) {

            }
        });
    }

    public boolean getPhotoRequest(String id) {

        photoReceived = false;

        Call<Photo> getPhotoResponse = retrofitAPI.getPhotoResquest(id);
        getPhotoResponse.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                Photo photo = response.body();
                if (photo != null) {
                    photoReceived = true;
                    gridViewAdapter.addPhotoToList(photo);
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {

            }
        });
        return photoReceived;
    }

    class Worker implements Runnable {

        String id;
        boolean die;

        public Worker(String id) {
            this.id = id;
        }

        @Override
        public void run() {

            while (true) {

                try {
                    die = getPhotoRequest(id);
                    if (die) break;
                    wait(250);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
