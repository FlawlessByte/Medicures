package co.realinventor.medicures.AdminMod;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.Common.AmbulanceServiceShowActivity;
import co.realinventor.medicures.Common.FeedbackActivity;
import co.realinventor.medicures.Common.MedStoreShowActivity;
import co.realinventor.medicures.Common.Statics;
import co.realinventor.medicures.MedStore.MedStoreDetails;
import co.realinventor.medicures.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminLoggedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_logged);
    }


    public void adminMedReviewButtonPressed(View view){
        Statics.MED_REQ_ACTIVITY = "Admin";
        startActivity(new Intent(this, MedStoreShowActivity.class));
    }

    public void adminServiceReviewButtonPressed(View view){
        Statics.SERVICE_REQ_ACTIVITY = "Admin";
        startActivity(new Intent(this, AmbulanceServiceShowActivity.class));
    }

    public void chatsButtonClicked(View view){
        startActivity(new Intent(this, FeedbackActivity.class));
    }
}
