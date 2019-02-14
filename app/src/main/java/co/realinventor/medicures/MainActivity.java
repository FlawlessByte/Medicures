package co.realinventor.medicures;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.Authentication.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Activity", "MainActivity");


    }

    public void modeSelected(View view){
        Log.d("Button", view.getTag().toString());

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("mode", view.getTag().toString());
        startActivity(intent);
    }
}
