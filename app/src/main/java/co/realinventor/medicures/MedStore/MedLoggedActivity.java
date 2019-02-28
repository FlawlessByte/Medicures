package co.realinventor.medicures.MedStore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import co.realinventor.medicures.Common.FeedbackActivity;
import co.realinventor.medicures.Common.NotificationsActivity;
import co.realinventor.medicures.Common.SentMailActivity;
import co.realinventor.medicures.MainActivity;
import co.realinventor.medicures.R;
import co.realinventor.medicures.UserMod.ComposeFeedbackActivity;

public class MedLoggedActivity extends AppCompatActivity {
    private String senderName, uid;
    private DatabaseReference ref;
    private MedStoreDetails medStoreDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_logged);

        Log.d("Activity", "MedLoggedActivity");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarX);
        setSupportActionBar(toolbar);

        ref = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref.child("MedStores").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDatabase", "Got ambulance data");
                medStoreDetails = dataSnapshot.getValue(MedStoreDetails.class);
                senderName = medStoreDetails.shopName;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

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
        startActivity(new Intent(this, SentMailActivity.class));
    }

    public void medFeedbackButtonPressed(View view){
        Log.d("FeedbackButton", "Pressed");
        Intent i = new Intent(this, ComposeFeedbackActivity.class);
        i.putExtra("senderName", senderName);
        startActivity(i);
    }

    public void medUserButtonPressed(View view){
        Log.d("userButton", "Pressed");
        startActivity(new Intent(this, MedRequestsActivity.class));
    }


    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        super.onBackPressed();
    }
}
