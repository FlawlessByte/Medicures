package co.realinventor.medicures.UserMod;

public class UserDetails {
    public String first_name;
    public String last_name;
    public String gender;
    public String age;
    public String locality;
    public String phone;

    public UserDetails(){}

    public UserDetails(String first_name, String last_name, String gender, String age, String locality, String phone) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.age = age;
        this.locality = locality;
        this.phone = phone;
    }
}
