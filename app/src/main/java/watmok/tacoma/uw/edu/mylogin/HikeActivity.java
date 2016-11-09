package watmok.tacoma.uw.edu.mylogin;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
//import android.view.Window;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

public class HikeActivity extends AppCompatActivity implements HikeFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hike);


        if (savedInstanceState == null
            && getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            HikeFragment hikeFragment = new HikeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, hikeFragment).commit();
        }

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HikeActivity.this,
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onListFragmentInteraction(Hike item) {

    }
}
