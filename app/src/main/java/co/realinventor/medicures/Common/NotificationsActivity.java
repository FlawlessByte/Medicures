package co.realinventor.medicures.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.MedStore.Medicine;
import co.realinventor.medicures.MedStore.MedicineAdapter;
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

public class NotificationsActivity extends AppCompatActivity {
    private List<Notifications> notificationsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotificationsAdapter notificationsAdapter;
    private String uid ;
    private DatabaseReference mDatabase;
    private TextView textViewMsgNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        textViewMsgNotifications = findViewById(R.id.textViewMsgNotifications);



        Log.d("Activity", "NotificationsActivity");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_notifications);

        notificationsAdapter = new NotificationsAdapter(notificationsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(notificationsAdapter);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myFeedbackQuery = mDatabase.child("Notifications");
        myFeedbackQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Log.d("MFirebaseDatabase", "Got Notifications");
                    Notifications notifications = postSnapshot.getValue(Notifications.class);
                    if (notifications.getTo().equals(uid)){
                        notificationsList.add(notifications);
                    }

                }

                if(notificationsList.size() == 0)
                    textViewMsgNotifications.setVisibility(View.VISIBLE);

                notificationsAdapter.notifyDataSetChanged();
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
        this.finish();
        super.onBackPressed();
    }
}
