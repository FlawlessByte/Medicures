package co.realinventor.medicures.UserMod;

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

import java.util.UUID;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import co.realinventor.medicures.MedStore.MedStoreDetails;
import co.realinventor.medicures.MedStore.Medicine;
import co.realinventor.medicures.R;

public class MedShowDialog extends DialogFragment {
    public static String TAG = "MedShowDialog";
    public static MedStoreDetails currentMedDetails;
    private TextView medYShopName;
    private EditText medYMedicineName, medYMedicineDosage, medYMedicineQty;
    private Button medYOrder, medYCancel;
    private DatabaseReference ref;
    String uid;
    private UserDetails userDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

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

        medYShopName.setText(currentMedDetails.shopName);

        ref = FirebaseDatabase.getInstance().getReference();

        medYOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                Medicine newMedicine = new Medicine(trans_id, medicine_name, dosage, quantity, to, from, customerEmail, customerName, reviewed, status);

                ref.child("MedRequests").child(trans_id).setValue(newMedicine);
                Log.d("Medicine Reuqest", "Placed");
                Toast.makeText(getContext(), "Your order has been placed!", Toast.LENGTH_SHORT).show();
                closeDialog();
            }
        });


        medYCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        return view;
    }


    private void closeDialog(){
        this.dismiss();
    }


}
