package co.realinventor.medicures.MedStore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.R;
import co.realinventor.medicures.UserMod.UserDetails;

public class MedAccountActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private String uid;
    private MedStoreDetails medStoreDetails;
    private TextView medNameEditText, medOwnerEditText, medLocalityEditText, medContactEditText, medMailEditText;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_account);
        Log.d("Activity", "MedAccountActivity");

        medNameEditText = findViewById(R.id.medNameEditText);
        medOwnerEditText = findViewById(R.id.medOwnerEditText);
        medLocalityEditText = findViewById(R.id.medLocalityEditText);
        medContactEditText = findViewById(R.id.medContactEditText);
        medMailEditText = findViewById(R.id.medMailEditText);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("MedStores").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDatabase", "Medstore details retrieved");
                medStoreDetails = dataSnapshot.getValue(MedStoreDetails.class);
                fillTheFields();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void fillTheFields(){
        medNameEditText.setText(medStoreDetails.shopName);
        medOwnerEditText.setText(medStoreDetails.ownerName);
        medLocalityEditText.setText(medStoreDetails.locality);
        medContactEditText.setText(medStoreDetails.phone);
        medMailEditText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    public void medNameEditButtonPressed(View view){
        Log.d("MedNameButton", "Pressed");
        showDialogs("Type new name", (TextView) view, InputType.TYPE_CLASS_TEXT);
    }

    public void medOwnerEditButtonPressed(View view){
        Log.d("OwnerButton", "Pressed");
        showDialogs("Type new owner name", (TextView) view, InputType.TYPE_CLASS_TEXT);
    }

    public void medLocalityEditButtonPressed(View view){
        Log.d("LocalityButton", "Pressed");
        showDialogs("Type new locality", (TextView) view, InputType.TYPE_CLASS_TEXT);
    }

    public void medContactEditButtonPressed(View view){
        Log.d("ContactButton", "Pressed");
        showDialogs("Type new contact no.", (TextView) view, InputType.TYPE_CLASS_PHONE);
    }

    public void medMailEditButtonPressed(View view){
        Log.d("MailButton", "Pressed");
        showDialogs("Type new mail ID", (TextView) view, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    public void saveMedEditsButtonClicked(View view){
        Log.d("SaveButton", "Pressed");
        MedStoreDetails newMedStoreDetails = new MedStoreDetails(medStoreDetails.mUid,medNameEditText.getText().toString(), medLocalityEditText.getText().toString(),
                medOwnerEditText.getText().toString(),
                medStoreDetails.pharmacist,
                medContactEditText.getText().toString(), medStoreDetails.pinCode, medStoreDetails.verified);

        ref.child("MedStores").child(uid).setValue(newMedStoreDetails);

        //Update email ID
        FirebaseAuth.getInstance().getCurrentUser().updateEmail(medMailEditText.getText().toString());

        Toast.makeText(this, "Details updated", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, MedAccountActivity.class));
        this.finish();
    }


    private void showDialogs(String title, final TextView textview, int inputType){
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
