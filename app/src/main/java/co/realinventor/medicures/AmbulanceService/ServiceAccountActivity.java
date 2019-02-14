package co.realinventor.medicures.AmbulanceService;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.text.InputType;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.R;

public class ServiceAccountActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private String uid;
    private ServiceDetails serviceDetails;
    private EditText nameEditTextView, contactEditTextView, input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_account);

        Log.d("Activity", "ServiceAcccountActiivyt");

        nameEditTextView = findViewById(R.id.nameEditText);
        contactEditTextView = findViewById(R.id.contactEditText);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("Ambulances").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDatabase", "Got ambulance data");
                serviceDetails = dataSnapshot.getValue(ServiceDetails.class);
                fillTheFields();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void fillTheFields(){
        nameEditTextView.setText(serviceDetails.driverName);
        contactEditTextView.setText(serviceDetails.phone); // not added in database
    }

    public void nameEditButtonPressed(View view){
        Log.d("NameEditButton", "Pressed");
        showDialogs("Type new name",(EditText) view, InputType.TYPE_CLASS_TEXT);
    }

    public void contactEditButtonPressed(View view){
        Log.d("ContactEditButton", "Pressed");
        showDialogs("Type new contact number",(EditText) view, InputType.TYPE_CLASS_PHONE);
    }

    public void saveButtonPressed(View view){
        Log.d("SaveButton", "Pressed");
//        save data to firebase
        String name = nameEditTextView.getText().toString();
        String phone = contactEditTextView.getText().toString();
        ref.child("Ambulances").child(uid).child("driverName").setValue(name);
        ref.child("Ambulances").child(uid).child("phone").setValue(phone);

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, ServiceLoggedActivity.class));

        this.finish();
    }

    private void showDialogs(String title, final EditText textview, int inputType){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        // Set up the input
        input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(inputType);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textview.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
