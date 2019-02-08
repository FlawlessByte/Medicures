package co.realinventor.medicures.UserMod;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import co.realinventor.medicures.R;

public class MedicineAlarmedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_alarmed);

        //get current time and compare with that of in sqlite database
        //and retrieve details
    }

    public void alarmDismissButtonClicked(View view){

    }
}
