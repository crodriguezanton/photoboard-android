package tech.photoboard.photoboard.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import tech.photoboard.photoboard.*;
import java.util.ArrayList;

import tech.photoboard.photoboard.Activities.ImageViewerActivity;
import tech.photoboard.photoboard.Photo;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by pc1 on 23/10/2016.
 */

public class FullScreenImageAdapter extends PagerAdapter {

    private ImageViewerActivity activity;
    private LayoutInflater inflater;
    private ArrayList<Photo> photoList;
    private ImageButton btnDownload;

    // constructor
    public FullScreenImageAdapter(ImageViewerActivity activity,
                                  ArrayList<Photo> photoList) {
        super();
        this.activity = activity;
        this.photoList = photoList;
    }

    @Override
    public int getCount() {
        return this.photoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imgDisplay;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View viewLayout = inflater.inflate(R.layout.adapter_fullscreen_image, container,false);
        imgDisplay = (ImageView) viewLayout.findViewById(R.id.iv_fullscreen_adapter);
        Picasso.with(activity)
                .load(photoList.get(position).getURL())
                .into(imgDisplay);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imgDisplay);

        ((ViewPager) container).addView(viewLayout);



        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);

    }


}
