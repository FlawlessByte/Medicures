package co.realinventor.medicures.Common;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import co.realinventor.medicures.AmbulanceService.ServiceDetails;
import co.realinventor.medicures.Common.Statics;
import co.realinventor.medicures.R;
import co.realinventor.medicures.UserMod.ComposeFeedbackActivity;

public class ServiceReviewDialog extends DialogFragment{

    public static String TAG = "ServiceReviewDialog";
    public static ServiceDetails currentServiceDetails;
    private TextView serviceXDriverName, serviceXLocality, serviceXAge, serviceXPhone;
    private Button serviceXApproveButton, serviceXDenyButton;
    private DatabaseReference ref;
    private String CALL_NUMBER;
    private final int CALL_REQUEST = 100;


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
            serviceXApproveButton.setText("Call");
            serviceXDenyButton.setText("Cancel");
        }

        serviceXDriverName.setText("Driver Name : " + currentServiceDetails.driverName);
        serviceXLocality.setText("Locality : " + currentServiceDetails.driverLocality);
        serviceXAge.setText("Age : " + currentServiceDetails.driverAge);
        serviceXPhone.setText("Phone : " + currentServiceDetails.phone);

        final String to = currentServiceDetails.serviceID;
        final String from = "admin@medcure";
        final String senderName = "Admin";
        final String senderEmail = "admin@medcure.com";

        serviceXApproveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Statics.SERVICE_REQ_ACTIVITY.equals("User")){ //user access
                    //send feedback
//                    Intent i = new Intent(getContext(), ComposeFeedbackActivity.class);
//                    i.putExtra("to" , currentServiceDetails.serviceID);
//                    i.putExtra("role", currentServiceDetails.driverName);
//                    startActivity(i);
                    //call the service
                    CALL_NUMBER = currentServiceDetails.phone;
                    callPhoneNumber();


                }
                else { //admin access
                    currentServiceDetails.verified = "yes";
                    ref.child("Ambulances").child(currentServiceDetails.serviceID).setValue(currentServiceDetails);
                    String msg = "Your request for Ambulance account has been approved!";

                    new NotificationManager().makeNotification(msg, to, from, senderName, senderEmail);
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
                    String msg = "Your request for Ambulance account has been rejected!";

                    new NotificationManager().makeNotification(msg, to, from, senderName, senderEmail);

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


    public void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);

                    return;
                }
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel: "+CALL_NUMBER));
            startActivity(callIntent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if(requestCode == CALL_REQUEST)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();
            }
            else
            {
                Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
