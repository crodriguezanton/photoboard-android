package tech.photoboard.photoboard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import tech.photoboard.photoboard.Activities.ImageViewerActivity;
import tech.photoboard.photoboard.Photo;
import tech.photoboard.photoboard.R;
import tech.photoboard.photoboard.Utils.SquareImageView;

/**
 * Created by pc1 on 23/10/2016.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private List<Photo> photoList;

    public GridViewAdapter(Context context, ArrayList<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }


    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SquareImageView img;
        if(convertView == null) {
            img = new SquareImageView(context);
            convertView = img;
            img.setScaleType(SquareImageView.ScaleType.CENTER_CROP);


        } else {

            img = (SquareImageView) convertView;

        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Llama a la nueva actividad

                Intent intent = new Intent(context, ImageViewerActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(photoList);
                intent.putExtra("FULLSCREEN_IMAGES",json);
                intent.putExtra("POSITION",position);
                context.startActivity(intent);

            }
        });

        Picasso.with(context)
                .load(photoList.get(position).getURL())
                .noFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(img);

        return convertView;
    }
}
