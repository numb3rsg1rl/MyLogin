package watmok.tacoma.uw.edu.mylogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * MainMenuActivity class is the first thing that the user sees after logging in.
 * This class contains a group of buttons that navigate to different parts of the app.
 * For example, the button "maps" leads to the map activity that shows the locations of all the
 * trails in the database.
 */
public class MainMenuActivity extends AppCompatActivity {
    private GoogleApiClient mGoogleApiClient;
    private TextView mWelcomeTextView;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";

    SharedPreferences sharedpreferences;

    /**
     * OnCreate creates a group of buttons that lead the user to other activities in the
     * app.
     * Button "maps" lead to the maps activity
     * Button "all_hikes" leads to an activity of the list of hikes
     * Button "fav_hikes" leads to an activity containing the list of favorite activities
     * Button "sign_out" signs the user out and directs the user back to the sign in page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mWelcomeTextView = (TextView) findViewById(R.id.welcome_view);
        mWelcomeTextView.setText(getString(R.string.welcome_fmt,
                sharedpreferences.getString(Name, "Guest")));
        Button maps = (Button) findViewById(R.id.browse_hike_map_button);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenuActivity.this, TrailMapActivity.class);
                startActivity(i);
                finish();
            }
        });
        Button all_hikes = (Button) findViewById(R.id.view_all_hikes_button);
        all_hikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenuActivity.this, HikeActivity.class);
                startActivity(i);
                finish();
            }
        });
        Button fav_hikes = (Button) findViewById(R.id.view_favorite_hikes_button);
        fav_hikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenuActivity.this, FavoritesActivity.class);
                startActivity(i);
                finish();
            }
        });
        Button signout = (Button) findViewById(R.id.sign_out_button2);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    /**
     * onStart() allows the Google API to be initialized so it can be used throughout the rest of
     * the app.
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

}
