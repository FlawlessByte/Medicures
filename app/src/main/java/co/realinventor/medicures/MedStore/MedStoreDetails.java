package co.realinventor.medicures.MedStore;

import java.util.HashMap;
import java.util.Map;

public class MedStoreDetails {
    public String shopName, locality, ownerName, phone, pinCode, verified, mUid;

    public MedStoreDetails(){}

    public MedStoreDetails(String mUid, String shopName, String locality, String ownerName, String phone, String pinCode, String verified) {
        this.shopName = shopName;
        this.locality = locality;
        this.ownerName = ownerName;
        this.phone = phone;
        this.pinCode = pinCode;
        this.verified = verified;
        this.mUid = mUid;
    }

    public String getShopName() {
        return shopName;
    }

    public String getLocality() {
        return locality;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getPinCode() {
        return pinCode;
    }

    public String getVerified() {
        return verified;
    }

    public String getmUid() {
        return mUid;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

}
