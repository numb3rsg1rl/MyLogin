package watmok.tacoma.uw.edu.mylogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.cketti.mailto.EmailIntentBuilder;
import watmok.tacoma.uw.edu.mylogin.hike.Hike;

public class HikeDetailActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private String myHikeName;
    private String lastActivity;
    private Hike mHike;
    private List<Hike> mHikeList;
    private Bitmap mBitmap;

    private static final String HIKES_URL
            = "http://cssgate.insttech.washington.edu/~debergma/hike_detail.php?cmd=hike_detail";
    private static final String HIKES_URL2
            = "http://cssgate.insttech.washington.edu/~debergma/pics/";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);
        Intent intent = getIntent();
        if (intent.getStringExtra("PREVIOUS_ACTIVITY").equals("Map")){
            setUpFromMap(intent.getStringExtra("TRAIL_NAME"));
        } else {
            setUpFromList(intent.getStringExtra("TRAIL_NAME"));
        }
        //instantiate the Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_hd_toolbar);
        setSupportActionBar(myToolbar);

        addButtons();

        DownloadHikesTask task = new DownloadHikesTask();
        task.execute(HIKES_URL);

    }

    /**
     * add functionality for the buttons
     */
    private void addButtons() {
        //set up findOnMap button
        Button findOnMap = (Button) findViewById(R.id.map_button);
        findOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trailName = myHikeName;
                Intent intent = new Intent(HikeDetailActivity.this, SpecificTrailMapsActivity.class);
                intent.putExtra("TRAIL_NAME",trailName);
                startActivity(intent);

            }
        });

        // set up email a friend button
        final Button email = (Button) findViewById(R.id.email_button);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailIntentBuilder.from(getApplicationContext())
                        .subject("Please join me for a hike at " + myHikeName)
                        .start();

            }
        });


        //Set up review buttons
        final Button submit = (Button) findViewById(R.id.submit_button);
        final EditText reviewBox = (EditText) findViewById(R.id.review_box);
        final Button postReview = (Button) findViewById(R.id.post_review_button);



        postReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reviewBox.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                postReview.setVisibility(View.GONE);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences shared = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String displayName = shared.getString(Name,"Name unavailable");
                String newReview = reviewBox.getText().toString() + "\n   Reviewed by: "+displayName;
                TextView oldReviewBox = (TextView) findViewById(R.id.reviews);
                if (!oldReviewBox.getText().toString().equals("No reviews yet.")) {
                    newReview = oldReviewBox.getText().toString() + "\n\n" + newReview;
                }

                try {
                    UploadReviewTask task = new UploadReviewTask();
                    String url =
                            "http://cssgate.insttech.washington.edu/~debergma/UpdateReviews.php?name="
                                    + URLEncoder.encode(myHikeName,"UTF-8") + "&reviews="
                                    + URLEncoder.encode(newReview,"UTF-8");
                    Log.e("URL: ", url);
                    task.execute(url);

                    postReview.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    reviewBox.setVisibility(View.GONE);
                    reviewBox.setText("");

                    Toast.makeText(getApplicationContext(),"Review Submitted",
                            Toast.LENGTH_SHORT).show();

                } catch (UnsupportedEncodingException e) {
                    Log.e("Upload Review", e.toString());
                    e.printStackTrace();
                }

            }
        });

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


    private void setUpFromList(String trail_name) {
        lastActivity = "List";
        myHikeName = trail_name;
    }

    /**
     * Sets up the activity with parameters from the map
     */
    protected void setUpFromMap (String hikeName){
        lastActivity = "Map";
        myHikeName = hikeName;
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

    /**
     * provides functionality for menu items
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
     * @param menu - the menu that needs a layout, in this case the Toolbar from onCreate()
     * @return returns true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    /**
     * Helper method to create the text for the elevation imageView
     *
     * @param theElevation the elevation returned by the database
     * @return A String that can go in the imageView
     */
    private String getMaxElevationText(String theElevation) {
        if (theElevation != null && !theElevation.equals("null")) return "Max Elevation in Feet: "
                + theElevation;
        return "Max Elevation in Feet: Not Available";
    }

    /**
     * Helper method to create the text for the elevation gain imageView
     *
     * @param theElevationGain the gain in elevation
     * @return A String that can go in the imageView
     */
    private String getElevationGainText(String theElevationGain) {
        if (theElevationGain != null && !theElevationGain.equals("null"))
            return "Elevation Gain in Feet: " + theElevationGain;
        return "Elevation Gain in Feet: Not Available";
    }

    /**
     * Helper method to create the text for the trail length imageView
     *
     * @param theLength the trail length from the mysql database
     * @return A String that can go in the imageView
     */
    private String getTrailLengthText(String theLength) {
        if (theLength != null && !theLength.equals("null"))
            return "Trail Length in Miles: " + theLength;
        return "Trail Length in Miles: Not Available";
    }

    /**
     * Takes the saved values from mHike and put them into the layout.
     */
    private void displayHike() {
        TextView title = (TextView) findViewById(R.id.hike_name);
        title.setText(myHikeName);

        ImageView picture = (ImageView) findViewById(R.id.imageView);
        Picasso.with(getApplicationContext())
                .load(HIKES_URL2+mHike.getmPicUrlEnding())
                .into(picture);

        TextView length = (TextView) findViewById(R.id.trail_length);
        length.setText(getTrailLengthText(mHike.getmLength()));

        TextView maxElevation = (TextView) findViewById(R.id.elevation);
        maxElevation.setText(getMaxElevationText(mHike.getmMaxElevation()));

        TextView elevationGain = (TextView) findViewById(R.id.elevation_gain);
        elevationGain.setText(getElevationGainText(mHike.getmElevationGain()));

        TextView description = (TextView) findViewById(R.id.description);
        description.setText(mHike.getmLongDescription());

        TextView reviews = (TextView) findViewById(R.id.reviews);
        String reviewText = mHike.getmReviews();
        if (reviewText == null || reviewText.equals("null")) {
            reviews.setText("No reviews yet.");
        } else {
            reviews.setText(reviewText);
        }


    }

    /**
     * A nested AsyncTask class that performs the actual work of connecting to the web service
     * to download the details of the Hike.
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
            HttpURLConnection urlConnection ;

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
         * @param result the result message of the Download Task
         */
        protected void onPostExecute(String result) {
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            mHikeList = new ArrayList<>();
            result = Hike.parseHikeJSON(result, mHikeList, true);
            if (result != null) {
                Toast.makeText(getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            for (Hike hike: mHikeList) {
                if (hike.getmHikeName().equals(myHikeName)) {
                    mHike = hike;
                }
            }
            displayHike();
        }

    }
    /**
     * A nested AsyncTask class that performs the actual work of connecting to the web service
     * to upload the a new review for this Hike.
     */
    private class UploadReviewTask extends AsyncTask<String, Void, String> {

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
            HttpURLConnection urlConnection ;

            for (String url : urls) {
                try {

                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = reader.readLine()) != null) {
                        builder.append(s);
                    }
                    response = builder.toString();

                } catch (Exception e) {
                    response = "Unable to upload the Hike list. Reason: " + e.getMessage();
                }
            }

            return response;
        }

        /**
         * Makes a toast if there was an error message to display it.
         * Otherwise, calls the Hike class parseHikeJSON() method to fill the Hike list, and
         * then passes that list with the HikeFragments mListener to the RecycleViewAdapter.
         *
         * @param result the result message of the Download Task
         */
        protected void onPostExecute(String result) {
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }
        }

    }


}
