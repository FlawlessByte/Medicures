package co.realinventor.medicures.MedStore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import co.realinventor.medicures.Common.FeedbackActivity;
import co.realinventor.medicures.Common.NotificationsActivity;
import co.realinventor.medicures.MainActivity;
import co.realinventor.medicures.R;

public class MedLoggedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_logged);

        Log.d("Activity", "MedLoggedActivity");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarX);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logged, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void medAccountButtonPressed(View view){
        Log.d("AccountButton", "Pressed");
        startActivity(new Intent(this, MedAccountActivity.class));
    }

    public void medNotificationButtonPressed(View view){
        Log.d("NotifivationButton", "Pressed");
        startActivity(new Intent(this, NotificationsActivity.class));
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
