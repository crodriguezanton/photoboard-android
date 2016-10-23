package tech.photoboard.photoboard.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tech.photoboard.photoboard.Adapter.GridViewAdapter;
import tech.photoboard.photoboard.Photo;
import tech.photoboard.photoboard.R;

public class MainPageActivity extends AppCompatActivity {
    public static final int PHOTO_MODE = 10;
    public static final int BLUETOOTH_MODE = 11;
    public int currentMode;
    private ArrayList<Photo> photoList;
    private GridView gridview;
    private GridViewAdapter gridViewAdapter;

    public String[] photos = {
            "http://www.qdtricks.org/wp-content/uploads/2015/02/hd-wallpapers-1080p-for-mobile.png",
            "http://media.salemwebnetwork.com/cms/CROSSCARDS/31680-27279-june-2015-faith-bigger-than-fear-iphone-6-plus.jpg",
            "http://www.mobileswall.com/wp-content/uploads/2015/12/300-Play-the-Game-Who-is-to-Come-l.jpg",
            "http://cdn.wonderfulengineering.com/wp-content/uploads/2014/05/mobile-wallpaper-16-610x1084.jpg",
            "http://cdn.wonderfulengineering.com/wp-content/uploads/2014/07/black-tiger-mobile-phone-wallpaper.jpg",
            "http://cdn.wonderfulengineering.com/wp-content/uploads/2014/05/mobile-samsung-16-610x1084.jpg",
            "http://www.mobileswall.com/wp-content/uploads/2015/11/300-Black-Baroque-Pattern-l.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        photoList = new ArrayList<>();
        int i =0;
        for(String p: photos) {
            photoList.add(new Photo(p));
        }

        gridview = (GridView) findViewById(R.id.gv_main_page);
        gridViewAdapter = new GridViewAdapter(MainPageActivity.this,photoList);
        gridview.setAdapter(gridViewAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_take_capture);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(currentMode) {
                    case PHOTO_MODE:
                        Toast.makeText(MainPageActivity.this, "Taking Photo", Toast.LENGTH_SHORT).show();
                        break;
                    case BLUETOOTH_MODE:
                        Toast.makeText(MainPageActivity.this, "Searching Bluetooth", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainPageActivity.this, "Something wrong is going on here", Toast.LENGTH_SHORT).show();

                }
               /*nackbar.make(view, "What in the world is going on", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            */}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
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
}
