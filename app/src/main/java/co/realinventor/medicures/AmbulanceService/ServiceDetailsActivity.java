package co.realinventor.medicures.AmbulanceService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.MedStore.MedSignInActivity;
import co.realinventor.medicures.R;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;

public class ServiceDetailsActivity extends AppCompatActivity {
    EditText inputDriverName,inputDriverLocality, inputDriverAge, textVehicleLicence, textAadhar, textRC, textDriverLicence;
    ImageButton buttonVehicleLicence, buttonAadhar, buttonRC, buttonDriverLicence;
    String currentFile;
    FirebaseStorage storage;
    StorageReference storageRef;
    ArrayList<String> docPaths = new ArrayList<>();
    private int count = 0;
    private Timer t1;

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
        textDriverLicence = findViewById(R.id.textDriverLicence);

        buttonVehicleLicence = findViewById(R.id.buttonVehicleLicence);
        buttonAadhar = findViewById(R.id.buttonAadhar);
        buttonRC = findViewById(R.id.buttonRC);
        buttonDriverLicence = findViewById(R.id.buttonDriverLicence);

        buttonVehicleLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("VehicleLicence Button", "Pressed");
                currentFile = "vehicle_licence";
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1)
                        .setActivityTitle("Select Vehicle Licence")
                        .setActivityTheme(R.style.LibAppTheme)
                        .sortDocumentsBy(SortingTypes.name)
                        .pickPhoto(ServiceDetailsActivity.this);
                    }
        });
        buttonAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AdharButton", "Pressed");
                currentFile = "aadhar";
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1)
                        .setActivityTitle("Select Aadhar Card")
                        .setActivityTheme(R.style.LibAppTheme)
                        .sortDocumentsBy(SortingTypes.name)
                        .pickPhoto(ServiceDetailsActivity.this);
            }
        });
        buttonRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RCButton", "Pressed");
                currentFile = "rc_book";
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1)
                        .setActivityTitle("Select RC doc")
                        .setActivityTheme(R.style.LibAppTheme)
                        .sortDocumentsBy(SortingTypes.name)
                        .pickPhoto(ServiceDetailsActivity.this);}
        });
        buttonDriverLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DriverLicenceButton", "Pressed");
                currentFile = "driver_licence";
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1)
                        .setActivityTitle("Select Driver Licence")
                        .setActivityTheme(R.style.LibAppTheme)
                        .sortDocumentsBy(SortingTypes.name)
                        .pickPhoto(ServiceDetailsActivity.this);
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

                //Declare the timer
                t1 = new Timer();
                //Set the schedule function and rate
                t1.scheduleAtFixedRate(new TimerTask() {
                                           @Override
                                           public void run() {
                                               //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                               Log.d("Count", ""+count);
                                               if(count==4){
                                                   startActivity(new Intent(ServiceDetailsActivity.this, ServiceLoggedActivity.class));
                                                   finish();
                                                   t1.cancel();
                                               }
                                           }
                                       },
                        //Set how long before to start calling the TimerTask (in milliseconds)
                        2000,
                        //Set the amount of time between each execution (in milliseconds)
                        500);


            }

        }
    }

    private void saveUserData(String uid){
        String driverName,driverLocality,driverAge,verified = "no";
        driverName = inputDriverName.getText().toString();
        driverLocality = inputDriverLocality.getText().toString();
        driverAge = inputDriverAge.getText().toString();
        ServiceDetails serviceDetails = new ServiceDetails(uid,driverName,driverLocality,driverAge,"not defined",verified, "no"/*availablity*/);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Ambulances").child(uid).setValue(serviceDetails);

        Toast.makeText(this, "Data is being saved!", Toast.LENGTH_SHORT).show();

        saveFiles(uid);
    }

    public void saveFiles(final String uid){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        uploadFile(Uri.fromFile(new File(textVehicleLicence.getText().toString())), uid, 1);
        uploadFile(Uri.fromFile(new File(textAadhar.getText().toString())), uid, 2);
        uploadFile(Uri.fromFile(new File(textRC.getText().toString())), uid, 3);
        uploadFile(Uri.fromFile(new File(textDriverLicence.getText().toString())), uid, 4);

    }


    public void uploadFile(Uri file,String uid, int no){
        StorageReference riversRef = storageRef.child("docs/ambulance/"+uid+"/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading file "+no);
        progressDialog.show();


        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                count++;
                progressDialog.dismiss();
                Toast.makeText(ServiceDetailsActivity.this, "Failed "+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                count++;
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

        if(requestCode == FilePickerConst.REQUEST_CODE_PHOTO){
            if(resultCode== Activity.RESULT_OK && data!=null)
            {
                docPaths.clear();
                docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
            }
        }

        if(docPaths.size()==0){
            Log.d("docPaths", "size 0");
        }
        else {
            if (currentFile.equals("vehicle_licence")) {
                textVehicleLicence.setText(docPaths.get(0));
            }
            if (currentFile.equals("aadhar")) {
                textAadhar.setText(docPaths.get(0));
            }
            if (currentFile.equals("rc_book")) {
                textRC.setText(docPaths.get(0));
            }
            if (currentFile.equals("driver_licence")) {
                textDriverLicence.setText(docPaths.get(0));
            }
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

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Complete the process")
                .setMessage("You haven't completed the registration. Please complete and save your details!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();

    }
}
