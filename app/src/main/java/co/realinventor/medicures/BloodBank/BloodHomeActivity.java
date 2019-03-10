package co.realinventor.medicures.BloodBank;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BloodHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_home);


    }


    public void viewBloodBankClicked(View view){
        startActivity(new Intent(this, ViewBloodBankActivity.class));
    }

    public void registerBloodBankClicked(View view){
        startActivity(new Intent(this, RegisterBloodBankActivity.class));
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
