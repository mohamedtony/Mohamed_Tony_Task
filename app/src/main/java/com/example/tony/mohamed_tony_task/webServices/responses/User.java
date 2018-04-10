package com.example.tony.mohamed_tony_task.webServices.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable{
    @SerializedName("contact_id")
    @Expose
    private String contactId;
    @SerializedName("contact_name")
    @Expose
    private String contactName;
    @SerializedName("contact_mobile")
    @Expose
    private String contactMobile;
    @SerializedName("contact_phone")
    @Expose
    private String contactPhone;
    @SerializedName("contact_fax")
    @Expose
    private String contactFax;
    @SerializedName("contact_email")
    @Expose
    private String contactEmail;
    @SerializedName("contact_site")
    @Expose
    private String contactSite;
    @SerializedName("contact_address")
    @Expose
    private String contactAddress;
    @SerializedName("contact_img")
    @Expose
    private String contactImg;

    public User(){

    }

    protected User(Parcel in) {
        contactId = in.readString();
        contactName = in.readString();
        contactMobile = in.readString();
        contactPhone = in.readString();
        contactFax = in.readString();
        contactEmail = in.readString();
        contactSite = in.readString();
        contactAddress = in.readString();
        contactImg = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactFax() {
        return contactFax;
    }

    public void setContactFax(String contactFax) {
        this.contactFax = contactFax;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactSite() {
        return contactSite;
    }

    public void setContactSite(String contactSite) {
        this.contactSite = contactSite;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactImg() {
        return contactImg;
    }

    public void setContactImg(String contactImg) {
        this.contactImg = contactImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(contactId);
        parcel.writeString(contactName);
        parcel.writeString(contactMobile);
        parcel.writeString(contactPhone);
        parcel.writeString(contactFax);
        parcel.writeString(contactEmail);
        parcel.writeString(contactSite);
        parcel.writeString(contactAddress);
        parcel.writeString(contactImg);
    }
}
