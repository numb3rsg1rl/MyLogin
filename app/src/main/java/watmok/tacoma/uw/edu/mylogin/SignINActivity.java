package watmok.tacoma.uw.edu.mylogin;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignINActivity extends AppCompatActivity {
    EditText editTextUserName, editTextPassword;
    Button btnSignIn;
    Context context = this;
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        editTextUserName = (EditText) findViewById(R.id.editTextUserNameToLogin);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordToLogin);

        btnSignIn = (Button) findViewById(R.id.buttonSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String userName = editTextUserName.getText().toString();

                String password = editTextPassword.getText().toString();
                if (userName.equals("") || password.equals("")) {

                    Toast.makeText(getApplicationContext(), "Field Vaccant",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(loginDataBaseAdapter.getSingleEntry(userName))) {
                    Toast.makeText(getApplicationContext(),
                            "Password does not match", Toast.LENGTH_LONG)
                            .show();
                    return;
                } else {

                    //loginDataBaseAdapter.insertEntry(userName, password);
                    Toast.makeText(getApplicationContext(),
                            "Account Successfully Confirmed ", Toast.LENGTH_LONG)
                            .show();
                    Intent i = new Intent(SignINActivity.this,
                            HikeActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        loginDataBaseAdapter.close();
    }
}