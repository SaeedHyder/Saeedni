package com.ingic.saeedni.entities;


public class InProgressParentEnt {

    private String Job;
    private String JobPosted;
    private String ClientName;
    private String JobTitle;
    private String Earning;
    private String Address;

    public InProgressParentEnt(String Job, String JobPosted, String ClientName, String JobTitle,String Earning, String address ){

        setJob(Job);
        setJobPosted(JobPosted);
        setClientName(ClientName);
        setJobTitle(JobTitle);
        setEarning(Earning);
        setAddress(address);
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public String getJobPosted() {
        return JobPosted;
    }

    public void setJobPosted(String jobPosted) {
        JobPosted = jobPosted;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getEarning() {
        return Earning;
    }

    public void setEarning(String earning) {
        Earning = earning;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
