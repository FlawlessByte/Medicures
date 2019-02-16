package co.realinventor.medicures.AmbulanceService;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import co.realinventor.medicures.Common.FeedbackActivity;
import co.realinventor.medicures.Common.Notifications;
import co.realinventor.medicures.Common.NotificationsActivity;
import co.realinventor.medicures.MainActivity;
import co.realinventor.medicures.R;

public class ServiceLoggedActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private String uid;
    ServiceDetails serviceDetails;
    Button buttonWelcome;
    ImageView availabilityImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_logged);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarY);
        setSupportActionBar(toolbar);

        Log.d("Activity", "ServiceLoggedActivity");

        buttonWelcome = findViewById(R.id.buttonWelcome);
        availabilityImage = findViewById(R.id.availabilityImage);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        // Get availability thing and update

        ref.child("Ambulances").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDatabase", "Got ambulance data");
                serviceDetails = dataSnapshot.getValue(ServiceDetails.class);
                buttonWelcome.setText("Welcome, "+ serviceDetails.driverName);
                if(serviceDetails.availability.equals("no"))
                    availabilityImage.setImageResource(R.drawable.cross_large);
                else
                    availabilityImage.setImageResource(R.drawable.tick_large);
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

    public void accountButtonClicked(View view){
        Log.d("AccountButton", "Pressed");
        startActivity(new Intent(this, ServiceAccountActivity.class));
    }

    public void feedbackButtonClicked(View view){
        Log.d("FeedbackButton", "Pressed");
        startActivity(new Intent(this, FeedbackActivity.class).putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()));
    }

    public void notificationButtonClicked(View view){
        Log.d("NotifivationButton", "Pressed");
        startActivity(new Intent(this, NotificationsActivity.class));
    }

    public void availabilityButtonClicked(View view){
        Log.d("AvailabilityButton", "Pressed");
        Intent i = new Intent(this, ServiceAvailabilityActivity.class);
        i.putExtra("availability",serviceDetails.availability);
        startActivity(i);
    }
}
