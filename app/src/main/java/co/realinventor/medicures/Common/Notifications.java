package co.realinventor.medicures.Common;

public class Notifications {
    public String msg, to, from, sender_name, senderEmail, date, time;

    public Notifications(String msg, String to, String from, String sender_name, String senderEmail, String date, String time) {
        this.msg = msg;
        this.to = to;
        this.from = from;
        this.sender_name = sender_name;
        this.date = date;
        this.time = time;
        this.senderEmail = senderEmail;
    }

    public Notifications() {
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

    public void setSenderName(String sender_id) {
        this.sender_name = sender_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getSenderName() {
        return sender_name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }
}
