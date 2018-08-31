package com.ingic.saeedni.entities;



public class NewJobEnt {

    private String logo;
    private String notification;
    private String arrow;

    public NewJobEnt(String logo, String notification, String arrow){
        setLogo(logo);
        setNotification(notification);
        setArrow(arrow);
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getArrow() {
        return arrow;
    }

    public void setArrow(String arrow) {
        this.arrow = arrow;
    }
}
