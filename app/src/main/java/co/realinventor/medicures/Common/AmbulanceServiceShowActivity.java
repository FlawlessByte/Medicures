package co.realinventor.medicures.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.AmbulanceService.ServiceDetails;
import co.realinventor.medicures.MedStore.RecyclerTouchListener;
import co.realinventor.medicures.R;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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

public class AmbulanceServiceShowActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ServiceShowAdapter mAdapter;
    private DatabaseReference mDatabase;
    private List<ServiceDetails> serviceDetailsList = new ArrayList<>();
    private List<ServiceDetails> allServiceDetailsList = new ArrayList<ServiceDetails>();;
    private TextView textViewMsg;
    private EditText searchEditText;
    private Button searchButton;
    private final int CALL_REQUEST = 100;
    private String CALL_NUMBER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_service_show);

        textViewMsg = findViewById(R.id.textViewMMessageServiceShow);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        //RecyclerView things
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ServiceShow);

        mAdapter = new ServiceShowAdapter(serviceDetailsList);
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
                    serviceDetailsList.clear();
                    serviceDetailsList.addAll(allServiceDetailsList);
                }
                else{
//                    Toast.makeText(getApplicationContext(), "AllServiceList count : " + allServiceDetailsList.size(), Toast.LENGTH_SHORT).show();
                    List<ServiceDetails> filteredList = new ArrayList<>();
                    for (ServiceDetails row : allServiceDetailsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.driverLocality.toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    serviceDetailsList.clear();

                    serviceDetailsList.addAll(filteredList);
                }
                if(serviceDetailsList.size() == 0){
                    textViewMsg.setVisibility(View.VISIBLE);
                }
                else{
                    textViewMsg.setVisibility(View.GONE);
                }

                mAdapter = new ServiceShowAdapter(serviceDetailsList);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }
        });



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ServiceReviewDialog.currentServiceDetails = serviceDetailsList.get(position);
                ServiceReviewDialog dialog = new ServiceReviewDialog();
                dialog.show(getSupportFragmentManager().beginTransaction(), ServiceReviewDialog.TAG);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Call the contact
                CALL_NUMBER = serviceDetailsList.get(position).phone;
                callPhoneNumber();
            }
        }));


        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myFeedbackQuery = mDatabase.child("Ambulances");
        myFeedbackQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Log.d("FirebaseDatabase", "Got ambulances");
                    ServiceDetails mdetails = postSnapshot.getValue(ServiceDetails.class);
                    if(Statics.SERVICE_REQ_ACTIVITY.equals("User")){
                        if(mdetails.verified.equals("yes")){
                            serviceDetailsList.add(mdetails);
                        }
                    }else {
                        serviceDetailsList.add(mdetails);
                    }

                }
                if(serviceDetailsList.size() == 0){
                    textViewMsg.setVisibility(View.VISIBLE);
                }
                else{
                    textViewMsg.setVisibility(View.GONE);
                }

                allServiceDetailsList.addAll(serviceDetailsList); // copied the all list
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


    public void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);

                    return;
                }
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel: "+CALL_NUMBER));
            startActivity(callIntent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if(requestCode == CALL_REQUEST)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();
            }
            else
            {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
