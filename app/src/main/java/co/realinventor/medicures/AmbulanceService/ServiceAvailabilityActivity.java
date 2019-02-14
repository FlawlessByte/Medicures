package co.realinventor.medicures.AmbulanceService;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.R;

public class ServiceAvailabilityActivity extends AppCompatActivity {

    private Switch switchAvail;
    private TextView textView;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_availability);

        String currentState = getIntent().getStringExtra("availability");

        switchAvail = findViewById(R.id.switchAvail);
        textView = findViewById(R.id.textViewStatus);

        if(currentState.equals("yes")){
            switchAvail.setChecked(true);
            textView.setText("Status : available");
        }
        else{
            switchAvail.setChecked(false);
            textView.setText("Status : unavailable");
        }


        ref = FirebaseDatabase.getInstance().getReference();



        switchAvail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ref.child("Ambulances").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("availability").setValue("yes");
                    textView.setText("Status : available");
                }
                else{
                    ref.child("Ambulances").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("availability").setValue("no");
                    textView.setText("Status : unavailable");
                }
            }
        });


    }
}
