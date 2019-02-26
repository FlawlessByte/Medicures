package co.realinventor.medicures.UserMod;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.yayandroid.locationmanager.LocationManager;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.ProviderType;
import com.yayandroid.locationmanager.listener.LocationListener;

import androidx.drawerlayout.widget.DrawerLayout;
import co.realinventor.medicures.Common.AmbulanceServiceShowActivity;
import co.realinventor.medicures.Common.FeedbackActivity;
import co.realinventor.medicures.Common.MedStoreShowActivity;
import co.realinventor.medicures.Common.NotificationsActivity;
import co.realinventor.medicures.Common.SentMailActivity;
import co.realinventor.medicures.Common.Statics;
import co.realinventor.medicures.MainActivity;
import co.realinventor.medicures.R;

public class LoggedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private final int CALL_REQUEST = 100;
    FirebaseAuth auth;
    private TextView textViewUserNameNav;
    private LocationManager awesomeLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MedCure");
        setSupportActionBar(toolbar);

        Log.d("Activity", "LoggedActivity");

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is logged in
            Log.d("Logged activity", "User logged in");
        }
        else{
            Log.d("Logged activity", "User not logged in");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        textViewUserNameNav = (TextView) headerView.findViewById(R.id.textViewUsernameNav);
        textViewUserNameNav.setText(auth.getCurrentUser().getEmail());

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        Log.d("Action", "Back button pressed");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logged, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            Log.d("MenuSignOutButton", "Pressed");
            auth.signOut();
            startActivity(new Intent(LoggedActivity.this, MainActivity.class));
            this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            Log.d("Menu Account Button", "Pressed");
            startActivity(new Intent(this, MyAccountActivity.class));
        }
        else if (id == R.id.nav_locality) {
            Log.d("Locality Edit", "Pressed");
            startActivity(new Intent(LoggedActivity.this, UserLocalityActivity.class));
        }
        else if (id == R.id.nav_feedback) {
            Log.d("Menu FeedbackButton", "Pressed");
            Intent i = new Intent(this, ComposeFeedbackActivity.class);
            i.putExtra("to" , "admin@medicure");
            i.putExtra("role", "Admin");
            startActivity(i);
        }
        else if (id == R.id.nav_reminder) {
            Log.d("MenuReminderButton", "Pressed");
            startActivity(new Intent(LoggedActivity.this, MedicineReminderActivity.class));
        }
        else if (id == R.id.nav_doc_visit) {
            Log.d("MenuDocVisitButton", "Pressed");
            startActivity(new Intent(LoggedActivity.this, DocReminderActivity.class));
        }
        else if (id == R.id.nav_notification) {
            Log.d("MenuNotificationButton", "Pressed");
            startActivity(new Intent(LoggedActivity.this, NotificationsActivity.class));
        }
        else if (id == R.id.nav_sent) {
            Log.d("MenuSentButton", "Pressed");
            startActivity(new Intent(LoggedActivity.this, SentMailActivity.class));
        }
        else if (id == R.id.nav_logout) {
            Log.d("MenuSignOutButton", "Pressed");
            auth.signOut();
            startActivity(new Intent(LoggedActivity.this, MainActivity.class));
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loggedAlertButtonPressed(View view){
        setupGPSConfig();
        callPhoneNumber();
    }

    private void setupGPSConfig(){


        // LocationManager MUST be initialized with Application context in order to prevent MemoryLeaks
        awesomeLocationManager = new LocationManager.Builder(getApplicationContext())
                .activity(this) // Only required to ask permission and/or GoogleApi - SettingsApi
                .configuration(Configurations.defaultConfiguration("The GPS permission is required!", "GPS coordinates obtained"))
                .notify(this)
                .build();

        LocationManager.enableLog(true);

        awesomeLocationManager.get();
    }


    public void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(LoggedActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);

                    return;
                }
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel: "+Statics.ALERT_CALL_NUMBER));
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
                Toast.makeText(LoggedActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loggedReminderButtonPressed(View view){
        Log.d("ReminderButton", "Pressed");
        startActivity(new Intent(LoggedActivity.this, MedicineReminderActivity.class));
    }

    public void loggedDocVisitButtonPressed(View view){
        Log.d("DocVisitButton", "Pressed");
        startActivity(new Intent(LoggedActivity.this, DocReminderActivity.class));
    }

    public void loggedMedicineButtonPressed(View view){
        Log.d("MedicinetButton", "Pressed");
        Statics.MED_REQ_ACTIVITY = "User";
        startActivity(new Intent(LoggedActivity.this, MedStoreShowActivity.class));
    }

    public void loggedAmbulanceButtonPressed(View view){
        Log.d("AmbulanceButton", "Pressed");
        Statics.SERVICE_REQ_ACTIVITY = "User";
        startActivity(new Intent(LoggedActivity.this, AmbulanceServiceShowActivity.class));
    }


    private void sendAlertSMS(final double latitude, final double longitude){
        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                String phoneNo = Statics.ALERT_CALL_NUMBER;
                String link = "http://maps.google.com/maps?q=loc:" + String.format("%f,%f", latitude , longitude );
                String sms = "Here is a medical emergency! Please contact! \nLocation details: \n"+link;

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }




    /*LocationListener Stuff*/

    @Override
    public void onProcessTypeChanged(int processType) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Longitude", ""+location.getLongitude());
        Log.d("Latitude", ""+location.getLatitude());
        sendAlertSMS(location.getLongitude(), location.getLatitude());

    }

    @Override
    public void onLocationFailed(int type) {

    }

    @Override
    public void onPermissionGranted(boolean alreadyHadPermission) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



}
