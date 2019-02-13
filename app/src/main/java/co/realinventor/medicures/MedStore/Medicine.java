package co.realinventor.medicures.MedStore;

public class Medicine {
    public String trans_id, medicine_name, dosage ,quantity, to, from, customerName;

    public Medicine(String medicine_name, String dosage, String quantity, String to, String from, String customerName) {
        this.medicine_name = medicine_name;
        this.dosage = dosage;
        this.quantity = quantity;
        this.to = to;
        this.from = from;
        this.customerName = customerName;
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
}
