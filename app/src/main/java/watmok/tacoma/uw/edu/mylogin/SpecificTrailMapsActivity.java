package watmok.tacoma.uw.edu.mylogin;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

public class SpecificTrailMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private String mTrailName;
    private Hike mHike;
    private static final String HIKES_URL = "http://cssgate.insttech.washington.edu/~debergma/hikes.php?cmd=hikes1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_trail_maps);

        //instantiate the Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_stm_toolbar);
        setSupportActionBar(myToolbar);


        // Get trail name from the intent
        Intent intent = getIntent();
        mTrailName = intent.getStringExtra("TRAIL_NAME");

        DownloadHikesTask task = new DownloadHikesTask();
        task.execute(HIKES_URL);
        waitForHikeTask();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     *
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
     * Waits up to 2 seconds for the DownloadHikesTask to load data into mHike;
     */
    private void waitForHikeTask() {
        double counter = 0;
        while (counter < 2 && mHike == null) {
            try {
                TimeUnit.SECONDS.sleep(1);
                counter++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * provides functionality for menu items
     *
     * @param item the menu item that has been selected
     * @return always true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back_item) {
            Intent i = new Intent(SpecificTrailMapsActivity.this,
                        HikeDetailActivity.class);


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
                        }
                    });

        }

        return true;
    }

    /**
     * Inflates the menu Layout onto the toolbar
     *
     * @param menu - the menu that needs a layout, in this case the Toolbar from onCreate()
     * @return returns true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng trailhead = mHike.getmCoordinates();
        mMap.addMarker(new MarkerOptions().position(trailhead).title(mHike.getmHikeName())
                .snippet(mHike.getmShortDescription()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trailhead,9));
    }

    private class DownloadHikesTask extends AsyncTask<String, Void, String> {

        /**
         * Uses the URL(s) for the webservice to check for service and connect to the Hike database
         * @param urls the url(s) for the web service
         * @return a String with a JSON message, if successful, or an error message if something
         * went wrong.
         */
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            StringBuilder builder = new StringBuilder();
            HttpURLConnection urlConnection = null;

            for (String url:urls) {
                try {

                    URL urlObject = new URL(url);
                    urlConnection  = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s=reader.readLine())!=null) {
                        builder.append(s);
                    }
                    response = builder.toString();

                } catch (Exception e) {
                    response = "Unable to download the Hike list. Reason: " + e.getMessage();
                }
            }

            return response;
        }

        /**
         * Makes a toast if there was an error message to display it.
         * Otherwise, calls the Hike class parseHikeJSON() method to fill the Hike list, and
         * then passes that list with the HikeFragments mListener to the RecycleViewAdapter.
         * @param result
         */
        protected void onPostExecute(String result) {
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(),
                        result,Toast.LENGTH_LONG).show();
                return;
            }
            ArrayList<Hike> mHikeList = new ArrayList<>();
            result = Hike.parseHikeJSON(result,mHikeList,true);
            if (result != null) {
                Toast.makeText(getApplicationContext(),
                        result,Toast.LENGTH_LONG).show();
                return;


            }
            if (!mHikeList.isEmpty()) {
                for (Hike hike: mHikeList) {
                    if  (hike.getmHikeName().equals(mTrailName)) {
                        mHike = hike;
                    }
                }
            }


        }
    }
}
