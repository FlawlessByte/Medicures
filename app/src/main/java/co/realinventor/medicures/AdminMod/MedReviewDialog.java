package co.realinventor.medicures.AdminMod;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import co.realinventor.medicures.MedStore.MedStoreDetails;
import co.realinventor.medicures.R;

public class MedReviewDialog extends DialogFragment {
    public static String TAG = "MedReviewDialog";
    public static MedStoreDetails currentMedDetails;
    private TextView medXShopName, medXLocality, medXOwnerName, medXPharmacist, medXPhone , medXPinCode;
    private Button medXApproveButton, medXDenyButton;
    private DatabaseReference ref;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

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


    private void closeDialog(){
        this.dismiss();
    }
}
