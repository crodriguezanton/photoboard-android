package tech.photoboard.photoboard.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

<<<<<<< HEAD
import tech.photoboard.photoboard.Activities.ImageViewerActivity;
import tech.photoboard.photoboard.Activities.LoginActivity;
import tech.photoboard.photoboard.Activities.MainActivity;
import tech.photoboard.photoboard.Activities.MySPHelper;
=======
import tech.photoboard.photoboard.Activities.LoginActivity;
import tech.photoboard.photoboard.Activities.MainActivity;
>>>>>>> origin/master
import tech.photoboard.photoboard.Adapter.SubjectsListAdapter;
import tech.photoboard.photoboard.Classes.Subject;
import tech.photoboard.photoboard.Classes.User;
import tech.photoboard.photoboard.R;


/**
 * Created by Elias on 02/12/2016.
 */

public class SubjectFragment extends Fragment {

    private ListView subjectsList;
    public ArrayList<Subject> subjects;
    private SubjectsListAdapter subjectsListAdapter;
<<<<<<< HEAD
    private MySPHelper mySPHelper;
=======
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
>>>>>>> origin/master

    /*"Constructor"*/
    public static SubjectFragment newInstance(ArrayList<Subject> subjects) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putSerializable(MainActivity.SUBJECTS_LIST, gson.toJson(subjects));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        mySPHelper = new MySPHelper(getActivity());
        mySPHelper.setCurrentSubject(null);
=======
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        editor = sharedPreferences.edit();
>>>>>>> origin/master
        subjects = new ArrayList<>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        /*Extracting subjects from SharedPreferences*/
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Subject>>() {}.getType();
        subjects = gson.fromJson((String) getArguments().getSerializable(MainActivity.SUBJECTS_LIST), type);

        /*Setting the adapter*/
        subjectsList = (ListView) view.findViewById(R.id.lv_fragment);
        subjectsListAdapter = new SubjectsListAdapter(getActivity(), subjects);
        subjectsList.setAdapter(subjectsListAdapter);

<<<<<<< HEAD
        /*Setting the navigation header*/
        setNavigationDrawerStyle();
        return view;
    }
    private void setNavigationDrawerStyle() {
=======
        setNavigationDrawerStyle();
        /*Setting the navigation header*/
        return view;
    }
    private void setNavigationDrawerStyle() {

>>>>>>> origin/master
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        LinearLayout background = (LinearLayout) hView.findViewById(R.id.nav_bar_header_background);
        TextView name = (TextView) hView.findViewById(R.id.nav_bar_header_name);
        TextView userName = (TextView) hView.findViewById(R.id.nav_bar_header_user);
        ImageView icon = (ImageView) hView.findViewById(R.id.nav_bar_header_icon);

        background.setBackgroundResource(R.drawable.nav_background_campus_nord);
<<<<<<< HEAD
        Picasso.with(getActivity()).load(R.drawable.nav_icon_upc).resize(150,150).into(icon);
        name.setText("UPC - Telecom");
        User user = mySPHelper.getUser();
        if(user != null) userName.setText(user.getEmail());
    }
=======
        name.setText("UPC - Telecom");
        Picasso.with(getContext()).load(R.drawable.nav_icon_upc).resize(150,150).into(icon);
        User user = getUserFromSharedPreferences();
        if(user != null) userName.setText(user.getEmail());
    }

    public User getUserFromSharedPreferences() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LoginActivity.USER, null);
        return gson.fromJson(json, User.class);
    }



>>>>>>> origin/master
}
