package co.realinventor.medicures.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.AdminMod.AdminLoggedActivity;
import co.realinventor.medicures.AmbulanceService.ServiceLoggedActivity;
import co.realinventor.medicures.MedStore.MedLoggedActivity;
import co.realinventor.medicures.R;
import co.realinventor.medicures.UserMod.LoggedActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    String intentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Activity", "LoginActivity");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            switch (auth.getCurrentUser().getDisplayName()) {
                case "User": {
                    Log.d("Logged", "User");
                    startActivity(new Intent(LoginActivity.this, LoggedActivity.class));
                    break;
                }
                case "Med_Store": {
                    Log.d("Logged", "Med_Store");
                    startActivity(new Intent(LoginActivity.this, MedLoggedActivity.class));
                    break;
                }
                case "Ambulance": {
                    Log.d("Logged", "Ambulance");
                    startActivity(new Intent(LoginActivity.this, ServiceLoggedActivity.class));
                    break;
                }
                default:
                    break;

            }
            finish();

        }

        try {
            intentMode = getIntent().getStringExtra("mode");

        }
        catch(Exception e){
            Log.d("Intent", "null");
        }


        // set the view now
        setContentView(R.layout.activity_login);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button", "SignUp button clicked");
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("mode", intentMode);
                startActivity(intent);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button", "reset button clicked");
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button", "Login button clicked");
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    Log.d("Sign In", "Some error");
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Log.d("Sign in", "Successful");

                                    Intent intent = null;
                                    if(auth.getCurrentUser().getDisplayName().equals("user")){
                                        intent = new Intent(LoginActivity.this, LoggedActivity.class);
                                    }
                                    else if(auth.getCurrentUser().getDisplayName().equals("medical")){
                                        intent = new Intent(LoginActivity.this, MedLoggedActivity.class);
                                    }
                                    else if(auth.getCurrentUser().getDisplayName().equals("ambulance")){
                                        intent = new Intent(LoginActivity.this, MedLoggedActivity.class);
                                    }
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}