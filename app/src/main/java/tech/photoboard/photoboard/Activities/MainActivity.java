package tech.photoboard.photoboard.Activities;

import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.Intent;
<<<<<<< HEAD
import android.net.Uri;
import android.os.Bundle;
=======
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.solver.SolverVariable;
>>>>>>> origin/master
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
<<<<<<< HEAD
import android.view.Gravity;
=======
>>>>>>> origin/master
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
=======
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

>>>>>>> origin/master
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.photoboard.photoboard.API.ApiClient;
import tech.photoboard.photoboard.API.RetrofitAPI;
import tech.photoboard.photoboard.Adapter.SubjectsListAdapter;
import tech.photoboard.photoboard.Classes.Subject;
import tech.photoboard.photoboard.Classes.User;
import tech.photoboard.photoboard.R;
import tech.photoboard.photoboard.fragments.GalleryFragment;
import tech.photoboard.photoboard.fragments.SubjectFragment;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SubjectsListAdapter.SubjectsGalleryManager,BluetoothListDialogFragment.OnItemSelectedListener

{

    public static final String SUBJECTS_LIST = "SUBJECT_LIST";
<<<<<<< HEAD
    public static final String SUBJECT_ID = "SUBJECT_ID";
=======
>>>>>>> origin/master

    private SubjectFragment subjectFragment;
    private GalleryFragment galleryFragment;
    final RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
    private ArrayList<Subject> subjects;
    private User user;
<<<<<<< HEAD
    private MySPHelper mySPHelper;
=======
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
>>>>>>> origin/master


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        mySPHelper = new MySPHelper(this);
        user = mySPHelper.getUser();
        createSubjectListFragment();
        setAppBarContent();

    }

=======
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        getUserFromSharedPreferences();
        setAppBarContent();
        createSubjectListFragment();
    }

   public void getUserFromSharedPreferences() {
       Gson gson = new Gson();
       String json = sharedPreferences.getString(LoginActivity.USER, null);
       user = gson.fromJson(json, User.class);
   }
>>>>>>> origin/master

    @Override
    public void openGallery(Subject subject) {

<<<<<<< HEAD
        /*This will allow us to know at every moment in which subject are we*/
        mySPHelper.setCurrentSubject(subject);

        /*Process to change the fragment*/
=======
>>>>>>> origin/master
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        galleryFragment = GalleryFragment.newInstance(subject);
        transaction.replace(R.id.fragment_container, galleryFragment, "HELLO");
        transaction.addToBackStack("HELLO");
        transaction.commit();

    }

    private void createSubjectListFragment() {
        subjects = new ArrayList<>();
        getSubjectsFromServer();
<<<<<<< HEAD
        /*subjects.add(new Subject(0,"PSAVC", "AUDIO-VIDEO"));
        subjects.add(new Subject(0, "RP","PHYSICS"));
        subjects.add(new Subject(0,"PBE", "PROJECT"));
        subjects.add(new Subject(0, "TD", "PROGRAMMING"));
        subjects.add(new Subject(0, "DSBM","ELECTRONICS"));*/
=======
        subjects.add(new Subject(0,"PSAVC", "AUDIO-VIDEO"));
        subjects.add(new Subject(0, "RP","PHYSICS"));
        subjects.add(new Subject(0,"PBE", "PROJECT"));
        subjects.add(new Subject(0, "TD", "PROGRAMMING"));
        subjects.add(new Subject(0, "DSBM","ELECTRONICS"));
>>>>>>> origin/master

        /*Saving subjects in sharedpreferences*/
        /*This is done in order to pass the subjects to the fragment*/
        /*Setting the subjects container fragment*/
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        subjectFragment = SubjectFragment.newInstance(subjects);
        transaction.replace(R.id.fragment_container, subjectFragment);
        transaction.commit();
    }

    private void setAppBarContent() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
<<<<<<< HEAD
        toolbar.setNavigationIcon(null);

=======
>>>>>>> origin/master

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setNavigationHeaderStyle(navigationView);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void setNavigationHeaderStyle( NavigationView navigationView) {
        View hView =  navigationView.getHeaderView(0);
        LinearLayout background = (LinearLayout) hView.findViewById(R.id.nav_bar_header_background);
        TextView name = (TextView) hView.findViewById(R.id.nav_bar_header_name);
        TextView userName = (TextView) hView.findViewById(R.id.nav_bar_header_user);
        ImageView icon = (ImageView) hView.findViewById(R.id.nav_bar_header_icon);

        background.setBackgroundResource(R.drawable.nav_background_campus_nord);
        name.setText("UPC - Telecom");
        if(user != null) userName.setText(user.getEmail());
        Picasso.with(getApplicationContext()).load(R.drawable.nav_icon_upc).resize(150,150).into(icon);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

<<<<<<< HEAD
=======
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

>>>>>>> origin/master
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
<<<<<<< HEAD
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
=======
>>>>>>> origin/master

        if (id == R.id.nav_camera) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://atenea.upc.edu/moodle/login/index.php"));
            startActivity(browserIntent);
<<<<<<< HEAD
        }else if(id==R.id.nav_fav) {
            if(galleryFragment == null) {
                Toast.makeText(this, "No subject selected.", Toast.LENGTH_SHORT).show();
                return true;
            }
            galleryFragment.filterFavorites();
            return true;
        }

        else if (id == R.id.nav_git_hub) {
=======
        }else if (id == R.id.nav_git_hub) {
>>>>>>> origin/master

            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/photoboard"));
                startActivity(browserIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request."
<<<<<<< HEAD
                        + " Please install a web browser",  Toast.LENGTH_LONG).show();
=======
                        + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
>>>>>>> origin/master
                e.printStackTrace();
            }

        } else if (id == R.id.nav_log_out) {
<<<<<<< HEAD
            mySPHelper.setLoggedIn(false);
            mySPHelper.removeUser();

=======

            editor.remove(LoginActivity.LOGGED_IN);
            editor.remove(LoginActivity.USER);
            editor.commit();
>>>>>>> origin/master
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

<<<<<<< HEAD
        drawer.closeDrawer(GravityCompat.END);



=======
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
>>>>>>> origin/master
        return true;
    }

    @Override
    public void onItemSelected(BluetoothDevice bd) {
        galleryFragment.onItemSelected(bd);
    }

    public void getSubjectsFromServer () {
        Call<ArrayList<Subject>> getSubjects = retrofitAPI.getSubjectsList();
        getSubjects.enqueue(new Callback<ArrayList<Subject>>() {
            @Override
            public void onResponse(Call<ArrayList<Subject>> call, Response<ArrayList<Subject>> response) {
                subjects = response.body();
                for (int i = 0; i < subjects.size() && subjects!=null; i++) {
                    Log.e("Message", subjects.get(i).getName() + " has been added.");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Subject>> call, Throwable t) {
                Log.e("Message", "Could not retrieve subjects.");
            }
        });
    }
}



