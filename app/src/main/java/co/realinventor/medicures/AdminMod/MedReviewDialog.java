package co.realinventor.medicures.AdminMod;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import co.realinventor.medicures.MedStore.MedStoreDetails;
import co.realinventor.medicures.R;

public class MedReviewDialog extends DialogFragment {
    public static String TAG = "MedReviewDialog";
    public static MedStoreDetails currentMedDetails;
    private TextView medXShopName, medXLocality, medXOwnerName, medXPharmacist, medXPhone , medXPinCode;
    private Button medXApproveButton, medXDenyButton, bluePrintButton, gstButton, sanctionButton, pharmaceuticalButton;
    private DatabaseReference ref;
    FirebaseStorage storage;
    StorageReference storageRef;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference();
//        StorageReference ref = storageRef.child("docs/med_store/"+currentMedDetails.mUid+"/"+"gst.jpg");
//
//        try {
//            File localFile = File.createTempFile("images", "jpg");
//        }
//        catch (Exception e){
//
//        }
//
//        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Local temp file has been created
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.med_review_dialog, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarDialogMedAdmin);
        toolbar.setNavigationIcon(R.drawable.close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        toolbar.setTitle("Review Medical Store");


        ref = FirebaseDatabase.getInstance().getReference();

        medXShopName = view.findViewById(R.id.medXShopName);
        medXLocality = view.findViewById(R.id.medXLocality);
        medXOwnerName = view.findViewById(R.id.medXOwnerName);
        medXPharmacist= view.findViewById(R.id.medXPharmacist);
        medXPhone = view.findViewById(R.id.medXPhone);
        medXPinCode = view.findViewById(R.id.medXPinCode);

        medXApproveButton = view.findViewById(R.id.medicineXApproveButton);
        medXDenyButton = view.findViewById(R.id.medicineXDenyButton);
        gstButton = view.findViewById(R.id.gstButton);
        bluePrintButton = view.findViewById(R.id.bluePrintButton);
        sanctionButton = view.findViewById(R.id.sanctionButton);
        pharmaceuticalButton = view.findViewById(R.id.pharmaceuticalButton);



//        gstButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showImageAlert(R.drawable.ambulance_large, "Ambulance");
//            }
//        });


        medXShopName.setText("Shop Name : " + currentMedDetails.shopName);
        medXLocality.setText("Locality : " + currentMedDetails.locality);
        medXOwnerName.setText("Owner Name : " + currentMedDetails.ownerName);
        medXPharmacist.setText("Pharmacist : " + currentMedDetails.pharmacist);
        medXPhone.setText("Phone : " + currentMedDetails.phone);
        medXPinCode.setText("Pin Code : " + currentMedDetails.pinCode);

        medXApproveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMedDetails.verified = "yes";

                ref.child("MedStores").child(currentMedDetails.mUid).setValue(currentMedDetails);
                Log.d("Medical Store Data", "Stored");
                Toast.makeText(getContext(), "The Medical store request has been approved!", Toast.LENGTH_SHORT) .show();
            }
        });

        medXDenyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMedDetails.verified = "no";

                ref.child("MedStores").child(currentMedDetails.mUid).setValue(currentMedDetails);
                Log.d("Medical Store Data", "Stored");
                Toast.makeText(getContext(), "The Medical store request has been rejected!", Toast.LENGTH_SHORT) .show();
                closeDialog();
            }
        });



        return view;
    }

    private void showImageAlert(int res,String title){
        ImageView image = new ImageView(getContext());
        image.setImageResource(res);

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle(title)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setView(image)
                .show();
    }


    private void closeDialog(){
        this.dismiss();
    }
}
