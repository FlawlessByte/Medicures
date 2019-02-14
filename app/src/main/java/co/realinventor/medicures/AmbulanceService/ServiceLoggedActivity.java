package co.realinventor.medicures.AmbulanceService;

import android.content.Intent;
import android.os.Bundle;
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
import co.realinventor.medicures.Common.FeedbackActivity;
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

        buttonWelcome = findViewById(R.id.buttonWelcome);
        availabilityImage = findViewById(R.id.availabilityImage);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        // Get availability thing and update

        ref.child("Ambulances").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

    public void accountButtonClicked(View view){
        startActivity(new Intent(this, ServiceAccountActivity.class));
    }

    public void feedbackButtonClicked(View view){
        startActivity(new Intent(this, FeedbackActivity.class).putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()));
    }

    public void notificationButtonClicked(View view){
        startActivity(new Intent(this, ServiceNotificationActivity.class));
    }

    public void availabilityButtonClicked(View view){
        startActivity(new Intent(this, ServiceAvailabilityActivity.class).putExtra("availability",serviceDetails.availability));
    }
}
