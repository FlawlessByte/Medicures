package co.realinventor.medicures.UserMod;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.Common.FeedbackManager;
import co.realinventor.medicures.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ComposeFeedbackActivity extends AppCompatActivity {

    private EditText editTextFeedback;
    private DatabaseReference ref;
    private String senderName, uid, to, role, senderEmail;
    private UserDetails userDetails;
    private TextView textViewRole;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_feedback);

        editTextFeedback = findViewById(R.id.editTextFeedback);
        textViewRole = findViewById(R.id.textviewRole);

        try {
            senderEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }
        catch (Exception e){
            Log.d("ComposeFeedbackActivity", "Not a user, but admin");
            senderEmail = "admin@medcure";
        }

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        to = getIntent().getStringExtra("to");
        role = getIntent().getStringExtra("role");

        textViewRole.setText("Send a Feedback to "+role);

        ref.child("User").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDatabase", "Got user data");
                userDetails = dataSnapshot.getValue(UserDetails.class);
                senderName = userDetails.first_name;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FirebaseDatabase", "Error retrieving data");
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public void feedbackSendButtonPressed(View view){
        String msg = editTextFeedback.getText().toString();

        if(msg.equals("")){
            Toast.makeText(this, "Empty feedback!", Toast.LENGTH_SHORT).show();
        }
        else{

            new FeedbackManager().writeFeedback(msg,to,uid,senderName,senderEmail);
            Toast.makeText(this, "Your feedback has been sent to admin!", Toast.LENGTH_LONG).show();
            finish();

        }
    }
}
