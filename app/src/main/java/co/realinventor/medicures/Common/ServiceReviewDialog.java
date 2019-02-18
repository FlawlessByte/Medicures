package co.realinventor.medicures.Common;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import co.realinventor.medicures.AmbulanceService.ServiceDetails;
import co.realinventor.medicures.Common.Statics;
import co.realinventor.medicures.R;

public class ServiceReviewDialog extends DialogFragment{

    public static String TAG = "ServiceReviewDialog";
    public static ServiceDetails currentServiceDetails;
    private TextView serviceXDriverName, serviceXLocality, serviceXAge, serviceXPhone;
    private Button serviceXApproveButton, serviceXDenyButton;
    private DatabaseReference ref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.service_review_dailog, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarDialogServiceAdmin);
        toolbar.setNavigationIcon(R.drawable.close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        toolbar.setTitle("Review Ambulance");


        ref = FirebaseDatabase.getInstance().getReference();

        serviceXDriverName= view.findViewById(R.id.serviceXDriverName);
        serviceXLocality = view.findViewById(R.id.serviceXLocality);
        serviceXAge = view.findViewById(R.id.serviceXAge);
        serviceXPhone= view.findViewById(R.id.serviceXPhone);

        serviceXApproveButton = view.findViewById(R.id.serviceXApproveButton);
        serviceXDenyButton = view.findViewById(R.id.serviceXDenyButton);


        if(Statics.SERVICE_REQ_ACTIVITY.equals("User")){
            serviceXApproveButton.setText("Add to favourites");
            serviceXDenyButton.setText("Cancel");
        }

        serviceXDriverName.setText("Driver Name : " + currentServiceDetails.driverName);
        serviceXLocality.setText("Locality : " + currentServiceDetails.driverLocality);
        serviceXAge.setText("Age : " + currentServiceDetails.driverAge);
        serviceXPhone.setText("Phone : " + currentServiceDetails.phone);

        serviceXApproveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Statics.SERVICE_REQ_ACTIVITY.equals("User")){ //user access
                    //add to favourites
                    ref.child("User").child(FirebaseAuth.getInstance().getUid()).child("fav_services").child(currentServiceDetails.serviceID)
                            .setValue("fav");
                    Toast.makeText(getContext(), "The Ambulance service has been added to favourites!", Toast.LENGTH_SHORT).show();
                }
                else { //admin access
                    currentServiceDetails.verified = "yes";
                    ref.child("Ambulances").child(currentServiceDetails.serviceID).setValue(currentServiceDetails);
                    Log.d("Ambulance Data", "Stored");
                    Toast.makeText(getContext(), "The Ambulance Service request has been approved!", Toast.LENGTH_SHORT).show();
                }
                closeDialog();
            }
        });

        serviceXDenyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentServiceDetails.verified = "no";
                if(!Statics.SERVICE_REQ_ACTIVITY.equals("User")) {
                    ref.child("Ambulances").child(currentServiceDetails.serviceID).setValue(currentServiceDetails);
                    Log.d("Ambulance Data", "Stored");
                    Toast.makeText(getContext(), "The Ambulance Service request has been rejected!", Toast.LENGTH_SHORT).show();
                }
                closeDialog();
            }
        });



        return view;
    }


    private void closeDialog(){
        this.dismiss();
    }




}
