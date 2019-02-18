package co.realinventor.medicures.MedStore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.R;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;

public class MedSignInActivity extends AppCompatActivity {
    ImageButton buttonBluePrint, buttonGST, buttonSanction, buttonPharmaceutical;
    EditText textBluePrint, textGST, textSanction, textPharmaceutical, inputShopName, inputLocality,inputPinCode, inputOwnerName, inputPhone, inputPharmacist;
    ArrayList<String> docPaths = new ArrayList<>();
    String currentFile  = "";
    FirebaseStorage storage;
    StorageReference storageRef;
    private boolean finished;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_sign_in);

        Log.d("Activity", "MedSignInActivity");

        buttonBluePrint = findViewById(R.id.buttonBluePrint);
        buttonGST = findViewById(R.id.buttonGST);
        buttonSanction = findViewById(R.id.buttonSanction);
        buttonPharmaceutical = findViewById(R.id.buttonPharmaceutical);

        inputShopName = findViewById(R.id.inputShopName);
        inputLocality = findViewById(R.id.inputLocality);
        inputPinCode = findViewById(R.id.inputPinCode);
        inputOwnerName = findViewById(R.id.inputOwnerName);
        inputPharmacist = findViewById(R.id.inputPharmacist);
        inputPhone = findViewById(R.id.inputPhone);

        textBluePrint = findViewById(R.id.textBluePrint);
        textGST = findViewById(R.id.textGST);
        textSanction = findViewById(R.id.textSanction);
        textPharmaceutical = findViewById(R.id.textPharmaceutical);




        buttonBluePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("bluePrintButton", "Pressed");
                currentFile = "blue_print";
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1)
                        .setActivityTitle("Select Blue print")
                        .setActivityTheme(R.style.LibAppTheme)
                        .sortDocumentsBy(SortingTypes.name)
                        .pickPhoto(MedSignInActivity.this);
            }
        });
        buttonGST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GSTButton", "Pressed");
                currentFile = "GST";
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1)
                        .setActivityTitle("Select GST doc")
                        .setActivityTheme(R.style.LibAppTheme)
                        .sortDocumentsBy(SortingTypes.name)
                        .pickPhoto(MedSignInActivity.this);
            }
        });
        buttonSanction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SanctionButton", "Pressed");
                currentFile = "sanction";
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1)
                        .setActivityTitle("Select sanction doc")
                        .setActivityTheme(R.style.LibAppTheme)
                        .sortDocumentsBy(SortingTypes.name)
                        .pickPhoto(MedSignInActivity.this);
            }
        });
        buttonPharmaceutical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PharmaceuticalButton", "Pressed");
                currentFile = "pharmaceutical";
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1)
                        .setActivityTitle("Select pharmaceutical doc")
                        .setActivityTheme(R.style.LibAppTheme)
                        .sortDocumentsBy(SortingTypes.name)
                        .pickPhoto(MedSignInActivity.this);
            }
        });



    }

    public void medRegisterButtonClicked(View view){
        Log.d("RegisterButton", "Pressed");
        if(!inputsOk()){
            Toast.makeText(getApplicationContext(), "Fill all the fields and try again!", Toast.LENGTH_SHORT).show();
        }
        else{
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if(auth.getCurrentUser() != null){
                saveUserData(auth.getCurrentUser().getUid());

                startActivity(new Intent(MedSignInActivity.this, MedLoggedActivity.class));
                finish();
            }

        }

    }


    public void saveUserData(String uid){
        String shopName,locality,ownerName,pharmacist,phone,pinCode,verified = "no";
        shopName = inputShopName.getText().toString();
        locality = inputLocality.getText().toString();
        ownerName = inputOwnerName.getText().toString();
        pharmacist = inputPharmacist.getText().toString();
        phone = inputPhone.getText().toString();
        pinCode = inputPinCode.getText().toString();

        MedStoreDetails medStoreDetails = new MedStoreDetails(uid ,shopName, locality, ownerName, pharmacist, phone, pinCode, verified);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("MedStores").child(uid).setValue(medStoreDetails);

        Toast.makeText(this, "Data is being saved!", Toast.LENGTH_SHORT).show();

        saveFiles(uid);
    }

    public void saveFiles(String uid){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        uploadFile(Uri.fromFile(new File(textBluePrint.getText().toString())), uid, 1);
        uploadFile(Uri.fromFile(new File(textGST.getText().toString())), uid, 2);
        uploadFile(Uri.fromFile(new File(textSanction.getText().toString())), uid, 3);
        uploadFile(Uri.fromFile(new File(textPharmaceutical.getText().toString())), uid, 4);
    }

    public void uploadFile(Uri file,String uid, int no){
        finished =  false;
        StorageReference riversRef = storageRef.child("docs/med_store/"+uid+"/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading file "+no);
        progressDialog.show();


        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                finished = true;
                progressDialog.dismiss();
                Toast.makeText(MedSignInActivity.this, "Failed "+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                finished = true;
                progressDialog.dismiss();
                Toast.makeText(MedSignInActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                        .getTotalByteCount());
                progressDialog.setMessage("Uploaded "+(int)progress+"%");
            }
        });

        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                if(finished){
                    handler.removeCallbacks(this);
                }
                handler.postDelayed(this, 200);
            }
        };

        handler.postDelayed(r, 100);


    }



    public boolean inputsOk(){
        boolean status = true;

        if(inputShopName.getText().toString().equals(""))
            status = false;
        if(inputLocality.getText().toString().equals(""))
            status = false;
        if(inputOwnerName.getText().toString().equals(""))
            status = false;
        if(inputPharmacist.getText().toString().equals(""))
            status = false;
        if(inputPhone.getText().toString().equals(""))
            status = false;
        if(inputPinCode.getText().toString().equals(""))
            status = false;
        if(textBluePrint.getText().toString().equals("Upload blue print"))
            status = false;
        if(textGST.getText().toString().equals("Upload GST Certificate"))
            status = false;
        if(textSanction.getText().toString().equals("Upload Sanction Certificate"))
            status = false;
        if(textPharmaceutical.getText().toString().equals("Upload Pharmaceutical Certificate"))
            status = false;

        return status;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("ActivityResult", resultCode+"");
        if(requestCode == FilePickerConst.REQUEST_CODE_PHOTO){
            if(resultCode== Activity.RESULT_OK && data!=null)
            {
                docPaths.clear();
                docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
            }
        }

        if(docPaths.size() != 0) {
            if (currentFile.equals("blue_print")) {
                textBluePrint.setText(docPaths.get(0));
            }
            if (currentFile.equals("GST")) {
                textGST.setText(docPaths.get(0));
            }
            if (currentFile.equals("sanction")) {
                textSanction.setText(docPaths.get(0));
            }
            if (currentFile.equals("pharmaceutical")) {
                textPharmaceutical.setText(docPaths.get(0));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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
