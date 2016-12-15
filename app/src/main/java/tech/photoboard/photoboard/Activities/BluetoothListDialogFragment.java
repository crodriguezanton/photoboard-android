package tech.photoboard.photoboard.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import tech.photoboard.photoboard.Adapter.BluetoothListAdapter;
import tech.photoboard.photoboard.R;

/**
 * Created by pc1 on 23/10/2016.
 */

public class BluetoothListDialogFragment extends DialogFragment {

    private ListView devicesList;
    private Button btnSearch;
    private Button btnDismiss;

    private BluetoothListAdapter arrayAdapter;
    private ArrayList<String> arrayBluetooth;
    private HashMap<String, BluetoothDevice> mapBluetooth;
    private BluetoothAdapter bluetoothAdapter;
    OnItemSelectedListener myItemSelectedListener;
    Context context;


    public BluetoothListDialogFragment(){
        //Empty constructor needed for Dialog
    }


    //Interface to pass data between the FragmentDialog and the father Activity
    public interface OnItemSelectedListener {
        void onItemSelected(BluetoothDevice bd);
    }


    public void searchDevices (){
        //Start discovery
        bluetoothAdapter.startDiscovery();
        arrayBluetooth.clear();

        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (true && !mapBluetooth.containsValue(device)) {
                            arrayBluetooth.add(device.getName() + "\n" + device.getAddress());
                            mapBluetooth.put(device.getName(), device);
                            arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);

        //Display the Edison Devices
        devicesList.setAdapter(arrayAdapter);

    }

    //Resize the Fragment
    @Override
    public void onResume() {

        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();

        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        // Set the width of the dialog proportional of the screen width and with height too
        window.setLayout((int) (size.x * 0.89), (int) (size.y * 0.78));
        window.setGravity(Gravity.CENTER);

        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Delete the Dialog title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.fragment_dialog_list_bluetooth, container, false);
        btnSearch = (Button) v.findViewById(R.id.btn_search_devices);
        btnDismiss = (Button) v.findViewById(R.id.btn_dismiss);
        devicesList = (ListView) v.findViewById(R.id.lv_bluetooth_devices);
        context = getActivity().getApplicationContext();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        arrayBluetooth = new ArrayList<>();
        mapBluetooth = new HashMap<>();
        arrayAdapter = new BluetoothListAdapter(arrayBluetooth, getContext());

        searchDevices();

        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] name = arrayBluetooth.get(position).split("\n");
                BluetoothDevice bd = mapBluetooth.get(name[0]);
                if(bd != null) {
                    //Pass the device to the container activity
                    myItemSelectedListener.onItemSelected(bd);
                    bluetoothAdapter.cancelDiscovery();
                    dismiss();
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdapter.cancelDiscovery();
                searchDevices();
            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdapter.cancelDiscovery();
                dismiss();
            }
        });

        return v;
    }

    // Verify that the host activity implements the callback interface onItemSelectedListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //Instantiate the EditNameDialogListener so we can send events to the host
            myItemSelectedListener = (OnItemSelectedListener) context;
        } catch (ClassCastException e) {
            //The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditNameDialogListener");
        }
    }


}