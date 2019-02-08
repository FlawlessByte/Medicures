package co.realinventor.medicures;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import co.realinventor.medicures.Authentication.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void modeSelected(View view){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("mode", view.getTag().toString());
        startActivity(intent);
    }
}
