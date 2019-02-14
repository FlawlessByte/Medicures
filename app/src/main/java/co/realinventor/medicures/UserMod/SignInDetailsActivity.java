package co.realinventor.medicures.UserMod;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.Authentication.LoginActivity;
import co.realinventor.medicures.R;

public class SignInDetailsActivity extends AppCompatActivity {
    int flag = 0;

    EditText inputFirstName, inputLastName, inputAge, inputLocality, inputPhone;
    Button registerButton;
    RadioButton radioMale, radioFemale, radioOthers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_details);

        inputFirstName = findViewById(R.id.inputFirstName);
        inputLastName = findViewById(R.id.inputLastName);
        inputAge = findViewById(R.id.inputAge);
        inputLocality = findViewById(R.id.inputLocality);
        inputPhone = findViewById(R.id.inputPhone);
        registerButton = findViewById(R.id.registerButton);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        radioOthers = findViewById(R.id.radioOthers);

        Log.d("Activity", "SignInDetailsActivity");
    }

    public void registerButtonClicked(View v){
        Log.d("Button", "RegisterButtonClicked");
        if(!isInputsOk()){
            Log.d("RegisterButton", "Inputs Not ok");
            Toast.makeText(this, "Make sure you have filled all the inputs!", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d("RegisterButton", "inputs ok");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if(auth.getCurrentUser() == null){
                //User not logged in
                Toast.makeText(this, "Session expired! Please login again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignInDetailsActivity.this, LoginActivity.class));
                this.finish();
            }
            else{
                // user logged in
                Log.d("RegisterButton", "Save user data");
                saveUserData(auth.getCurrentUser().getUid());
            }
        }
    }

    public void saveUserData(String uid){
        Log.d("UID", uid);
        String fname = inputFirstName.getText().toString();
        String lname = inputLastName.getText().toString();
        String age = inputAge.getText().toString();
        String locality = inputLocality.getText().toString();
        String phone = inputPhone.getText().toString();
        String gender = "";
        if(radioMale.isChecked())
            gender = "Male";
        if(radioFemale.isChecked())
            gender = "Female";
        if(radioOthers.isChecked())
            gender = "Other";

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        UserDetails userDetails = new UserDetails(fname,lname, gender, age, locality, phone);

        myRef.child("User").child(uid).setValue(userDetails);
        Log.d("saveUserData", "Saved data to firebase database");

        Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(SignInDetailsActivity.this, LoginActivity.class));
        this.finish();
    }

    public boolean isInputsOk(){
        flag = 0;
        if(inputFirstName.getText().toString().equals("")){
            flag = 1;
        }
        if(inputLastName.getText().toString().equals("")){
            flag = 1;
        }
        if(inputAge.getText().toString().equals("")){
            flag = 1;
        }
        if(inputLocality.getText().toString().equals("")){
            flag = 1;
        }
        if(inputPhone.getText().toString().equals("")){
            flag = 1;
        }
        if(!(radioMale.isChecked() || radioFemale.isChecked() || radioOthers.isChecked())){
            flag = 1;
        }

        if(flag == 0)
            return true;
        else
            return false;
    }

}
