package com.ingic.saeedni.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


    public class TechInProgressEnt {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("technician_id")
    @Expose
    private int technician_id;
    @SerializedName("request_id")
    @Expose
    private int request_id;
    @SerializedName("status")
    @Expose
    int status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("arrival_time")
    @Expose
    private  String arrival_time;
    @SerializedName("completion_time")
    @Expose
    private  String completion_time;
    @SerializedName("finish")
    @Expose
    private   int finish;
    @SerializedName("created_at")
    @Expose
    private  String created_at;
    @SerializedName("updated_at")
    @Expose
    private  String updated_at;
    @SerializedName("technician_detail")
    @Expose
    private TechnicianDetail technician_detail;
    @SerializedName("request_detail")
    @Expose
    private RequestDetail request_detail;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTechnician_id() {
        return technician_id;
    }

    public void setTechnician_id(int technician_id) {
        this.technician_id = technician_id;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getCompletion_time() {
        return completion_time;
    }

    public void setCompletion_time(String completion_time) {
        this.completion_time = completion_time;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
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

    public TechnicianDetail getTechnician_detail() {
        return technician_detail;
    }

    public void setTechnician_detail(TechnicianDetail technician_detail) {
        this.technician_detail = technician_detail;
    }

    public RequestDetail getRequest_detail() {
        return request_detail;
    }

    public void setRequest_detail(RequestDetail request_detail) {
        this.request_detail = request_detail;
    }


}
