package co.realinventor.medicures.UserMod;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DocVisitReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("BroadCastReceiver", "DocVisitReminderReceiver");


        Intent i = new Intent(context, MedicineAlarmedActivity.class);
        i.putExtra("doctor", intent.getStringExtra("doctor"));
        i.putExtra("ringtone", intent.getStringExtra("ringtone"));
        context.startActivity(i);
    }
}
