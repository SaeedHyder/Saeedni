package com.ingic.saeedni.entities;


public class CompletedJobsEnt {

    private String Job;
    private String jobCompleted;
    private String ClientName;
    private String JobTitle;
    private String rating;
    private String Earning;

    public CompletedJobsEnt(String Job, String jobCompleted, String ClientName, String JobTitle, String rating, String Earning){

        setJob(Job);
        setJobCompleted(jobCompleted);
        setClientName(ClientName);
        setJobTitle(JobTitle);
        setRating(rating);
        setEarning(Earning);

    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public String getJobCompleted() {
        return jobCompleted;
    }

    public void setJobCompleted(String jobCompleted) {
        this.jobCompleted = jobCompleted;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getEarning() {
        return Earning;
    }

    public void setEarning(String earning) {
        Earning = earning;
    }
}


