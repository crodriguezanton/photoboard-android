package tech.photoboard.photoboard.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.photoboard.photoboard.API.ApiClient;
import tech.photoboard.photoboard.API.RetrofitAPI;
import tech.photoboard.photoboard.Activities.BluetoothListDialogFragment;
import tech.photoboard.photoboard.Activities.MySPHelper;
import tech.photoboard.photoboard.Adapter.GridViewAdapter;
import tech.photoboard.photoboard.Classes.Photo;
import tech.photoboard.photoboard.Classes.PhotoPool;
import tech.photoboard.photoboard.Classes.PictureGallery;
import tech.photoboard.photoboard.Classes.Subject;
import tech.photoboard.photoboard.Classes.TakePhotoResponse;
import tech.photoboard.photoboard.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Elias on 02/12/2016.
 */

public class GalleryFragment extends Fragment implements BluetoothListDialogFragment.OnItemSelectedListener{


    public static final int BLUETOOTH_REQUEST_ENABLE = 1;
    public static final int PHOTO_MODE = 2502;
    public static final int BLUETOOTH_MODE = 1991;
    final RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);

    private int currentMode;
    private ArrayList<Photo> photoList;
    private GridView gridview;
    private GridViewAdapter gridViewAdapter;
    private TakePhotoResponse requestData;
    private FloatingActionButton btnTakePhoto;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    SwipeRefreshLayout swipeRefreshLayout;

    boolean photoReceived;
    private MySPHelper mySPHelper;

    private Subject subject;
    private static final String SUBJECT_KEY = "subject_key";
    private String photos[] = {"http://www.computus.org/wp/wp-content/uploads/2009/02/rees-l.jpg",
            "http://previews.123rf.com/images/nexusplexus/nexusplexus1210/nexusplexus121000372/15597956-Personas-de-negocios-de-pie-contra-la-pizarra-con-una-gran-cantidad-de-datos-escrito-en-l-Foto-de-archivo.jpg",
            "http://img00.deviantart.net/f805/i/2010/141/9/a/highschool_blackboard_by_peanut28.jpg",
            "https://whatsonmyblackboard.files.wordpress.com/2015/10/commonroomsept2015.jpg",
            "http://www.computus.org/wp/wp-content/uploads/2009/02/rees-l.jpg",
            "https://www.internationalsugarjournal.com/wp-content/uploads/2014/08/theblackboard-800x580.jpg",
            "http://img00.deviantart.net/f805/i/2010/141/9/a/highschool_blackboard_by_peanut28.jpg",
            "https://whatsonmyblackboard.files.wordpress.com/2015/10/commonroomsept2015.jpg",
            "http://previews.123rf.com/images/nexusplexus/nexusplexus1210/nexusplexus121000372/15597956-Personas-de-negocios-de-pie-contra-la-pizarra-con-una-gran-cantidad-de-datos-escrito-en-l-Foto-de-archivo.jpg"};



    public static GalleryFragment newInstance(Subject subject) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putSerializable(SUBJECT_KEY, gson.toJson(subject));
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mySPHelper = new MySPHelper(getActivity());
        /*Getting subject from bundle*/
        Gson gson = new Gson();
        Type type = new TypeToken<Subject>() {}.getType();
        subject = gson.fromJson((String) getArguments().getSerializable(SUBJECT_KEY) ,type);
        setSubjectStyle();

        /*Swipe Refresh Layout*/
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_gallery);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_1, R.color.refresh_2, R.color.refresh_3, R.color.refresh_4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPhotosFromServer();
            }
        });

        /*Setting the gallery*/
        photoList = new ArrayList<>();
        gridview = (GridView) view.findViewById(R.id.gv_subject_gallery);
        gridViewAdapter = new GridViewAdapter(getActivity(), photoList);
        gridview.setAdapter(gridViewAdapter);

        /*Update GridView*/
        getPhotosFromServer();
