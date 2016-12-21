package tech.photoboard.photoboard.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.photoboard.photoboard.API.ApiClient;
import tech.photoboard.photoboard.API.RetrofitAPI;
import tech.photoboard.photoboard.Activities.ImageViewerActivity;
import tech.photoboard.photoboard.Activities.LoginActivity;
import tech.photoboard.photoboard.Activities.MainActivity;
import tech.photoboard.photoboard.Activities.MySPHelper;
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
    private MySPHelper mySPHelper;
    final RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySPHelper = new MySPHelper(getActivity());
        mySPHelper.setCurrentSubject(null);
        subjects = new ArrayList<>();

    }


    public void getSubjectsFromServer () {

        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);

        Call<ArrayList<Subject>> getSubjects = retrofitAPI.getSubjectsList(mySPHelper.getUser());
        getSubjects.enqueue(new Callback<ArrayList<Subject>>() {
            @Override
            public void onResponse(Call<ArrayList<Subject>> call, Response<ArrayList<Subject>> response) {
                subjects = response.body();
                if(subjects != null) {
                    for (int i = 0; i < subjects.size() && subjects != null; i++) {
                        Log.e("Message", subjects.get(i).getShort_name() + " has been added.");
                    }
                }
                mySPHelper.setSubjects(subjects);
                subjectsListAdapter.subjects = subjects;
                subjectsListAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
            }

            @Override
            public void onFailure(Call<ArrayList<Subject>> call, Throwable t) {
                Log.e("Message", "Could not retrieve subjects.");
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        /*Extracting subjects from SharedPreferences*/


        /*Setting the adapter*/
        subjectsList = (ListView) view.findViewById(R.id.lv_fragment);
        subjectsListAdapter = new SubjectsListAdapter(getActivity(), subjects);
        subjectsList.setAdapter(subjectsListAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_subject);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_1, R.color.refresh_2, R.color.refresh_3, R.color.refresh_4);
        swipeRefreshLayout.setEnabled(false);
        if(mySPHelper.getSubjects()==null) {
            getSubjectsFromServer();
        } else {
            subjectsListAdapter.subjects = mySPHelper.getSubjects();
            subjectsListAdapter.notifyDataSetChanged();
        }



        /*Setting the navigation header*/
        setNavigationDrawerStyle();
        return view;
    }
    private void setNavigationDrawerStyle() {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        LinearLayout background = (LinearLayout) hView.findViewById(R.id.nav_bar_header_background);
        TextView name = (TextView) hView.findViewById(R.id.nav_bar_header_name);
        TextView userName = (TextView) hView.findViewById(R.id.nav_bar_header_user);
        ImageView icon = (ImageView) hView.findViewById(R.id.nav_bar_header_icon);

        background.setBackgroundResource(R.drawable.nav_background_campus_nord);
        Picasso.with(getActivity()).load(R.drawable.nav_icon_upc).resize(150,150).into(icon);
        name.setText("UPC - Telecom");
        User user = mySPHelper.getUser();
        if(user != null) userName.setText(user.getEmail());
    }
}
