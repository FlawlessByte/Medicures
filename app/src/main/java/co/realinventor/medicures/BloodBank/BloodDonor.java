package co.realinventor.medicures.BloodBank;

import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class BloodDonor {
    public String uid, name, age, gender, contact, bloodGroup;

    public BloodDonor() {
    }

    public BloodDonor(String uid, String name, String age, String gender, String contact, String bloodGroup) {
        this.uid = uid;
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Task registerBloodDonor(String name, String gender, String contact, String bloodGroup){
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        uid = ref.child("blood_donors").push().getKey();

        return ref.child("blood_donors").child(uid).setValue(this);

    }
}
