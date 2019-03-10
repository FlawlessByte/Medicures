package co.realinventor.medicures.BloodBank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.R;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewBloodBankActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BloodDonorAdapter adapter;
    private List<BloodDonor> allDonorsList = new ArrayList<BloodDonor>();
    private List<BloodDonor> filteredDonorsList = new ArrayList<BloodDonor>();
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blood_bank);

        recyclerView = findViewById(R.id.bloodRecyclerView);
        adapter = new BloodDonorAdapter(filteredDonorsList);
        adapter.context = this;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);


        //get data from database
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("blood_donors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot donorSnapShot : dataSnapshot.getChildren()){
                    BloodDonor donor = donorSnapShot.getValue(BloodDonor.class);
                    allDonorsList.add(donor);
                    filteredDonorsList.add(donor);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public void chipSelected(View view){
        String tag = view.getTag().toString();
        if (tag.equals("All")){
            filteredDonorsList.clear();
            filteredDonorsList.addAll(allDonorsList);
        }
        else{
            filteredDonorsList.clear();
            for(BloodDonor donor : allDonorsList){
                if(donor.getBloodGroup().contains(tag))
                    filteredDonorsList.add(donor);
            }
        }
        adapter.notifyDataSetChanged();


    }
}
