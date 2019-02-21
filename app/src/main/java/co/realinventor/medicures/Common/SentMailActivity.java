package co.realinventor.medicures.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.MedStore.Medicine;
import co.realinventor.medicures.R;

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

public class SentMailActivity extends AppCompatActivity {
    String uid ;
    private DatabaseReference mDatabase;
    private List<Object> allItemsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SentMailAdapter mAdapter;
    private TextView textViewMsgFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_mail);

        Log.d("Activity", "SentMailActiivity");

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        textViewMsgFeedback = findViewById(R.id.textViewMsgFeedback);

        //RecyclerView things
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_sent_mail);

        mAdapter = new SentMailAdapter(allItemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myFeedbackQuery = mDatabase.child("feedbacks");
        myFeedbackQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Log.d("FirebaseDatabase", "Got feedbacks");
                    Feedback feedback = postSnapshot.getValue(Feedback.class);
                    if (feedback.getFrom().equals(uid)){
                        allItemsList.add(feedback);
                    }

                }

                if(allItemsList.size() == 0)
                    textViewMsgFeedback.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Get Feedbacks", databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Sorry, some error occured!", Toast.LENGTH_SHORT).show();
            }
        });

        myFeedbackQuery = mDatabase.child("Notifications");
        myFeedbackQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Log.d("FirebaseDatabase", "Got notifications");
                    Notifications notifications = postSnapshot.getValue(Notifications.class);
                    if (notifications.getFrom().equals(uid)){
                        allItemsList.add(notifications);
                    }

                }

                if(allItemsList.size() == 0)
                    textViewMsgFeedback.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Get Notifications", databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Sorry, some error occured!", Toast.LENGTH_SHORT).show();
            }
        });


        myFeedbackQuery = mDatabase.child("MedRequests");
        myFeedbackQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Log.d("FirebaseDatabase", "Got Med Requests");
                    Medicine medicine = postSnapshot.getValue(Medicine.class);
                    if (medicine.getFrom().equals(uid)){
                        allItemsList.add(medicine);
                    }

                }

                if(allItemsList.size() == 0)
                    textViewMsgFeedback.setVisibility(View.VISIBLE);
                else{
                    textViewMsgFeedback.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Get MedRequests", databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Sorry, some error occured!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
