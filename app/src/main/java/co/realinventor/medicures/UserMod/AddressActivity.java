package co.realinventor.medicures.UserMod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.Common.PrescDetails;
import co.realinventor.medicures.MedStore.MedSignInActivity;
import co.realinventor.medicures.MedStore.Medicine;
import co.realinventor.medicures.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {
    ArrayList<Medicine> medList;
    private TextInputEditText inputName, inputAddress, inputContact;
    private String uri = "", uid;
    private DatabaseReference ref;
    FirebaseStorage storage;
    StorageReference storageRef;
    PrescDetails presc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        Log.d("AddressActivity", "In ");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        inputAddress = findViewById(R.id.addressInput);
        inputContact = findViewById(R.id.contactInput);
        inputName = findViewById(R.id.nameInput);

        ref = FirebaseDatabase.getInstance().getReference();

        try{
            medList = (ArrayList<Medicine>) getIntent().getSerializableExtra("list");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            uri = getIntent().getStringExtra("uri");
            presc = (PrescDetails) getIntent().getSerializableExtra("presc");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void sendMedicineDetailsClicked(View view){

        if(medList != null){
            Log.d("MedList", "not null");
            for (Medicine m : medList){
                m.contact = inputContact.getText().toString();
                m.address = inputAddress.getText().toString();
                m.customerName = inputName.getText().toString();
                ref.child("MedRequests").child(m.trans_id).setValue(m);
            }
            Toast.makeText(this, "Request is being sent to Medical Store", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(!uri.equals("")){
            Log.d("Uri ", uri);
            Uri file = Uri.fromFile(new File(uri));
            StorageReference riversRef = storageRef.child("docs/med_requests/"+presc.trans_id+"/"+file.getLastPathSegment());
            presc.filename = file.getLastPathSegment();
            presc.contact = inputContact.getText().toString();
            presc.customerName  = inputName.getText().toString();
            presc.address = inputAddress.getText().toString();
            UploadTask uploadTask = riversRef.putFile(file);

            ref.child("MedPrescRequests").child(presc.trans_id).setValue(presc);


            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading file ");
            progressDialog.show();


            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d("Upload File", "failure");
                    progressDialog.dismiss();
                    startActivity(new Intent(AddressActivity.this, LoggedActivity.class));
                    finish();
                    Toast.makeText(AddressActivity.this, "Failed "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.d("Upload File", "success");
                    progressDialog.dismiss();
                    startActivity(new Intent(AddressActivity.this, LoggedActivity.class));
                    finish();
                    Toast.makeText(AddressActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });

        }
        else{
            Log.d("SendMedicine", "else");
        }
    }
}
