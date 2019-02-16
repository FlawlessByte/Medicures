package co.realinventor.medicures.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.AdminMod.MedReviewDialog;
import co.realinventor.medicures.MedStore.MedStoreDetails;
import co.realinventor.medicures.MedStore.RecyclerTouchListener;
import co.realinventor.medicures.R;
import co.realinventor.medicures.UserMod.MedShowDialog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MedStoreShowActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MedStoreAdapter mAdapter;
    private DatabaseReference mDatabase;
    private List<MedStoreDetails> medStoreList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_store_show);

        //RecyclerView things
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_MedShow);

        mAdapter = new MedStoreAdapter(medStoreList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if(Statics.MED_REQ_ACTIVITY.equals("User")){
                    MedShowDialog.currentMedDetails = medStoreList.get(position);
                    MedShowDialog dialog = new MedShowDialog();
                    dialog.show(getSupportFragmentManager().beginTransaction(), MedShowDialog.TAG);
                }
                else{
                    MedReviewDialog.currentMedDetails = medStoreList.get(position);
                    MedReviewDialog dialog = new MedReviewDialog();
                    dialog.show(getSupportFragmentManager().beginTransaction(), MedReviewDialog.TAG);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myFeedbackQuery = mDatabase.child("MedStores");
        myFeedbackQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Log.d("FirebaseDatabase", "Got feedbacks");
                    MedStoreDetails mdetails = postSnapshot.getValue(MedStoreDetails.class);
                    if(Statics.MED_REQ_ACTIVITY.equals("User")){
                        if(mdetails.verified.equals("yes")){
                            medStoreList.add(mdetails);
                        }
                    }else {
                        medStoreList.add(mdetails);
                    }

                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase database", databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Sorry, some error occured!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
