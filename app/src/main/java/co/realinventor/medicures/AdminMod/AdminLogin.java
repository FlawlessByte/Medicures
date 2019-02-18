package co.realinventor.medicures.AdminMod;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.R;

public class AdminLogin extends AppCompatActivity {
    private TextInputEditText adminEmailInput, adminPasswordInput;
    private Button adminLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
        }

        adminEmailInput = (TextInputEditText) findViewById(R.id.adminEmailInput);
        adminPasswordInput = (TextInputEditText) findViewById(R.id.adminPasswordInput);

        adminLoginButton = findViewById(R.id.adminLoginButton);

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adminEmailInput.getText().toString().equals("admin") && adminPasswordInput.getText().toString().equals("admin")){
                    startActivity(new Intent(AdminLogin.this, AdminLoggedActivity.class));
                    AdminLogin.this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Check username or password. Invalid credentials", Toast.LENGTH_SHORT ).show();
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
