package co.realinventor.medicures.Common;

public class Feedback {
    public String fid, date, time, msg, to, from, senderEmail, datetime;

    public Feedback(){}


    public Feedback(String fid, String date, String time, String msg, String to, String from, String senderEmail) {
        this.fid = fid;
        this.date = date;
        this.time = time;
        this.msg = msg;
        this.to = to;
        this.from = from;
        this.senderEmail = senderEmail;
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

    public String getDateNTime() {
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

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }
}
