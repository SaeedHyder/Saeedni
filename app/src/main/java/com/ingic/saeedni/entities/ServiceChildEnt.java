package com.ingic.saeedni.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 6/7/2017.
 */

public class ServiceChildEnt {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("service_id")
    @Expose
    private Integer serviceId;
    @SerializedName("request_id")
    @Expose
    private Integer requestId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
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

    public ServiceEnt getServiceEnt() {
        return serviceEnt;
    }

    public void setServiceEnt(ServiceEnt serviceEnt) {
        this.serviceEnt = serviceEnt;
    }

    @SerializedName("service_detail")
    @Expose
    private ServiceEnt serviceEnt;
}
