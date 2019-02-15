package co.realinventor.medicures.UserMod;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;
import co.realinventor.medicures.MainActivity;
import co.realinventor.medicures.R;

public class LoggedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        Log.d("Action", "Back button pressed");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
        } else if (id == R.id.nav_feedback) {
            Log.d("Menu FeedbackButton", "Pressed");

        } else if (id == R.id.nav_reminder) {
            Log.d("MenuReminderButton", "Pressed");
            startActivity(new Intent(LoggedActivity.this, MedicineReminderActivity.class));

        } else if (id == R.id.nav_doc_visit) {
            Log.d("MenuDocVisitButton", "Pressed");
            startActivity(new Intent(LoggedActivity.this, DocReminderActivity.class));

        } else if (id == R.id.nav_notification) {
            Log.d("MenuNotificationButton", "Pressed");

        } else if (id == R.id.nav_sent) {
            Log.d("MenuNavButton", "Pressed");

        } else if (id == R.id.nav_logout) {
            Log.d("MenuSignOutButton", "Pressed");
            auth.signOut();
            startActivity(new Intent(LoggedActivity.this, MainActivity.class));
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void loggedReminderButtonPressed(View view){
        Log.d("ReminderButton", "Pressed");
        startActivity(new Intent(LoggedActivity.this, MedicineReminderActivity.class));
    }

    public void loggedDocVisitButtonPressed(View view){
        Log.d("DocVisitButton", "Pressed");
        startActivity(new Intent(LoggedActivity.this, DocReminderActivity.class));
    }


}
