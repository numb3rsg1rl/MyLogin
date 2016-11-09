package watmok.tacoma.uw.edu.mylogin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

public class HikeActivity extends AppCompatActivity implements HikeFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null
            && getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            HikeFragment hikeFragment = new HikeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, hikeFragment).commit();
        }

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
