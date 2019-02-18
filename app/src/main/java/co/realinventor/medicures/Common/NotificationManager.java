package co.realinventor.medicures.Common;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class NotificationManager {
    public String msg, to, from, sender_name, date, time;

    public NotificationManager() {
    }

    public void makeNotification(String msg, String to, String from, String sender_name){
        this.msg = msg;
        this.to = to;
        this.from = from;
        this.sender_name = sender_name;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        this.date = df.format(Calendar.getInstance().getTime());
        df = new SimpleDateFormat("HH:mm:ss");
        this.time = df.format(Calendar.getInstance().getTime());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String unique_id = UUID.randomUUID().toString();
        ref.child("Notifications").child(unique_id).setValue(this);
    }

    public String getMsg() {
        return msg;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