/*
        ArrayList<String> ids = new ArrayList( Arrays.asList(photos) );
        ArrayList<Photo> newPhotos = new ArrayList<>();
        int iterator = 0;
        for(String s: ids) {
            newPhotos.add(new Photo(s,iterator));
            iterator++;
        }
        photoList = newPhotos;
        filterFavorites(mySPHelper.getFavMode());
*/


        /*Setting the fab button*/
        btnTakePhoto = (FloatingActionButton) view.findViewById(R.id.fab_take_capture);
        btnTakePhoto.setImageResource(R.drawable.ic_btn_bluetooth);
        currentMode = BLUETOOTH_MODE;

        bluetoothDevice = null;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        /*Giving functionality to the fab button*/
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentMode) {
                    //Dependiendo del modo:
                    case PHOTO_MODE:
                        btnTakePhoto.setEnabled(false);
                        if(bluetoothDevice != null) takePhotoRequest();
                        break;

                    case BLUETOOTH_MODE:

                        //Check if bluetooth is supported
                        if (bluetoothAdapter == null) {

                            Toast.makeText(getActivity(), "Bluetooth is not supported", Toast.LENGTH_SHORT).show();

                        } else {

                            //Check if bluetooth is enabled and try to enable it
                            if (!bluetoothAdapter.isEnabled()) {

                                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBluetoothIntent, BLUETOOTH_REQUEST_ENABLE);

                            }
                            //If it's enabled show the Dialog
                            else {

                                DialogFragment newFragment = new BluetoothListDialogFragment();
                                newFragment.show(getActivity().getSupportFragmentManager(), "dialog");

                            }
                        }

                        break;

                    default:

                        Toast.makeText(getActivity(), "Internal error", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
        btnTakePhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /* Disconnects from Joule*/
                if (currentMode == PHOTO_MODE) {
                    Toast.makeText(getActivity(), "Changed to Bluetooth Functionality", Toast.LENGTH_SHORT).show();
                    btnTakePhoto.setImageResource(R.drawable.ic_btn_bluetooth);
                    currentMode = BLUETOOTH_MODE;
                }

                return true;
            }
        });
        return view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Manage the activity result for the BluetoothEnable
        if (requestCode == BLUETOOTH_REQUEST_ENABLE) {
            //Check if the intent to enable the bluetooth was successful
            if (resultCode == RESULT_CANCELED) {
                //If it is, show de DialogFragment
            } else if (resultCode == RESULT_OK) {
                DialogFragment newFragment = new BluetoothListDialogFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "dialog");

            }
        }
    }
    @Override
    public void onItemSelected(BluetoothDevice bd) {
        //Al seleccionar un dispositivos en la lista del Dialog, lo cogemos y cambiamos la
        //funcionalidad del boton
        bluetoothDevice = bd;
        currentMode = PHOTO_MODE;
        btnTakePhoto.setImageResource(R.drawable.ic_btn_photo);

    }

    public void takePhotoRequest() {

        swipeRefreshLayout.setRefreshing(true);

        //Pedimos foto y esperamos respuesta
        Call<TakePhotoResponse> takePhotoResponse = retrofitAPI.takePhotoRequest(subject.getId());
        takePhotoResponse.enqueue(new Callback<TakePhotoResponse>() {
            @Override
            public void onResponse(Call<TakePhotoResponse> call, Response<TakePhotoResponse> response) {
                btnTakePhoto.setEnabled(true);
                Log.e("Raw", response.message()+response.raw().toString());
                requestData = response.body();
                if (requestData.isSuccess()) {
                    //Si es afirmativa, creamos un Thread que espere para recibir la foto
                    new Thread(new Worker(requestData.getUrl())).start();
                }
            }

            @Override
            public void onFailure(Call<TakePhotoResponse> call, Throwable t) {
                btnTakePhoto.setEnabled(true);
            }
        });
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
                    //Cada 250ms preguntamos al servidor si tiene ya la foto
                    die = getPhotoRequest(id);
                    if (die) break;
                    wait(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public void getPhotosFromServer() {

        Call<PictureGallery> getPhotos = retrofitAPI.getSubjectPhotos(subject.getUrl());
        getPhotos.enqueue(new Callback<PictureGallery>() {
            @Override
            public void onResponse(Call<PictureGallery> call, Response<PictureGallery> response) {
                PictureGallery photoGallery = response.body();
                photoList = photoGallery.getPictures();
                gridViewAdapter.updateList(photoList);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PictureGallery> call, Throwable t) {
             swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public boolean getPhotoRequest(String id) {

        photoReceived = false;

        //Pedimos la foto
        Call<PhotoPool> getPhotoResponse = retrofitAPI.getPhotoResquest(id);
        getPhotoResponse.enqueue(new Callback<PhotoPool>() {
            @Override
            public void onResponse(Call<PhotoPool> call, Response<PhotoPool> response) {
                PhotoPool photo = response.body();
                //Si nos la envia, la añadimos a la lista.
                if (photo.isReady()) {
                    photoReceived = true;
                    if(photo.getPicture() != null) gridViewAdapter.addPhotoToList(photo.getPicture());
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<PhotoPool> call, Throwable t) {

            }
        });
        return photoReceived;
    }

    private void setSubjectStyle() {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView name = (TextView) hView.findViewById(R.id.nav_bar_header_name);
        name.setText(subject.getShort_name());

    }
    public void filterFavorites(boolean favMode) {
        if(favMode) {
            ArrayList<Photo> filteredPhotos = new ArrayList<>();
            ArrayList<String> favPhotos;
            String actualSubject = mySPHelper.getCurrentSubject();
            favPhotos = mySPHelper.getFavPhotos(actualSubject);
            if(favPhotos == null) {
                Toast.makeText(getActivity(), "No favorites added.", Toast.LENGTH_SHORT).show();
                return;
            }
            for(Photo photo: photoList) {
                if(favPhotos.contains("" + photo.getId())) {
                    filteredPhotos.add(photo);
                }
            }
            gridViewAdapter.updateList(filteredPhotos);
        } else gridViewAdapter.updateList(photoList);

    }


}