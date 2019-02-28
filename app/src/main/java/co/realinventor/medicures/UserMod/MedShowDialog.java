package co.realinventor.medicures.UserMod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import co.realinventor.medicures.Common.PrescDetails;
import co.realinventor.medicures.MedStore.MedStoreDetails;
import co.realinventor.medicures.MedStore.Medicine;
import co.realinventor.medicures.R;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;

public class MedShowDialog extends DialogFragment {
    public static String TAG = "MedShowDialog";
    public static MedStoreDetails currentMedDetails;
    private TextView medYShopName;
    private EditText medYMedicineName, medYMedicineDosage, medYMedicineQty;
    private Button medYOrder, medYCancel, medYUploadPresc;
    private DatabaseReference ref;
    String uid;
    private UserDetails userDetails;
    private ArrayList<Medicine> medList ;
    private ArrayList<String> docPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        medList = new ArrayList<Medicine>();

        ref = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref.child("User").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDatabase", "Got user data");
                userDetails = dataSnapshot.getValue(UserDetails.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FirebaseDatabase", "Error retrieving data");
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.med_show_dialog, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarDialogMedUser);
        toolbar.setNavigationIcon(R.drawable.close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        toolbar.setTitle("Place an order!");


        medYShopName = view.findViewById(R.id.medYShopName);
        medYMedicineName = view.findViewById(R.id.medYMedicineName);
        medYMedicineDosage = view.findViewById(R.id.medYMedicineDosage);
        medYMedicineQty = view.findViewById(R.id.medYMedicineQty);

        medYOrder = view.findViewById(R.id.medYOrder);
        medYCancel= view.findViewById(R.id.medYCancel);
        medYUploadPresc = view.findViewById(R.id.medYUploadPresc);

        medYShopName.setText(currentMedDetails.shopName);

        ref = FirebaseDatabase.getInstance().getReference();


        medYUploadPresc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upload prescription

                String trans_id, to, from, customerEmail, customerName, reviewed /*yes or no*/, status /*approved or denied*/, filename;
                trans_id = UUID.randomUUID().toString();
                to = currentMedDetails.mUid;
                from = FirebaseAuth.getInstance().getCurrentUser().getUid();
                customerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                customerName = userDetails.first_name;
                reviewed = "no";
                status = "";
                filename = "";

                PrescDetails presc = new PrescDetails(trans_id, to, from, customerEmail, customerName, reviewed, status, "", "", filename);
                Intent i = new Intent(getContext(), ViewPrescActivity.class);
                i.putExtra("presc", presc);
                startActivity(i);



            }
        });

        medYOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String trans_id, medicine_name, dosage ,quantity, to, from, customerEmail,date, time, customerName, reviewed /*yes or no*/, status /*approved or denied*/;
                trans_id = UUID.randomUUID().toString();
                medicine_name = medYMedicineName.getText().toString();
                dosage = medYMedicineDosage.getText().toString();
                quantity = medYMedicineQty.getText().toString();
                to = currentMedDetails.mUid;
                from = FirebaseAuth.getInstance().getCurrentUser().getUid();
                customerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                customerName = userDetails.first_name;
                reviewed = "no";
                status = "";
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                date = df.format(Calendar.getInstance().getTime());
                df = new SimpleDateFormat("HH:mm:ss");
                time = df.format(Calendar.getInstance().getTime());

                Medicine newMedicine = new Medicine(trans_id, medicine_name, dosage, quantity, to, from, customerEmail, customerName, reviewed, status, "","");
                newMedicine.date = date + " | " +time;


                if(TextUtils.isEmpty(medicine_name) || TextUtils.isEmpty(dosage) || TextUtils.isEmpty(quantity)){
                    Toast.makeText(getContext(), "Some of the fields are empty!", Toast.LENGTH_SHORT).show();
                }else{
                    medList.add(newMedicine);
                }

                if(medList.size() != 0){
                    //Proceed to next acitivty
                    Intent i = new Intent(getContext(), ViewMedListActivity.class);
                    i.putExtra("list", medList);
                    getActivity().startActivity(i);
                    closeDialog();

                }


//                Log.d("Medicine Reuqest", "Placed");
                //Toast.makeText(getContext(), "Your order has been placed!", Toast.LENGTH_SHORT).show();

            }
        });


        medYCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear all the fields and add the details to List


                String trans_id, medicine_name, dosage ,quantity, to, from, customerEmail, customerName, reviewed /*yes or no*/, status /*approved or denied*/;
                trans_id = UUID.randomUUID().toString();
                medicine_name = medYMedicineName.getText().toString();
                dosage = medYMedicineDosage.getText().toString();
                quantity = medYMedicineQty.getText().toString();
                to = currentMedDetails.mUid;
                from = FirebaseAuth.getInstance().getCurrentUser().getUid();
                customerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                customerName = userDetails.first_name;
                reviewed = "no";
                status = "";

                Medicine newMedicine = new Medicine(trans_id, medicine_name, dosage, quantity, to, from, customerEmail, customerName, reviewed, status,  "", "");

                medList.add(newMedicine);

                medYMedicineName.setText("");
                medYMedicineDosage.setText("");
                medYMedicineQty.setText("");

                Toast.makeText(getContext(), "Add another medicine!", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }



    private void closeDialog(){
        this.dismiss();
    }


}
