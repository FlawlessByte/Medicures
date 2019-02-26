package co.realinventor.medicures.MedStore;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.R;

public class MedRequestsActivity extends AppCompatActivity {
    private List<Medicine> medicineList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MedicineAdapter mAdapter;
    private String uid ;
    private DatabaseReference mDatabase;
    private TextView textViewMsgMedRequest;
    private DatabaseReference ref;
    private MedStoreDetails medStoreDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_requests);

        Log.d("Activity", "MedRequestsActivity");

        textViewMsgMedRequest = findViewById(R.id.textViewMsgMedRequest);

        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("MedStores").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDatabase", "Medstore details retrieved");
                medStoreDetails = dataSnapshot.getValue(MedStoreDetails.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_medicine);

        mAdapter = new MedicineAdapter(medicineList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FullScreenDialog.currentMedicine = medicineList.get(position);
                //Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                FullScreenDialog.medStoreDetails = medStoreDetails;
                FullScreenDialog dialog = new FullScreenDialog();
                dialog.show(getSupportFragmentManager().beginTransaction(), FullScreenDialog.TAG);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myFeedbackQuery = mDatabase.child("MedRequests");
        myFeedbackQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Log.d("MFirebaseDatabase", "Got MedRequests");
                    Medicine medicine = postSnapshot.getValue(Medicine.class);
                    if (medicine.getTo().equals(uid)){
                        medicine.trans_id = postSnapshot.getKey();
                        medicineList.add(medicine);
                    }

                }

                if(medicineList.size() == 0)
                    textViewMsgMedRequest.setVisibility(View.VISIBLE);

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FirebaseDatabase", databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Sorry, some error occured!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
