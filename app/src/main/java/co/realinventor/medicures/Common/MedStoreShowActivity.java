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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private List<MedStoreDetails> allMedStoreList = new ArrayList<>();
    private TextView textViewMsgMedStore;
    private EditText searchEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_store_show);

        textViewMsgMedStore = findViewById(R.id.textViewMsgMedStore);
        searchEditText = findViewById(R.id.searchEditTextMed);
        searchButton = findViewById(R.id.searchButtonMed);


        //RecyclerView things
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_MedShow);

        mAdapter = new MedStoreAdapter(medStoreList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Search Button", "Pressed");
                String query = searchEditText.getText().toString();
                Log.d("Search Query", query);
                //Toast.makeText(getApplicationContext(), "Query : " + query, Toast.LENGTH_SHORT).show();

                if(query.equals("")){
                    //Show all elements
                    medStoreList.clear();
                    medStoreList.addAll(allMedStoreList);
                }
                else{
//                    Toast.makeText(getApplicationContext(), "AllServiceList count : " + allMedStoreList.size(), Toast.LENGTH_SHORT).show();
                    List<MedStoreDetails> filteredList = new ArrayList<>();
                    for (MedStoreDetails row : allMedStoreList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.locality.toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    medStoreList.clear();

                    medStoreList.addAll(filteredList);
                }
                if(medStoreList.size() == 0){
                    textViewMsgMedStore.setVisibility(View.VISIBLE);
                }
                else{
                    textViewMsgMedStore.setVisibility(View.GONE);
                }

                mAdapter = new MedStoreAdapter(medStoreList);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }
        });



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

                    Log.d("FirebaseDatabase", "Got Med Stores");
                    Log.d("Med store count ", ""+dataSnapshot.getChildrenCount());
                    MedStoreDetails mdetails = postSnapshot.getValue(MedStoreDetails.class);
                    if(mdetails!=null) {
                        if (Statics.MED_REQ_ACTIVITY.equals("User")) {
                            if (mdetails.verified.equals("yes")) {
                                Log.d("FirebaseDatabase", "verified ");
                                medStoreList.add(mdetails);
                            }
                        } else {
                            medStoreList.add(mdetails);
                        }
                    }

                }

                if(medStoreList.size() == 0){
                    textViewMsgMedStore.setVisibility(View.VISIBLE);
                }
                else{
                    textViewMsgMedStore.setVisibility(View.GONE);
                }

                allMedStoreList.addAll(medStoreList); // copied the all list

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase database", databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Sorry, some error occured!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}
