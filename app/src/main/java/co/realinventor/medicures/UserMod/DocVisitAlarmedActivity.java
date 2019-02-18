package co.realinventor.medicures.UserMod;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.R;

public class DocVisitAlarmedActivity extends AppCompatActivity {
    String doctorName, ringtone;
    TextView doctorTextview;
    private Handler handler;
    private Runnable runnable;
    private MediaPlayer mMediaPlayer;
    private Uri ringtoneUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_alarmed);

        Log.d("Activity", "DocVisitAlarmedActivity");

        //get current time and compare with that of in sqlite database
        //and retrieve details

        doctorTextview = findViewById(R.id.doctorTextview);

        doctorName = getIntent().getStringExtra("doctor");
        ringtone = getIntent().getStringExtra("ringtone");
        ringtoneUri = Uri.parse(ringtone);
        doctorTextview.setText(doctorTextview.getText()+"\n"+doctorName);



        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(1000);
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                }
                finally{
                    //also call the same runnable to call it at regular interval
                    handler.postDelayed(this, 1500);
                }
            }
        };

        //runnable must be execute once
        handler.post(runnable);



        //play ringtone


        try {
            //Uri alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this, ringtoneUri);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch(Exception e) {
            Log.d("MediaPlayer Error", e.getLocalizedMessage());
        }



    }

    public void alarmDismissButtonClicked(View view){
        Log.d("AlarmDismissButton", "Pressed");
        handler.removeCallbacks(runnable);
        mMediaPlayer.stop();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        mMediaPlayer.stop();
        finish();
        super.onBackPressed();
    }
}
