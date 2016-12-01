package watmok.tacoma.uw.edu.mylogin;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

/**
 * The activity that acts as a fragment container for the Hike list. You land here when you sign in,
 * and logging off sends you back to main, at which point you need to sign in again to get back
 * here.
 * Note: At this point, the OnClickInteractionListener is implemented with an empty method. We will
 * be adding detail fragments later, at which point we will be adding content to that method.
 */
public class HikeActivity extends AppCompatActivity implements HikeFragment.OnListFragmentInteractionListener {

    /**
     * Creates the activity, and instantiates the HikeFragment inside. Also creates the logout button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hike);

        //instantiate the Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        if (savedInstanceState == null
            && getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            HikeFragment hikeFragment = new HikeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, hikeFragment).commit();
        }



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

    /**
     * What happens when you click on a Hike item from the list.
     * Currently does nothing. Content will be added to this method at a later point, upon the
     * creation of a HikeDetail fragment.
     * @param item
     */
    @Override
    public void onListFragmentInteraction(Hike item) {
        Intent i = new Intent(HikeActivity.this, HikeDetailActivity.class);
        startActivity(i);
    }

    /**
     * provides functionality for menu items
     * @param item the menu item that has been selected
     * @return always true
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.back_item) {
            Intent i = new Intent(HikeActivity.this,
                    MainMenuActivity.class);
            startActivity(i);
            finish();
        } else if (item.getItemId() == R.id.logout_item) {
            Intent i = new Intent(HikeActivity.this,
                    MainActivity.class);
            startActivity(i);
            finish();
        }

        return true;
    }
}
