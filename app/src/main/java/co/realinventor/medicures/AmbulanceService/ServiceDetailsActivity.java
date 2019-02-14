package co.realinventor.medicures.AmbulanceService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.MedStore.MedSignInActivity;
import co.realinventor.medicures.R;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class ServiceDetailsActivity extends AppCompatActivity {
    EditText inputDriverName,inputDriverLocality, inputDriverAge, textVehicleLicence, textAadhar, textRC, textDriverLicence;
    ImageButton buttonVehicleLicence, buttonAadhar, buttonRC, buttonDriverLicence;
    String currentFile;
    FirebaseStorage storage;
    StorageReference storageRef;
    ArrayList<String> docPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        Log.d("Activity", "ServiceDetailsActivity");


        inputDriverName = findViewById(R.id.inputDriverName);
        inputDriverLocality = findViewById(R.id.inputDriverLocality);
        inputDriverAge = findViewById(R.id.inputDriverAge);
        inputDriverName = findViewById(R.id.inputDriverName);
        textVehicleLicence = findViewById(R.id.textVehicleLicence);
        textAadhar = findViewById(R.id.textAadhar);
        textRC = findViewById(R.id.textRC);
        textVehicleLicence = findViewById(R.id.textVehicleLicence);

        buttonVehicleLicence = findViewById(R.id.buttonVehicleLicence);
        buttonAadhar = findViewById(R.id.buttonAadhar);
        buttonRC = findViewById(R.id.buttonRC);
        buttonDriverLicence = findViewById(R.id.buttonDriverLicence);

        buttonVehicleLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("VehicleLicence Button", "Pressed");
                currentFile = "vehicle_licence";
                FilePickerBuilder filePickerBuilder = new FilePickerBuilder();
                filePickerBuilder.setMaxCount(1).setActivityTitle("Select Vehicle Licence").setActivityTheme(R.style.LibAppTheme).pickFile(getParent());
            }
        });
        buttonAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AdharButton", "Pressed");
                currentFile = "aadhar";
                FilePickerBuilder filePickerBuilder = new FilePickerBuilder();
                filePickerBuilder.setMaxCount(1).setActivityTitle("Select Aadhar Card").setActivityTheme(R.style.LibAppTheme).pickFile(getParent());
            }
        });
        buttonRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RCButton", "Pressed");
                currentFile = "rc_book";
                FilePickerBuilder filePickerBuilder = new FilePickerBuilder();
                filePickerBuilder.setMaxCount(1).setActivityTitle("Select RC doc").setActivityTheme(R.style.LibAppTheme).pickFile(getParent());
            }
        });
        buttonDriverLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DriverLicenceButton", "Pressed");
                currentFile = "driver_licence";
                FilePickerBuilder filePickerBuilder = new FilePickerBuilder();
                filePickerBuilder.setMaxCount(1).setActivityTitle("Select Driver Licence").setActivityTheme(R.style.LibAppTheme).pickFile(getParent());
            }
        });




    }

    public void serviceRegisterButtonClicked(View view){
        Log.d("ServiceRegisteButton", "Pressed");

        if(!inputsOk()){
            Log.d("CheckInputs", "Not ok");
            Toast.makeText(getApplicationContext(), "Fill all the fields and try again!", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d("CheckInputs", "ok");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if(auth.getCurrentUser() != null){
                saveUserData(auth.getCurrentUser().getUid());

                startActivity(new Intent(ServiceDetailsActivity.this, ServiceLoggedActivity.class));
            }

        }
    }

    private void saveUserData(String uid){
        String driverName,driverLocality,driverAge,verified = "no";
        driverName = inputDriverName.getText().toString();
        driverLocality = inputDriverLocality.getText().toString();
        driverAge = inputDriverAge.getText().toString();
        ServiceDetails serviceDetails = new ServiceDetails(driverName,driverLocality,driverAge,verified);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Ambulances").child(uid).setValue(serviceDetails);

        Toast.makeText(this, "Data is being saved!", Toast.LENGTH_SHORT).show();

        saveFiles(uid);
    }

    public void saveFiles(String uid){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        uploadFile(Uri.fromFile(new File(textVehicleLicence.getText().toString())), uid, 1);
        uploadFile(Uri.fromFile(new File(textAadhar.getText().toString())), uid, 2);
        uploadFile(Uri.fromFile(new File(textRC.getText().toString())), uid, 3);
        uploadFile(Uri.fromFile(new File(textDriverLicence.getText().toString())), uid, 4);
    }


    public void uploadFile(Uri file,String uid, int no){
        StorageReference riversRef = storageRef.child("docs/ambulance/"+uid+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading file "+no);
        progressDialog.show();


        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progressDialog.dismiss();
                Toast.makeText(ServiceDetailsActivity.this, "Failed "+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                progressDialog.dismiss();
                Toast.makeText(ServiceDetailsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.d("ActivityResult", ""+resultCode);

        if(requestCode == FilePickerConst.REQUEST_CODE_DOC){
            if(resultCode== Activity.RESULT_OK && data!=null)
            {
                docPaths.clear();
                docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
            }
        }

        if(currentFile.equals("vehicle_licence")){
            textVehicleLicence.setText(docPaths.get(0));
        }
        if(currentFile.equals("aadhar")){
            textAadhar.setText(docPaths.get(0));
        }
        if(currentFile.equals("rc_book")){
            textRC.setText(docPaths.get(0));
        }
        if(currentFile.equals("driver_licence")){
            textDriverLicence.setText(docPaths.get(0));
        }


        super.onActivityResult(requestCode, resultCode, data);
    }





    public boolean inputsOk(){
        boolean status = true;

        if(inputDriverName.getText().toString().equals(""))
            status = false;
        if(inputDriverLocality.getText().toString().equals(""))
            status = false;
        if(inputDriverAge.getText().toString().equals(""))
            status = false;
        if(textVehicleLicence.getText().toString().equals("Upload vehicle licence"))
            status = false;
        if(textRC.getText().toString().equals("Upload RC certificate"))
            status = false;
        if(textAadhar.getText().toString().equals("Upload Aadhar card"))
            status = false;
        if(textDriverLicence.getText().toString().equals("Upload Driver Licence"))
            status = false;

        return status;
    }



}
