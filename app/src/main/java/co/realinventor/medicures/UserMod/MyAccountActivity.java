package co.realinventor.medicures.UserMod;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.realinventor.medicures.AmbulanceService.ServiceDetails;
import co.realinventor.medicures.MedStore.MedStoreDetails;
import co.realinventor.medicures.R;

public class MyAccountActivity extends AppCompatActivity {
    private String uid;
    UserDetails userDetails;
    TextView nameEditTextView, ageEditTextView, localityEditTextView, phoneEditTextView, mailEditTextView, medicalEditTextView, ambulanceEditTextView;
    private EditText input;
    private DatabaseReference ref;
    private ArrayList<String> medicalList, ambulanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        nameEditTextView = findViewById(R.id.nameEditTextView);
        ageEditTextView = findViewById(R.id.ageEditTextView);
        localityEditTextView = findViewById(R.id.localityEditTextView);
        phoneEditTextView = findViewById(R.id.phoneEditTextView);
        mailEditTextView = findViewById(R.id.mailEditTextView);
        medicalEditTextView = findViewById(R.id.medicalEditTextView);
        ambulanceEditTextView = findViewById(R.id.ambulanceEditTextView);

        medicalList = new ArrayList<String>();
        ambulanceList = new ArrayList<String>();


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("User").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userDetails = dataSnapshot.getValue(UserDetails.class);
                fillTheFields();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void fillTheFields(){
        nameEditTextView.setText(userDetails.first_name);
        ageEditTextView.setText(userDetails.age);
        localityEditTextView.setText(userDetails.locality);
        phoneEditTextView.setText(userDetails.phone);
        mailEditTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    public void nameEditButtonPressed(View view){
        showDialogs("Type new name", (TextView) view, InputType.TYPE_CLASS_TEXT);
    }

    public void ageEditButtonPressed(View view){
        showDialogs("Type age",(TextView) view, InputType.TYPE_CLASS_NUMBER);
    }

    public void localityEditButtonPressed(View view){
        showDialogs("Type new locality", (TextView) view, InputType.TYPE_CLASS_TEXT);
    }

    public void phoneEditButtonPressed(View view){
        showDialogs("Type new phone number", (TextView) view, InputType.TYPE_CLASS_PHONE);
    }

    public void mailEditButtonPressed(View view){
        showDialogs("Type new mail ID", (TextView) view, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    public void medicalEditButtonPressed(View view){
        Query medQuery = ref.child("MedStore");
        medQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicalList.clear();
                String element = "";
                for (DataSnapshot medSnapShot : dataSnapshot.getChildren()){
                    MedStoreDetails medStoreDetails = medSnapShot.getValue(MedStoreDetails.class);
                    element = medStoreDetails.shopName + "| " + medStoreDetails.locality;
                    medicalList.add(element);
                }

                showMedStoreList(medicalList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void ambulanceEditButtonPressed(View view){

        Query ambQuery = ref.child("Ambulances");
        ambQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ambulanceList.clear();
                String element = "";
                for (DataSnapshot ambSnapShot : dataSnapshot.getChildren()){
                    ServiceDetails serviceDetails = ambSnapShot.getValue(ServiceDetails.class);
                    element = serviceDetails.driverName+ "| " + serviceDetails.driverLocality;
                    ambulanceList.add(element);
                }

                showAmbulanceList(ambulanceList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void showAmbulanceList(ArrayList<String> list){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose some ambulance service");

        // add a checkbox list
        String[] ambs = list.toArray(new String[0]);
        boolean[] checkedItems = new boolean[list.size()];
        builder.setMultiChoiceItems(ambs, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void showMedStoreList(ArrayList<String> list){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose some medical stores");

        // add a checkbox list
        String[] meds = list.toArray(new String[0]);
        boolean[] checkedItems = new boolean[list.size()];
        builder.setMultiChoiceItems(meds, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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
