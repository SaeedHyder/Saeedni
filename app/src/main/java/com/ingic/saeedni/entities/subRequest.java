package com.ingic.saeedni.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class subRequest {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("service_id")
    @Expose
    private Integer serviceId;
    @SerializedName("discription")
    @Expose
    private String discription;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("full_address")
    @Expose
    private String fullAddress;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("estimate_from")
    @Expose
    private String estimateFrom;
    @SerializedName("estimate_to")
    @Expose
    private String estimateTo;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("service_detail")
    @Expose
    private ServiceDetail serviceDetail;
    @SerializedName("servics_list")
    @Expose
    private List<serviceList> servicsList = null;

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

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEstimateFrom() {
        return estimateFrom;
    }

    public void setEstimateFrom(String estimateFrom) {
        this.estimateFrom = estimateFrom;
    }

    public String getEstimateTo() {
        return estimateTo;
    }

    public void setEstimateTo(String estimateTo) {
        this.estimateTo = estimateTo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public ServiceDetail getServiceDetail() {
        return serviceDetail;
    }

    public void setServiceDetail(ServiceDetail serviceDetail) {
        this.serviceDetail = serviceDetail;
    }

    public List<serviceList> getServicsList() {
        return servicsList;
    }

    public void setServicsList(List<serviceList> servicsList) {
        this.servicsList = servicsList;
    }
}
