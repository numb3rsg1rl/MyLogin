package watmok.tacoma.uw.edu.mylogin;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

/**
 * The activity that acts as a fragment container for the Favorites Hike list. You land here when you
 * click on the View Favorites button and logging off sends you back to main,
 * at which point you need to sign in again to get back to the main menu.
 */
public class FavoritesActivity extends AppCompatActivity implements FavoritesFragment.OnListFragmentInteractionListener {

    private GoogleApiClient mGoogleApiClient;
    /**
     * Creates the activity, and instantiates the HikeFragment inside. Also creates the logout button.
     * When the activity is created, the content view is set to
     * @param savedInstanceState The
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorites);

        //instantiate the Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        if (savedInstanceState == null
                && getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            FavoritesFragment hikeFragment = new FavoritesFragment();
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
     * Gets the name of the hike, and passes it through the intent
     * to create a HikeDetail fragment of that Hike.
     * @param item
     */
    @Override
    public void onListFragmentInteraction(Hike item) {
        String trailName = item.getmHikeName();
        Intent intent = new Intent(FavoritesActivity.this, HikeDetailActivity.class);
        intent.putExtra("PREVIOUS_ACTIVITY","Saved_List");
        intent.putExtra("TRAIL_NAME",trailName);
        startActivity(intent);
    }

    /**
     * onStart() allows the Google API to be initialized so it can be used throughout the rest of
     * the app
     */
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    /**
     * provides functionality for menu items
     * If BACK then the user is redirected to the main menu
     * If SIGN OUT then the user is signed out of the app and brought back to
     * the login Page, with a Toast Message saying that the user is logged out
     * @param item the menu item that has been selected
     * @return always true
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.back_item) {
            Intent i = new Intent(FavoritesActivity.this,
                    MainMenuActivity.class);
            startActivity(i);
            finish();
        } else if (item.getItemId() == R.id.logout_item) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
        }

        return true;
    }
}
