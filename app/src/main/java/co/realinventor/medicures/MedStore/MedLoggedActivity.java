package co.realinventor.medicures.MedStore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import co.realinventor.medicures.Common.FeedbackActivity;
import co.realinventor.medicures.R;

public class MedLoggedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_logged);
    }






    public void medAccountButtonPressed(View view){
        startActivity(new Intent(this, MedAccountActivity.class));
    }

    public void medNotificationButtonPressed(View view){}

    public void medSentMailButtonPressed(View view){}

    public void medFeedbackButtonPressed(View view){
        startActivity(new Intent(this, FeedbackActivity.class).putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()));
    }

    public void medUserButtonPressed(View view){
        startActivity(new Intent(this, MedRequestsActivity.class));
    }

    public void medUpdateButtonPressed(View view){
        startActivity(new Intent(this, MedAccountActivity.class));
    }
}
