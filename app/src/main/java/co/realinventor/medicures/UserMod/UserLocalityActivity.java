package co.realinventor.medicures.UserMod;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserLocalityActivity extends AppCompatActivity {
    private TextInputEditText localityTextInput;
    private String uid;
    private DatabaseReference ref;
    private UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_locality);

        localityTextInput = (TextInputEditText) findViewById(R.id.localityTextInput);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("User").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDatabase", "Got user data");
                userDetails = dataSnapshot.getValue(UserDetails.class);
                localityTextInput.setText(userDetails.locality);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FirebaseDatabase", "Error retrieving data");
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }



    public void buttonSaveEditLocalityPressed(View view){
        String value = localityTextInput.getText().toString();
        if(value.equals(userDetails.locality)){
            Toast.makeText(this, "No change to commit!", Toast.LENGTH_SHORT).show();
        }
        else if(value.equals("")){
            Toast.makeText(this, "Please type some locality!", Toast.LENGTH_SHORT).show();
        }
        else{
            ref.child("User").child(uid).child("locality").setValue(value);
            Toast.makeText(this, "Data saved!!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void buttonCancelEditLocalityPressed(View view){
        finish();
    }


}
