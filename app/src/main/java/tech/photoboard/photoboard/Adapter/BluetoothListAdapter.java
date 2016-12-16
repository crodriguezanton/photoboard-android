package tech.photoboard.photoboard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tech.photoboard.photoboard.R;

/**
 * Created by sergi on 09/11/2016.
 */

public class BluetoothListAdapter extends BaseAdapter {


    ArrayList<String> devices;
    TextView textView;
    Context context;

    public BluetoothListAdapter (ArrayList<String> devices, Context context) {
        this.devices = devices;
        this.context = context;
    }
    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.adapter_bluetooth_list,null);
        }
        String device = (String) getItem(position);
        textView = (TextView) v.findViewById(R.id.tv_bluetooth_devices_adapter);
        textView.setText(device);
        return v;
    }
}
