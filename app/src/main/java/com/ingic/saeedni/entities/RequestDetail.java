package com.ingic.saeedni.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;



public class RequestDetail {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("service_id")
    @Expose
    private int service_id;
    @SerializedName("discription")
    @Expose
    private String discription;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("full_address")
    @Expose
    private String full_address;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("payment_type")
    @Expose
    private String payment_type;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("estimate_from")
    @Expose
    private String estimate_from;
    @SerializedName("estimate_to")
    @Expose
    private String estimate_to;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("user_id")
    @Expose
    private String user_id;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    @SerializedName("category_id")
    @Expose
    private int category_id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("parent_id")
    @Expose
    private String parent_id;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("servics_list")
    @Expose
    private List<serviceList> servics_list;
    @SerializedName("service_detail")
    @Expose
    private ServiceDetail service_detail;
    @SerializedName("user_detail")
    @Expose
    private UserDetail user_detail;
    @SerializedName("sub_request")
    @Expose
    private List<RequestDetail> subRequest = null;
    private ArrayList<ImageDetailEnt> image_detail;
    private ArrayList<FeedbackDetail> feedbackdetail;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    @SerializedName("total_amount")
    @Expose

    private String total_amount = "";


    public List<RequestDetail> getSubRequest() {
        return subRequest;
    }


    public void setSubRequest(List<RequestDetail> subRequest) {
        this.subRequest = subRequest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<ImageDetailEnt> getImage_detail() {
        return image_detail;
    }

    public void setImage_detail(ArrayList<ImageDetailEnt> image_detail) {
        this.image_detail = image_detail;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
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

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
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

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEstimate_from() {
        return estimate_from;
    }

    public void setEstimate_from(String estimate_from) {
        this.estimate_from = estimate_from;
    }

    public String getEstimate_to() {
        return estimate_to;
    }

    public void setEstimate_to(String estimate_to) {
        this.estimate_to = estimate_to;
    }

    public String getTotal() {
        return total==null?"":total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public List<serviceList> getServics_list() {
        return servics_list;
    }

    public void setServics_list(List<serviceList> servics_list) {
        this.servics_list = servics_list;
    }

    public ServiceDetail getService_detail() {
        return service_detail;
    }

    public void setService_detail(ServiceDetail service_detail) {
        this.service_detail = service_detail;
    }

    public UserDetail getUser_detail() {
        return user_detail;
    }

    public void setUser_detail(UserDetail user_detail) {
        this.user_detail = user_detail;
    }


    public ArrayList<FeedbackDetail> getFeedbackdetail() {
        return feedbackdetail;
    }

    public void setFeedbackdetail(ArrayList<FeedbackDetail> feedbackdetail) {
        this.feedbackdetail = feedbackdetail;
    }

}
