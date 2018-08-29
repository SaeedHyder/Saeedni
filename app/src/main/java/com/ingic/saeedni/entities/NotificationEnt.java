package com.ingic.saeedni.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class NotificationEnt {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("reciever_id")
    @Expose
    private Integer recieverId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ar_message")
    @Expose
    private String armessage;
    @SerializedName("action_type")
    @Expose
    private String actionType;
    @SerializedName("action_id")
    @Expose
    private Integer actionId;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("reciever_detail")
    @Expose
    private RegistrationResultEnt recieverDetail;
    @SerializedName("request_detail")
    @Expose
    private UserInProgressEnt requestDetail;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public UserInProgressEnt getRequestDetail() {
        return requestDetail;
    }

    public void setRequestDetail(UserInProgressEnt requestDetail) {
        this.requestDetail = requestDetail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(Integer recieverId) {
        this.recieverId = recieverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
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

    public RegistrationResultEnt getRecieverDetail() {
        return recieverDetail;
    }

    public void setRecieverDetail(RegistrationResultEnt recieverDetail) {
        this.recieverDetail = recieverDetail;
    }

    public String getArmessage() {
        return armessage;
    }

    public void setArmessage(String armessage) {
        this.armessage = armessage;
    }
}
