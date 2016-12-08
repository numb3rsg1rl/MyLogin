package watmok.tacoma.uw.edu.mylogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

/**
 * This activity is the first thing that the user sees when they open the application.
 * They are shown two buttons with two options, either Login or Register. The Register button will
 * take the user to the Register activity for the user to register, while the Login button will
 * take the user to the Login Activity and prompt the user for their username and password.
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private TextView mStatusTextView;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";


    SharedPreferences sharedpreferences;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    /**
     * When the application is opened, the main page is opened, which is the login/register page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        // [END customize_button]
    }

    /**
     * onStart() allows the Google API to be initialized so it can be used throughout the rest of
     * the app. If the user's cached credentials are valid, the OptionalPendingResult will be "done"
     * and the GoogleSignInResult will be available instantly.
     * If the user has not previously signed in on this device or the sign-in has expired,
     * this asynchronous branch will attempt to sign in the user silently.  Cross-device
     * single sign-on will occur in this method.
     */
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
       if (opr.isDone()) {

            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);

       } else {

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
       }
    }

    /**
     * this method is the result returned from launching the Intent from
     * GoogleSignInApi.getSignInIntent(...);
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            handleSignInResult(result);
        }
    }

    /**
     * This method handles the result of the sign in. If the user signed in
     * successfully, show authenticated UI. Otherwise, the user is signed out,
     * show unauthenticated UI.
     * @param result
     */
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Name, acct.getDisplayName());
            editor.putString(Email, acct.getEmail());
            editor.commit();
            updateUI(true);

        } else {
            updateUI(false);
        }
    }

    /**
     * This method is called if there is an unresolvable error has occurred and
     * Google APIs (including Sign-In) will not be available.
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }

    /**
     * This lets the user know whether the user is signed in or not
     * @param signedIn TRUE if the user is signed in, and FALSE if not signed in
     */
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            Intent signIn = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(signIn);
            //finish();
        } else {
            mStatusTextView.setText(R.string.signed_out);
            //finish();
        }
    }

    /**
     * Destroys Activity
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * Opens the Sign In Intent created by Google and starts the Activity
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}

