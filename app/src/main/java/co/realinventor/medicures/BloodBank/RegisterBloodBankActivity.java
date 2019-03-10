package co.realinventor.medicures.BloodBank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.R;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterBloodBankActivity extends AppCompatActivity {
    private TextInputEditText textInputDonorName, textInputDonorPhone;
    private MaterialRadioButton maleRadioButton, femaleRadioButton, otherRadioButton;
    private Spinner bloodGroupSpinner;
    private String[] bloodGroups= {"A+ve", "A-ve", "B+ve", "B-ve", "O+ve", "O-ve", "AB+ve", "AB+ve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_blood_bank);

        textInputDonorName = findViewById(R.id.textInputDonorName);
        textInputDonorPhone = findViewById(R.id.textInputDonorPhone);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        otherRadioButton = findViewById(R.id.otherRadioButton);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bloodGroups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        bloodGroupSpinner.setAdapter(adapter);
        bloodGroupSpinner.setSelection(0);

    }

    public void bloodRegisterButtonClicked(View view){
        String name, phone, gender = "", bloodGroup;
        name = textInputDonorName.getText().toString();
        phone = textInputDonorPhone.getText().toString();
        if (maleRadioButton.isChecked()) {
            gender = maleRadioButton.getText().toString();
        }
        if (femaleRadioButton.isChecked()) {
            gender = femaleRadioButton.getText().toString();
        }
        if (otherRadioButton.isChecked()) {
            gender = otherRadioButton.getText().toString();
        }
        bloodGroup = bloodGroupSpinner.getSelectedItem().toString();

        if(!(TextUtils.isEmpty(name) || (TextUtils.isEmpty(phone)))) {
            Task task = new BloodDonor().registerBloodDonor(name, gender, phone, bloodGroup);
            task.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterBloodBankActivity.this, "You have registered successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(RegisterBloodBankActivity.this, "Sorry Some error occured!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            Toast.makeText(this, "Fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
