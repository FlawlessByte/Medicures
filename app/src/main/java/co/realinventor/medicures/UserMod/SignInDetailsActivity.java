package co.realinventor.medicures.UserMod;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

import co.realinventor.medicures.Authentication.LoginActivity;
import co.realinventor.medicures.R;

public class SignInDetailsActivity extends AppCompatActivity {
    int flag = 0;

    EditText inputFirstName = findViewById(R.id.inputFirstName);
    EditText inputLastName = findViewById(R.id.inputLastName);
    EditText inputAge = findViewById(R.id.inputAge);
    EditText inputLocality = findViewById(R.id.inputLocality);
    EditText inputPhone = findViewById(R.id.inputPhone);
    Button registerButton = findViewById(R.id.registerButton);
    RadioButton radioMale = findViewById(R.id.radioMale);
    RadioButton radioFemale = findViewById(R.id.radioFemale);
    RadioButton radioOthers = findViewById(R.id.radioOthers);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_details);
    }

    public void registerButtonClicked(View v){
        if(!isInputsOk()){
            Toast.makeText(this, "Make sure you have filled all the inputs!", Toast.LENGTH_SHORT).show();
        }
        else{
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if(auth.getCurrentUser() == null){
                //User not logged in
                Toast.makeText(this, "Session expired! Please login again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignInDetailsActivity.this, LoginActivity.class));
                this.finish();
            }
            else{
                // user logged in
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
        if(radioMale.isSelected())
            gender = "Male";
        if(radioFemale.isSelected())
            gender = "Female";
        if(radioOthers.isSelected())
            gender = "Other";

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        UserDetails userDetails = new UserDetails(fname,lname, gender, age, locality, phone);

        myRef.child("User").child(uid).setValue(userDetails);

        Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(SignInDetailsActivity.this, LoginActivity.class));
        this.finish();
    }

    public boolean isInputsOk(){
        flag = 0;
        if(inputFirstName.getText().equals(""))
            flag = 1;
        if(inputLastName.getText().equals(""))
            flag = 1;
        if(inputAge.getText().equals(""))
            flag = 1;
        if(inputLocality.getText().equals(""))
            flag = 1;
        if(inputPhone.getText().equals(""))
            flag = 1;
        if(!(radioMale.isSelected() == true || radioFemale.isSelected() == true || radioOthers.isSelected() == true))
            flag = 1;

        if(flag == 0)
            return true;
        else
            return false;
    }

}
