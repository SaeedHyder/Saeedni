package com.ingic.saeedni.entities;



public class NavigationEnt {
    private int selectedDrawable;
    private int unselectedDrawable;
    private String item_text;
    private int NotificationCount;
    public int getSelectedDrawable() {
        return selectedDrawable;
    }

    public void setSelectedDrawable(int selectedDrawable) {
        this.selectedDrawable = selectedDrawable;
    }

    public int getUnselectedDrawable() {
        return unselectedDrawable;
    }

    public void setUnselectedDrawable(int unselectedDrawable) {
        this.unselectedDrawable = unselectedDrawable;
    }

    public String getItem_text() {
        return item_text;
    }

    public void setItem_text(String item_text) {
        this.item_text = item_text;
    }

    public int getNotificationCount() {
        return NotificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        NotificationCount = notificationCount;
    }

    public NavigationEnt(int selectedDrawable, int unselectedDrawable, String item_text) {

        this.selectedDrawable = selectedDrawable;
        this.unselectedDrawable = unselectedDrawable;
        this.item_text = item_text;
    }

    public NavigationEnt(int selectedDrawable, int unselectedDrawable, String item_text,int NotificationCount) {

        this.selectedDrawable = selectedDrawable;
        this.unselectedDrawable = unselectedDrawable;
        this.item_text = item_text;
        setNotificationCount(NotificationCount);
    }


}
