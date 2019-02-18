package co.realinventor.medicures.Common;


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

public class FeedbackActivity extends AppCompatActivity {
    String uid ;
    private DatabaseReference mDatabase;
    private List<Feedback> feedbackList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FeedbackAdapter mAdapter;
    private TextView textViewMsgFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Log.d("Activity", "FeedbackActiivity");

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        textViewMsgFeedback = findViewById(R.id.textViewMsgFeedback);

        //RecyclerView things
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_feedback);

        mAdapter = new FeedbackAdapter(feedbackList);
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
                    if (feedback.getTo().equals(uid)){
                        feedbackList.add(feedback);
                    }

                }

                if(feedbackList.size() == 0)
                    textViewMsgFeedback.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Menu Account Button", databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Sorry, some error occured!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
