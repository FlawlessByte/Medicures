package co.realinventor.medicures.Common;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class FeedbackManager {

    public String fid, date, time, msg, to, from, senderName;

    public FeedbackManager(){}

    public FeedbackManager(String fid, String date, String time, String msg, String to, String from, String senderName) {
        this.fid = fid;
        this.date = date;
        this.time = time;
        this.msg = msg;
        this.to = to;
        this.from = from;
        this.senderName = senderName;
    }


    public void writeFeedback(String msg, String to, String from, String senderName){
        this.msg = msg;
        this.to = to;
        this.from = from;
        this.senderName = senderName;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        this.date = df.format(Calendar.getInstance().getTime());
        df = new SimpleDateFormat("HH:mm:ss");
        this.time = df.format(Calendar.getInstance().getTime());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        this.fid = UUID.randomUUID().toString();
        ref.child("feedbacks").child(fid).setValue(this);
    }



    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public String getDateTime() {
        return getDate() + " | " + getTime();
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
