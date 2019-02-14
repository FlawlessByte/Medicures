package co.realinventor.medicures.AdminMod;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.R;

public class AdminLogin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_reset_password = findViewById(R.id.btn_reset_password);
        Button btn_signup = findViewById(R.id.btn_signup);

        btn_reset_password.setVisibility(View.GONE);
        btn_signup.setVisibility(View.GONE);


    }
}
