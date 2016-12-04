package watmok.tacoma.uw.edu.mylogin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

public class TrailMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnInfoWindowLongClickListener {


    private GoogleMap mMap;
    private List<Hike> mHikeList;
    private static final String HIKES_URL = "http://cssgate.insttech.washington.edu/~debergma/hikes.php?cmd=hikes1";
    private static final int MY_PERMISSIONS_LOCATIONS = 0;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trail_map);

        //instantiate the Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // fill the Hike List from the mysql server

        DownloadHikesTask task = new DownloadHikesTask();
        task.execute(HIKES_URL);

        waitForHikeTask();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * provides functionality for menu items
     * @param item the menu item that has been selected
     * @return always true
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.back_item) {
            Intent i = new Intent(TrailMapActivity.this,
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
//            Intent i = new Intent(TrailMapActivity.this,
//                    MainActivity.class);
//            startActivity(i);
//            finish();
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

    /**
     * Waits up to 2 seconds for the DownloadHikesTask to load data into mHikeList;
     */
    private void waitForHikeTask() {
        double counter = 0;
        while (counter < 2 && mHikeList == null) {
            try {
                TimeUnit.SECONDS.sleep(1);
                counter++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
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
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_LOCATIONS);

        }

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowLongClickListener(this);

        mMap.setMyLocationEnabled(true);
        Location currentLocation = getMyLocation();

        //check that location was found
        if(currentLocation!=null) {
            // recenter on current location and zoom in
            LatLng position = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
            Log.d("position:", "Position = " + position.toString());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,9));


        } else {
            Log.e("position", "null position");
        }

        // Add a marker for each hike
        if (mHikeList != null && !mHikeList.isEmpty()) {
            for (Hike hike : mHikeList) {

                mMap.addMarker(new MarkerOptions().position(hike.getmCoordinates())
                        .title(hike.getmHikeName()).snippet(hike.getmShortDescription()));
            }
        } else {
            Log.e("null", "Unable to generate markers. mHikeList is null or empty");
        }


        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    /**
     * @author This code is from http://stackoverflow.com/questions/14502102/zoom-on-current-user-location-displayed/14511032#14511032
     * by DMCapps
     * @return the best guess at user;s current location
     */

    private Location getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_LOCATIONS);
        }

        // Get location from GPS if it's available
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Location wasn't found, check the next most accurate place for the current location
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider = lm.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            myLocation = lm.getLastKnownLocation(provider);
        }

        return myLocation;
    }

    /**
     *
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        String trailName = marker.getTitle();
        Intent intent = new Intent(TrailMapActivity.this, HikeDetailActivity.class);
        intent.putExtra("PREVIOUS_ACTIVITY","Map");
        intent.putExtra("TRAIL_NAME",trailName);
        startActivity(intent);

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        String trailName = marker.getTitle();
        Intent intent = new Intent(TrailMapActivity.this, HikeDetailActivity.class);
        intent.putExtra("PREVIOUS_ACTIVITY","Map");
        intent.putExtra("TRAIL_NAME",trailName);
        startActivity(intent);
    }


    /**
     * A nested AsyncTask class that performs the actual business of connecting to the web service.
     */
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
            mHikeList = new ArrayList<>();
            result = Hike.parseHikeJSON(result,mHikeList,true);
            if (result != null) {
                Toast.makeText(getApplicationContext(),
                        result,Toast.LENGTH_LONG).show();
                return;
            }


        }
    }
}
