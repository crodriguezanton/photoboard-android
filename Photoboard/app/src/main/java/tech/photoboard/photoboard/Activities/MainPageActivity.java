package tech.photoboard.photoboard.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import tech.photoboard.photoboard.R;

public class MainPageActivity extends AppCompatActivity {
    public static final int PHOTO_MODE = 10;
    public static final int BLUETOOTH_MODE = 11;
    public int currentMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
