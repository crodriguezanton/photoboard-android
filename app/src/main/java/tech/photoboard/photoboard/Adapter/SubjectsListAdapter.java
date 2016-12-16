package tech.photoboard.photoboard.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tech.photoboard.photoboard.Classes.Subject;
import tech.photoboard.photoboard.R;


/**
 * Created by Elias on 02/12/2016.
 */

public class SubjectsListAdapter extends BaseAdapter {

    public ArrayList<Subject> subjects;
    private Context context;

    public SubjectsListAdapter(Context context, ArrayList<Subject> subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Object getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView subjectTextView;
        LinearLayout background;
        if(v  == null) {
            v= LayoutInflater.from(context).inflate(R.layout.adapter_subjects_list,null);
        }

        subjectTextView = (TextView) v.findViewById(R.id.subject_name);
        background = (LinearLayout) v.findViewById(R.id.lv_item_background);

        background.setBackgroundResource(R.color.colorProgramming);

        String subjectName = subjects.get(position).getName();
        if( subjectName!= null) {
            subjectTextView.setText(subjects.get(position).getName());
        } else subjectTextView.setText("Null");

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof SubjectsGalleryManager){
                    if(context instanceof SubjectsGalleryManager){
                        ((SubjectsGalleryManager)context).openGallery(subjects.get(position));
                    }
                }
            }
        });
        /*Fill the parent with the the subjects*/
        //v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, parent.getHeight() / subjects.size()));

        return v;
    }


    public interface SubjectsGalleryManager {
        void openGallery(Subject subject);
    }

}
