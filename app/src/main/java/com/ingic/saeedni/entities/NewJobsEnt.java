package com.ingic.saeedni.entities;



public class NewJobsEnt {

    int id;
    int technician_id;
    int request_id;
    int status;
    String message;
    String arrival_time;
    String completion_time;
    String created_at;
    String updated_at;
    TechnicianDetail technician_detail;
    RequestDetail request_detail;




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
