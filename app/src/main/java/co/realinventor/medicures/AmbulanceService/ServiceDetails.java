package co.realinventor.medicures.AmbulanceService;

public class ServiceDetails {
    public String driverName, driverLocality, driverAge, verified, phone, availability, serviceID;

    public ServiceDetails(){}

    public ServiceDetails(String serviceID,String driverName, String driverLocality, String driverAge, String phone, String verified, String availability) {
        this.serviceID = serviceID;
        this.driverName = driverName;
        this.driverLocality = driverLocality;
        this.driverAge = driverAge;
        this.verified = verified;
        this.availability = availability;
        this.phone = phone;
    }
}
