package co.realinventor.medicures.UserMod;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import co.realinventor.medicures.Common.AlarmsOpenHelper;

public class MedicineReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Alarm check the time and details

        Intent i = new Intent(context, MedicineAlarmedActivity.class);
        i.putExtra("medicine", intent.getStringExtra("medicine"));
        i.putExtra("ringtone", intent.getStringExtra("ringtone"));
        context.startActivity(i);

    }

}
