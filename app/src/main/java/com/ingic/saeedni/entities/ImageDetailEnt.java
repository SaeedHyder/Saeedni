package com.ingic.saeedni.entities;

import com.ingic.saeedni.helpers.DateHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 6/5/2017.
 */

public class ImageDetailEnt {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("request_id")
    @Expose
    private Integer requestId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("file_link")
    @Expose
    private String fileLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = DateHelper.getLocalTimeDate(createdAt);
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = DateHelper.getLocalTimeDate(updatedAt);
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof String)
        {
            isEqual = (this.fileLink.equals(object.toString()) );
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
