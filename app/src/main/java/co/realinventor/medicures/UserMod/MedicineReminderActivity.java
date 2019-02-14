package co.realinventor.medicures.UserMod;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;


import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.Common.AlarmsOpenHelper;
import co.realinventor.medicures.R;

public class MedicineReminderActivity extends AppCompatActivity {
    EditText inputMedicineName;
    TextView ringtoneText;
    Button ringtoneSelectButton, buttonCancel, buttonNext;
    Uri selectedRingtoneUri;
    TimePicker timePicker;
    String medicineName = "no name";
    TimeEntity time;
    static AlarmsOpenHelper alarmsOpenHelper;
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_medicine);

        inputMedicineName = findViewById(R.id.inputMedicineName);
        ringtoneText = findViewById(R.id.ringtoneText);
        ringtoneSelectButton = findViewById(R.id.ringtoneSelectButton);
        timePicker = findViewById(R.id.timePick);


        ringtoneSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RingtonePickerDialog.Builder ringtonePickerBuilder = new RingtonePickerDialog
                        .Builder(MedicineReminderActivity.this, getSupportFragmentManager())
                        .setTitle("Select ringtone")
                        .displayDefaultRingtone(true)
                        .setPositiveButtonText("Set Ringtone")
                        .setCancelButtonText("Cancel")
                        .setPlaySampleWhileSelection(true)
                        .setListener(new RingtonePickerListener() {
                            @Override
                            public void OnRingtoneSelected(@NonNull String ringtoneName, Uri ringtoneUri) {
                                //Do someting with selected uri...
                                selectedRingtoneUri = ringtoneUri;
                                ringtoneText.setText(selectedRingtoneUri.getLastPathSegment());
                            }
                        });

                //Add the desirable ringtone types.
                ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_MUSIC);
                ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE);
                ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM);

                //Display the dialog.
                ringtonePickerBuilder.show();
            }
        });
    }

    public void nextButtonClicked(View view){
        medicineName = inputMedicineName.getText().toString();
        time = new TimeEntity(timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);


        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getApplicationContext());
        }
        builder.setTitle("Set Reminder")
                .setMessage("Medicine Name : "+medicineName+"\nTime :"+time.getTime() + "\nSelected Ringtone : " +selectedRingtoneUri.getLastPathSegment())
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // set alarm
                        createAlarm();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void createAlarm(){
        initDatabase();
        addAlarmToDatabase(medicineName, ""+time.getHour(), ""+time.getMinute(), selectedRingtoneUri.toString());



        // Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
        calendar.set(Calendar.MINUTE, time.getMinute());

        Intent intent = new Intent(this, MedicineReminderReceiver.class);
        intent.putExtra("medicine", medicineName);
        intent.putExtra("ringtone", selectedRingtoneUri.toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm set!",Toast.LENGTH_LONG).show();
    }


    static void addAlarmToDatabase(String medicine, String hour, String minute, String uri) {
        db.execSQL("INSERT INTO "
                + AlarmsOpenHelper.TABLE_NAME
                +" ("
                + AlarmsOpenHelper.COLUMN_MEDICINE
                + ", "
                + AlarmsOpenHelper.COLUMN_HOUR
                + ","
                + AlarmsOpenHelper.COLUMN_MINUTE
                + ","
                + AlarmsOpenHelper.COLUMN_TONE
                + ") VALUES ('"
                + medicine
                + "', '"
                + hour
                + "', '"
                + minute
                + "', '"
                + uri
                + "');");
    }

    private void initDatabase() {
        alarmsOpenHelper = new AlarmsOpenHelper(getApplicationContext());
        db = alarmsOpenHelper.getWritableDatabase();
    }
}
