package watmok.tacoma.uw.edu.mylogin;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

/**
 * The activity that acts as a fragment container for the Hike list. You land here when you sign in,
 * and logging off sends you back to main, at which point you need to sign in again to get back
 * here.
 * Note: At this point, the OnClickInteractionListener is implemented with an empty method. We will
 * be adding detail fragments later, at which point we will be adding content to that method.
 */
public class HikeActivity extends AppCompatActivity implements HikeFragment.OnListFragmentInteractionListener {

    /**
     * Creates the activity, and instantiates the HikeFragment inside. Also creates the logout button.
     * @param savedInstanceState
     */
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

    }

    /**
     * What happens when you click on a Hike item from the list.
     * Currently does nothing. Content will be added to this method at a later point, upon the
     * creation of a HikeDetail fragment.
     * @param item
     */
    @Override
    public void onListFragmentInteraction(Hike item) {
        Intent i = new Intent(HikeActivity.this, TrailMapActivity.class);
        startActivity(i);
    }
}
