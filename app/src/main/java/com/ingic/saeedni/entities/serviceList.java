package com.ingic.saeedni.entities;


public class serviceList {

    int id;
    int service_id;
    int request_id;
    String created_at;
    String updated_at;
    ServiceDetail service_detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
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

    public ServiceDetail getService_detail() {
        return service_detail;
    }

    public void setService_detail(ServiceDetail service_detail) {
        this.service_detail = service_detail;
    }
}
