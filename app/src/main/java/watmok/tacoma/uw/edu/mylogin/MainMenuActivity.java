package watmok.tacoma.uw.edu.mylogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button maps = (Button) findViewById(R.id.browse_hike_map_button);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenuActivity.this, TrailMapActivity.class);
                startActivity(i);
                finish();
            }
        });
        Button saved_hikes = (Button) findViewById(R.id.view_saved_hikes_button);
        saved_hikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenuActivity.this, HikeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


}
