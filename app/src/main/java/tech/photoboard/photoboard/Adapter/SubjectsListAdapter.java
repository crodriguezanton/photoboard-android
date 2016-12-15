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
        ImageView subjectIcon;
        if(v  == null) {
            v= LayoutInflater.from(context).inflate(R.layout.adapter_subjects_list,null);
        }

        subjectTextView = (TextView) v.findViewById(R.id.subject_name);
        subjectIcon = (ImageView) v.findViewById(R.id.iv_subject_icon);
        background = (LinearLayout) v.findViewById(R.id.lv_item_background);

        String subjectName = subjects.get(position).getName();
        if( subjectName!= null) {
            subjectTextView.setText(subjects.get(position).getName());
        } else subjectTextView.setText("Null");
        if (subjects.get(position).getGroup()!=null){
            setSubjectStyle(subjects.get(position).getGroup(), subjectIcon, background);
        }


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

    private void setSubjectStyle(String area, ImageView subjectIcon, LinearLayout background) {
        switch (area) {
            case "PHYSICS":
                subjectIcon.setImageResource(R.drawable.ic_physics_black);
                background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPhysics));
                //background.setBackgroundResource(R.drawable.background_physics);
                break;
            case "AUDIO-VIDEO":
                subjectIcon.setImageResource(R.drawable.ic_audio_video_black);
                background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAudioVideo));
                //background.setBackgroundResource(R.drawable.background_audio_video);
                break;
            case "PROJECT":
                subjectIcon.setImageResource(R.drawable.ic_project_black);
                background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorProject));
                //background.setBackgroundResource(R.drawable.background_project);
                break;
            case "ELECTRONICS":
                subjectIcon.setImageResource(R.drawable.ic_electronics_black);
                background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorElectronics));
                //background.setBackgroundResource(R.drawable.background_electronics);
                break;
            case "PROGRAMMING":
                subjectIcon.setImageResource(R.drawable.ic_programming_black);
                background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorProgramming));
                //background.setBackgroundResource(R.drawable.background_programming);
                break;
            default:
                break;
        }
    }

    public interface SubjectsGalleryManager {
        void openGallery(Subject subject);
    }

}
