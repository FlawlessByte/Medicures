package co.realinventor.medicures.UserMod;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DocVisitReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Intent i = new Intent(context, MedicineAlarmedActivity.class);
        i.putExtra("doctor", intent.getStringExtra("doctor"));
        i.putExtra("ringtone", intent.getStringExtra("ringtone"));
        context.startActivity(i);
    }
}
