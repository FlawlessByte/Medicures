package co.realinventor.medicures.MedStore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.Common.FeedbackActivity;
import co.realinventor.medicures.R;

public class MedLoggedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_logged);

        Log.d("Activity", "MedLoggedActivity");
    }






    public void medAccountButtonPressed(View view){
        Log.d("AccountButton", "Pressed");
        startActivity(new Intent(this, MedAccountActivity.class));
    }

    public void medNotificationButtonPressed(View view){
        Log.d("NotifivationButton", "Pressed");
    }

    public void medSentMailButtonPressed(View view){
        Log.d("SentButton", "Pressed");
    }

    public void medFeedbackButtonPressed(View view){
        Log.d("FeedbackButton", "Pressed");
        startActivity(new Intent(this, FeedbackActivity.class).putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()));
    }

    public void medUserButtonPressed(View view){
        Log.d("userButton", "Pressed");
        startActivity(new Intent(this, MedRequestsActivity.class));
    }

    public void medUpdateButtonPressed(View view){
        Log.d("UpdateButton", "Pressed");
        startActivity(new Intent(this, MedAccountActivity.class));
    }
}
