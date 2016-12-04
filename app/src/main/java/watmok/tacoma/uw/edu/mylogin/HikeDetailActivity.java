package watmok.tacoma.uw.edu.mylogin;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

public class HikeDetailActivity extends AppCompatActivity {

    private String myHikeName;
    private String lastActivity;
    private Hike mHike;
    private List<Hike> mhikeList;

    private static final String HIKES_URL = "http://cssgate.insttech.washington.edu/~debergma/hikes.php?cmd=hike_detail";
    private static final String HIKES_URL2 = "http://cssgate.insttech.washington.edu/~debergma/hikes.php?cmd=hike_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);
        Intent intent = getIntent();
        if (intent.getStringExtra("PREVIOUS_ACTIVITY").equals("map")) {
            setUpFromMap(intent.getStringExtra("TRAIL_NAME"));
        }
        //instantiate the Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        DownloadHikesTask task = new DownloadHikesTask();
        task.execute(HIKES_URL);

        waitForHikeTask();

        DownloadPicturesTask task1 = new DownloadPicturesTask();
        task1.execute(HIKES_URL2);

    }

    /**
     * Sets up the activity with parameters from the map
     */
    protected void setUpFromMap(String hikeName) {
        lastActivity = "Map";
        myHikeName = hikeName;
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
     * Waits up to 2 seconds for the DownloadHikesTask to load data into mHikeList;
     */
    private void waitForHikeTask() {
        double counter = 0;
        while (counter < 2 && mhikeList == null) {
            try {
                TimeUnit.SECONDS.sleep(1);
                counter++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
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
     * Helper method to create the text for the elevation imageView
     *
     * @param theElevation the elevation returned by the database
     * @return A String that can go in the imageView
     */
    private String getMaxElevationText(String theElevation) {
        if (theElevation != null) return "Max Elevation in Feet: " + theElevation;
        return "Max Elevation in Feet: Not Available";
    }

    /**
     * Helper method to create the text for the elevation gain imageView
     *
     * @param theElevationGain
     * @return A String that can go in the imageView
     */
    private String getElevationGainText(String theElevationGain) {
        if (theElevationGain != null) return "Elevation Gain in Feet: " + theElevationGain;
        return "Elevation Gain in Feet: Not Available";
    }

    /**
     * Helper method to create the text for the trail length imageView
     *
     * @param theLength the trail length from the mysql database
     * @return A String that can go in the imageView
     */
    private String getTrailLengthText(String theLength) {
        if (theLength != null) return "Trail Length in Miles: " + theLength;
        return "Trail Length in Miles: Not Available";
    }

    /**
     * Takes the saved values from mHike and put them into the layout.
     */
    private void displayHike() {
        TextView title = (TextView) findViewById(R.id.hike_name);
        title.setText(myHikeName);

        ImageView picture = (ImageView) findViewById(R.id.imageView);
        picture.setImageBitmap(mHike.getmPicture());

        TextView length = (TextView) findViewById(R.id.trail_length);
        length.setText(getTrailLengthText(mHike.getmLength()));

        TextView maxElevation = (TextView) findViewById(R.id.elevation);
        maxElevation.setText(getMaxElevationText(mHike.getmMaxElevation()));

        TextView elevationGain = (TextView) findViewById(R.id.elevation_gain);
        elevationGain.setText(getElevationGainText(mHike.getmElevationGain()));

        TextView description = (TextView) findViewById(R.id.description);
        description.setText(mHike.getmLongDescription());

        TextView reviews = (TextView) findViewById(R.id.reviews);
        reviews.setText(mHike.getmReviews());

    }

    /**
     * A nested AsyncTask class that performs the actual business of connecting to the web service.
     */
    private class DownloadHikesTask extends AsyncTask<String, Void, String> {

        /**
         * Uses the URL(s) for the webservice to check for service and connect to the Hike database
         *
         * @param urls the url(s) for the web service
         * @return a String with a JSON message, if successful, or an error message if something
         * went wrong.
         */
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            StringBuilder builder = new StringBuilder();
            HttpURLConnection urlConnection = null;

            for (String url : urls) {
                try {

                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = reader.readLine()) != null) {
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
         *
         * @param result
         */
        protected void onPostExecute(String result) {
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            mhikeList = new ArrayList<>();
            result = Hike.parseHikeJSON(result, mhikeList, true);
            if (result != null) {
                Toast.makeText(getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }


        }



    }
    /**
     * A nested AsyncTask class that performs the actual business of connecting to the web service.
     */
    private class DownloadPicturesTask extends AsyncTask<String, Void, String> {

        /**
         * Uses the URL(s) for the webservice to check for service and connect to the Hike database
         *
         * @param urls the url(s) for the web service
         * @return a String with a JSON message, if successful, or an error message if something
         * went wrong.
         */
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            StringBuilder builder = new StringBuilder();
            HttpURLConnection urlConnection = null;

            for (String url : urls) {
                try {

                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = reader.readLine()) != null) {
                        builder.append(s);
                    }
                    response = builder.toString();

                } catch (Exception e) {
                    response = "Unable to download the pictures list. Reason: " + e.getMessage();
                }
            }

            return response;
        }

        /**
         * Makes a toast if there was an error message to display it.
         * Otherwise, calls the Hike class parseHikeJSON() method to fill the Hike list, and
         * then passes that list with the HikeFragments mListener to the RecycleViewAdapter.
         *
         * @param result
         */
        protected void onPostExecute(String result) {
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            result = Hike.parseImageJSON(result, mhikeList);
            if (result != null) {
                Toast.makeText(getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            for (Hike hike: mhikeList) {
                if (hike.getmHikeName().equals(myHikeName)) {
                    mHike = hike;
                }
            }

            displayHike();


        }
    }

}
