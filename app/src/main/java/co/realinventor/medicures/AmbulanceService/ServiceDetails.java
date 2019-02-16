package co.realinventor.medicures.AmbulanceService;

public class ServiceDetails {
    public String driverName, driverLocality, driverAge, verified, phone, availability;

    public ServiceDetails(){}

    public ServiceDetails(String driverName, String driverLocality, String driverAge, String verified, String availability) {
        this.driverName = driverName;
        this.driverLocality = driverLocality;
        this.driverAge = driverAge;
        this.verified = verified;
        this.availability = availability;
    }
}
