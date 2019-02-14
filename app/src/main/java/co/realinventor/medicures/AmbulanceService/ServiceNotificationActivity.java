package co.realinventor.medicures.AmbulanceService;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.R;

public class ServiceNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_notification);
        Log.d("Activity", "ServiceNotificationActivity");
    }
}
