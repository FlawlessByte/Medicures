package co.realinventor.medicures.MedStore;

public class MedStoreDetails {
    public String shopName, locality, ownerName, pharmacist, phone, pinCode, verified, mUid;

    public MedStoreDetails(){}

    public MedStoreDetails(String mUid, String shopName, String locality, String ownerName, String pharmacist, String phone, String pinCode, String verified) {
        this.shopName = shopName;
        this.locality = locality;
        this.ownerName = ownerName;
        this.pharmacist = pharmacist;
        this.phone = phone;
        this.pinCode = pinCode;
        this.verified = verified;
        this.mUid = mUid;
    }
}
