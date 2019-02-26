package co.realinventor.medicures.Common;

import java.io.Serializable;

public class PrescDetails implements Serializable {
    public String trans_id;
    public String to;
    public String from;
    public String customerEmail;
    public String customerName;
    public String reviewed /*yes or no*/;
    public String status /*approved or denied*/;
    public String address;
    public String contact;
    public String filename;

    public PrescDetails(){}


    public PrescDetails(String trans_id, String to, String from, String customerEmail, String customerName, String reviewed, String status, String address, String contact, String filename) {
        this.trans_id = trans_id;
        this.to = to;
        this.from = from;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.reviewed = reviewed;
        this.status = status;
        this.address = address;
        this.contact = contact;
        this.filename = filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setReviewed(String reviewed) {
        this.reviewed = reviewed;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getReviewed() {
        return reviewed;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }
}
