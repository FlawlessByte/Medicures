package co.realinventor.medicures.MedStore;

import java.io.Serializable;

public class Medicine implements Serializable {
    public String trans_id, medicine_name, dosage ,quantity, to, from, customerEmail, customerName, reviewed /*yes or no*/, status /*approved or denied*/, address, contact;

    public Medicine(String trans_id,String medicine_name, String dosage, String quantity, String to, String from, String customerEmail,
                    String customerName, String reviewed, String  status, String address, String contact) {
        this.medicine_name = medicine_name;
        this.dosage = dosage;
        this.quantity = quantity;
        this.to = to;
        this.from = from;
        this.customerName = customerName;
        this.reviewed = reviewed;
        this.status = status;
        this.customerEmail = customerEmail;
        this.trans_id = trans_id;
        this.address = address;
        this.contact = contact;
    }

    public Medicine() {
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }


    public String getMedicine_name() {
        return medicine_name;
    }

    public String getDosage() {
        return dosage;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getTrans_id() {
        return trans_id;
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

    public String getCustomerEmail() {
        return customerEmail;
    }
}
