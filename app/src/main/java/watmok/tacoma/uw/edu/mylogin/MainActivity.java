package watmok.tacoma.uw.edu.mylogin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void signUp(View V) {
        Intent intentSignUP = new Intent(getApplicationContext(), SignUPActivity.class);
        startActivity(intentSignUP);
    }
    public void signIn(View V) {
        Intent intentSignIN = new Intent(getApplicationContext(), SignINActivity.class);
        startActivity(intentSignIN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginDataBaseAdapter.close();
    }
}

