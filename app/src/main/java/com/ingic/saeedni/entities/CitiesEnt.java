package com.ingic.saeedni.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 6/4/18.
 */
public class CitiesEnt {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("ar_address")
    @Expose
    private String ar_address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getAr_location() {
        return ar_address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAr_address(String ar_address) {
        this.ar_address = ar_address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CitiesEnt)) {
            return false;
        }
        CitiesEnt rhs = ((CitiesEnt) other);
        return rhs.location.equals(location);
    }
}
