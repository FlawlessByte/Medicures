package co.realinventor.medicures.AdminMod;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import co.realinventor.medicures.Common.NotificationManager;
import co.realinventor.medicures.MedStore.MedStoreDetails;
import co.realinventor.medicures.R;

public class MedReviewDialog extends DialogFragment {
    public static String TAG = "MedReviewDialog";
    public static MedStoreDetails currentMedDetails;
    private TextView medXShopName, medXLocality, medXOwnerName, medXPhone , medXPinCode;
    private Button medXApproveButton, medXDenyButton, bluePrintButton, gstButton, sanctionButton, pharmaceuticalButton;
    private DatabaseReference ref;
    FirebaseStorage storage;
    StorageReference storageRef;
    private NiceSpinner medXPharmacist;
    private Map<String, Object> pharmacists = new HashMap<>();
    private File gstFile = null, bluePrint = null, sanction = null, pharmaceutical = null;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        ref = FirebaseDatabase.getInstance().getReference();

//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReferenceFromUrl("gs://medicure-9c14e.appspot.com/docs/med_store");
//
//        Toast.makeText(getContext(), "StorageRefucket: "+storageRef.getBucket(), Toast.LENGTH_SHORT).show();
//
//
//        File rootPath = new File(Environment.getExternalStorageDirectory(), "docs");
//        if(!rootPath.exists()) {
//            rootPath.mkdirs();
//        }
//
//        gstFile = new File(rootPath, "gst.jpg");
//        bluePrint = new File(rootPath, "bluePrint.jpg");
//        sanction = new File(rootPath, "sanction.jpg");
//        pharmaceutical = new File(rootPath, "pharmaceutical.jpg");
//
//
//
////        try {
////            gstFile = File.createTempFile("gst", "jpg");
////            bluePrint = File.createTempFile("bluePrint", "jpg");
////            sanction = File.createTempFile("sanction", "jpg");
////            pharmaceutical = File.createTempFile("pharmaceutical", "jpg");
////        }
////        catch (Exception e){
////            e.printStackTrace();
////        }
//
//        //Get pharmaceutical file
//        StorageReference refs = storage.getReferenceFromUrl("gs://medicure-9c14e.appspot.com/docs/med_store/"+currentMedDetails.mUid+"/pharmaceutical.jpg");
//        refs.getFile(pharmaceutical).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Local temp file has been created
//                Toast.makeText(getContext(), "pharma file downloaded", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Toast.makeText(getContext(), "pharma file download failure", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //Get saanction file
//        refs = storage.getReferenceFromUrl("gs://medicure-9c14e.appspot.com/docs/med_store/"+currentMedDetails.mUid+"/sanction.jpg");
//        refs.getFile(sanction).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Local temp file has been created
//                Toast.makeText(getContext(), "sanction file downloaded", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Toast.makeText(getContext(), "sanction file download failure", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //Get blue print file
//        refs = storage.getReferenceFromUrl("gs://medicure-9c14e.appspot.com/docs/med_store/"+currentMedDetails.mUid+"/blue_print.jpg");
//        refs.getFile(bluePrint).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Local temp file has been created
//                Toast.makeText(getContext(), "blue print file downloaded", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Toast.makeText(getContext(), "blue print file download failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //Get gst file
//        refs = storage.getReferenceFromUrl("gs://medicure-9c14e.appspot.com/docs/med_store/"+currentMedDetails.mUid+"/gst.jpg");
//        refs.getFile(gstFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Local temp file has been created
//                Toast.makeText(getContext(), "GST file downloaded", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Toast.makeText(getContext(), "GST file downloa failed\nException: "+exception, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //Toast.makeText(getContext(), "bucket: "+refs.getBucket(), Toast.LENGTH_SHORT).show();




    }


    public void showImage(Uri imageUri) {
        Toast.makeText(getContext(), "Show Image", Toast.LENGTH_SHORT).show();
        Dialog builder = new Dialog(getContext(), android.R.style.Theme_Light);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(getContext());
        imageView.setImageURI(imageUri);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
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




        medXShopName = view.findViewById(R.id.medXShopName);
        medXLocality = view.findViewById(R.id.medXLocality);
        medXOwnerName = view.findViewById(R.id.medXOwnerName);
        medXPharmacist=(NiceSpinner) view.findViewById(R.id.medXPharmacist);
        medXPhone = view.findViewById(R.id.medXPhone);
        medXPinCode = view.findViewById(R.id.medXPinCode);

        medXApproveButton = view.findViewById(R.id.medicineXApproveButton);
        medXDenyButton = view.findViewById(R.id.medicineXDenyButton);
        gstButton = view.findViewById(R.id.gstButton);
        bluePrintButton = view.findViewById(R.id.bluePrintButton);
        sanctionButton = view.findViewById(R.id.sanctionButton);
        pharmaceuticalButton = view.findViewById(R.id.pharmaceuticalButton);



        gstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(Uri.fromFile(gstFile));
            }
        });
        bluePrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(Uri.fromFile(bluePrint));
            }
        });
        sanctionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(Uri.fromFile(sanction));
            }
        });
        pharmaceuticalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(Uri.fromFile(pharmaceutical));
            }
        });


        medXShopName.setText("Shop Name : " + currentMedDetails.shopName);
        medXLocality.setText("Locality : " + currentMedDetails.locality);
        medXOwnerName.setText("Owner Name : " + currentMedDetails.ownerName);
        medXPhone.setText("Phone : " + currentMedDetails.phone);
        medXPinCode.setText("Pin Code : " + currentMedDetails.pinCode);
        medXPharmacist.setVisibility(View.GONE);


        final String to = currentMedDetails.mUid;
        final String from = "Admin";
        final String senderName = "Admin";
        final String senderEmail = "admin@medcure.com";

        medXApproveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMedDetails.verified = "yes";

                ref.child("MedStores").child(currentMedDetails.mUid).setValue(currentMedDetails);
                String msg = "Your request for Medical Store account has been approved!";

                new NotificationManager().makeNotification(msg, to, from, senderName, senderEmail);
                Log.d("Medical Store Data", "Stored");
                Toast.makeText(getContext(), "The Medical store request has been approved!", Toast.LENGTH_SHORT) .show();
                closeDialog();

            }
        });

        medXDenyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMedDetails.verified = "no";

                ref.child("MedStores").child(currentMedDetails.mUid).setValue(currentMedDetails);
                String msg = "Your request for Medical Store account has been rejected!";

                new NotificationManager().makeNotification(msg, to, from, senderName, senderEmail);
                Log.d("Medical Store Data", "Stored");
                Toast.makeText(getContext(), "The Medical store request has been rejected!", Toast.LENGTH_SHORT) .show();
                closeDialog();
            }
        });



        return view;
    }



    private void closeDialog(){
        this.dismiss();
    }
}
