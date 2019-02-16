package co.realinventor.medicures.MedStore;

public class Medicine {
    public String trans_id, medicine_name, dosage ,quantity, to, from, customerEmail, customerName, reviewed /*yes or no*/, status /*approved or denied*/;

    public Medicine(String trans_id,String medicine_name, String dosage, String quantity, String to, String from, String customerEmail, String customerName, String reviewed, String  status) {
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
    }

    public Medicine() {
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
