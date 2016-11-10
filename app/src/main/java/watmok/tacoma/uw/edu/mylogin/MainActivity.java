package watmok.tacoma.uw.edu.mylogin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * This activity is the first thing that the user sees when they open the application.
 * They are shown two buttons with two options, either Login or Register. The Register button will
 * take the user to the Register activity for the user to register, while the Login button will
 * take the user to the Login Activity and prompt the user for their username and password.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * When the application is opened, the main page is opened, which is the login/register page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * When called, this method opens up the Activity SignUPActivity
     * @param V
     */
    public void signUp(View V) {
        Intent intentSignUP = new Intent(getApplicationContext(), SignUPActivity.class);
        startActivity(intentSignUP);
    }

    /**
     * When called, this method opens up the Activity SignINActivity
     * @param V
     */
    public void signIn(View V) {
        Intent intentSignIN = new Intent(getApplicationContext(), SignINActivity.class);
        startActivity(intentSignIN);
    }

    /**
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

