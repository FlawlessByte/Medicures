package co.realinventor.medicures.UserMod;

public class DateEntity {
    int year, month, day;

    public DateEntity(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getDate(){
        return day + "-" + month + "-" +year;
    }
}
