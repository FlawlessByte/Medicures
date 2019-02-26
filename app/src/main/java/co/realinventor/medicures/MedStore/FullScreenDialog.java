package co.realinventor.medicures.MedStore;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.sendgrid.SendGrid;
//import com.sendgrid.SendGridException;
import org.angmarch.views.NiceSpinner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import co.realinventor.medicures.Common.NotificationManager;
import co.realinventor.medicures.R;

public class FullScreenDialog extends DialogFragment {

    public static String TAG = "FullScreenDialog";
    public static Medicine currentMedicine;
    private Medicine newMed;
    private String SENDGRID_API_KEY = "";
    private TextView medicineReqTransID, medicineReqCustomerName, medicineReqMedicineName, medicineReqMedicineDosage, medicineReqMedicineQuantity;
    private Button medicineReqApproveButton, medicineReqDenyButton;
    private EditText editTextAmount;
    String mMailTo, mMailFrom, mUserUid, mMedUid, mMsg, mSub, senderMail;
    private DatabaseReference ref;
    public static MedStoreDetails medStoreDetails ;
    private NiceSpinner spinner;
    private Map<String, Object> pharmacists = new HashMap<>();
    private List<String> pharms = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        Log.d("FullScreenDialog", "Entered");

        mMailFrom = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mUserUid = currentMedicine.getFrom();
        mMailTo = currentMedicine.getCustomerEmail();
        mMedUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("FullScreenDialog MedUid", mMedUid);

        senderMail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        ref = FirebaseDatabase.getInstance().getReference();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_full_screen_dialog, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarDialog);
        toolbar.setNavigationIcon(R.drawable.close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        toolbar.setTitle("Review medicine request");


        medicineReqTransID = view.findViewById(R.id.medicineReqTransID);
        medicineReqCustomerName = view.findViewById(R.id.medicineReqCustomerName);
        medicineReqMedicineName = view.findViewById(R.id.medicineReqMedicineName);
        medicineReqMedicineDosage = view.findViewById(R.id.medicineReqMedicineDosage);
        medicineReqMedicineQuantity = view.findViewById(R.id.medicineReqMedicineQuantity);
        editTextAmount = view.findViewById(R.id.editTextAmount);
        spinner = (NiceSpinner) view.findViewById(R.id.pharmSpinner);

        medicineReqApproveButton = view.findViewById(R.id.medicineReqApproveButton);
        medicineReqDenyButton = view.findViewById(R.id.medicineReqDenyButton);


        medicineReqTransID.setText("Transaction ID : " +currentMedicine.getTrans_id());
        medicineReqCustomerName.setText("Customer Name : " +currentMedicine.getCustomerName());
        medicineReqMedicineName.setText("Medicine Name : " +currentMedicine.getMedicine_name());
        medicineReqMedicineDosage.setText("Dosage : " +currentMedicine.getDosage());
        medicineReqMedicineQuantity.setText("Quantity : " +currentMedicine.getQuantity());


        ref.child("MedStoresPharm").child(mMedUid+"Pharm").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDatabase", "Medstore details retrieved");

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    pharms.add(data.getValue().toString());
                }
                spinner.attachDataSource(pharms);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });






        medicineReqApproveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Confirm?")
                        .setMessage("Are you sure want to approve the request?")
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sendApprovalMail();
                                deleteMedRequest();
                                Toast.makeText(getContext(), "The approval notification has been sent to user!", Toast.LENGTH_SHORT).show();
                                dismiss();

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing

                            }
                        })
                        .show();

            }
        });

        medicineReqDenyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Confirm?")
                        .setMessage("Are you sure want to reject the request?")
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sendRejectionMail();
                                deleteMedRequest();
                                Toast.makeText(getContext(), "The rejection notification has been sent to user!", Toast.LENGTH_SHORT).show();
                                dismiss();

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing

                            }
                        })
                        .show();
            }
        });


        return view;
    }


    private void deleteMedRequest(){
        ref.child("MedRequests").child(currentMedicine.trans_id).removeValue();
    }


    private void sendApprovalMail(){
        mMsg = "Dear user, \n" +
                "Your medicine request with transaction id "+ currentMedicine.getTrans_id() +", has been approved by, "+
                medStoreDetails.shopName +". \nMedicine Name : " + currentMedicine.getMedicine_name() + "\nMedicine dosage : "
                +currentMedicine.getDosage() + "\nQuantity : " + currentMedicine.getQuantity()+
                "\nTotal amount : "+ editTextAmount.getText().toString() + "\nPharmacist : "+pharms.get(spinner.getSelectedIndex());
        mSub = "Approval of order";

        newMed = currentMedicine;
        newMed.status = "approved";
        newMed.reviewed = "yes";

        ref.child("MedRequests").child(mMedUid).setValue(newMed);

        new NotificationManager().makeNotification(mMsg, currentMedicine.from, medStoreDetails.mUid, medStoreDetails.shopName, senderMail);

        sendEmail(mMailTo, mMailFrom, mSub, mMsg);
    }

    private void sendRejectionMail(){
        mMsg = "Dear user, \n" +
                "Your medicine request with transaction id "+ currentMedicine.getTrans_id() +", has been rejected by, "+
                medStoreDetails.shopName +". Make another request with another medical shop";
        mSub = "Rejection of order";

        newMed = currentMedicine;
        newMed.status = "rejected";
        newMed.reviewed = "yes";

        ref.child("MedRequests").child(mMedUid).setValue(newMed);

        new NotificationManager().makeNotification(mMsg, currentMedicine.from, medStoreDetails.mUid, medStoreDetails.shopName, senderMail);

        sendEmail(mMailTo, mMailFrom, mSub, mMsg);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    private void closeDialog(){
        this.dismiss();
    }


    //Your method you are sending the email from
    public void sendEmail(String mailTo, String mailFrom, String subject, String msg) {
        //Alternate way of instantiating
        //SendGrid sendGrid = new SendGrid(SENDGRID_USERNAME,SENDGRID_PASSWORD);

        //Instantiate the object using your API key String
//        SendGrid sendgrid = new SendGrid(SENDGRID_API_KEY);
//
//        SendGrid.Email email = new SendGrid.Email();
//        email.addTo(mailTo);
//        email.setFrom(mailFrom);
//        email.setSubject(subject);
//        email.setText(msg);
//
//        try {
//            SendGrid.Response response = sendgrid.send(email);
//        }
//        catch (SendGridException e) {
//            Log.e("sendError", "Error sending email");
//        }
    }
}
