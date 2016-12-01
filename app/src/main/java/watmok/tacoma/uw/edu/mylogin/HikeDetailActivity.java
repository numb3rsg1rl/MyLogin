package watmok.tacoma.uw.edu.mylogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

public class HikeDetailActivity extends AppCompatActivity {

    private String myHikeName;
    private String lastActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);
        Intent intent = getIntent();
        if (intent.getStringExtra("PREVIOUS_ACTIVITY").equals("map")){
            setUpFromMap(intent.getStringExtra("TRAIL_NAME"));
        }
        //instantiate the Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }

    /**
     * Sets up the activity with parameters from the map
     */
    protected void setUpFromMap (String hikeName){
        lastActivity = "Map";
        myHikeName = hikeName;
    }

    /**
     * provides functionality for menu items
     * @param item the menu item that has been selected
     * @return always true
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.back_item) {
            Intent i;
            if (lastActivity.equals("map")) {
                i = new Intent(HikeDetailActivity.this,
                        TrailMapActivity.class);
            } else {
                i = new Intent(HikeDetailActivity.this,
                        HikeActivity.class);
            }
            startActivity(i);
            finish();
        } else if (item.getItemId() == R.id.logout_item) {
            Intent i = new Intent(HikeDetailActivity.this,
                    MainActivity.class);
            startActivity(i);
            finish();
        }

        return true;
    }

    /**
     * Inflates the menu Layout onto the toolbar
     * @param menu - the menu that needs a layout, in this case the Toolbar from onCreate()
     * @return returns true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

}
